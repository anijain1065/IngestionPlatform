package com.example.ingestionplatform.dao.delivery_unit;

import com.example.ingestionplatform.dao.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KafkaDeliveryUnit {

    // Default constructor
    public KafkaDeliveryUnit() {}

    @JsonProperty("topic")
    private String topic;
    
    @JsonProperty("kafkaProducerProperties")
    private Object kafkaProducerProperties;
    
    @JsonProperty("schema")
    private Schema schema;

    // Getters and setters
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Object getKafkaProducerProperties() {
        return kafkaProducerProperties;
    }

    public void setKafkaProducerProperties(Object kafkaProducerProperties) {
        this.kafkaProducerProperties = kafkaProducerProperties;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "KafkaDeliveryUnit{" +
                "topic='" + topic + '\'' +
                ", kafkaProducerProperties=" + kafkaProducerProperties +
                ", schema=" + schema +
                '}';
    }
} 