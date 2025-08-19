package com.training.sec06;

public class Lec04MinMaxMetrics {
    private volatile long minimum;
    private volatile long maximum;
    final Object lock;

    /**
     * Initializes all member variables
     */
    public Lec04MinMaxMetrics() {
        lock = new Object();
        minimum = Long.MAX_VALUE;
        maximum = Long.MIN_VALUE;
    }

    /**
     * Adds a new sample to our metrics.
     */
    public void addSample(long newSample) {
        synchronized (lock) {
            minimum = Math.min(newSample, minimum);
            maximum = Math.max(newSample, maximum);
        }
    }

    /**
     * Returns the smallest sample we've seen so far.
     */
    public long getMin() {
        return minimum;
    }

    /**
     * Returns the biggest sample we've seen so far.
     */
    public long getMax() {
        return maximum;
    }
}
