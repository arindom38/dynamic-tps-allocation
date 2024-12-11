package com.cafecoder.dynamictpsallocation.controller;

import com.cafecoder.dynamictpsallocation.service.PodManager;
import com.cafecoder.dynamictpsallocation.utill.QueueUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@RestController
@RequestMapping("/processor")
@RequiredArgsConstructor
public class TestController {

    private final PodManager podManager;
    private final RestTemplate restTemplate;

    @Value("${test-service.uri}")
    private String testAppUri;

    @GetMapping("/count")
    public int getProcessorPodCount() {
        return podManager.getPodCount();
    }

    @GetMapping("/pods")
    public Set<String> getProcessorPods() {
        return podManager.getAllPodNames();
    }

    @GetMapping("/call-test-module")
    public String callTestModule() {
        QueueUtil.throttle();
        return restTemplate.getForObject(testAppUri+"/test/random-string", String.class);
    }

    @GetMapping("/target-tps/{tps}")
    public void updateTargetTps(@PathVariable double tps) {
        podManager.updateTargetTps(tps);
    }
}
