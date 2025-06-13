package com.example.ingestionplatform.sinks;

import com.example.ingestionplatform.dao.delivery_unit.KafkaDeliveryUnit;
import com.example.ingestionplatform.dao.sinks.Kafka;
import com.example.ingestionplatform.dto.KafkaEvent;
import com.example.ingestionplatform.serde.CustomJsonSerializer;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.connector.base.DeliveryGuarantee;

public class KafkaSink implements DataSink<KafkaEvent> {

    private final String bootstrapServers;
    private final String topic;

    private org.apache.flink.connector.kafka.sink.KafkaSink<KafkaEvent> internalFlinkKafkaSink;

    public KafkaSink(Kafka kafka) {
        KafkaDeliveryUnit kafkaDeliveryUnit = kafka.getKafkaDeliveryUnit();
        System.out.println("Kafka initialized with BootstrapServers: " + kafka.getBootstrapServers() + ", Topic: " + kafkaDeliveryUnit.getTopic());
        if (kafka.getBootstrapServers() == null || kafka.getBootstrapServers().trim().isEmpty()) {
            throw new IllegalArgumentException("Bootstrap servers cannot be null or empty.");
        }
        if (kafkaDeliveryUnit.getTopic() == null || kafkaDeliveryUnit.getTopic().trim().isEmpty()) {
            throw new IllegalArgumentException("Topic cannot be null or empty.");
        }
        this.bootstrapServers = kafka.getBootstrapServers();
        this.topic = kafkaDeliveryUnit.getTopic();
    }

    @Override
    public void getSinkProvider() {
        System.out.println("Kafka: getSinkProvider() called for topic: " + this.topic);

        KafkaRecordSerializationSchema<KafkaEvent> serializationSchema = KafkaRecordSerializationSchema.<KafkaEvent>builder()
                .setTopic(this.topic) // Use the topic passed in constructor
                .setValueSerializationSchema(new CustomJsonSerializer<>())
                .build();

        this.internalFlinkKafkaSink = org.apache.flink.connector.kafka.sink.KafkaSink.<KafkaEvent>builder()
                .setBootstrapServers(bootstrapServers)
                .setRecordSerializer(serializationSchema)
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }

    @Override
    public void writeData(DataStream<KafkaEvent> stream) {
        if (internalFlinkKafkaSink == null) {
            // Ensure getSinkProvider() has been called, which is typically done by IngestionJob
            System.err.println("Kafka: Flink Kafka Sink is not initialized. Call getSinkProvider() before writeData().");
            throw new IllegalStateException("Flink Kafka Sink not initialized. Call getSinkProvider() first.");
        }

        if (stream.getType().getTypeClass().equals(KafkaEvent.class)) {
            DataStream<KafkaEvent> kafkaEventStream = stream;
            kafkaEventStream
                    .filter(r -> r.partitionKey != null) // Filter out events with null partitionKey for keyBy
                    .keyBy(r -> r.partitionKey)
                    .sinkTo(this.internalFlinkKafkaSink).name("Kafka Generic Sink for topic: " + this.topic);

            // For events with null partitionKey, send directly to sink without keyBy
            kafkaEventStream
                    .filter(r -> r.partitionKey == null)
                    .sinkTo(this.internalFlinkKafkaSink).name("Kafka Generic Sink for topic (no keyBy): " + this.topic);

        } else {
            stream.sinkTo(this.internalFlinkKafkaSink).name("Kafka Generic Sink for topic: " + this.topic);
        }
    }
}