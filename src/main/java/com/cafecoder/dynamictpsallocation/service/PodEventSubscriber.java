package com.cafecoder.dynamictpsallocation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PodEventSubscriber implements MessageListener {

    private final PodManager podManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received pod event: {}", message.toString());
        try {
            podManager.updateRateLimiter();
        } catch (Exception e) {
            log.error("Error updating rate limiter", e);
        }
    }
}
