import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WordCheckerV2 {
    //* Static Parameters
    private int WORD_LENGTH;
    private int NUM_OF_ROUNDS;
    private String alphabetString;

    //* Word Validity Parameters
    private char[] currentLockedChars;
    private ArrayList<Character> currentProvenChars;
    private ArrayList<Character> currentTestableCharacters;
    private ArrayList<Character> currentInvalidChars;
    private Map<Integer, ArrayList<Character>> currentPositionalInvalidChars;

    //* Word Scoring Parameters
    private int currentRound;
    private List<String> baseWords;

    private Map<Character, Integer> baseRankedChars;
    private Map<Character, Integer> currentRankedChars;

    private Map<Integer, Map<Character, Integer>> basePositionalRankedChars;
    private Map<Integer, Map<Character, Integer>> currentPositionalRankedChars;

    private Map<Character, Integer> currentDiversityRankedChars;

    //* Output Parameters
    private List<String> currentValidWords;
    private List<String> currentInvalidWords;
    private List<String> currentOrderedWords;

    //* Debug or testing parameters
    //Currently there are none

    //* Constructor(s)
    public WordCheckerV2(List<String> testData, int wordLength, int numberOfGuesses){
        // Static Parameters 
        this.WORD_LENGTH = wordLength;
        this.NUM_OF_ROUNDS = numberOfGuesses;
        this.alphabetString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        

        // Word Validity Parameters
        initializeValidityParams();

        // Word Scoring Parameters
        initializeScoringParams(testData);

        // Output Parameters
        initializeOutputParams();


        //? Turn on to validate that that wordChecker was constructed
        //System.out.println("\nDebug Helper: new wordChecker successfully constructed");
    }

        //* Constructor Helpers (In call order)
        private void initializeValidityParams() {
            //Creation of Parameters
            this.currentLockedChars = new char[5]; 
            this.currentProvenChars = new ArrayList<>(); 
            this.currentTestableCharacters = new ArrayList<>(26); //Needs Filled
            this.currentInvalidChars = new ArrayList<>();
            this.currentPositionalInvalidChars = new HashMap<>(this.WORD_LENGTH); //Needs Filled
            ArrayList<Character> blankArrayList = new ArrayList<>();

            //Filling of Parameters
            for(int i = 0; i < this.WORD_LENGTH; i++){
                this.currentLockedChars[i] = '#';
                this.currentPositionalInvalidChars.put(i, blankArrayList);
            }
            
                //Filling of currentTestableCharacters
                char[] charArray = this.alphabetString.toCharArray();
                //iterating through the character array
                for(char currentChar : charArray){
                    this.currentTestableCharacters.add(currentChar);
                }
            
            //? Turn on to check functionality of Validity Param Initializer
            //System.out.println("Validity Params Initialized");
        }

        private void initializeScoringParams(List<String> testData) {
            //Creation of Parameters
            this.currentRound = 0;
            this.baseWords = new ArrayList<>();
            this.baseRankedChars = new HashMap<>();
            this.currentRankedChars = new HashMap<>();
            this.basePositionalRankedChars = new HashMap<>();
            this.currentDiversityRankedChars = new HashMap<>();
            this.currentPositionalRankedChars = new HashMap<>();
            

            //Temporary Parameter Prep
            Map<Character, Double> charTotal = charMapFiller();
            Map<Character, Double> diversity = charMapFiller();

            Map<Character, Double> pos0CharTotal = charMapFiller();
            Map<Character, Double> pos1CharTotal = charMapFiller();
            Map<Character, Double> pos2CharTotal = charMapFiller();
            Map<Character, Double> pos3CharTotal = charMapFiller();
            Map<Character, Double> pos4CharTotal = charMapFiller();
            
            
            
            //Filling of baseWords and all Temp Parameters
            for (String string : testData) {
                if(string.length() == this.WORD_LENGTH){
                    this.baseWords.add(string);
                    char[] charArray = string.toCharArray();
                    ArrayList<Character> previousChars = new ArrayList<>();
                    pos0CharTotal.put(charArray[0], pos0CharTotal.get(charArray[0]) + 1);
                    pos1CharTotal.put(charArray[1], pos1CharTotal.get(charArray[1]) + 1);
                    pos2CharTotal.put(charArray[2], pos2CharTotal.get(charArray[2]) + 1);
                    pos3CharTotal.put(charArray[3], pos3CharTotal.get(charArray[3]) + 1);
                    pos4CharTotal.put(charArray[4], pos4CharTotal.get(charArray[4]) + 1);

                    for (int i = 0; i < charArray.length; i++){
                        char currentChar = charArray[i];
                        charTotal.put(currentChar, charTotal.get(currentChar) + 1);
                        
                        if(!previousChars.contains(currentChar)){
                            diversity.put(currentChar, diversity.get(currentChar) + 1);
                            previousChars.add(currentChar);
                        }
                        
                    }
                }

            }

            //Filling of true Parameters
            this.baseRankedChars = baseCharRank(charTotal);
            this.currentRankedChars = this.baseRankedChars;
            this.basePositionalRankedChars.put(0, baseCharRank(pos0CharTotal));
            this.basePositionalRankedChars.put(1, baseCharRank(pos1CharTotal));
            this.basePositionalRankedChars.put(2, baseCharRank(pos2CharTotal));
            this.basePositionalRankedChars.put(3, baseCharRank(pos3CharTotal));
            this.basePositionalRankedChars.put(4, baseCharRank(pos4CharTotal));
            this.currentPositionalRankedChars = this.basePositionalRankedChars;
            this.currentDiversityRankedChars = baseCharRank(diversity);


            //? Turn on to check functionality of Scoring Param Initializer
            //System.out.println("Debug Helper: WordChecker.initializeScoringParams() ran successfully");
        }

            //* Scoring Initializer Helpers (In call order)
            private Map<Character, Double> charMapFiller(){
                Map<Character, Double> dataMap = new HashMap<>();
                char[] charArray = this.alphabetString.toCharArray();
                for (char currentChar : charArray) {
                    dataMap.put(currentChar, 0d);
                }
                return dataMap;
            }

            private Map<Character, Integer> baseCharRank(Map<Character, Double> dataMap){
                //Sorting and duplicate checking for characters
                char[] charArray = this.alphabetString.toCharArray();
                TreeMap<Double, Character> orderedCharMap = new TreeMap<>();
                ArrayList<Double> previousScores = new ArrayList<>();
                double currentScoreSubtraction = .01d;

                for (char currentChar : charArray) {
                    double currentScore = dataMap.get(currentChar);
                    if(previousScores.contains(currentScore)){
                        orderedCharMap.put(currentScore - currentScoreSubtraction, currentChar);
                        previousScores.add(currentScore - currentScoreSubtraction);
                        currentScoreSubtraction += .01d;
                    }
                    else{
                        orderedCharMap.put(currentScore, currentChar);
                        previousScores.add(currentScore);
                    }
                    
                }

                Map<Character, Integer> dataOutMap = new HashMap<>();

                //Filling of dataOutMap
                Set<Map.Entry<Double, Character>> keySet = orderedCharMap.entrySet();
                Iterator<Map.Entry<Double, Character>> iterator = keySet.iterator();
                int charRank = 1;
                while(iterator.hasNext()){
                    Map.Entry<Double,Character> currentEntry = iterator.next();
                    if(currentEntry.getKey() > 0){
                        dataOutMap.put(currentEntry.getValue(), charRank);
                        charRank++;
                    }
                }
                
                //? Turn Line on to test if characters are being sorted correctly
                //System.out.println("\nDebug Info: wordChecker.baseCharRank() ran successfully");

                //Returning ranked charMaP
                return dataOutMap;
            }

        private void initializeOutputParams() {
            //Instantiation and filling of output parameters
            this.currentValidWords = this.baseWords;
            this.currentInvalidWords = this.baseWords;
            this.currentOrderedWords = listOrdering(this.baseWords);
        }



    //* Main Methods (In call order)
    private List<String> listOrdering(List<String> listToOrder) {
            TreeMap<Double, String> orderedScores = new TreeMap<>();
            List<Double> previousScores = new ArrayList<>();
            double currentScoreSubtraction = .000001d;

            for (String string : listToOrder) {
                //Keep in mind that everything in here is ran at max over 12k times per call so keep everything streamlined
                Double stringScore = getStringScore(string);
                if(previousScores.contains(stringScore)){
                    orderedScores.put(stringScore - currentScoreSubtraction, string);
                    previousScores.add(stringScore - currentScoreSubtraction);
                    currentScoreSubtraction += .000001d;    
                }
                else{
                    orderedScores.put(stringScore, string);
                    previousScores.add(stringScore);
                }    
            }

            List<String> dataOut = new ArrayList<>();

            //Filling of dataOut
            Set<Map.Entry<Double,String>> keySet = orderedScores.entrySet();
            Iterator<Map.Entry<Double,String>> iterator = keySet.iterator();
            while(iterator.hasNext()){
                dataOut.add(iterator.next().getValue());
            }

            //? Turn Line on to test if words are being sorted correctly
            //System.out.println("\n Debug Info: wordChecker.listOrdering() ran successfully");

            return dataOut;
        }

        //* listOrdering() helper functions (by call order)
        private Double getStringScore(String currentString) {
                //Everything in here running at max over 12k times per listOrdering() call
                //Keep all code as streamlined as possible
                Double wordScore = 0d;
                ArrayList<Character> previousCharacters = new ArrayList<>();

                //Score changes for every character in the word
                for(int i = 0; i < currentString.length(); i++){
                    //Clears previous data
                    previousCharacters.clear();

                    //Adds this characters score to the main score 
                    wordScore += getCharScore(currentString.charAt(i), i, previousCharacters);
                    
                    //Adds Character to previousCharacters
                    if(!previousCharacters.contains(currentString.charAt(i))){
                        previousCharacters.add(currentString.charAt(i));
                    }
                }

                //Score changes based on word
                    //Score Loss based on if word is invalid
                    //Checks if word is present in the smaller of the two lists to help streamline (especially on round 0)
                    //score / 4 ^ currentRound
                    if(this.currentValidWords.size() < this.currentInvalidWords.size() && !this.currentValidWords.contains(currentString)){
                        wordScore = wordScore / Math.pow(4, this.currentRound);
                    }
                    else if(this.currentInvalidWords.contains(currentString)){
                        wordScore = (wordScore / Math.pow(4, this.currentRound));
                    }

                //? Turn line(s) on to test if words are being scored correctly
                // System.out.printf("\nDebug Info: wordChecker.getStringScore(%s) ran successfully %n", currentString);
                // System.out.printf("Word: %s -- Score: %.2f", currentString, wordScore);

                //Return Data
                return wordScore;
            }

            //* getStringScore helper methods (by call order)
            private Double getCharScore(char currentChar, int i, ArrayList<Character> previousCharacters) {
                //Everything in here running at max over 63k times per listOrdering() call
                // Keep all code as streamlined as possible
                //All score changers marked with:
                //ON or !OFF
                //Max and Min score changer
                //* if code line(s) needed to run for execution

                double charScore = 0;
                int characterRank = 0;
                int characterPositionRank = 0;
                int characterDiversityRank = 0;
                
                //Score based on current total character rank 
                //ON
                //Max: 26 - Min: 0
                if(this.currentRankedChars.get(currentChar) != null){
                    characterRank = this.currentRankedChars.get(currentChar);
                    charScore += characterRank;
                }

                
                //Score based on current positional rank 
                //ON
                //Max: 26 - Min: 0
                if(this.currentPositionalRankedChars.get(i).get(currentChar) != null){
                    characterPositionRank = this.currentPositionalRankedChars.get(i).get(currentChar);
                    charScore += characterPositionRank;
                }

                //Score based on current diversity ranked chars (how many other words this character shows up in)
                //ON
                //Max: 26 - Min: 0
                if(this.currentDiversityRankedChars.get(currentChar) != null){
                    characterDiversityRank = this.currentDiversityRankedChars.get(currentChar);
                    charScore += characterDiversityRank;
                }

                //Score based on if character is proven to be correct at position
                //Score is set to 0 if round 1-4
                //Score is doubled if round 5 - 6
                //ON
                //Min: 0 - Max: score * 2 (max of 78)
                if(this.currentLockedChars[i] == currentChar && this.currentRound <= 4)
                    charScore -= charScore;
                else if(this.currentLockedChars[i] == currentChar)
                    charScore += charScore;

                //Score based on if character appears in possible character list
                // Added score is multiplied by roundMultiplier / 2
                //ON
                //Max: 108 - Min: 0
                if(this.currentTestableCharacters.indexOf(currentChar) != -1){
                    charScore += (10 + characterPositionRank) * ((this.NUM_OF_ROUNDS - this.currentRound) / 2d);
                }

                //Score based on if character is proven to be in the word, and hasn't already been proven invalid at location
                // Added Score is multiplied by round number
                //ON
                //Max: 198 - Min: 0
                if(this.currentProvenChars.contains(currentChar) && !this.currentPositionalInvalidChars.get(i).contains(currentChar)){
                    charScore += (7 + characterPositionRank) * this.currentRound;
                }

                //Score subtraction based on if this character has already appeared in the word (duplicate)
                //IMPORTANT also adds to previous characters - this part of the code will need to be moved if new duplication code is implemented
                // Score subtraction / 2 ^ this.currentRound
                //ON
                //Max: 0 - Min: subtracting half of total score (Max subtraction would in theory be 488 but that score is impossible for many reasons)
                if(previousCharacters.contains(currentChar)){
                    charScore -= charScore / Math.pow(2, this.currentRound);
                }

                //Score subtraction based if the character is invalid in the current position
                // Score Subtraction 
                //ON
                //Max: 0 - Min: subtracting total score (Max subtraction would in theory be 488 but that score is impossible for many reasons)
                if(currentPositionalInvalidChars.get(i).contains(currentChar)){
                    charScore -= charScore; 
                }

                //TODO: Add new and/or volatile scoring tests here to the impact on findable words

                //? Run this line to make sure that each character is being scored correctly
                // System.out.printf("%n wordChecker.getStringScore.getCharScore(%C) ran succesfully %n", currentChar);
                // System.out.println("\n" + currentChar + ": " + charScore);


                //* Return characters score
                return charScore;
            }
    

    
    

    

}
