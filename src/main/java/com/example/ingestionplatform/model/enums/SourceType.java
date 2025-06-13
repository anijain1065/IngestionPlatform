package com.example.ingestionplatform.model.enums;

public enum SourceType {
    KAFKA("KAFKA"),
    S3("S3");

    private final String type;

    SourceType(String type) {
        this.type = type;
    }

}