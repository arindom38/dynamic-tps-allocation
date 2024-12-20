package com.cafecoder.dynamictpsallocation.controller;

import com.cafecoder.dynamictpsallocation.service.InstanceSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@RestController
@RequestMapping("/processor")
@RequiredArgsConstructor
public class TestController {

    private final InstanceSetService instanceSetService;
    private final RestTemplate restTemplate;

    @Value("${test-service.uri}")
    private String testAppUri;

    @GetMapping("/count")
    public Long getProcessorPodCount() {
        return instanceSetService.getActivePodCount();
    }

    @GetMapping("/pods")
    public Set<String> getProcessorPods() {
        return instanceSetService.getAllPodNames();
    }

    @GetMapping("/call-test-module")
    public String callTestModule() {
        return restTemplate.getForObject(testAppUri+"/test/random-string", String.class);
    }
}
