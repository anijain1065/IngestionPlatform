package com.example.ingestionplatform.dao.sinks;

import com.example.ingestionplatform.model.enums.SinkType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Kafka.class, name = "KAFKA"),
        @JsonSubTypes.Type(value = S3.class, name = "S3"),
})
public class Sink implements Serializable {
    public Sink(SinkType type) {
        this.type = type;
    }
    private SinkType type;

    public SinkType getType() {
        return type;
    }
}