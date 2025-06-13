package com.example.ingestionplatform.factory.flink;


import com.example.ingestionplatform.dao.sinks.Kafka;
import com.example.ingestionplatform.dao.sinks.S3;
import com.example.ingestionplatform.dao.sinks.Sink;
import com.example.ingestionplatform.sinks.DataSink;
import com.example.ingestionplatform.sinks.KafkaSink;
import com.example.ingestionplatform.sinks.S3Sink;

public class DataSinkFactory {

    public <T> DataSink<T> dataSinkProvider(Sink sink) {
        if (sink == null) {
            throw new IllegalArgumentException("Sink cannot be null");
        }
        switch (sink.getType()) {
            case KAFKA:
                if (!(sink instanceof Kafka)) {
                    throw new IllegalArgumentException("Expected Kafka sink");
                }
                return (DataSink<T>) new KafkaSink((Kafka) sink);
            case S3:
                if (!(sink instanceof S3)) {
                    throw new IllegalArgumentException("Expected Iceberg sink");
                }
                return (DataSink<T>) new S3Sink((S3) sink);
            default:
                System.err.println("DataSinkFactory: Unknown sink type: " + sink.getType());
                return null;
        }
    }
} 