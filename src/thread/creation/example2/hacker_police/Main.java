package thread.creation.example2.hacker_police;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static final int maxPassword = 9999;

    public static void main (String[] args){

        Random random = new Random();

        // set the vault's password
        Vault vault = new Vault(random.nextInt(maxPassword));

        List<Thread> threadList = new ArrayList<>();
        threadList.add(new AscendingHackerThread(vault));
        threadList.add(new DescendingHackerThread(vault));
        threadList.add(new PoliceThread());
        threadList.forEach(Thread::start);

        threadList.size();
    }

    /**
     * This class keeps the password
     */
    private static class Vault {
        private final int password;

        public Vault(int password) {
            this.password = password;
        }

        /**
         * Verify if the password is matches with the vault
         */
        public boolean isCorrectPassword(int guess) {
            // delay the step for a hacker's retries
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return password == guess;
        }
    }

    /**
     * The parent class containing Vault and setter for thread name and priorities
     */
    private static abstract class HackerThread extends Thread {
        protected final Vault vault;

        public HackerThread(Vault paramV) {
            // set the vault you want to crack
            this.vault = paramV;
            // sets the name and priority of the thread
            this.setName(this.getClass().getSimpleName());
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public void start() {
            System.out.println("Starting thread: [" + this.getName() +"]");
            super.start();
        }
    }

    /**
     * Try to guess the password with loop counting from 1 to 9999
     */
    public static class AscendingHackerThread extends HackerThread {
        public AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public  void run() {
            // try to guess the password between 1 - 9,999
            for(int i = 1; i<maxPassword; i++) {
                if (vault.isCorrectPassword(i)) {
                    System.out.println("The correct code is:" + i);
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Try to guess the password with loop counting from 9999 to 1
     */
    public static class DescendingHackerThread extends HackerThread {
        public DescendingHackerThread(Vault paramV) {
            super(paramV);
        }

        @Override
        public  void run() {
            // try to guess the password between 9,999 to 1
            for(int i = maxPassword; i > 0; i--) {
                if (vault.isCorrectPassword(i)) {
                    System.out.println(this.getName() + " guessed the correct code is:" + i);
                    System.exit(0);
                }
            }
        }
    }

    /**
     * This thread will count up to 10 sec and if the code is still not cracked, it will end the process.
     */
    private static class PoliceThread extends Thread {
        @Override
        public void run() {
            // do something
            for(int i=10; i>0; i--) {
                try {  // delay for 1 sec for each count
                    Thread.sleep(1000);
                } catch (InterruptedException e) { e.printStackTrace();}
                System.out.println(i);
            }

            System.out.println("Your time is up, hackers!");
            System.exit(0);
        }
    }
}
