package com.training.sec02;

import com.sun.source.doctree.ThrowsTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec05ClassRunnable {
    private static final Logger log = LoggerFactory.getLogger(Lec05ClassRunnable.class);
    public static void main(String[] args) {
        Thread thread = new Thread(new RunnableClass());

        thread.start();
    }

    // this way we can decouple the threading functionality
    public static class RunnableClass implements Runnable {
        @Override
        public void run() {
            log.info("doing some");
        }
    }
}
