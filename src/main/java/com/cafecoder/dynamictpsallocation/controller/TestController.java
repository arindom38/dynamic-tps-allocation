package com.cafecoder.dynamictpsallocation.controller;

import com.cafecoder.dynamictpsallocation.service.InstanceSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/processor")
@RequiredArgsConstructor
public class TestController {

    private final InstanceSetService instanceSetService;

    @GetMapping("/count")
    public Long getProcessorPodCount() {
        return instanceSetService.getActivePodCount();
    }

    @GetMapping("/pods")
    public Set<String> getProcessorPods() {
        return instanceSetService.getAllPodNames();
    }
}
