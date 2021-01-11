package haw.rg.bs.lounge;

/**
 * Lounge for the smoking (producer/consumer) simulator. Manages the programmes sequencing and all objects:
 * Creates a table, some smokers and agents and lets them interact for a while, before stopping all of their
 * threads.
 * @author Robert Gnehr
 */
public class Lounge {

    private static final int DEFAULT_SMOKER = Ingredients.values().length;
    private static final int DEFAULT_AGENTS = 2;
    private final static int DURATION = 20;

    private Table<Ingredients> table = new Table<>();

    /**
     * Smoking main method. Creates a new lounge object and starts the simulation process.
     * @param args none
     */
    public static void main(String[] args) {
        new Lounge().startSession();
    }

    /**
     * Starts a new smoking session simulation by creating (and starting) agents and smokers and letting them
     * run for a set amount of time, before finally interrupting and consequently stopping them.
     */
    public void startSession() {
        // By collecting all threads in one array the can easily be targeted to interrupt them
        Thread[] participants = new Thread[DEFAULT_AGENTS + DEFAULT_SMOKER];

        System.out.println("========== Time for a break. Smoke away. ==========");
        // create agents
        for (int i = 0; i < DEFAULT_AGENTS; i++) {
            participants[i] = new Agent(i, table);
            participants[i].start();
        }

        // create smokers
        for (int i = 0; i < DEFAULT_SMOKER; i++) {
            participants[DEFAULT_AGENTS + i] = new Smoker(Ingredients.fromOrdinal(i), table);
            participants[DEFAULT_AGENTS + i].start();
        }

        // Let smokers and agents run for a while
        try {
            Thread.sleep(1000 * DURATION);
        } catch (InterruptedException e) {
            System.err.println("Session was interrupted.");
        }

        // stop smokers and agents
        System.out.println("====== Smoking session is over. Get some fresh air. ======");
        for (Thread thread : participants) {
            thread.interrupt();
        }
    }
}