package thread.creation.example5.sychronized_reentrant;

public class Main {

    private static int MAX_LIMIT = 1000000;

    public static void main (String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();

        IncrementThread ithread = new IncrementThread(inventoryCounter);
        DecrementThread dthread = new DecrementThread(inventoryCounter);
        long start = System.currentTimeMillis();
        ithread.start();
        dthread.start();
        ithread.join();
        dthread.join();
        long end = System.currentTimeMillis();
        System.out.println("Our current inventory is " + inventoryCounter.getItems() + " and it takes time " + (end-start) + "ms to process.");
    }

    // Creates the inventory
    private static class IncrementThread extends Thread{
        InventoryCounter myInventory;

        // accepts InventoryCounter object
        public IncrementThread(InventoryCounter inventoryCounter){
            myInventory = inventoryCounter;
        }

        @Override
        public void run() {
            for(int i=0; i<MAX_LIMIT; i++) {
                myInventory.increment();
            }
            System.out.println("Finished at " + Thread.currentThread().getName());
        }
    }

    // Creates another thread that decrements the inventory
    private static class DecrementThread extends Thread{
        InventoryCounter myInventory;

        // accepts InventoryCounter object
        public DecrementThread(InventoryCounter inventoryCounter){
            myInventory = inventoryCounter;
        }

        @Override
        public void run() {
            for(int i=0; i<MAX_LIMIT; i++) {
                myInventory.decrement();
            }
            System.out.println("Finished at " + Thread.currentThread().getName());
        }
    }

    // The inventory class counter
    private static class InventoryCounter {
        private int items = 0;
        private final Object lock = new Object();
        public void increment() {
            synchronized (lock) {
                this.items++;
            }
        }

        public void decrement() {
            synchronized (lock) {
                this.items--;
            }
        }

        public int getItems() {
            synchronized (lock) {
                return this.items;
            }
        }
    }
}
