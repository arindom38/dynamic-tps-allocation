package com.cafecoder.dynamictpsallocation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Slf4j
@Service
@RequiredArgsConstructor
public class PodLifecycleHandler implements SmartLifecycle {

    private final PodManager podManager;
    private final PodEventPublisher podEventPublisher;

    private String podName;
    private boolean running = false;

    @Override
    public void start() {
        try {
            podName = InetAddress.getLocalHost().getHostName();
            podManager.addPodName(podName);
            podEventPublisher.publish(podName);
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
                podManager.removePodName(podName);
                podEventPublisher.publish(podName);
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
