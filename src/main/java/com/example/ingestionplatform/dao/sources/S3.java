package com.example.ingestionplatform.dao.sources;

import com.example.ingestionplatform.dao.ingestion_unit.S3IngestionUnit;
import com.example.ingestionplatform.model.enums.SourceType;

public class S3 extends Source {
    public S3() {
        super(SourceType.S3);
    }

    private String bucketName;
    private String region;
    private String accessKeyId;
    private String secretAccessKey;
    private S3IngestionUnit s3IngestionUnit;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public S3IngestionUnit getS3IngestionUnit() {
        return s3IngestionUnit;
    }

    public void setS3IngestionUnit(S3IngestionUnit s3IngestionUnit) {
        this.s3IngestionUnit = s3IngestionUnit;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public String toString() {
        return "S3{" +
                "bucketName='" + bucketName + '\'' +
                ", region='" + region + '\'' +
                ", accessKeyId='" + accessKeyId + '\'' +
                ", secretAccessKey='" + secretAccessKey + '\'' +
                ", s3IngestionUnit=" + s3IngestionUnit +
                '}';
    }
}
