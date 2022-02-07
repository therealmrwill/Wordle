import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCheckerV2 {
    //* Static Parameters
    private int WORD_LENGTH;
    private int NUM_OF_GUESSES;

    //* Word Validity Parameters
    private char[] currentLockedChars;
    private char[] currentProvenChars;
    private ArrayList<Character> currentTestableCharacters;
    private ArrayList<Character> currentInvalidChars;
    private Map<Integer, ArrayList<Character>> currentPositionalInvalidChars;

    //* Word Scoring Parameters
    private List<String> baseWords;

    private Map<Character, Integer> baseRankedChars;
    private Map<Character, Integer> currentRankedChars;

    private Map<Integer, Map<Character, Integer>> basePositionalRankedChars;
    private Map<Integer, Map<Character, Integer>> currentPositionalRankedChars;

    private Map<Character, Integer> currentDiversityRankedChars;

    //* Output Parameters
    private List<String> currentValidWords;
    private List<String> currentOrderedWords;

    //* Debug or testing parameters
    //Currently there are none

    //* Constructor(s)
    public WordCheckerV2(List<String> testData, int wordLength, int numberOfGuesses){
        // Static Parameters 
        this.WORD_LENGTH = wordLength;
        this.NUM_OF_GUESSES = numberOfGuesses;

        // Word Validity Parameters
        initializeValidityParams();

        // Word Scoring Parameters
        initializeScoringParams(testData);

        // Output Parameters
        initializeOutputParams();
    }

        //* Constructor Helpers (In call order)
        private void initializeValidityParams() {
            //Creation of Parameters
            this.currentLockedChars = new char[this.WORD_LENGTH]; //Needs Filled
            this.currentProvenChars = new char[this.WORD_LENGTH]; //Needs Filled
            this.currentTestableCharacters = new ArrayList<>(26); //Needs Filled
            this.currentInvalidChars = new ArrayList<>();
            this.currentPositionalInvalidChars = new HashMap<>(this.WORD_LENGTH); //Needs Filled
            ArrayList<Character> blankArrayList = new ArrayList<>();

            //Filling of Parameters
            for(int i = 0; i < this.WORD_LENGTH; i++){
                this.currentLockedChars[i] = '_';
                this.currentProvenChars[i] = '_';
                this.currentPositionalInvalidChars.put(i, blankArrayList);
            }
            
                //Filling of currentTestableCharacters
                String validLetterString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                char[] charArray = validLetterString.toCharArray();
                //iterating through the character array
                for(char currentChar : charArray){
                    this.currentTestableCharacters.add(currentChar);
                }
            
            //? Turn on to check functionality of Validity Param Initializer
            //System.out.println("Validity Params Initialized");
        }

        private void initializeScoringParams(List<String> testData) {
            //Creation of Parameters
            this.baseWords = new ArrayList<>();
            this.baseRankedChars = new HashMap<>();
            this.currentRankedChars = new HashMap<>();
            this.basePositionalRankedChars = new HashMap<>();
            this.currentDiversityRankedChars = new HashMap<>();
            this.currentPositionalRankedChars = new HashMap<>();
            

            //Temporary Parameter Prep
            Map<Character, Double> charTotal = charMapFiller();
            Map<Character, Double> diversity = charMapFiller();

            Map<Character, Double> posCharTotal = new HashMap<>();
            Map<Character, Double> pos0CharTotal = charMapFiller();
            Map<Character, Double> pos1CharTotal = charMapFiller();
            Map<Character, Double> pos2CharTotal = charMapFiller();
            Map<Character, Double> pos3CharTotal = charMapFiller();
            Map<Character, Double> pos4CharTotal = charMapFiller();
            
            
            
            //Filling of baseWords and all Temp Parameters
            for (String string : testData) {
                if(string.length() == this.WORD_LENGTH){
                    baseWords.add(string);
                    char[] charArray = string.toCharArray();
                    ArrayList<Character> previousChars = new ArrayList<>();
                    pos0CharTotal.put(charArray[0], pos0CharTotal.get(charArray[0]) + 1);
                    pos1CharTotal.put(charArray[1], pos1CharTotal.get(charArray[1]) + 1);
                    pos2CharTotal.put(charArray[2], pos2CharTotal.get(charArray[2]) + 1);
                    pos3CharTotal.put(charArray[3], pos3CharTotal.get(charArray[3]) + 1);
                    pos4CharTotal.put(charArray[4], pos4CharTotal.get(charArray[4]) + 1);

                    for (int i = 0; i < charArray.length; i++){
                        boolean isDuplicate = false;
                        char currentChar = charArray[i];
                        charTotal.put(currentChar, charTotal.get(currentChar) + 1);
                        
                        if(!previousChars.contains(currentChar)){
                            diversity.put(currentChar, diversity.get(currentChar) + 1);
                            previousChars.add(currentChar);
                        }
                        
                    }
                }

            }


            //? Turn on to check functionality of Scoring Param Initializer
            //System.out.println("Scoring Params Initialized");
        }

            //* Scoring Initializer Helpers (In call order)
            private Map<Character, Double> charMapFiller(){
                Map<Character, Double> dataMap = new HashMap<>();
                String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                char[] charArray = alphabet.toCharArray();
                for (char currentChar : charArray) {
                    dataMap.put(currentChar, 0d);
                }
                return dataMap;
            }

        private void initializeOutputParams() {
        }
    

    
    

    

}
