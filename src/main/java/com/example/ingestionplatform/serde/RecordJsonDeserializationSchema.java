package com.example.ingestionplatform.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class RecordJsonDeserializationSchema<T>
        implements DeserializationSchema<T> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> targetType; // You need to tell it what type to deserialize into

    public RecordJsonDeserializationSchema(Class<T> targetType) { // Constructor to specify the type
        this.targetType = targetType;
    }

    @Override
    public T deserialize(byte[] bytes) throws IOException {
        // Assumes your producer emitted JSON of the AWS SDK Record class
        return mapper.readValue(bytes, this.targetType);
    }

    @Override
    public boolean isEndOfStream(T next) {
        return false;
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return TypeInformation.of(this.targetType);
    }
}