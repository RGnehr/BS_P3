package haw.rg.bs.mensa;

import haw.rg.util.Timer;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class Student extends Thread {
    private final Checkout[] checkouts;
    private final ReentrantLock checkoutPicker;
    private Timer timer;

    public Student(int number, Checkout[] checkouts, ReentrantLock checkoutPicker, Timer timer) {
        this.checkouts = checkouts;
        this.setName("Student " + number);
        this.checkoutPicker = checkoutPicker;
        this.timer = timer;
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
                System.err.println(timer.time() + "| " + getName() + " is leaving the canteen.");
                return;
            }
        }
    }

    private Checkout findQueue() throws InterruptedException {
        checkoutPicker.lockInterruptibly();
        Arrays.sort(checkouts);
        Checkout tmp = checkouts[0];
        tmp.queueUp();
        System.err.println(timer.time() + "| " + getName() + " is hungry and moves to " + tmp.getName() + " (Queue: " + tmp.getQueueLength() + ")");
        checkoutPicker.unlock();

        return tmp;
    }


    private void eat() throws InterruptedException {
        System.err.println(timer.time() + "| " +getName() + " starts eating. Yummy.");
        Thread.sleep((int) (3000 + Math.random() * 3000 + 1));
    }

    private void idle() throws InterruptedException {
        System.err.println(timer.time() + "| " +getName() + " procrastinates.");
        Thread.sleep((int) (4000 + Math.random() * 5000 + 1));
    }
}
