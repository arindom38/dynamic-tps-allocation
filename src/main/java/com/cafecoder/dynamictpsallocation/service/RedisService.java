package com.cafecoder.dynamictpsallocation.service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisService {
    void addToSet(String key, String value);
    void removeFromSet(String key, String value);
    long getSetSize(String key);
    Set<Object> getSetMembers(String setKeyName);
}
