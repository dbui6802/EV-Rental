package com.webserver.evrentalsystem.utils;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Logger {

    private static boolean debugMode;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        String debugModeString = System.getenv("DEBUG_MODE");
        if (debugModeString != null) {
            debugMode = Boolean.parseBoolean(debugModeString);
        }
    }

    public static void printf(String message) {
        if (debugMode) {
            System.out.println(message);
        }
    }
}
