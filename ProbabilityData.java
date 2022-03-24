import java.util.HashMap;

/**
 * ProbabilityData
 */
public class ProbabilityData {
    //*Data for protection and linkage
    private int uniqueId;
    private boolean locked;


    //* Data used to get probabilities
    private HashMap<Integer, Character> validChars;
    private HashMap<String, TestWord> wordMap;
    private ValidityData validityData;
    private WordData wordData;

    //* Data to be read by other methods
    private HashMap<Integer, HashMap<Character, Double>> probGreenMap;
    private HashMap<Integer, HashMap<Character, Double>> probYellowMap;
    private HashMap<Integer, HashMap<Character, Double>> probBlackMap;

    //* Main constructor
    //* Built for no linkage issues
    public ProbabilityData(HashMap<Integer, Character> validChars, HashMap<String, TestWord> wordMap, ValidityData validityData, WordData wordData){
        this.uniqueId = 0;
        this.locked = false;

        this.validChars = new HashMap<>(validChars);
        this.wordMap = new HashMap<>(wordMap);
        this.validityData = validityData.getCopy();
        this.wordData = wordData.getLockedCopy();

        this.probGreenMap = fillGreenMap();
        this.probYellowMap = new HashMap<>();
        this.probBlackMap = new HashMap<>();


    }



    private HashMap<Integer, HashMap<Character, Double>> fillGreenMap() {
        HashMap<Integer, HashMap<Character, Double>> dataOut = new HashMap<>(); 

        for(int position = 0; position < App.WORD_LENGTH; position++){
            dataOut.put(position, new HashMap<>());
            for(Character curChar : this.validChars.values()){
                double totalChars = 0;
                double validChars = 0;

                // for(Character validityChar : )

                // double percentage = 1 / 1;
                // dataOut.get(position).put(curChar, percentage);
            }
        }


        return dataOut;
    }

    

}