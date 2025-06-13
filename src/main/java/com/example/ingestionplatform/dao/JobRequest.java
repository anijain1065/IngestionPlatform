package com.example.ingestionplatform.dao;

import com.example.ingestionplatform.dao.sinks.Sink;
import com.example.ingestionplatform.dao.sources.Source;

public class JobRequest {
    private String jobId;
    private String mode;
    private Source source;
    private Sink sink;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Sink getSink() {
        return sink;
    }

    public void setSink(Sink sink) {
        this.sink = sink;
    }

    @Override
    public String toString() {
        return "JobRequest{" +
                "jobId='" + jobId + '\'' +
                ", mode='" + mode + '\'' +
                ", source=" + source +
                ", sink=" + sink +
                '}';
    }
}