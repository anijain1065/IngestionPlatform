package com.example.ingestionplatform.sinks;

import com.example.ingestionplatform.dao.delivery_unit.S3DeliveryUnit;
import com.example.ingestionplatform.dao.sinks.S3;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.common.serialization.Encoder;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.configuration.Configuration;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class S3Sink implements DataSink<JsonNode>, Serializable {
    private static final long serialVersionUID = 1L;

    private final String bucketName;
    private final String endpoint;
    private final String accessKey;
    private final String secretKey;
    private final String pathPrefix;
    private final String batchInterval;
    private transient FileSink<JsonNode> internalFlinkS3Sink;
    private transient ObjectMapper objectMapper;

    public S3Sink(S3 s3) {
        this.bucketName = s3.getBucketName();
        S3DeliveryUnit deliveryUnit = s3.getS3DeliveryUnit();
        this.endpoint = deliveryUnit.getEndpoint();
        this.accessKey = deliveryUnit.getAccessKeyId();
        this.secretKey = deliveryUnit.getSecretAccessKey();
        this.pathPrefix = deliveryUnit.getPathPrefix();
        this.batchInterval = deliveryUnit.getBatchInterval();
        initializeTransients();
        System.out.println("S3 initialized with Bucket Name: " + this.bucketName);
    }

    private void initializeTransients() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initializeTransients();
    }

    @Override
    public void getSinkProvider() {
        // Configure S3 properties
        Configuration config = new Configuration();
        
        // Set S3 access configuration
        config.setString("fs.s3a.endpoint", endpoint);
        config.setString("fs.s3a.access.key", accessKey);
        config.setString("fs.s3a.secret.key", secretKey);
        
        // Enable path style access if using LocalStack or similar
        config.setString("fs.s3a.path.style.access", "true");
        
        // Set the filesystem scheme to s3a for the bucket
        String outputPath = String.format("s3a://%s/%s", bucketName, pathPrefix);
        
        // Configure the rolling policy
        DefaultRollingPolicy<JsonNode, String> rollingPolicy = DefaultRollingPolicy.builder()
                .withRolloverInterval(TimeUnit.MINUTES.toMillis(
                        Long.parseLong(batchInterval)))
                .withInactivityInterval(TimeUnit.MINUTES.toMillis(5))
                .withMaxPartSize(128 * 1024 * 1024) // 128 MB
                .build();

        // Create an encoder that writes JSON with newlines
        Encoder<JsonNode> jsonEncoder = new Encoder<JsonNode>() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void encode(JsonNode element, OutputStream stream) throws IOException {
                // Ensure serializer is initialized
                initializeTransients();
                
                byte[] serialized = objectMapper.writeValueAsBytes(element);
                if (serialized != null && serialized.length > 0) {
                    stream.write(serialized);
                    stream.write('\n');  // Add newline for better readability
                }
            }
        };

        // Create the FileSink with JSON serialization
        this.internalFlinkS3Sink = FileSink
                .forRowFormat(new Path(outputPath), jsonEncoder)
                .withBucketAssigner(new DateTimeBucketAssigner<>("yyyy-MM-dd--HH"))
                .withRollingPolicy(rollingPolicy)
                .build();
    }

    // Serializable MapFunction for timestamp handling
    private static class TimestampMapper implements MapFunction<JsonNode, JsonNode>, Serializable {
        private static final long serialVersionUID = 1L;
        private transient ObjectMapper mapper;

        private void initMapper() {
            if (mapper == null) {
                mapper = new ObjectMapper();
            }
        }

        @Override
        public JsonNode map(JsonNode event) {
            initMapper();
            ObjectNode node = (ObjectNode) event;
            if (!node.has("timestamp")) {
                node.put("timestamp", System.currentTimeMillis());
            }
            return node;
        }
    }

    // Key selector for partitioning based on event_type
    private static class EventTypeKeySelector implements KeySelector<JsonNode, String>, Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public String getKey(JsonNode event) {
            return event.has("event_type") ? event.get("event_type").asText() : "default";
        }
    }

    @Override
    public void writeData(DataStream<JsonNode> stream) {
        if (internalFlinkS3Sink == null) {
            System.err.println("S3: Flink S3 Sink is not initialized. Call getSinkProvider() before writeData().");
            throw new IllegalStateException("Flink S3 Sink not initialized. Call getSinkProvider() first.");
        }

        // Add a timestamp if not present and write to S3
        stream.map(new TimestampMapper())
              .returns(new TypeHint<JsonNode>(){})
              .keyBy(new EventTypeKeySelector())
              .sinkTo(internalFlinkS3Sink)
              .name("S3 Sink - " + bucketName);
    }
} 