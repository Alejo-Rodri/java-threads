package com.training.sec06;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Lec02SynchronizedLock {
    private static final Logger log = LoggerFactory.getLogger(Lec02SynchronizedLock.class);

    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

        incrementingThread.start();
//        incrementingThread.join();;
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        log.info("We currently have {} items", inventoryCounter.getItems());
    }

    public static class DecrementingThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    public static class IncrementingThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public IncrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.increment();
            }
        }
    }

    private static class InventoryCounter {
        private int items = 0;
        final Object lock = new Object();

        public synchronized void increment() {
            synchronized (this.lock) {
                items++;
            }
        }
        public synchronized void decrement() {
            synchronized (this.lock) {
                items--;
            }
        }
        public synchronized int getItems() {
            synchronized (this.lock) {
                return items;
            }
        }
    }
}
