package haw.rg.bs.lounge;

/**
 * Ingredients for the smoking simulator.
 * @author Robert Gnehr
 */
public enum Ingredients {
    TOBACCO,
    PAPER,
    MATCHES;

    /**
     * Returns the enum from a their corresponding ordinal value.
     * @param i the enums ordinal value
     * @return Ingredients enum object
     */
    public static Ingredients fromOrdinal(int i) {
        try {
            return values()[i];
        }
        catch ( IndexOutOfBoundsException e ) {
            return null;
        }
    }
}
