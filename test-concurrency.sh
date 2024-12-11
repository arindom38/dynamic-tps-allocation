#!/bin/bash

# The URL to be accessed
URL="http://192.168.49.2:30004/processor/call-test-module"

# Number of concurrent requests
CONCURRENT_REQUESTS=10

# Function to execute the curl command
run_curl() {
    curl -X GET "$URL" &
}

# Run the curl command concurrently
for ((i=1; i<=CONCURRENT_REQUESTS; i++)); do
    run_curl
    echo "Request $i sent"
done

# Wait for all background jobs to complete
wait

echo "All $CONCURRENT_REQUESTS requests have been sent."
