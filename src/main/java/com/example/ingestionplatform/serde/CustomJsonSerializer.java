package com.example.ingestionplatform.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.api.common.serialization.SerializationSchema;
import java.io.Serializable;

public class CustomJsonSerializer<T> implements SerializationSchema<T>, Serializable {
    private static final long serialVersionUID = 1L;

    private transient ObjectMapper objectMapper;
    private transient ObjectWriter objectWriter;

    public CustomJsonSerializer() {
        initializeMappers();
    }

    private void initializeMappers() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
        if (this.objectWriter == null) {
            this.objectWriter = this.objectMapper.writer();
        }
    }

    private Object readResolve() throws java.io.ObjectStreamException {
        initializeMappers();
        return this;
    }

    @Override
    public byte[] serialize(T element) {
        initializeMappers();
        if (element == null) {
            return null;
        }

        try {
            return this.objectWriter.writeValueAsBytes(element);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            System.err.println("Failed to serialize object of type " + element.getClass().getName() + " to JSON: " + e.getMessage());
            return new byte[0];
        } catch (Exception e) {
            System.err.println("Error during custom serialization via ToJsonConverter: " + e.getMessage());
            e.printStackTrace();
            return new byte[0];
        }
    }
}