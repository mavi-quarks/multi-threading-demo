package practice1.stack.creation.coordination;

import java.math.BigInteger;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        BigInteger base = new BigInteger("2");
        BigInteger power = new BigInteger("12345123123123123123123123");
        CalculatorThread calculatorThread = new CalculatorThread(base, power);
        calculatorThread.setName(CalculatorThread.class.getSimpleName());
        calculatorThread.start();
        calculatorThread.interrupt();
        System.out.println("The power of "+base +"^" + power +"=" + calculatorThread.getResults());
    }

    // extends class
    private static class CalculatorThread extends Thread {
        private final BigInteger base;
        private final BigInteger power;
        private BigInteger results = BigInteger.ONE;

        public CalculatorThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println("Hello there from " + Thread.currentThread().getName());
            for(int i=0; i<power.intValue(); i++) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Thread is prematurely interrupted.");
                    results = BigInteger.ZERO;
                    System.exit(0);
                }
                results = results.multiply(base);
            }
        }

        public BigInteger getResults() {
            return  results;
        }
    }
}
