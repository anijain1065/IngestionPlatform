package com.example.ingestionplatform.dao.sinks;

import com.example.ingestionplatform.dao.delivery_unit.S3DeliveryUnit;
import com.example.ingestionplatform.model.enums.SinkType;

public class S3 extends Sink {
    public S3() {
        super(SinkType.S3);
    }

    private String bucketName;
    private S3DeliveryUnit s3DeliveryUnit;

    public S3DeliveryUnit getS3DeliveryUnit() {
        return s3DeliveryUnit;
    }

    public void setS3DeliveryUnit(S3DeliveryUnit s3DeliveryUnit) {
        this.s3DeliveryUnit = s3DeliveryUnit;
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
                ", s3DeliveryUnit=" + s3DeliveryUnit +
                '}';
    }
}