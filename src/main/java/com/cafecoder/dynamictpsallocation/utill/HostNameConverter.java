package com.cafecoder.dynamictpsallocation.utill;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostNameConverter extends ClassicConverter {

    private static String hostName;

    static {
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostName = "";
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        return hostName;
    }
}
