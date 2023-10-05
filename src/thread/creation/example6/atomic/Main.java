package thread.creation.example6.atomic;

import java.util.Random;

public class Main {

    public static void main(String[] args) {


    }

    private static class BusinessLogic extends Thread{
        private Metrics metrics;
        private Random random = new Random();


        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();

            try {
                Thread.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long end = System.currentTimeMillis();
            metrics.addSample(end-start);
        }
    }


    private class Metrics {
        private long count = 0;
        private volatile double average = 0.0;

        public void addSample(long value) {
            synchronized (this) {
                double sum = count * average;
                count++;
                average = (sum + value) / count;
            }
        }

        public double getAverage() {
            return average;
        }

    }
}
