package thread.creation.example7.deadlock;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class MainWithReentrant {

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
                intersection.takeRoadAV2();
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
                intersection.takeRoadBV2();
            }
        }
    }

    private static class Intersection {
        // different roads
        private final Object roadA = new Object();
        private final Object roadB =new Object();

        private final ReentrantLock lockerA = new ReentrantLock();
        private final ReentrantLock lockerB = new ReentrantLock();

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

        public void takeRoadAV2() {

            if (lockerA.tryLock()) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                try {
                    if (lockerB.tryLock()) {
                        try {
                            System.out.println("Train is passing road A.");
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            lockerB.unlock();
                        }

                    } else {
                        System.out.println("Road B is not ready");
                    }
                } finally {
                    lockerA.unlock();
                }


            }
            else {
                System.out.println("Road A is not ready");
            }

        }

        // Note: Make sure that the locking sequence of objects are the same for both methods that consumes them.
        // Note:This is to avoid circular deadlock.
        // Note: When both threads are waiting for 1 resource to be finished.
        public void takeRoadB() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing road B.");

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void takeRoadBV2() {

            if (lockerA.tryLock()) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                try {
                    if (lockerB.tryLock()) {
                        try {
                            System.out.println("Train is passing road B.");
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            lockerB.unlock();
                        }

                    }
                } finally {
                    lockerA.unlock();
                }


            }
            else {
                System.out.println("Road A is not ready");
            }

        }

    }
}
