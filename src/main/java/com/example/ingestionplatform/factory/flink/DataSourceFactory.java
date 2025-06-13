package com.example.ingestionplatform.factory.flink;

import com.example.ingestionplatform.dao.sources.*;
import com.example.ingestionplatform.sources.DataSource;
import com.example.ingestionplatform.sources.KafkaSource;


public class DataSourceFactory {
    @SuppressWarnings("unchecked")
    public <T> DataSource<T> getDataSource(Source source) {
        if (source == null) {
            return null;
        }

        switch (source.getType().name()) {
            case "KAFKA":
                if (!(source instanceof Kafka)) {
                    throw new IllegalArgumentException("Expected KafkaIngestionUnit for Kafka source");
                }
                return (DataSource<T>) new KafkaSource((Kafka) source);
            default:
                System.err.println("DataSourceFactory: Unknown source type: " + source.getType());
                return null;
        }
    }
} 