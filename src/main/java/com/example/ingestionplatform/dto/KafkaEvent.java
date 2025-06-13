package com.example.ingestionplatform.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class KafkaEvent implements Serializable {
    public String id;
    public String action;
    public String partitionKey = null;
    public Map<String, Object> currentEvent = new HashMap<>();
    public Map<String, Object> oldEvent = new HashMap<>();
    public String timestamp = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC).format(Instant.now());
} 