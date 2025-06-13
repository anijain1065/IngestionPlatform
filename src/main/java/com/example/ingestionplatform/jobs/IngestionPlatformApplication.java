package com.example.ingestionplatform.jobs;

import com.example.ingestionplatform.dao.JobRequest;
import com.example.ingestionplatform.factory.flink.DataSinkFactory;
import com.example.ingestionplatform.factory.flink.DataSourceFactory;
import com.example.ingestionplatform.sinks.DataSink;
import com.example.ingestionplatform.sources.DataSource;
import com.example.ingestionplatform.dto.S3Event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.ingestionplatform")
public class IngestionPlatformApplication extends IngestionJob<Object> {
    
    private static final Logger logger = LoggerFactory.getLogger(IngestionPlatformApplication.class);

    private JobRequest jobRequest;
    private DataSourceFactory dataSourceFactory;
    private DataSinkFactory dataSinkFactory;

    public IngestionPlatformApplication() {
        // Default constructor for Spring Boot
        this.dataSourceFactory = new DataSourceFactory();
        this.dataSinkFactory = new DataSinkFactory();
    }

    public IngestionPlatformApplication(JobRequest jobRequest) {
        this(jobRequest, new DataSourceFactory(), new DataSinkFactory());
    }

    public IngestionPlatformApplication(JobRequest jobRequest, DataSourceFactory dataSourceFactory, DataSinkFactory dataSinkFactory) {
        this.jobRequest = jobRequest;
        this.dataSourceFactory = dataSourceFactory;
        this.dataSinkFactory = dataSinkFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected DataSource<Object> createSource() {
        logger.info("Creating source for job ID: {}", jobRequest.getJobId());
        // The source type will be determined by the source configuration
        DataSource<?> source = dataSourceFactory.getDataSource(jobRequest.getSource());
        if (source == null) {
            throw new RuntimeException("Failed to create source for type: " + jobRequest.getSource().getType());
        }
        return (DataSource<Object>) source;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected DataSink<Object> createSink() {
        logger.info("Creating sink for job ID: {}", jobRequest.getJobId());
        // The sink type will be determined by the sink configuration
        DataSink<?> sink = dataSinkFactory.dataSinkProvider(jobRequest.getSink());
        if (sink == null) {
            throw new RuntimeException("Failed to create sink for type: " + jobRequest.getSink().getType());
        }
        return (DataSink<Object>) sink;
    }

    public static void main(String[] args) {
        // Start Spring Boot application
        SpringApplication.run(IngestionPlatformApplication.class, args);

        // Example job configuration
        String jsonResponse = """
                {
    "jobId": "kafka-to-s3-job-001",
    "mode": "STREAMING",
    "source": {
        "type": "KAFKA",
        "bootstrapServers": "localhost:9092",
        "jsonType": "clickstream",
        "kafkaIngestionUnit": {
            "topic": "clickstream_data",
            "metadata": "{\\"description\\": \\"Kafka source for clickstream data\\"}",
            "groupId": "flink-consumer-group-1",
            "schema": {
                "schemaId": 1,
                "schemaFieldList": [
                    {
                        "id": 1,
                        "fieldName": "event_id",
                        "dataType": "VARCHAR",
                        "nullable": false,
                        "isPrimaryKey": "true",
                        "defaultValue": null
                    },
                    {
                        "id": 2,
                        "fieldName": "timestamp",
                        "dataType": "BIGINT",
                        "nullable": false,
                        "isPrimaryKey": "false",
                        "defaultValue": null
                    },
                    {
                        "id": 3,
                        "fieldName": "user_id",
                        "dataType": "VARCHAR",
                        "nullable": false,
                        "isPrimaryKey": "false",
                        "defaultValue": null
                    },
                    {
                        "id": 4,
                        "fieldName": "event_type",
                        "dataType": "VARCHAR",
                        "nullable": false,
                        "isPrimaryKey": "false",
                        "defaultValue": null
                    },
                    {
                        "id": 5,
                        "fieldName": "data",
                        "dataType": "VARCHAR",
                        "nullable": true,
                        "isPrimaryKey": "false",
                        "defaultValue": null
                    }
                ]
            },
            "runtimeConfig": "{\\"maxRecordsPerFetch\\": 1000, \\"autoOffsetReset\\": \\"earliest\\"}"
        }
    },
    "sink": {
        "type": "S3",
        "bucketName": "clickstream",
        "s3DeliveryUnit": {
            "region": "us-east-1",
            "accessKeyId": "test",
            "secretAccessKey": "test",
            "endpoint": "http://localhost:4566",
            "pathPrefix": "raw-data/",
            "fileNamePattern": "clickstream-data-{part}-{ts}",
            "batchInterval": "60",
            "format": "json",
            "compressionCodec": "gzip"
        }
    }
}
                """;
        try {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            JobRequest jobRequest = objectMapper.readValue(jsonResponse, JobRequest.class);

            logger.info("Starting SourceToSinkJob with job ID: {}", jobRequest.getJobId());
            IngestionPlatformApplication app = new IngestionPlatformApplication(jobRequest);
            app.run();
            logger.info("SourceToSinkJob finished or submitted for job ID: {}", jobRequest.getJobId());
        } catch (Exception e) {
            logger.error("Error running SourceToSinkJob", e);
        }
    }
} 