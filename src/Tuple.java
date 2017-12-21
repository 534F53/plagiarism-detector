/**
 * @author Chen Zeng
 */

public class Tuple {

    private String[] wordTuple;

    private int tupleSize;
    private int addIndex;

    /**
     * Constructor
     * @param tupleSize Size of the tuple
     */
    public Tuple(int tupleSize) {
        this.tupleSize = tupleSize;
        this.addIndex = 0;
        wordTuple = new String[tupleSize];
    }

    /**
     * Add a word to tuple
     * @param word Word to add
     */
    public void addWord(String word) {
        if (addIndex == tupleSize) {
            System.out.println("Warning: Cannot add more word to tuple.");
            return;
        }
        wordTuple[addIndex] = word;
        addIndex++;
    }

    /**
     * Get a word from tuple
     * @param index Index of the target word in tuple
     * @return The target word
     */
    public String getWord(int index) {
        return wordTuple[index];
    }

    /**
     * Compare the tuple with another tuple to see are they equal
     * @param another Another tuple for comparison
     * @return Are the two tuples equal
     */
    public boolean equalsTo(Tuple another) {
        if (this.tupleSize != another.getTupleSize()) {
            return false;
        }
        for (int i = 0; i < this.tupleSize; i++) {
            if (!this.getWord(i).equals(another.getWord(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper function
     * @return Tuple size
     */
    public int getTupleSize() {
        return tupleSize;
    }

    /**
     * Helper function
     * Print the tuple
     */
    public void print() {
        for (String word : wordTuple) {
            System.out.print(word + " ");
        }
        System.out.println();
    }

}
