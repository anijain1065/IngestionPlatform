package com.example.ingestionplatform.model.enums;

public enum SinkType {

    KAFKA("KAFKA"),
    S3("S3");

    private final String type;

    SinkType(String type) {
        this.type = type;
    }
}
