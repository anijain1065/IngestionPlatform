package com.example.ingestionplatform.sinks;

// Import Flink classes needed for the writeData method signature
import org.apache.flink.streaming.api.datastream.DataStream;

public interface DataSink<T> {
    // Define methods for data sinks, e.g., connect, write, etc.
    void getSinkProvider();

    /**
     * Writes a Flink DataStream of RowData to the sink.
     * The implementation will depend on the specific sink type (e.g., Kafka, Iceberg)
     * and will typically involve using a Flink sink connector.
     *
     * @param stream The Flink DataStream<RowData> to be written.
     */
    void writeData(DataStream<T> stream);

} 