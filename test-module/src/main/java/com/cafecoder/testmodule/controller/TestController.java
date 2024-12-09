package com.cafecoder.testmodule.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/random-string")
    public String testCall() throws UnknownHostException {
        return UUID.randomUUID()+ " from container name: "+InetAddress.getLocalHost().getHostName();
    }
}
