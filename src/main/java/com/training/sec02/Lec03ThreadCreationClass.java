package com.training.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec03ThreadCreationClass {
    private static final Logger log = LoggerFactory.getLogger(Lec03ThreadCreationClass.class);
    public static void main(String[] args) {
        Thread thread = new NewThread();
        thread.start();
    }

    public static class NewThread extends Thread {
        @Override
        public void run() {
            log.info("We are now in thread {}", this.getName());
            log.info("Thread Priority: {}", this.getPriority());
        }
    }
}
