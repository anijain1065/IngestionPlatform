package com.example.ingestionplatform.dao.delivery_unit;

import com.example.ingestionplatform.dao.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

public class S3DeliveryUnit {
    // Default constructor
    public S3DeliveryUnit() {}

    @JsonProperty("region")
    private String region;

    @JsonProperty("accessKeyId")
    private String accessKeyId;

    @JsonProperty("secretAccessKey")
    private String secretAccessKey;

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("pathPrefix")
    private String pathPrefix;

    @JsonProperty("fileNamePattern")
    private String fileNamePattern;

    @JsonProperty("batchInterval")
    private String batchInterval;
    
    @JsonProperty("format")
    private String format;

    @JsonProperty("compressionCodec")
    private String compressionCodec;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    public String getBatchInterval() {
        return batchInterval;
    }

    public void setBatchInterval(String batchInterval) {
        this.batchInterval = batchInterval;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCompressionCodec() {
        return compressionCodec;
    }

    public void setCompressionCodec(String compressionCodec) {
        this.compressionCodec = compressionCodec;
    }

    @Override
    public String toString() {
        return "S3DeliveryUnit{" +
                "region='" + region + '\'' +
                ", accessKeyId='" + accessKeyId + '\'' +
                ", secretAccessKey='" + secretAccessKey + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", pathPrefix='" + pathPrefix + '\'' +
                ", fileNamePattern='" + fileNamePattern + '\'' +
                ", batchInterval='" + batchInterval + '\'' +
                ", format='" + format + '\'' +
                ", compressionCodec='" + compressionCodec + '\'' +
                '}';
    }
}