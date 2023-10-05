package thread.creation.example7.deadlock;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Intersection intersection = new Intersection();
        TrainA trainA = new TrainA(intersection);
        TrainB trainB = new TrainB(intersection);

        trainA.start();
        trainB.start();

    }

    private static class TrainA extends Thread {
        Intersection intersection;
        Random random = new Random();

        public TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while(true) {
                // pick a random time to wait when the train comes
                long wait = random.nextInt(5);
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intersection.takeRoadA();
            }
        }
    }

    private static class TrainB extends Thread {
        Intersection intersection;
        Random random = new Random();

        public TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while(true) {
                // pick a random time to wait when the train comes
                long wait = random.nextInt(5);
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intersection.takeRoadB();
            }
        }
    }

    private static class Intersection {
        // different roads
        private final Object roadA = new Object();
        private final Object roadB =new Object();

        private final int test = 0;

        // Note: Make sure that the locking sequence of objects are the same for both methods that consumes them.
        // Note:This is to avoid circular deadlock.
        // Note: When both threads are waiting for 1 resource to be finished.
        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing road A.");

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Note: Make sure that the locking sequence of objects are the same for both methods that consumes them.
        // Note:This is to avoid circular deadlock.
        // Note: When both threads are waiting for 1 resource to be finished.
        public void takeRoadB() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing road .");

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
