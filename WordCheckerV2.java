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

    private Map<Character, Double> baseScoredChars;
    private Map<Character, Double> currentScoredChars;
    private Map<Character, Integer> currentRankedChars;

    private Map<Integer, Map<Character, Double>> basePositionalScoredChars;
    private Map<Integer, Map<Character, Double>> currentPositionalScoredChars;
    private Map<Integer, Map<Character, Integer>> currentPositionalRankedChars;

    private Map<Character, Double> baseDiversityScoredChars;
    private Map<Character, Double> currentDiversityScoredChars;
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

            //Filling of Parameters
            for(int i = 0; i < this.WORD_LENGTH; i++){
                this.currentLockedChars[i] = '#';
                this.currentPositionalInvalidChars.put(i, new ArrayList<>());
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

            this.baseScoredChars = charMapFiller();
            this.currentScoredChars = charMapFiller();
            this.currentRankedChars = new HashMap<>();

            this.basePositionalScoredChars = new HashMap<>();
            for(int i = 0; i < this.WORD_LENGTH; i++){
                this.basePositionalScoredChars.put(i, charMapFiller());
            }
            
            this.currentPositionalScoredChars = new HashMap<>();
            this.currentPositionalRankedChars = new HashMap<>();

            this.baseDiversityScoredChars = charMapFiller();
            this.currentDiversityScoredChars = charMapFiller();
            this.currentDiversityRankedChars = new HashMap<>();
            
            //Filling of all score based parameters
            for (String string : testData) {
                if(string.length() == this.WORD_LENGTH){
                    this.baseWords.add(string);
                    char[] charArray = string.toCharArray();

                    ArrayList<Character> previousChars = new ArrayList<>();

                    for (int i = 0; i < charArray.length; i++){
                        char currentChar = charArray[i];

                        //Adds character to based score char
                        this.baseScoredChars.put(currentChar, this.baseScoredChars.get(currentChar) + 1);

                        //Adds character to positional score char
                        this.basePositionalScoredChars.get(i).put(currentChar, this.basePositionalScoredChars.get(i).get(currentChar) + 1);

                        //If the letter has not already been seen in the word, at it to character diversity
                        if(!previousChars.contains(currentChar)){
                            this.baseDiversityScoredChars.put(currentChar, this.baseDiversityScoredChars.get(currentChar) + 1);
                            previousChars.add(currentChar);
                        }
                        
                    }
                }

            }

            //?After the code above runs, all parameters that are fully prepped are
            //baseWords 
            //baseScoredChars
            //basePositionalScoredChars
            //Base diversityScoredChars

            //Filling of current scored hashmaps
            this.currentScoredChars = this.baseScoredChars;
            this.currentPositionalScoredChars = this.basePositionalScoredChars;
            this.currentDiversityScoredChars = this.baseDiversityScoredChars;

            //Filling of current ranked hashmaps
            this.currentRankedChars = baseCharRank(this.baseScoredChars);

            for(int i = 0; i < this.WORD_LENGTH; i++){
                this.currentPositionalRankedChars.put(i, baseCharRank(this.basePositionalScoredChars.get(i)));
            }

            this.currentDiversityRankedChars = baseCharRank(this.baseDiversityScoredChars);
            
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
            this.currentInvalidWords = new ArrayList<>();
            this.currentOrderedWords = listOrdering(this.baseWords);
        }



    //* Main Methods (In call order)
    public List<String> listOrdering(List<String> listToOrder) {
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
    
    public void addInvalidString(String invalidString, String answerString){

        //Adds word the the invalid word list
        this.currentInvalidWords.add(invalidString);

        //Removes word from valid word list
        if(this.currentValidWords.contains(invalidString)){
            this.currentValidWords.remove(invalidString);
        }

        //Check through every character and add data to validity params
        for(int charIterator = 0; charIterator < invalidString.length(); charIterator++){
            char currentChar = invalidString.charAt(charIterator);

            //* Adding char to validity params
            //Adding a character that is locked in the right position
            if(currentChar == answerString.charAt(charIterator) && this.currentLockedChars[charIterator] == '#'){
                this.currentLockedChars[charIterator] = currentChar;
            } //If character is not "Green" checks if character exists in the word at another position !Could be buggy
            else if(answerString.contains(invalidString.substring(charIterator, charIterator + 1))){
                this.currentProvenChars.add(currentChar);

                if(!this.currentPositionalInvalidChars.get(charIterator).contains(currentChar)){
                    this.currentPositionalInvalidChars.get(charIterator).add(currentChar);
                }

            }// If character is not "Green" or "Yellow" treat it as an invalid character
            else{
                if(!this.currentPositionalInvalidChars.get(charIterator).contains(currentChar)){
                    this.currentPositionalInvalidChars.get(charIterator).add(currentChar);
                }
                if(!this.currentInvalidChars.contains(currentChar)){
                    this.currentInvalidChars.add(currentChar);
                }

            }   


            if(currentTestableCharacters.contains(currentChar)){
                this.currentTestableCharacters.remove(currentChar);
            }
            
        }

        //Removing word from scoring parameters
        //!Does not update any of the ranked score variables
        removeWordFromScoring(invalidString);

        //Adds 1 to round num
        this.currentRound++;

        //Moving words to their proper list
        //Either the valid or invalid list
        
    }

    private void removeWordFromScoring(String wordRemoved) {
        ArrayList<Character> previousCharacters = new ArrayList<>();

        for(int i = 0; i < wordRemoved.length(); i++){
            char currentChar = wordRemoved.charAt(i);

            this.currentScoredChars.put(currentChar, this.currentScoredChars.get(currentChar) - 1);

            this.currentPositionalScoredChars.get(i).put(currentChar, this.currentPositionalScoredChars.get(i).get(currentChar) - 1);

            if(!previousCharacters.contains(currentChar)){
                this.currentDiversityScoredChars.put(currentChar, this.currentDiversityScoredChars.get(currentChar) - 1);
            }
        }
    }

    
    
    

    

}
