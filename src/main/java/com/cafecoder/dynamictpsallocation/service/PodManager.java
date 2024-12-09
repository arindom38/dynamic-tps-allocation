package com.cafecoder.dynamictpsallocation.service;

import com.cafecoder.dynamictpsallocation.utill.QueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PodManager {

    private static final String PROCESSOR_SET_KEY = "processor_test_pod_names";
    private double targetTps; // Target TPS for the application
    private final RedisService redisService;

    public PodManager(RedisService redisService,@Value("${processor.target-tps}") double targetTps) {
        this.redisService = redisService;
        this.targetTps = targetTps;
    }

    public void updateTargetTps(double updatedValue){
        this.targetTps = updatedValue;
        log.info("Target Tps value is updated to {}",targetTps);
    }

    public void updateRateLimiter(){
        QueueUtil.updateRateLimiter(targetTps,getPodCount());
    }

    /**
     * Fetches the current pod count from Redis.
     */
    public Integer getPodCount() {
        return (int) redisService.getSetSize(PROCESSOR_SET_KEY);
    }


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
