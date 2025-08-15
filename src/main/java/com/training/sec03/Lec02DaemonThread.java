package com.training.sec03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Lec02DaemonThread {
    private static final Logger log = LoggerFactory.getLogger(Lec02DaemonThread.class);

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(Duration.ofSeconds(5));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("som som");
        });

        thread.setDaemon(true);
    }
}
