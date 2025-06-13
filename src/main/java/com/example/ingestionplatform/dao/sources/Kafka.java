package com.example.ingestionplatform.dao.sources;

import com.example.ingestionplatform.dao.ingestion_unit.KafkaIngestionUnit;
import com.example.ingestionplatform.model.enums.SourceType;

public class Kafka extends Source {

    public Kafka() {
        super(SourceType.KAFKA);
    }

    private String bootstrapServers;
    private String jsonType;
    private KafkaIngestionUnit kafkaIngestionUnit;


    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public KafkaIngestionUnit getKafkaIngestionUnit() {
        return kafkaIngestionUnit;
    }

    public void setKafkaIngestionUnit(KafkaIngestionUnit kafkaIngestionUnit) {
        this.kafkaIngestionUnit = kafkaIngestionUnit;
    }

    public String getJsonType() {
        return jsonType;
    }

    public void setJsonType(String jsonType) {
        this.jsonType = jsonType;
    }

}
