package com.example.ingestionplatform.dto;

import com.example.ingestionplatform.dao.delivery_unit.S3DeliveryUnit;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

public class S3Event implements Serializable {
    @JsonProperty("data")
    private String data;

    @JsonProperty("partitionKey")
    private String partitionKey;

    @JsonProperty("timestamp")
    private Long timestamp;

    // Default constructor required for Jackson
    public S3Event() {}

    public S3Event(String data, String partitionKey, Long timestamp) {
        this.data = data;
        this.partitionKey = partitionKey;
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "S3Event{" +
                "data='" + data + '\'' +
                ", partitionKey='" + partitionKey + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
} 