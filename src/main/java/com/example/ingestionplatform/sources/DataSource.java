package com.example.ingestionplatform.sources;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public interface DataSource<T> {
    // Define methods for data sources, e.g., connect, read, etc.
    void getSourceProvider(); // As suggested by --getSourceClient in DataSourceFactory

    /**
     * Reads data from the source and returns it as a Flink DataStream of type T.
     *
     * @param env The Flink StreamExecutionEnvironment.
     * @return A DataStream<T> representing the data from the source.
     */
    DataStream<T> getDataStream(StreamExecutionEnvironment env);
}