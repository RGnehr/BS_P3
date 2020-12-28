package haw.rg.bs.mensa;

import haw.rg.util.Timer;

/**
 * Provides a class that manages a canteen simulation. A number of students select from a pool of checkouts the one
 * with the shortest queue, pay there, eat and rest afterwards. Only one student can pay at every counter at the same
 * time.
 * @author Robert Gnehr
 */
public class Mensa {

    private final static int DEFAULT_CHECKOUTS = 3;
    private final static int DEFAULT_STUDENTS = 10;
    private final static int SECONDS_OPENED = 30;

    private Checkout[] checkouts;
    private Student[] students;
    private final Timer timer;

    /**
     * Creates a new mensa object and starts it. Number of checkouts and students can be set via args 0 and 1.
     * @param args 0 - number of checkouts
     *             1 - number of students
     */
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

    /**
     * Creates a new new mensa object with the specified number of checkouts and students.
     * @param checkouts number of checkouts
     * @param students number of students
     */
    public Mensa(int checkouts, int students) {
        this.checkouts = new Checkout[checkouts];
        this.students = new Student[students];
        this.timer = new Timer();
    }

    /**
     * Runs the mensa/canteen:
     * 1) The timer is started and a notification about mensa parameters displayed.
     * 2) All checkouts are created.
     * 3) All students are created and their threads started.
     * 4) After a set amount of time, interrupt all student threads.
     */
    public void start() {
        timer.start();
        System.err.println(timer.time() + "| " + "Setting up " + checkouts.length + " checkouts for " + students.length + " students.\nEnjoy your meal.");
        System.err.println("======================================================");

        for (int i = 0; i < checkouts.length; i++) {
            checkouts[i] = new Checkout(i, timer);
        }
        for (int i = 0; i < students.length; i++) {
            students[i] = new Student(i, checkouts, timer);
            students[i].start();
        }
        try {
            Thread.sleep(1000 * SECONDS_OPENED);
        } catch (InterruptedException e) {
            System.err.println("Früher Küchenschluss.");
        }
        finally {
            System.err.println(timer.time() + "| " + "=================== Küchenschluss! ===================");
            for (Student student : students) {
                student.interrupt();
            }
        }
    }
}
