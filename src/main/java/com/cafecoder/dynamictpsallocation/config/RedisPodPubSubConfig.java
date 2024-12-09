package com.cafecoder.dynamictpsallocation.config;

import com.cafecoder.dynamictpsallocation.service.PodEventSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisPodPubSubConfig {

    private  final PodEventSubscriber podEventSubscriber;

    public RedisPodPubSubConfig(PodEventSubscriber podEventSubscriber) {
        this.podEventSubscriber = podEventSubscriber;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(podEventSubscriber);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter(), topic());
        return container;
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("pod_events");
    }
}
