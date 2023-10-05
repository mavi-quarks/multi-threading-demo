package thread.creation.example1.basic_thread;

public class Main {

    public static void main (String[] args) {
        Thread thread = new MyNewThread();

        thread.setName("my_new_thread");
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    private static class MyNewThread extends Thread {
        @Override
        public void run() {
            // do something inside the new thread
            System.out.println("This is the new thread:" + Thread.currentThread().getName());
            System.out.println("Priority set:" + Thread.currentThread().getPriority());
        }
    }
}
