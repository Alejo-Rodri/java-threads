package com.training.sec06;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Lec05DataRace {
    private static final Logger log = LoggerFactory.getLogger(Lec05DataRace.class);
    public static void main(String[] args) {
        SharedClass sharedClass = new SharedClass();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.checkForDataRace();
            }
        });

        thread1.start();
        thread2.start();

        log.info("x: {}, y: {}", sharedClass.getX(), sharedClass.getY());
    }

    public static class SharedClass {
        // con volatile prevenimos los data race
        private volatile int x = 0;
        private volatile int y = 0;

        public void increment() {
            x++;
            y++;
        }

        public void checkForDataRace() {
            if (y > x) {
                log.info("y > x - Data Race Detected");
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
