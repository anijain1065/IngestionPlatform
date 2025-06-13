package com.example.ingestionplatform.sources;

import com.example.ingestionplatform.dao.sources.Kafka;
import com.example.ingestionplatform.dto.KafkaEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.ingestionplatform.serde.JsonDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSourceBuilder;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class KafkaSource<T> implements DataSource<T> {

    private final String bootstrapServers;
    private final String topic;
    private final Class<T> targetType;

    private org.apache.flink.connector.kafka.source.KafkaSource<T> internalFlinkKafkaSource;

    @SuppressWarnings("unchecked")
    public KafkaSource(Kafka kafka) {
        this.bootstrapServers = kafka.getBootstrapServers();
        this.topic = kafka.getKafkaIngestionUnit().getTopic();
        
        // Determine the target type based on the jsonType
        switch (kafka.getJsonType()) {
            case "CDC":
                this.targetType = (Class<T>) KafkaEvent.class;
                break;
            case "clickstream":
                this.targetType = (Class<T>) JsonNode.class;
                break;
            default:
                this.targetType = (Class<T>) String.class;
        }
    }

    @Override
    public void getSourceProvider() {
        System.out.println("Kafka: getSourceProvider() called for topic: " + this.topic);
        if (this.internalFlinkKafkaSource == null) {
            System.out.println("Kafka: Initializing internal Flink Kafka<T> for topic: " + this.topic);
            String consumerGroupId = "consumer-group-id-" + this.topic;

            KafkaSourceBuilder<T> builder = org.apache.flink.connector.kafka.source.KafkaSource.<T>builder()
                    .setBootstrapServers(this.bootstrapServers)
                    .setTopics(this.topic)
                    .setGroupId(consumerGroupId)
                    .setStartingOffsets(OffsetsInitializer.latest())
                    .setValueOnlyDeserializer(new JsonDeserializationSchema<>(this.targetType));

            this.internalFlinkKafkaSource = builder.build();
            System.out.println("Kafka: Internal Flink Kafka<T> initialized for topic: " + this.topic);
        } else {
            System.out.println("Kafka: Using already initialized internal Flink Kafka<T> for topic: " + this.topic);
        }
    }

    @Override
    public DataStream<T> getDataStream(StreamExecutionEnvironment env) {
        System.out.println("Kafka: getDataStream() called for topic: " + this.topic);
        if (env == null) {
            throw new IllegalArgumentException("StreamExecutionEnvironment cannot be null.");
        }

        if (this.internalFlinkKafkaSource == null) {
            System.out.println("Kafka: internalFlinkKafkaSource was null in getDataStream, attempting to initialize via getSourceProvider...");
            getSourceProvider();
            if (this.internalFlinkKafkaSource == null) {
                throw new IllegalStateException("Kafka: Flink Kafka (internalFlinkKafkaSource) was not initialized even after calling getSourceProvider. Check getSourceProvider logic and ensure it's called by IngestionJob.run().");
            }
        }

        DataStream<T> kafkaStream = env.fromSource(
                this.internalFlinkKafkaSource,
                WatermarkStrategy.noWatermarks(),
                "Flink Kafka Source: " + this.topic
        );

        System.out.println("Kafka: Returning DataStream<T> for topic: " + this.topic);
        return kafkaStream;
    }
}