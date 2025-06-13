package com.example.ingestionplatform.utils;

import com.example.ingestionplatform.dto.KafkaEvent;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.ExecutionConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer;

// Java utility types
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.TreeSet;

// Kryo-Serializers for special collection types
import de.javakaffee.kryoserializers.CollectionsEmptyListSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptyMapSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptySetSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonListSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonMapSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonSetSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

import java.util.concurrent.TimeUnit;

public class FlinkEnvUtils {

    public static void configure(StreamExecutionEnvironment env) {
        // Configure heartbeat settings
        Configuration config = new Configuration();
        config.setString("heartbeat.interval", "10000"); // 10 seconds
        config.setString("heartbeat.timeout", "50000"); // 50 seconds
        
        // Enable the Web UI
        config.setInteger(RestOptions.PORT, 8081);
        config.setString(RestOptions.BIND_PORT, "8081");
        config.setBoolean("web.submit.enable", true);
        
        // Set restart strategy
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
            3, // number of restart attempts
            Time.of(10, TimeUnit.SECONDS) // delay between attempts
        ));
        
        env.getConfig().setGlobalJobParameters(config);

        // Configure serialization
        env.getConfig().enableForceKryo();
        env.getConfig().enableObjectReuse();
        
        // Register types that need serialization
        env.getConfig().registerKryoType(KafkaEvent.class);
        env.getConfig().registerKryoType(JsonNode.class);

        // Disable collection serialization which can cause issues with Java modules
        env.getConfig().disableGenericTypes();

        // Register common Java utility types. These might be used by the Event or other operations.
        env.getConfig().getSerializerConfig().registerKryoType(java.util.Map.class);
        env.getConfig().getSerializerConfig().registerKryoType(HashMap.class);
        env.getConfig().getSerializerConfig().registerKryoType(LinkedHashMap.class);
        env.getConfig().getSerializerConfig().registerKryoType(TreeMap.class);
        env.getConfig().getSerializerConfig().registerKryoType(ArrayList.class);
        env.getConfig().getSerializerConfig().registerKryoType(HashSet.class);
        env.getConfig().getSerializerConfig().registerKryoType(TreeSet.class);
        env.getConfig().getSerializerConfig().registerKryoType(Object.class); // For the Map<String, Object> in DynamoDbEvent

        // --- Registrations for Unmodifiable and Special Collections using kryo-serializers ---
        env.getConfig().getSerializerConfig().addDefaultKryoSerializer(Collections.unmodifiableCollection(new ArrayList<>()).getClass(), UnmodifiableCollectionsSerializer.class);
        env.getConfig().getSerializerConfig().addDefaultKryoSerializer(Collections.unmodifiableList(new ArrayList<>()).getClass(), UnmodifiableCollectionsSerializer.class);
        env.getConfig().getSerializerConfig().addDefaultKryoSerializer(Collections.unmodifiableSet(new HashSet<>()).getClass(), UnmodifiableCollectionsSerializer.class);
        env.getConfig().getSerializerConfig().addDefaultKryoSerializer(Collections.unmodifiableMap(new HashMap<>()).getClass(), UnmodifiableCollectionsSerializer.class);
        env.getConfig().getSerializerConfig().addDefaultKryoSerializer(Collections.emptyList().getClass(), CollectionsEmptyListSerializer.class);
        env.getConfig().getSerializerConfig().addDefaultKryoSerializer(Collections.emptySet().getClass(), CollectionsEmptySetSerializer.class);
        env.getConfig().getSerializerConfig().addDefaultKryoSerializer(Collections.emptyMap().getClass(), CollectionsEmptyMapSerializer.class);
        env.getConfig().getSerializerConfig().addDefaultKryoSerializer(Collections.singletonList(null).getClass(), CollectionsSingletonListSerializer.class);
        env.getConfig().getSerializerConfig().addDefaultKryoSerializer(Collections.singleton(null).getClass(), CollectionsSingletonSetSerializer.class);
        env.getConfig().getSerializerConfig().addDefaultKryoSerializer(Collections.singletonMap(null, null).getClass(), CollectionsSingletonMapSerializer.class);
    }
} 