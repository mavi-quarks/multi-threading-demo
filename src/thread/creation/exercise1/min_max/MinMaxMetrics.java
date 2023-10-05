package thread.creation.exercise1.min_max;

public class MinMaxMetrics {
    private volatile long max;
    private volatile long min;
    /**
     * Initializes all member variables
     */
    public MinMaxMetrics() {
        // initialize the first value of total and that's also the same for minimum and max
        max = Long.MAX_VALUE;
        min = Long.MIN_VALUE;
    }

    /**
     * Adds a new sample to our metrics.
     */
    public synchronized void addSample(long sample) {

        synchronized (this) {
            this.min = Math.min(sample, min);
            this.max = Math.max(sample, max);
        }
    }
    /**
     * Returns the smallest sample we've seen so far.
     */
    public long getMin() {
        return min;
    }

    /**
     * Returns the biggest sample we've seen so far.
     */
    public long getMax() {
        return  max;
    }
}

