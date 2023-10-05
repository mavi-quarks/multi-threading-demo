package thread.creation.example4.factorial;

import java.math.BigInteger;

public class ComplexCalculation {

    public static void main(String[] args) throws InterruptedException {
        BigInteger base1 = new BigInteger("2");
        BigInteger power1 = new BigInteger("2");
        BigInteger base2 = new BigInteger("11");
        BigInteger power2 = new BigInteger("1234567");

        System.out.println("result of " +"(" + base1 +"^" + power1+")" + " + (" + base2 +"^" + power2 +")=" + calculateResult(base1, power1, base2, power2));
    }

    public static BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2)  {
        BigInteger result;
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */
        PowerCalculatingThread thread1 = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread thread2 = new PowerCalculatingThread(base2, power2);

        thread1.start();
        thread2.start();

        try {
            thread1.join(1000);
            thread2.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread1.interrupt();
        thread2.interrupt();

        return thread1.getResult().add(thread2.getResult());
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
           /*
           Implement the calculation of result = base ^ power
           */
            for(int i = 0; i < power.intValue(); i++) {
                if (Thread.currentThread().isInterrupted()){
                    System.out.println("Calculation is prematurely interrupted:" + base +"^" + power);
                    result = BigInteger.ZERO;
                    return;
                }
                result = result.multiply(base);
            }
        }

        private BigInteger calcPower(BigInteger base, BigInteger power) {
            BigInteger temp = BigInteger.ONE;
            for(int i = 0; i < power.intValue(); i++) {

                temp = temp.multiply(base);
            }
            return temp;
        }

        public BigInteger getResult() { return result; }
    }
}