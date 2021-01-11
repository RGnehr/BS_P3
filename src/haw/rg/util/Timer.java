package haw.rg.util;

/**
 * Simple timer class with milliseconds precision.
 * @author Robert Gnehr
 */
public class Timer {
    private long time = 0;
    public Timer() {}

    public void start() {
        time = System.currentTimeMillis();
    }

    public String  time() {
        return String.format("%5d" ,System.currentTimeMillis() - time);
    }
}