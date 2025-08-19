package com.training.sec06;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Lec06DeadLock {
    private static final Logger log = LoggerFactory.getLogger(Lec06DeadLock.class);

    public static void main(String[] args) {
        var intersection = new Intersection();
        Thread trainA = new Thread(new TrainA(intersection));
        var trainB = new Thread(new TrainB(intersection));

        trainA.start();
        trainB.start();
    }

    public static class TrainA implements Runnable {
        private final Intersection intersection;
        private final Random random = new Random();

        public TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
                }

                intersection.takeRoadA();
            }
        }
    }

    public static class TrainB implements Runnable {
        private final Intersection intersection;
        private final Random random = new Random();

        public TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
                }

                intersection.takeRoadB();
            }
        }
    }

    public static class Intersection {
        private Object roadA = new Object();
        private Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                log.info("road A is locked by thread {}", Thread.currentThread().getName());

                synchronized (roadB) {
                    log.info("train is passing through road A so B must wait");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
                    }
                }
            }
        }

        public void takeRoadB() {
//            synchronized (roadB) {
//                log.info("road B is locked by thread {}", Thread.currentThread().getName());
//                synchronized (roadA) {
//                    log.info("train is passing through road B so A must wait");
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
////                    throw new RuntimeException(e);
//                    }
//                }
//            }

            synchronized (roadA) {
                log.info("road A is locked by thread {}", Thread.currentThread().getName());
                synchronized (roadB) {
                    log.info("train is passing through road B so A must wait");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
