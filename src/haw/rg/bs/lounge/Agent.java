package haw.rg.bs.lounge;

/**
 * Class that simulates an agent who will provide random smoking ingredients to a table.
 * @author Robert Gnehr
 */
public class Agent extends Thread {

    private Table<Ingredients> table;

    /**
     * Creates an agent object.
     * @param name int, only used to create the agent's name.
     * @param table table to place ingredients on, a bounded buffer of sorts.
     */
    public Agent(int name, Table<Ingredients> table) {
        this.setName("Agent 00" + name);
        this.table = table;
    }

    /**
     * Overrides Thread.run(). The agent will continuously try to place randomly chosen ingredients on the table, so
     * that only on ingredient is missing. When interrupted the thread will return and stop.
     */
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                System.out.println(getName() + " wants to provide some smoking ingredients.");
                table.provideIngredients(chooseIngredients());
                Thread.sleep(200);
            }
            catch (InterruptedException e) {
                System.out.println("= " + getName() + " leaves the lounge.");
                return;
            }
        }
    }

    /**
     * Creates an array of ingredients where one value is randomly missing
     * @return ingredient array. One enum value is omitted.
     */
    private Ingredients[] chooseIngredients() {
        // zufällige Zutat auswählen (0/1/2)
        int exclude = (int) (Math.random() * 3);                                // length = 3 (paper, matches, tobacco)
        Ingredients[] tmp = new Ingredients[2];                                 // Ingredients[2]: one is missing

        // alle Zutat außer der zufällig ausgewählten in einen Array stecken
        int j =0;
        for (int i = 0; i < Ingredients.values().length; i++) {
            if (i == exclude) continue;
            tmp[j++] = Ingredients.fromOrdinal(i);
        }

        return tmp;
    }
}
