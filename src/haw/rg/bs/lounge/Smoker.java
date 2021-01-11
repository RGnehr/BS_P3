package haw.rg.bs.lounge;

/**
 * Class that simulates a smoker who has only one ingredient, but will grab the missing ones from a table to smoke
 * it all.
 * @author Robert Gnehr
 */
public class Smoker extends Thread {

    private final Ingredients ingredient;
    private Table<Ingredients> table;

    /**
     * Creates a new Smoker object.
     * @param ingredient the one ingredient the smoker possesses himself
     * @param table table to get ingredients from, a bounded buffer of sorts.
     */
    public Smoker(Ingredients ingredient, Table<Ingredients> table) {
        this.ingredient = ingredient;
        this.setName("Smoker with " + ingredient);
        this.table = table;
    }

    /**
     * Overrides Thread.run(). The smoker will continuously try to take ingredients from tha table to have a smoke.
     * When interrupted the thread will return and stop.
     */
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                System.out.println(getName() + " is craving a smoke.");

                table.takeIngredients();
                Thread.sleep(200);
            }
            catch (InterruptedException e) {
                System.out.println("= " + getName() + " leaves the lounge.");
                return;
            }
        }
    }

    /**
     * Returns the one ingredients the smoker possesses.
     * @return the one ingredients the smoker possesses.
     */
    public Ingredients getIngredient() {
        return ingredient;
    }
}
