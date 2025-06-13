package com.example.ingestionplatform.jobs;

import com.example.ingestionplatform.sources.DataSource;
import com.example.ingestionplatform.utils.FlinkEnvUtils;
import com.example.ingestionplatform.sinks.DataSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public abstract class IngestionJob<T> {
    protected abstract DataSource<T> createSource();
    protected abstract DataSink<T> createSink();

    public final void run() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Configure Flink environment using the new FlinkEnvUtils class
        FlinkEnvUtils.configure(env);

        DataSource<T> source = createSource();
        source.getSourceProvider();

        DataSink<T> sink = createSink();
        sink.getSinkProvider();

        DataStream<T> sourceStream = source.getDataStream(env);

        sink.writeData(sourceStream);

        // Execute the Flink job
        try {
            String jobName = this.getClass().getSimpleName(); 
            System.out.println("IngestionJob: Submitting Flink job: " + jobName);
            env.execute(jobName);
            System.out.println("IngestionJob: Flink job '" + jobName + "' execution submitted successfully.");
        } catch (Exception e) {
            System.err.println("IngestionJob: Error executing Flink job '" + this.getClass().getSimpleName() + "'.");
            e.printStackTrace();
        }
    }
} 