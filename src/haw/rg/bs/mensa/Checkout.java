package haw.rg.bs.mensa;

import haw.rg.util.Timer;
import java.util.concurrent.locks.ReentrantLock;

public class Checkout implements Comparable {
    private int number;
    private int queueLength = 0;
    private ReentrantLock mutex = new ReentrantLock();
    private Timer timer;

    Checkout(int number, Timer timer) {
        this.number = number;
        this.timer = timer;
    }

    void pay() throws InterruptedException {
        mutex.lockInterruptibly();
        System.err.println(timer.time() + "| " +"-- Checkout " + number + " processing student " + Thread.currentThread().getName());
        Thread.sleep(3000);
        queueLength--;
        mutex.unlock();
    }

    String getName() {
        return "Checkout " + number;
    }

    @Override
    public int compareTo(Object o) {
        Checkout other = (Checkout) o;
        return Integer.compare(this.queueLength, other.queueLength);
    }

    void queueUp() {
        queueLength++;
    }

    int getQueueLength() {
        return queueLength;
    }
}
