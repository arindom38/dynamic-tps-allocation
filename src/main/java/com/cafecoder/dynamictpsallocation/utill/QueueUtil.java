package com.cafecoder.dynamictpsallocation.utill;


import com.google.common.util.concurrent.RateLimiter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class QueueUtil {

    private static final AtomicReference<RateLimiter>  rateLimiter = new AtomicReference<>(RateLimiter.create(10));

    /**
     * Gets the current RateLimiter instance.
     */
    private static RateLimiter getRateLimiter() {
        return rateLimiter.get();
    }

    public static void throttle() {
        try {
            double time = getRateLimiter().acquire();
            log.info("Throttle thread for {} seconds", time);
        } catch (Exception e) {
            log.warn("Failed to throttle thread", e);
        }
    }
    /**
     * Updates the rate limiter based on the current pod count.
     */
    public static  synchronized void updateRateLimiter(double targetTps, int podCount) {
        if ( podCount <= 0) {
            log.warn("Invalid pod count: {}. Skipping rate update.", podCount);
            return;
        }
        double newRate = targetTps / podCount;
        getRateLimiter().setRate(newRate);
        log.info("RateLimiter updated to {} permits per second for {} pods.", newRate, podCount);
    }

}
