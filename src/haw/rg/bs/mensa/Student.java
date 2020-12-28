package haw.rg.bs.mensa;

import haw.rg.util.Timer;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides a class that simulates a student within a canteen. When their thread is run, they will repeatedly
 * enqueue at a checkout (with the shortest queue), pay there, eat and rest, until they are interrupted.
 * @author Robert Gnehr
 */
public class Student extends Thread {
    private static final ReentrantLock checkoutPicker = new ReentrantLock();
    private final Checkout[] checkouts;
    private Timer timer;

    /**
     * Creates a new student object.
     * @param number 'id' number of student. Used to build their name.
     * @param checkouts array of checkouts that can be used by them.
     * @param timer monitoring timer
     */
    public Student(int number, Checkout[] checkouts, Timer timer) {
        this.checkouts = checkouts;
        this.setName("Student " + number);
        this.timer = timer;
    }

    /**
     * Overrides the Thread run method: When started, a student continuously looks for the checkout with the shortest
     * queue, pays there, eats and pauses shortly, before starting again from the top. When interrupted, the student
     * will leave.
     */
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
                System.err.println(timer.time() + "| " + getName() + " leaves the canteen.");
                return;
            }
        }
    }

    /**
     * Finds the checkout with the shortest queue and queues up there. Uses a static mutex to prevent simultaneous
     * sorting by different students without increasing queue length.
     * @return checkout with shortest queue
     * @throws InterruptedException if interrupted
     */
    private Checkout findQueue() throws InterruptedException {
        checkoutPicker.lockInterruptibly();
        Arrays.sort(checkouts);
        Checkout tmp = checkouts[0];
        tmp.queueUp();
        System.err.println(timer.time() + "| " + getName() + " is hungry and moves to " + tmp + " (Queue: " + tmp.getQueueLength() + ")");
        checkoutPicker.unlock();

        return tmp;
    }

    /**
     * Student pauses for a random time to eat.
     * @throws InterruptedException if interrupted
     */
    private void eat() throws InterruptedException {
        System.err.println(timer.time() + "| " + getName() + " starts eating. Yummy.");
        Thread.sleep((int) (3000 + Math.random() * 3000 + 1));
    }

    /**
     * Student pauses for a random time to idle.
     * @throws InterruptedException if interrupted
     */
    private void idle() throws InterruptedException {
        System.err.println(timer.time() + "| " + getName() + " idles.");
        Thread.sleep((int) (4000 + Math.random() * 5000 + 1));
    }
}
