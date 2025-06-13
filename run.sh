#!/bin/bash

echo "Starting Ingestion Platform Spring Boot Application..."
echo "The application will be available at: http://localhost:8080"
echo ""
echo "Available endpoints:"
echo "  - Health Check: http://localhost:8080/api/health"
echo "  - App Info:     http://localhost:8080/api/info"
echo "  - Actuator:     http://localhost:8080/actuator/health"
echo "  - H2 Console:   http://localhost:8080/h2-console"
echo ""
echo "Press Ctrl+C to stop the application"
echo ""

./mvnw spring-boot:run 