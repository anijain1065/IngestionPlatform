package com.example.ingestionplatform.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonDeserializationSchema<T>
        implements DeserializationSchema<T> {

    private static final Logger LOG = LoggerFactory.getLogger(JsonDeserializationSchema.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> targetType; // You need to tell it what type to deserialize into

    public JsonDeserializationSchema(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public T deserialize(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            LOG.error("Received null or empty byte array");
            throw new IOException("Received null or empty byte array");
        }

        String jsonString = new String(bytes, StandardCharsets.UTF_8);
        LOG.debug("Attempting to deserialize JSON: {}", jsonString);

        try {
            // First parse as JsonNode to validate JSON structure
            JsonNode jsonNode = mapper.readTree(bytes);
            if (!jsonNode.isObject()) {
                LOG.error("Received non-object JSON: {}", jsonString);
                throw new IOException("Received non-object JSON");
            }

            // Then try to convert to target type
            return mapper.convertValue(jsonNode, this.targetType);
        } catch (IOException e) {
            LOG.error("Failed to parse JSON: {}", jsonString, e);
            throw e;
        } catch (IllegalArgumentException e) {
            LOG.error("Failed to convert JSON to target type {}: {}", targetType.getName(), jsonString, e);
            throw new IOException("Failed to convert JSON to target type: " + e.getMessage(), e);
        }
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
