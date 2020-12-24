package haw.hamburg.rg.bs.mensa;

import java.util.concurrent.locks.ReentrantLock;
// TODO: Siehe BoundedBufferServer
public class Checkout implements Comparable {
    private int number;
    private int queueLength = 0;
    private ReentrantLock mutex = new ReentrantLock();

    Checkout(int number) {
        this.number = number;
    }

    void pay() throws InterruptedException {

        mutex.lockInterruptibly();
        System.err.println("-- Checkout " + number + " processing student " + Thread.currentThread().getName());
        Thread.sleep(3000);
        mutex.unlock();

        queueLength--;
    }

    String getName() {
        return "Checkout " + number;
    }

    @Override
    public int compareTo(Object o) {
        Checkout other = (Checkout) o;
        return Integer.compare(this.queueLength, other.queueLength);
    }

    void queueUp(String name) throws InterruptedException{
        mutex.lockInterruptibly();
        queueLength++;
        System.err.println("---- Checkout " + number + " queue is now " + queueLength + " (" + name + " queued up) | " + mutex.getHoldCount());
        mutex.unlock();
    }

    int getQueueLength() {
        return queueLength;
    }
}
