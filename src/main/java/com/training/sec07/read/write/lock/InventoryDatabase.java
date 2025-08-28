package com.training.sec07.read.write.lock;

import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InventoryDatabase {
    private final TreeMap<Integer, Integer> priceToCountMap;
    //private final ReentrantLock lock = new ReentrantLock();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public InventoryDatabase() {
        priceToCountMap = new TreeMap<>();
    }

    public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {
        readLock.lock();
        try {
            Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
            Integer toKey = priceToCountMap.floorKey(upperBound);

            if (Objects.isNull(fromKey) || Objects.isNull(toKey)) return 0;

            NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(
                    fromKey,
                    true,
                    toKey,
                    true
            );

            int sum = 0;
            for (int numberOfItemsForPrice : rangeOfPrices.values())
                sum += numberOfItemsForPrice;

            return sum;
        } finally {
            readLock.unlock();
        }
    }

    public void addItem(int price) {
        writeLock.lock();
        try {
            Integer numberOfItemsForPrice = priceToCountMap.get(price);

            if (Objects.isNull(numberOfItemsForPrice)) priceToCountMap.put(price, 1);
            else priceToCountMap.put(price, ++numberOfItemsForPrice);
        } finally {
            writeLock.unlock();
        }
    }

    public void removeItem(int price) {
        writeLock.lock();
        try {
            Integer numberOfItemsForPrice = priceToCountMap.get(price);

            if (Objects.isNull(numberOfItemsForPrice) || numberOfItemsForPrice == 1) priceToCountMap.remove(price);
            else priceToCountMap.put(price, --numberOfItemsForPrice);
        } finally {
            writeLock.unlock();
        }
    }
}
