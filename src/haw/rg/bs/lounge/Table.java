package haw.rg.bs.lounge;

/**
 * Bounded Server for Smoking Simulation. Must be used with (some extension of) Ingredients class.
 * The table's 'buffer' has only 1 place for an ingredient array (currently only of length = 2).
 * This class offers a method to place ingredients into the buffer and one to take them out.
 * @author Robert Gnehr
 */
public class Table<E extends Ingredients> {

    private E[] ingredients = null;

    /**
     * Synchronized method to place ingredients onto the table (buffer). Thread will wait while table is not empty.
     * If the ingredients are provided in correct numbers and are different from each other, they are stored
     * to the table and all threads (agents & smokers) are notified.
     * @param ingredients array of 2 different ingredients
     * @throws InterruptedException if interrupted
     */
    public synchronized void provideIngredients(E[] ingredients) throws InterruptedException {

        // solange schon Zutaten auf dem Tisch sind: warten
        while (!tableEmpty()) {
            this.wait();
        }

        // Zutaten bereitstellen (nur wenn genau 2 unterschiedliche angeboten werden)
        if (ingredients.length == 2 && (ingredients[0] != ingredients[1])) {
            System.out.println(">> " + Thread.currentThread().getName() + " places " + ingredients[0] + " and " + ingredients[1] + " on the table.");
            this.ingredients = ingredients;
        }

        // andere Threads wecken
        this.notifyAll();
    }

    /**
     * Synchronized method to take the ingredients from the table (i.e. out of the buffer). Only Smoker objects
     * with the one missing ingredient are able to do that. Threads will also wait() while the buffer is empty.
     * After taking the ingredients the smoker thread will sleep for some time to 'smoke'. Lastly all other threads
     * (smokers & agents) are notified.
     * @return array of ingredients which the smoker was missing
     * @throws InterruptedException if interrupted
     */
    public synchronized E[] takeIngredients() throws InterruptedException {

        // warten, solange keine oder nicht die richtigen Zutaten auf dem Tisch sind
        while(tableEmpty() || !hasMissingIngredient()) {
            this.wait();
        }

        // Zutaten vom Tisch nehmen
        System.out.println("<< " + Thread.currentThread().getName() + " takes " + ingredients[0] + " and " + ingredients[1] + " and has a smoke.");

        E[] tmp = ingredients;
        ingredients = null;

        // Zeit zum gemütlichen Rauchen nehmen
        Thread.sleep(2500 + (int)(Math.random() * 1000));
        System.out.println(Thread.currentThread().getName() + " is done smoking.");

        // andere Threads wecken
        this.notifyAll();

        return tmp;
    }

    /**
     * Helper method to check if a thread is eligible for taking ingredients: Thread has to
     * 1) be an object of Smoker class and
     * 2) the ingredient it already owns must not be on the table (buffer).
     * @return true if object is allowed to take ingredients, false otherwise
     */
    private boolean hasMissingIngredient() {
        try {
            // Wenn cast fehlschlägt, ist Thread kein Smmoker
            Smoker smoker = (Smoker) Thread.currentThread();

            // Wenn mitgebrachte Zutat bereits auf dem Tisch liegt, darf der Smoker nicht zugreifen
            for (E ingredient: ingredients) {
                if (ingredient == smoker.getIngredient()) {
                    return false;
                }
            }
            return true;
        }
        catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Helper method to check if the buffer is full or empty, i.e. if the ingredients array has any elements
     * @return false if buffer is full, true otherwise
     */
    public boolean tableEmpty() {
        return (ingredients == null);
    }

}
