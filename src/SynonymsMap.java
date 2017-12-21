/**
 * @author Chen Zeng
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class SynonymsMap {

    /**
     * Synonyms hash map
     * KEYS: synonyms words
     * VALUE: synonyms root
     */
    private HashMap<String, String> synonymsMap;

    /**
     * Constructor, builds a hash map from the synonyms input
     *
     * Example:
     * run sprint jog
     * good great
     * -->
     * sprint: run
     * jog: run
     * great: good
     *
     * "run" will not be added to map since it's a synonyms root
     *
     * @param synonymsFile Path to the synonyms input file
     */
    public SynonymsMap(String synonymsFile) {
        this.synonymsMap = new HashMap<>();
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(synonymsFile));

            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                String[] synonymsGroup = currentLine.split(" ");
                for (int i = 1; i < synonymsGroup.length; i++) {
                    this.synonymsMap.put(synonymsGroup[i], synonymsGroup[0]);
                }
            }
            fileReader.close();
        } catch (java.io.IOException e) {
            System.err.println("Error: reading the synonyms input file");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Check if a word is in synonyms map
     * @param word The word to check
     * @return A boolean indicates whether the word is in synonyms map
     */
    public boolean isSynonymsWord(String word) {
        return this.synonymsMap.containsKey(word);
    }

    /**
     * Get the synonyms root from synonyms map
     * @param word The word to get synonyms root
     * @return The synonyms root of input word
     */
    public String getSynonymsRoot(String word) {
        return this.synonymsMap.get(word);
    }

    /**
     * Helper function
     * @return The synonyms hash map
     */
    public HashMap<String, String> getSynonymsMap() {
        return this.synonymsMap;
    }

    /**
     * Helper function
     * Print the key-value pair stored in the synonyms map
     */
    public void printMap() {
        System.out.println("Printing synonyms hash map:");
        for (String key : synonymsMap.keySet()){
            String value = synonymsMap.get(key.toString()).toString();
            System.out.println(key + " : " + value);
        }
        System.out.println();
    }
}
