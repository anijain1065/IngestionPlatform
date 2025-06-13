#!/bin/bash

# Configuration
KAFKA_BROKER="localhost:9092"
TOPIC_NAME="clickstream_data"
EVENT_TYPES=("page_view" "click" "scroll" "form_submit" "video_play")
INTERVAL=1  # seconds between messages

# Function to generate a random string
random_string() {
    LC_ALL=C tr -dc 'a-zA-Z0-9' < /dev/urandom | fold -w ${1:-32} | head -n 1
}

# Function to generate a random user ID (format: user_XXXX)
random_user_id() {
    printf "user_%04d" $((RANDOM % 1000))
}

# Function to generate random JSON data based on event type
random_data() {
    local event_type=$1
    case $event_type in
        "page_view")
            echo "{\"page\":\"/$(random_string 2)\",\"referrer\":\"$(random_string 3)\"}"
            ;;
        "click")
            echo "{\"element_id\":\"btn_$(random_string 1)\",\"position\":{\"x\":$((RANDOM % 1000)),\"y\":$((RANDOM % 1000))}}"
            ;;
        "scroll")
            echo "{\"scroll_depth\":$((RANDOM % 100)),\"page_height\":$((RANDOM % 2000 + 1000))}"
            ;;
        "form_submit")
            if [ $((RANDOM % 2)) -eq 0 ]; then
                echo "{\"form_id\":\"form_$(random_string 4)\",\"success\":true}"
            else
                echo "{\"form_id\":\"form_$(random_string 4)\",\"success\":false}"
            fi
            ;;
        "video_play")
            if [ $((RANDOM % 2)) -eq 0 ]; then
                echo "{\"video_id\":\"vid_$(random_string 4)\",\"duration\":$((RANDOM % 300)),\"quality\":\"HD\"}"
            else
                echo "{\"video_id\":\"vid_$(random_string 4)\",\"duration\":$((RANDOM % 300)),\"quality\":\"SD\"}"
            fi
            ;;
    esac
}

# Function to send message to Kafka using Docker
send_to_kafka() {
    local message="$1"
    echo -n "$message" | docker exec -i kafka kafka-console-producer --broker-list $KAFKA_BROKER --topic $TOPIC_NAME
}

# Main loop to generate and send messages
echo "Starting to generate clickstream data..."
echo "Press Ctrl+C to stop"

while true; do
    # Generate event data
    event_id=$(random_string 3)
    timestamp=$(date +%s%N | cut -b1-13)  # Current timestamp in milliseconds
    user_id=$(random_user_id)
    event_type=${EVENT_TYPES[$RANDOM % ${#EVENT_TYPES[@]}]}
    data=$(random_data "$event_type")
    
    # Create JSON message as a single line
    message=$(printf '{"event_id":"%s","timestamp":%s,"user_id":"%s","event_type":"%s","data":%s}\n' \
        "$event_id" \
        "$timestamp" \
        "$user_id" \
        "$event_type" \
        "$data")
    
    # Send to Kafka
    send_to_kafka "$message"
    echo "Sent: $message"
    
    # Wait for the specified interval
    sleep $INTERVAL
done 