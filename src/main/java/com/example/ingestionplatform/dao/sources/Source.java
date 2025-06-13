package com.example.ingestionplatform.dao.sources;

import com.example.ingestionplatform.model.enums.SourceType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Kafka.class, name = "KAFKA"),
})
public class Source implements Serializable {
    public Source(){}
    public Source(SourceType sourceType){
        this.type = sourceType;
    }
    private SourceType type;

    public SourceType getType() {
        return type;
    }

    public void setType(SourceType type) {
        this.type = type;
    }
}
