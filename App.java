import java.util.ArrayList;
import java.util.HashMap;

/**
 * App
 */
public class App {
    public static final int WORD_LENGTH = 5;
    public static final int NUM_OF_ROUNDS = 6;
    public static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //* Valid Debug Levels: 1. Testing, 2. Production
    public static final int DEBUG_LEVEL = 1;
    

    

    public static void main(String[] args) {
        HashMap<String, TestWord> words = DataReader.readFromFile("DataFiles/FullWordList.txt", true);


        ValidityData validity = new ValidityData();
        
        validity.addWord(new WordData("A_A_Z", "YRGRY".toCharArray()));        

        ProbabilityData probData = new ProbabilityData(validity);



        
        
        // System.out.println(validity.getColoredValidityMap());

        // HashMap<String, TestWord> validWords = new HashMap<>();
        // for (TestWord word : words.values()) {
        //     if(validity.isValid(word)){
        //         validWords.put(word.getName(), word);
        //     }
        // }

        


        System.out.println("Completed");
    }




    

}


