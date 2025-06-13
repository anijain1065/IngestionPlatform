package com.example.ingestionplatform.dao.ingestion_unit;

import com.example.ingestionplatform.dao.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

public class S3IngestionUnit {
    @JsonProperty("keyPrefix")
    private String keyPrefix;
    
    @JsonProperty("fileFormat")
    private String fileFormat;
    
    @JsonProperty("schema")
    private Schema schema;

    // Default constructor
    public S3IngestionUnit() {}

    // Getters and setters
    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "S3IngestionUnit{" +
                "keyPrefix='" + keyPrefix + '\'' +
                ", fileFormat='" + fileFormat + '\'' +
                ", schema=" + schema +
                '}';
    }
} 