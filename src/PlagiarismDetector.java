/**
 * @author Chen Zeng
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class PlagiarismDetector {

    private String synonymsFile;
    private String input1Path;
    private String input2Path;

    private int tupleSize;

    private SynonymsMap synonymsMap;

    /**
     * Constructor
     * @param synonymsFile Path to the synonyms input file
     * @param input1Path Path to the input file 1
     * @param input2Path Path to the input file 2
     */
    public PlagiarismDetector(String synonymsFile, String input1Path, String input2Path) {
        this.synonymsFile = synonymsFile;
        this.input1Path = input1Path;
        this.input2Path = input2Path;
        this.tupleSize = 3;
    }

    /**
     * Tuple size setter
     * @param size New tuple size
     */
    public void setTupleSize(int size) {
        this.tupleSize = size;
    }

    /**
     * Build the synonyms map
     */
    public void buildSynonymsMap() {
        this.synonymsMap = new SynonymsMap(synonymsFile);
    }

    /**
     * Read an input file to an array of lowercase words, without punctuations
     * @param path Path to the input file
     * @return Array of plain words in the file
     * @throws Exception
     */
    private String[] readFileToWords(String path) throws Exception {

        ArrayList<String> words = new ArrayList<>();
        String[] wordsArr;

        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(path));

            // read the file line by line, call readLineToWords() to process the lines
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                words.addAll(Arrays.asList(readLineToWords(currentLine)));
            }
            fileReader.close();

        } catch (java.io.IOException e) {
            System.err.println("Error: reading one of the input file");
            e.printStackTrace();
            System.exit(1);
        }

        // convert the array list to array and then return
        wordsArr = new String[words.size()];
        return words.toArray(wordsArr);
    }

    /**
     * Convert an input line to an array of lowercase words, without punctuations
     * @param line Input line
     * @return Array of plain words in the line
     * @throws Exception
     */
    private String[] readLineToWords(String line) throws Exception {
        return line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" ");
    }

    /**
     * Process two input files and calculate their similarity
     * @return The percentage of N-tuples in file1 that appear in file2, a ratio between 0~1
     * @throws Exception
     */
    public double calculateSimilarity() throws Exception {
        String[] file1Content = readFileToWords(input1Path);
        String[] file2Content = readFileToWords(input2Path);

        // generate tuple lists for both files
        // the synonyms words will all be unified to its root form (e.g. jog --> run)
        ArrayList<Tuple> file1TupleList = genUnifiedTuples(file1Content);
        ArrayList<Tuple> file2TupleList = genUnifiedTuples(file2Content);

        return CompareTupleSet(file1TupleList, file2TupleList);
    }

    /**
     * Generate N-tuples from the words array of the file
     * Meanwhile, unify the synonyms words to their root form (e.g. jog --> run)
     * "run" will remain as itself since it's a synonyms root
     *
     * Unify the synonyms words before comparisons
     * because tuples overlap each other,
     * doing synonyms check during comparison would cause lots of repeated checks,
     * especially when tuple size is big
     *
     * @param words Array of plain words in a file
     * @return A List of N-tuples generated from the words array
     * @throws Exception
     */
    private ArrayList<Tuple> genUnifiedTuples(String[] words) throws Exception {
        // check tuple size VS words array size first
        if (words.length < tupleSize) {
            System.out.println("Cannot generate tuples from file:");
            System.out.println("tuple size is too big, or input file size is too small.");
            System.exit(1);
        }

        ArrayList<Tuple> tupleList = new ArrayList<>();
        // index < N-1
        // don't have enough words to make a tuple
        // unify the synonyms words still
        for (int i = 0; i < tupleSize - 1; i++) {
            if (synonymsMap.isSynonymsWord(words[i])) {
                words[i] = synonymsMap.getSynonymsRoot(words[i]);
            }
        }
        // index reaches N-1
        // we have N words to make a tuple now
        for (int i = tupleSize - 1; i < words.length; i++) {
            // unify the synonyms words to their roots
            if (synonymsMap.isSynonymsWord(words[i])) {
                words[i] = synonymsMap.getSynonymsRoot(words[i]);
            }
            // create tuple and add N words to it
            Tuple newTuple = new Tuple(tupleSize);
            for (int j = i - tupleSize + 1; j <= i; j++) {
                newTuple.addWord(words[j]);
            }
            tupleList.add(newTuple);
        }
        return tupleList;
    }

    /**
     * Compare two tuple sets and calculate the percentage of matches
     * Made it a separate public function so that it could be utilized by other cases
     * @param file1Tuples Word array of file1
     * @param file2Tuples Word array of file2
     * @return The percentage of N-tuples in file1 that appear in file2, a ratio between 0~1
     */
    public double CompareTupleSet(ArrayList<Tuple> file1Tuples, ArrayList<Tuple> file2Tuples) {
        int file1TupleTotal = file1Tuples.size();
        int file1TupleSimilar = 0;

        for (Tuple file1Tuple : file1Tuples) {
            for (Tuple file2Tuple : file2Tuples) {
                if (file1Tuple.equalsTo(file2Tuple)) {
                    file1TupleSimilar++;
                }
            }
        }
        return ((double)file1TupleSimilar / (double)file1TupleTotal);
    }

    /**
     * Helper function, print detector information
     */
    public void printDetectorInfo() {
        System.out.println("Synonyms file input: " + synonymsFile);
        System.out.println("File 1 input: " + input1Path);
        System.out.println("File 2 input: " + input2Path);
        System.out.println("Tuple size: " + tupleSize);
    }


    /**
     * Main function of the Plagiarism Detector
     * @param args See usage below
     */
    public static void main(String[] args) {

        if (args.length < 3 || args.length > 4) {
            System.out.println("Usage:");
            System.out.println("Java -jar PlagiarismDetector.jar <synonyms file path> <file 1 path> <file 2 path> <tuple size>(optional)");
            System.exit(0);
        }

        PlagiarismDetector detector = new PlagiarismDetector(args[0], args[1], args[2]);

        if (args.length == 4) {
            try {
                int tupleSize = Integer.parseInt(args[3]);
                if (tupleSize < 2) {
                    System.err.println("Warning: Tuple size must be an integer, set to default = 3");
                } else {
                    detector.setTupleSize(tupleSize);
                }
            } catch (java.lang.NumberFormatException e) {
                System.err.println("Warning: Tuple size must be an integer, set to default = 3");
            }
        }

        try {
            detector.buildSynonymsMap();
            double similarity = detector.calculateSimilarity();

            System.out.println();
            detector.printDetectorInfo();
            System.out.println();
            System.out.printf("Similarity: %.2f%%", similarity * 100);
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}