package thread.creation.example4.factorial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main (String[] strings) throws InterruptedException {
        List<Long> inputNumbers = Arrays.asList(1200000L, 100L, 4L, 7L, 3L, 2L);

        // we want to calculate the factorial for each of these numbers
        List<FactorialThread> threadList = new ArrayList<>();

        // assign each numbers to diff threads
        inputNumbers.forEach( it -> {
            threadList.add(new FactorialThread(it));
        });

        // start all the threads
        for (Thread thread: threadList) {
            thread.setDaemon(true);
            thread.start();
        }

        // join the threads inorder wait for all the threads to finish before exiting
        for (Thread thread: threadList) {
            thread.join(1000);
        }

//        for (Thread thread: threadList) {
//            thread.interrupt();
//        }

        for (int i=0; i < inputNumbers.size(); i++) {
            FactorialThread factorialThread = threadList.get(i);
            if (factorialThread.isFinished()) {
                System.out.println("Factorial of " +inputNumbers.get(i) + " is " + factorialThread.getResult());
            }
            else {
                System.out.println("The calculation for " + inputNumbers.get(i) +" is still in progress");
            }
        }


    }

    private static class FactorialThread extends Thread{
        private Long input;
        private boolean isFinished = false;

        private BigInteger result = BigInteger.ONE;
        public FactorialThread(Long input){
            this.input = input;
        }

        @Override
        public void run() {
            this.result = factorial(this.input);
            isFinished = true;
        }

        private BigInteger factorial(long input) {
            BigInteger temp = BigInteger.ONE;
            for (long i = input; i > 0; i--) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Factorial calculation of " + input + " is prematurely interrupted");
                    return BigInteger.ZERO;
                }
               temp = temp.multiply(new BigInteger(Long.toString(i)));
            }
            return temp;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
