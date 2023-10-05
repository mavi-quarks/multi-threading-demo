package thread.creation.example10.lock_free_algo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        //StandardStack<Integer> stack = new StandardStack<>();
        Random random = new Random();

        // populating the stacks with random values
        for (int i = 0; i<10000; i++) {
            stack.push(random.nextInt());
        }

        // create a thread that stores the object
        List<Thread> threadList = new ArrayList<>();
        int popThreads = 2;
        int pushThreads = 2;

        // create each thread that runs in the loop forever
        for (int i =0; i< pushThreads; i++) {
            Thread thread = new Thread(() -> {
                while(true) {
                    stack.push(random.nextInt());
                }
            });

            thread.setDaemon(true);
            threadList.add(thread);
        }
        // create each thread that runs in the loop forever
        for (int i =0; i< popThreads; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.pop();
                }
            });

            thread.setDaemon(true);
            threadList.add(thread);
        }

        // start all the threads
        for (Thread thread : threadList) {
            thread.start();
        }

        Thread.sleep(10000);
        System.out.println(String.format("%,d operations were performed for 10 seconds. ", stack.getCounter()));
    }

    // Implement a lock free stack
    private static class LockFreeStack<T>{
        private AtomicReference<StackNode<T>> head = new AtomicReference<>();
        private AtomicInteger counter = new AtomicInteger(0);

        // Add new node to the stack
        public void push(T value) {
            StackNode<T> newHeadNode = new StackNode(value);

            while(true) {
                // get the current head node and set it as the next of the new node
                StackNode<T> currentNode = head.get();
                newHeadNode.next = currentNode;
                if (head.compareAndSet(currentNode, newHeadNode)) {
                    break; // meaning the head is already reset
                } else {
                    // try again
                    LockSupport.parkNanos(1);
                }
            }
            counter.incrementAndGet();
        }

        // Remove the head of the stack
        public T pop() {
            StackNode<T> currentNode = head.get();
            StackNode<T> newHeadNode;

            while (currentNode !=null) {
                newHeadNode = currentNode.next;
                if (head.compareAndSet(currentNode, newHeadNode)){
                    break;
                } else {
                    LockSupport.parkNanos(1);
                    currentNode = head.get();
                }
            }
            counter.incrementAndGet();
            return currentNode != null ? currentNode.value : null;
        }

        // Retrieves the counter
        public int getCounter() {
            return counter.get();
        }
    }


    // Implement your standard stack
    private static class StandardStack<T> {
        private StackNode<T> head;
        private int counter = 0;

        // Remove the head of the stack
        public synchronized T pop() {
            if (head == null) {
                counter++;
                return null;
            }

            T value = head.value;
            head = head.next;
            counter++;
            return value;
        }

        // Add new node to the stack
        public synchronized void push(T value) {
            // create the new node
            StackNode<T> newHead = new StackNode<>(value);
            newHead.next = head;
            head = newHead;
            counter++;
        }

        // Retrieves the counter
        public int getCounter() {
            return counter;
        }
    }

    // The resource that contains the value and the next node of the stack
    private static class StackNode<T> {
        private final T value;
        private StackNode<T> next;

        public StackNode(T value) {
            this.value = value;
        }
    }
}
