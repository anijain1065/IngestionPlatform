package com.example.ingestionplatform.dao.sinks;

import com.example.ingestionplatform.dao.delivery_unit.KafkaDeliveryUnit;
import com.example.ingestionplatform.model.enums.SinkType;

public class Kafka extends Sink {
    public Kafka() {
        super(SinkType.KAFKA);
    }

    private String bootstrapServers;
    private KafkaDeliveryUnit kafkaDeliveryUnit;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public KafkaDeliveryUnit getKafkaDeliveryUnit() {
        return kafkaDeliveryUnit;
    }

    public void setKafkaDeliveryUnit(KafkaDeliveryUnit kafkaDeliveryUnit) {
        this.kafkaDeliveryUnit = kafkaDeliveryUnit;
    }

    @Override
    public String toString() {
        return "Kafka{" +
                "bootstrapServers='" + bootstrapServers + '\'' +
                ", kafkaDeliveryUnit=" + kafkaDeliveryUnit +
                '}';
    }
}