package com.cafecoder.dynamictpsallocation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstanceSetService {

    private static final String PROCESSOR_SET_KEY = "processor_test_pod_names";

    private final RedisService redisService;
    /**
     * Add a pod hostname to the Redis set.
     *
     * @param podName the pod hostname
     */
    public void addPodName(String podName) {
        redisService.addToSet(PROCESSOR_SET_KEY, podName);
    }
    /**
     * Remove a pod hostname from the Redis set.
     *
     * @param podName the pod hostname
     */
    public void removePodName(String podName) {
        redisService.removeFromSet(PROCESSOR_SET_KEY, podName);
    }

    /**
     * Get the count of active pod hostnames in the Redis set.
     *
     * @return the size of the Redis set
     */
    public Long getActivePodCount() {
        return redisService.getSetSize(PROCESSOR_SET_KEY);
    }

    /**
     * Get all active pod hostnames from the Redis set.
     *
     * @return a set of pod hostnames
     */
    public Set<String> getAllPodNames() {
        Set<Object> objects = redisService.getSetMembers(PROCESSOR_SET_KEY);
        if (objects == null) {
            return Set.of(); // Return an empty set if Redis returns null
        }
        // Cast objects to strings
        return objects.stream()
                .map(String::valueOf) // Ensures conversion to String
                .collect(Collectors.toSet());
    }

}
