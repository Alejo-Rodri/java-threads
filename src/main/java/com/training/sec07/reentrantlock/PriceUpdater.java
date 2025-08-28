package com.training.sec07.reentrantlock;

import java.util.Random;

public class PriceUpdater extends Thread {
    private final PricesContainer pricesContainer;
    private final Random random;

    public PriceUpdater(PricesContainer pricesContainer) {
        this.pricesContainer = pricesContainer;
        random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            pricesContainer.getLockObject().lock();

            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                pricesContainer.setBitcoinPrice(random.nextInt(10000));
                pricesContainer.setEtherPrice(random.nextInt(20000));
                pricesContainer.setLitecoinPrice(random.nextInt(1000));
                pricesContainer.setRipplePrice(random.nextInt(30000));
                pricesContainer.setBitcoinCashPrice(random.nextInt(10000));
            } finally {
                pricesContainer.getLockObject().unlock();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
    }
}
