package com.example.ingestionplatform.dao.ingestion_unit;

import com.example.ingestionplatform.dao.Schema;

public class KafkaIngestionUnit {

    // Default constructor
    public KafkaIngestionUnit() {}

    private String topic;
    private String metadata;
    private String groupId;
    private Schema schema;
    private String runtimeConfig;

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public String getRuntimeConfig() {
        return runtimeConfig;
    }

    public void setRuntimeConfig(String runtimeConfig) {
        this.runtimeConfig = runtimeConfig;
    }

    @Override
    public String toString() {
        return "KafkaIngestionUnit{" +
                "topic='" + topic + '\'' +
                ", metadata='" + metadata + '\'' +
                ", groupId='" + groupId + '\'' +
                ", schema=" + schema +
                ", runtimeConfig='" + runtimeConfig + '\'' +
                '}';
    }

} 