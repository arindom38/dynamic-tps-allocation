package com.cafecoder.dynamictpsallocation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstanceLifecycleHandler implements SmartLifecycle {

    private final InstanceSetService instanceSetService;

    private String podName;
    private boolean running = false;

    @Override
    public void start() {
        try {
            podName = InetAddress.getLocalHost().getHostName();
            instanceSetService.addPodName(podName);
            log.info("Pod {} added to Redis set.", podName);
            running = true;
        } catch (Exception e) {
            log.error("Error adding pod to Redis set: {}", e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        if (podName != null) {
            try {
                instanceSetService.removePodName(podName);
                log.info("Pod {} removed from Redis set.", podName);
            } catch (Exception e) {
                log.error("Error removing pod from Redis set: {}", e.getMessage(), e);
            }
        }
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
