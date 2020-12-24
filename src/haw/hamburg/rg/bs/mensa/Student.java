package haw.hamburg.rg.bs.mensa;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class Student extends Thread {
    private final Checkout[] checkouts;
    private final ReentrantLock checkoutPicker;

    public Student(int number, Checkout[] checkouts, ReentrantLock checkoutPicker) {
        this.checkouts = checkouts;
        this.setName("Student " + number);
        this.checkoutPicker = checkoutPicker;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            // choose checkout with shortest queue



            try {
                findQueue().pay();
                eat();
                idle();
            }
            catch (InterruptedException e) {
                System.err.println(getName() + " is leaving the canteen.");
                return;
            }
        }
    }

    private Checkout findQueue() throws InterruptedException {
        checkoutPicker.lockInterruptibly();
        Arrays.sort(checkouts);
        Checkout tmp = checkouts[0];
        System.err.println(getName() + " is hungry and moves to " + checkouts[0].getName() + " (Queue: " + checkouts[0].getQueueLength() + ")");
        checkouts[0].queueUp(getName());

        checkoutPicker.unlock();
        return tmp;
    }


    private void eat() throws InterruptedException {
        System.err.println(getName() + " starts eating. Yummy.");
        Thread.sleep((int) (3000 + Math.random() * 3000 + 1));
    }

    private void idle() throws InterruptedException {
        System.err.println(getName() + " procrastinates.");
        Thread.sleep((int) (4000 + Math.random() * 5000 + 1));
    }
}
