package com.training.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01ThreadsCapabilities {
    private static final Logger log = LoggerFactory.getLogger(Lec01ThreadsCapabilities.class);

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("We are now in thread {}", Thread.currentThread().getName());
                log.info("Thread Priority: {}", Thread.currentThread().getPriority());
            }
        });
        thread.setName("new worker");
        thread.setPriority(Thread.MAX_PRIORITY);

        log.info("We are now in thread {}", Thread.currentThread().getName());
        thread.start();
        log.info("We are now in thread {}", Thread.currentThread().getName());

        Thread.sleep(10000);
    }
}
