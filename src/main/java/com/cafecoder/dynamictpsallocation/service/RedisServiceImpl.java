package com.cafecoder.dynamictpsallocation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisServiceImpl implements RedisService {


    private final RedisTemplate<String, Object> redisTemplate;

    private final SetOperations<String, Object> setOperations;

    @Autowired
    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.setOperations = redisTemplate.opsForSet();
    }


    @Override
    public void addToSet(String key, String value) {
        setOperations.add(key, value);
    }

    @Override
    public void removeFromSet(String key, String value) {
        setOperations.remove(key,value);
    }

    @Override
    public long getSetSize(String key) {
        return setOperations.size(key);
    }

    @Override
    public Set<Object> getSetMembers(String setKeyName) {
        return setOperations.members(setKeyName);
    }


}
