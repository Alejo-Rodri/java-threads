package com.training.sec03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lec03Join {
    private static final Logger log = LoggerFactory.getLogger(Lec03Join.class);

    public static void main(String[] args) throws InterruptedException {
        List<Long> inputNumbers = Arrays.asList(0L, 31L, 3192L, 40321L, 50332L, 63303L, 112092L, 594239L);
        List<FactorialThread> threads = new ArrayList<>();

        FactorialThread factorialThread;
        for (Long inputNumber : inputNumbers) {
            factorialThread = new FactorialThread(inputNumber);
            factorialThread.setPriority(Thread.MIN_PRIORITY);
            threads.add(factorialThread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join(Duration.ofSeconds(3));
        }

        for (int i = 0; i < inputNumbers.size(); i++) {
            factorialThread = threads.get(i);
            if (factorialThread.isFinished())
                log.info("Factorial of {} is {}", factorialThread.inputNumber, factorialThread.getResult());
            else
                log.info("The calculation of {} is still in progress", inputNumbers.get(i));
        }
    }

    private static class FactorialThread extends Thread {
        private final long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        public BigInteger factorial(long n) {
            BigInteger tempResult = BigInteger.ONE;

            for (long i = n; i > 0; i--)
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));

            return tempResult;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
