package haw.rg.bs.mensa;

import haw.rg.util.Timer;
import java.util.concurrent.locks.ReentrantLock;

public class Mensa {

    private final static int DEFAULT_CHECKOUTS = 3;
    private final static int DEFAULT_STUDENTS = 10;
    private final static int SECONDS_OPENED = 30;
    private final ReentrantLock checkoutPicker = new ReentrantLock();
    private Checkout[] checkouts;
    private Student[] students;
    private final Timer timer;


    public static void main(String[] args) {
        int c = DEFAULT_CHECKOUTS;
        int s = DEFAULT_STUDENTS;
        if (args.length == 2) {
            try {
                c = Integer.parseInt(args[0]);
                s = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e) {
                System.err.println("Faulty arguments. Continuing with default values.");
            }
        }

        new Mensa(c, s).start();
    }

    public Mensa(int checkouts, int students) {
        this.checkouts = new Checkout[checkouts];
        this.students = new Student[students];
        timer = new Timer();
    }

    public void start() {
        timer.start();
        System.err.println(timer.time() + "| " + "Setting up " + checkouts.length + " checkouts for " + students.length + " students.\nEnjoy your meal.");
        System.err.println("======================================================");

        for (int i = 0; i < checkouts.length; i++) {
            checkouts[i] = new Checkout(i, timer);
        }
        for (int i = 0; i < students.length; i++) {
            students[i] = new Student(i, checkouts, checkoutPicker, timer);
            students[i].start();
        }
        try {
            Thread.sleep(1000 * SECONDS_OPENED);
        } catch (InterruptedException e) {
            System.err.println("Früher Küchenschluss.");
        }
        finally {
            System.err.println("=================== Küchenschluss! ===================");
            for (Student student : students) {
                student.interrupt();
            }
        }
    }
}
