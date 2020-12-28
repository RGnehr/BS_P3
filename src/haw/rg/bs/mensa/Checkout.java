package haw.rg.bs.mensa;

import haw.rg.util.Timer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides a class that simulates a checkout within a canteen. Customers can enqueue or pay one at a time.
 * @author Robert Gnehr
 */
public class Checkout implements Comparable {

    private int number;
    private int queueLength = 0;
    private ReentrantLock mutex = new ReentrantLock();
    private Timer timer;

    /**
     * Creates a new Checkout object.
     * @param number 'id' number of checkout. Used to build its name.
     * @param timer monitoring timer
     */
    Checkout(int number, Timer timer) {
        this.number = number;
        this.timer = timer;
    }

    /**
     * Locks this checkout with a mutex and processes one customer at a time by sleeping for a set amount of time.
     * Afterwards decrements it's queue length by one and releases the lock.
     * @throws InterruptedException if interrupted
     */
    void pay() throws InterruptedException {
        // todo: enqueue here?
        mutex.lockInterruptibly();
        System.err.println(timer.time() + "| -- " + this + " processing " + Thread.currentThread().getName());
        Thread.sleep(3000);
        queueLength--;
        mutex.unlock();
    }

    /**
     * Overrides toString method.
     * @return this checkout's name
     */
    public String toString() {
        return "Checkout " + number;
    }

    /**
     * Overrides compareTo method: checkouts are compared by their current queue length.
     * @param o other checkout to be compared
     * @return a negative integer, zero, or a positive integer as this checkout has a
     *         shorter, equal to, or longer queue than the specified checkout.
     */
    @Override
    public int compareTo(Object o) {
        Checkout other = (Checkout) o;
        return Integer.compare(this.queueLength, other.queueLength);
    }

    /**
     * Increments the current queue length by one.
     */
    void queueUp() {
        queueLength++;
    }

    /**
     * Returns current queue length
     * @return current queue length
     */
    int getQueueLength() {
        return queueLength;
    }


    // todo: find shortest queue from here?

//    private static ReentrantLock mutex_f = new ReentrantLock();

//    public static Checkout findShortestQueue(Checkout[] list) throws InterruptedException {
//        mutex_f.lockInterruptibly();
//        Arrays.sort(list);
//        Checkout shortest = list[0];
//        mutex_f.unlock();
//        return shortest;
//    }
}
