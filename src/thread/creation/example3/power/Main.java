package thread.creation.example3.power;

import java.math.BigInteger;

public class Main {

    public static void main (String[] args) throws InterruptedException {
        Thread thread = new Thread(new LongComputationTask(new BigInteger("60000"), new BigInteger("200000")));

         thread.setDaemon(true); // gracefully kills the worker thread after main is terminated
        thread.start();
        Thread.sleep(1000);
        // signal the thread that it is interrupted and then you can add handling.
        thread.interrupt();

    }

    private static class LongComputationTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(this.base +"^" +this.power +"=" +computePower(this.base, this.power));
        }

        private BigInteger computePower(BigInteger base, BigInteger power) {
            BigInteger results = BigInteger.ONE;
            for (int i=0; i < power.intValue(); i++) {
//                if (Thread.currentThread().isInterrupted()) {
//                    System.out.println("Prematurely interrupted the calculation...");
//                    return BigInteger.ZERO;
//                }
                results = results.multiply(base);
            }

            return results;
        }
    }
}
