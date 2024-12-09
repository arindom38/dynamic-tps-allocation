package com.cafecoder.dynamictpsallocation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PodEventPublisher implements MessagePublisher{

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;


    @Override
    public void publish(String message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
        log.info("Pod Event is sent to topic {} with message body {}",topic.getTopic(),message);
    }
}
