package com.training.sec06;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Lec03Metrics {
    private static final Logger log = LoggerFactory.getLogger(Lec03Metrics.class);
    private long count = 0;
    private volatile double average = 0.0;

    public static void main(String[] args) {
        Lec03Metrics metrics = new Lec03Metrics();

        BusinessLogic businessLogicThread1 = new BusinessLogic(metrics);
        BusinessLogic businessLogicThread2 = new BusinessLogic(metrics);

        MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);

        businessLogicThread1.start();
        businessLogicThread2.start();
        metricsPrinter.start();
    }

    public synchronized void addSample(long sample) {
        double currentSum = average * count;
        count++;
        average = (currentSum + sample) / count;
    }

    public double getAverage() {
        return average;
    }

    public static class BusinessLogic extends Thread {
        private final Lec03Metrics metrics;
        private final Random random = new Random();

        public BusinessLogic(Lec03Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                long startTime = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                }
                long endTime = System.currentTimeMillis();

                metrics.addSample(endTime - startTime);
            }
        }
    }

    public static class MetricsPrinter extends Thread {
        private final Lec03Metrics metrics;

        public MetricsPrinter(Lec03Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                double currentAvg = metrics.getAverage();

                log.info("Current average is {}", currentAvg);
            }
        }
    }
}
