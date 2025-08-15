package com.training.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lec04VaultHackingThreads {
    private static final Logger log = LoggerFactory.getLogger(Lec04VaultHackingThreads.class);
    private static final int MAX_PASSWORD = 9999;

    public static void main(String[] args) {
        Random random = new Random();

        Vault vault = new Vault(random.nextInt(MAX_PASSWORD));
        List<Thread> threads = new ArrayList<>();

        threads.add(new AscendingThread(vault));
        threads.add(new DescendingThread(vault));
        threads.add(new PoliceThread());

        for (Thread thread : threads) {
            thread.start();
        }
    }

    private static class Vault {
        private final int password;

        public Vault(int password) {
            this.password = password;
        }

        public boolean isCorrectPassword(int guess) {
            try {
                Thread.sleep(Duration.ofMillis(500));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return this.password == guess;
        }
    }

    private static abstract class HackerThread extends Thread {
        protected final Vault vault;

        public HackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getName());
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public void start() {
            log.info("Starting thread {}", this.getName());
            super.start();
        }
    }

    private static class AscendingThread extends HackerThread {
        public AscendingThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int guess = 0; guess < MAX_PASSWORD; guess++) {
                if (vault.isCorrectPassword(guess)) {
                    log.info("{} guessed the password {}", this.getName(), guess);
                    System.exit(0);
                }
            }
        }
    }

    private static class DescendingThread extends HackerThread {
        public DescendingThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int guess = 0; guess < MAX_PASSWORD; guess++) {
                if (vault.isCorrectPassword(guess)) {
                    log.info("{} guessed the password {}", this.getName(), guess);
                    System.exit(0);
                }
            }
        }
    }

    private static class PoliceThread extends Thread {
        @Override
        public void run() {
            for (int i = 10; i > 0; i--) {
                try {
                    Thread.sleep(Duration.ofSeconds(1));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("{}", i);
            }

            log.info("Game over");
            System.exit(0);
        }
    }
}
