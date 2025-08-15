package com.training.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec02ThreadErrorHandling {
    private static final Logger log = LoggerFactory.getLogger(Lec02ThreadErrorHandling.class);
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            throw new RuntimeException("Intentional Exception");
        });
        thread.setName("Misbehaving Thread");

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.error("A critical error happened in thread {}", t.getName(), e);
            }
        });

        thread.start();
    }
}
