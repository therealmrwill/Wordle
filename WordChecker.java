import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class WordChecker {
    //* Unchanged Parameters
    private String originFileName = null;
    private static final int WORD_LENGTH = 5;


    //* Testing Parameters
    private char[] lockedChars;
    private char[] knownChars;
    private ArrayList<Character> possibleChars;
    private ArrayList<String> currentInvalidWordMap;
    private ArrayList<String> currentValidWordMap;
    private HashMap<Integer, ArrayList<Character>> invalidCharHashWrapper;
    private HashMap<Character, Double> baseTotalLetterMap;
    private HashMap<Character, Double> currentTotalLetterMap;
    private HashMap<Integer, HashMap<Character, Double>> basePositionalLetterMapWrapper;
    private HashMap<Integer, HashMap<Character, Double>> currentPositionalLetterMapWrapper;

    //* New And possibly buggy params
    private Map<Character, Double> wordsCharIsInMap;
    
    //* Output Parameters
    private ArrayList<String> orderedValidWordMap;
    private ArrayList<String> orderedInvalidWordMap;


    //* Constructors
    public WordChecker(String originFileName){
        //*Unchanged Parameters
        this.originFileName = originFileName;

        //*Base values for testing parameters
        this.lockedChars = new char[WORD_LENGTH];
        this.knownChars = new char[WORD_LENGTH];
        this.invalidCharHashWrapper = new HashMap<>();
        this.orderedInvalidWordMap = new ArrayList<>();
        this.currentInvalidWordMap = new ArrayList<>();
        this.basePositionalLetterMapWrapper = new HashMap<>();
        this.currentPositionalLetterMapWrapper = new HashMap<>();
        this.wordsCharIsInMap = charHashCleared();

        //* For loop is a continuation of the base values, some have to be filled using a loop
        for(int i = 0; i < WORD_LENGTH; i++){
            lockedChars[i] = '_';
            knownChars[i] = '_';
            ArrayList<Character> emptyArray = new ArrayList<>();
            this.invalidCharHashWrapper.put(i, emptyArray);
        }

        this.possibleChars = possibleCharFiller();
        
        this.currentValidWordMap = baseDataRetriever();
        this.baseTotalLetterMap = letterScoreFinder();
        this.currentTotalLetterMap = this.baseTotalLetterMap;
        
        for(int i = 0; i < WORD_LENGTH; i++){
            this.basePositionalLetterMapWrapper.put(i, positionedLetterScoreFinder(i));
            
        }

        this.currentPositionalLetterMapWrapper = basePositionalLetterMapWrapper;
        
        allDataUpdate();
    }

    public WordChecker(List<String> validWordArray){
        //*Unchanged Parameters

        //*Base values for testing parameters
        this.lockedChars = new char[WORD_LENGTH];
        this.knownChars = new char[WORD_LENGTH];
        this.invalidCharHashWrapper = new HashMap<>();
        this.orderedInvalidWordMap = new ArrayList<>();
        this.currentInvalidWordMap = new ArrayList<>();
        this.basePositionalLetterMapWrapper = new HashMap<>();
        this.currentPositionalLetterMapWrapper = new HashMap<>();
        this.wordsCharIsInMap = charHashCleared();

        //* For loop is a continuation of the base values, some have to be filled using a loop
        for(int i = 0; i < WORD_LENGTH; i++){
            lockedChars[i] = '_';
            knownChars[i] = '_';
            ArrayList<Character> emptyArray = new ArrayList<>();
            this.invalidCharHashWrapper.put(i, emptyArray);
        }

        this.possibleChars = possibleCharFiller();
        
        this.currentValidWordMap = currentWordMapFiller((validWordArray));
        this.baseTotalLetterMap = letterScoreFinder();
        this.currentTotalLetterMap = this.baseTotalLetterMap;
        
        for(int i = 0; i < WORD_LENGTH; i++){
            this.basePositionalLetterMapWrapper.put(i, positionedLetterScoreFinder(i));
            this.currentPositionalLetterMapWrapper.put(i, positionedLetterScoreFinder(i));
        }
        
        allDataUpdate();
    }

    

    //* Data Setters (Should never have to be used)
    public void setLockedChars(char[] lockedChars){
        this.lockedChars = lockedChars;
    }

    public void setKnownChars(char[] knownChars){
        this.knownChars = knownChars;
    }

    public void setInvalidCharHashWrapper(Map<Integer, ArrayList<Character>> invalidCharHashWrapper){
        this.invalidCharHashWrapper = new HashMap<>(invalidCharHashWrapper);
    }

    public void setCurrentValidWordMap(List<String> validWordMap){
        this.currentValidWordMap = new ArrayList<>(validWordMap);
    }
   
   
    //* Data Getters
    public List<String> getOrderedValidWordMap(){
        return this.orderedValidWordMap; 
    }

    public List<String> getOrderedInvalidWordMap(){
        return this.orderedInvalidWordMap; 
    }

    public List<String> getCurrentValidWordMap(){
        return this.currentValidWordMap;
    }


    public int getValidMapSize(){
        return this.orderedValidWordMap.size(); 
    }

    public int getInvalidMapSize(){
        return this.orderedInvalidWordMap.size();
    }


    //* Public Parameter Changers
    public void addWord(String wordAllCaps, String testValuesAllCaps){
        String word = wordAllCaps;
        String testData = testValuesAllCaps;

        for(int i = 0; i < word.length(); i++){
            if(testData.charAt(i) == 'G'){
                addGreenChar(word.charAt(i), i);
            }
            else if(testData.charAt(i) == 'Y'){
                addYellowChar(word.charAt(i), i);
            }
            else if(testData.charAt(i) == 'B'){
                if(trueBlack(i, word, testData)){
                    addBlackChar(word.charAt(i));  
                }
                else{
                    addYellowChar(word.charAt(i), i);
                }

                     
            }    
            
        }

        allDataUpdate();
    }



    //* ****Everything under this comment needs to be PRIVATE*****
    //* Private Parameter Changers

    private void addLockedChar(char newLockedChar, int position){
        this.lockedChars[position] = newLockedChar;

        // //!new code might be broken
        if(this.possibleChars.indexOf(newLockedChar) != -1){
            this.possibleChars.remove(this.possibleChars.indexOf(newLockedChar));
        }
        
    }

    private void addKnownChar(char newKnownChar){
        //* Checking if character is already known
        for(int i = 0; i < knownChars.length; i++){
            if(newKnownChar == knownChars[i]){
                return;
            }
        }

        //* Adds new character to the first available location
        for(int i = 0; i < knownChars.length; i++){
            if(knownChars[i] == '_'){
                knownChars[i] = newKnownChar;
                return;
            }
        }

    }
    
    private void addInvalidChar(char invalidChar, int letterPosition){
        ArrayList<Character> originalInvalidList = this.invalidCharHashWrapper.get(letterPosition);
        ArrayList<Character> invalidList = new ArrayList<>();
        int listSize = originalInvalidList.size();

        //Checking if the list already contains the character
        for(int i = 0; i < listSize; i++){
            //If the list contains the character immediately end
            if(invalidChar == originalInvalidList.get(i)){
                return; 
            }

            //Else add the other character into the arrayList to preserve data
            invalidList.add(originalInvalidList.get(i));
        }

        //If character is not already in the list, adds character to the now reformed list
        invalidList.add(invalidChar);
        //Also remove it from the possibleLettersList
        // There is a chance this line could cause problems in the future
        if(this.possibleChars.indexOf(invalidChar) != -1){
            this.possibleChars.remove(possibleChars.indexOf(invalidChar));
        }
        
        
        
        
        
        //Then resets the arrayList to be the just made arrayList, preserving all previous, and adding new
        this.invalidCharHashWrapper.put(letterPosition, invalidList);



    }

    private void addInvalidChar(char invalidChar){
        //Just loops through and adds to all positions in word
        for(int i = 0; i < WORD_LENGTH; i++){
            addInvalidChar(invalidChar, i);
        }   
    }

    public void addGreenChar(char greenChar, int positionFromZeroToFour){
        //Green Characters are in correct position already
        addLockedChar(greenChar, positionFromZeroToFour);
    }

    public void addYellowChar(char yellowChar, int positionFromZeroToFour){
        //Adds the character to the known char list
        addKnownChar(yellowChar);
        addInvalidChar(yellowChar, positionFromZeroToFour);
    }

    public void addBlackChar(char blackChar){
        //Adds the new blackCharacter to all parameters relating to invalidity
        addInvalidChar(blackChar);   
    }


//* All Data Prep Methods 

    //* Main Data Prep Methods
    public void allDataUpdate(){
        //? What this does
        //Update unordered maps
        //Update ordered maps using scoring data
        //Update letterMaps
            //Update currentTotalLetterMap
            //Update currentPositionalLetterMapWrapper

        unorderedMapUpdate();
        letterMapUpdate();
        orderedMapUpdate();

    }


    //* Update Helper Methods
    private void orderedMapUpdate(){
        //Orders both valid and invalid word maps
        this.orderedValidWordMap = wordOrdering(this.currentValidWordMap);
        this.orderedInvalidWordMap  = wordOrdering(this.currentInvalidWordMap);
    }

    private boolean wordIsValid(String word){
        if(word.length() > this.invalidCharHashWrapper.size() || word.length() != WORD_LENGTH){
            System.err.println("Incorrect Sizing");
            return false;
        }

        //Todo:
        //Check for invalid characters
        //Check for locked characters
        //Check if all known characters are in word


        for(int i = 0; i < word.length(); i++){
            char currentChar = word.charAt(i);

            //Checks if character is in the invalid character list
            if(inInvalidCharList(i, currentChar)){
                return false;
            }   
            
            //Checks if character is incorrectly in a locked spot
            if(lockedSpotMismatch(i, currentChar)){
                return false;
            }

            //Checks if knownChars[i] is in the current word
            //? If there is an error with this method, this is probably the cause
            if(!containingKnownChar(i, word)){
                return false;
            }

        }    

       return true; 
    }

    private ArrayList<String> wordOrdering(ArrayList<String> currentWordMap) {
        //Todo:
        //Create a new map (this will be the returned map)
        //Set mapPosition = 0
        //Create a new treeMap (automatically sorted just backwards) to store scores and words
        //Loop through every word in the current map 
            //Find the words score
            //Put score and word into treeMap
        //? If anything is wrong with this code its because I don't understand the rest of this 
        //Make a set
        //Make an iterator
        //Loop through treeMap with a for each
            //put <mapPosition, treeMap.getValue()>
            //mapPosition++
        //return main map
        ArrayList<String> correctlyOrderedMap = new ArrayList<>();
        Map<Double,String> scoreMap = new HashMap<>();

        for(int  k = 0; k < currentWordMap.size(); k++){
            String currentString = currentWordMap.get(k);
            Double singleWordScore = wordScore(currentString);

            if(!scoreMap.containsKey(singleWordScore)){
                scoreMap.put(singleWordScore, currentString);
            }
            else{
                while(scoreMap.containsKey(singleWordScore)){
                    singleWordScore -= 0.000001d;
                }

                scoreMap.put(singleWordScore, currentString);
            }
            

            //? For debug purposes only
            // if(k % 10 == 0){
            //     System.out.println("Loop: " + k);
            // }
            
        }
        

        //? If something is wrong definitely look into these lines
        Map<Double, String> sortedScoreMap = new TreeMap<>(scoreMap);
        Set<Map.Entry<Double,String>> set = sortedScoreMap.entrySet();
        Iterator<Map.Entry<Double,String>> i = set.iterator();

        while(i.hasNext()){
            correctlyOrderedMap.add(i.next().getValue());
        }

        return correctlyOrderedMap;
    }

    private void unorderedMapUpdate(){
        ArrayList<String> allData = new ArrayList<>();
        allData.addAll(this.currentValidWordMap);
        allData.addAll(this.currentInvalidWordMap);
        this.currentValidWordMap.clear();
        this.currentInvalidWordMap.clear();

        for (String string : allData) {
            if(wordIsValid(string)){
                this.currentValidWordMap.add(string);
            }
            else{
                this.currentInvalidWordMap.add(string);
            }
        }
    }

    

    private void letterMapUpdate(){
        this.currentTotalLetterMap = letterScoreFinder();
        for(int i = 0; i < WORD_LENGTH; i++){
            this.currentPositionalLetterMapWrapper.put(i, positionedLetterScoreFinder(i));
        }
        
        this.wordsCharIsInMap = updateWordsCharIsInMap();
    }

    

    //* Word Checker Helper Methods
    private boolean inInvalidCharList(int posInWord, char currentChar) {
        ArrayList<Character> invalidCharArray = this.invalidCharHashWrapper.get(posInWord);

        //For every piece of data in the invalidCharArray check if it is equal to the current char
        for(int i = 0; i < invalidCharArray.size(); i++){
            //If it is equal return true
            if(currentChar == invalidCharArray.get(i)){
                return true;
            }
        }

        //If it matches none, return false
        return false;
    }

    private boolean lockedSpotMismatch(int posInWord, char currentChar) {
        if(Character.isLetter(lockedChars[posInWord])){
            return currentChar != lockedChars[posInWord];
        }else{
            return false;
        }

        
    }

    private boolean containingKnownChar(int knownCharPos, String currentString) {
        for(int i = 0; i < currentString.length(); i++){
            //Checks if the character at spot i is equal to the known character that is being checked
            if(currentString.charAt(i) == knownChars[knownCharPos] || !Character.isLetter(knownChars[knownCharPos])){
                return true;
            }
        }

        return false;
    }


    //* Word Ordering Helper Methods
    private Double wordScore(String word) {
        //Todo: 
        //Set score to 0
        //Make a loop for letters in the word
            //Add points based on currentLetterMap
            //? Add quarter points based on original letter map
            //Add points based on positioning maps
            //? Add quarter points based on original positioning maps
            //Add half currentLetterMap points if letter is in known letters
            //Add points for every word in currentValidWordMap that contains character
            //Subtract if character is already invalid for that location (Should only affect invalid words)
            //Subtract currentLetterMap points if letter is duplicate
            //Todo: Subtract points if character is locked 
            //Todo: Subtract half point if character is known

        Double score = 0d;
        ArrayList<Character> pastChars = new ArrayList<>();

        for(int i = 0; i < word.length(); i++){

            //Points based on original maps
            //? Still here in case I can use it later
            // score += this.baseTotalLetterMap.get(word.charAt(i)) ;
            // score += this.basePositionalLetterMapWrapper.get(i).get(word.charAt(i));
            
            //Points based on current maps
            score += this.currentTotalLetterMap.get(word.charAt(i));
            score += this.currentPositionalLetterMapWrapper.get(i).get(word.charAt(i));

            //Points based on known letters
            if(isKnownCharacter(word.charAt(i))){
                score += this.currentTotalLetterMap.get(word.charAt(i)) / 2;
            }

            // New line could be buggy
            if(possibleChars.indexOf(word.charAt(i)) != -1){
                score += this.currentTotalLetterMap.get(word.charAt(i)) / 2;
            }
            
            //TODO: Change this
            score += this.wordsCharIsInMap.get(word.charAt(i));

            

            //Subtraction based on if character is already invalid in position
            if(invalidCharHashWrapper.get(i).contains(word.charAt(i))){
                score = score - (this.currentTotalLetterMap.get(word.charAt(i)) * (this.baseTotalLetterMap.size() / (double)this.currentTotalLetterMap.size()) );
            }

            //Subtraction based on if the character has been seen before
            for(int k = 0; k < pastChars.size(); k++){
                if(word.charAt(i) == pastChars.get(k)){
                    score =  score - (this.currentTotalLetterMap.get(word.charAt(i)) * (this.baseTotalLetterMap.size() / (double)this.currentTotalLetterMap.size()));
                }
            }

            pastChars.add(word.charAt(i));
        }

        return score;
    }

        //* Word Score Helper Methods
        private HashMap<Character, Double> letterScoreFinder(){
            HashMap<Character, Double> numCharHash = charHashCleared();
            String currentString = "";
    
            for(int i = 0; i < this.currentValidWordMap.size(); i++){
                if(this.currentValidWordMap.get(i) != null){
                    currentString = this.currentValidWordMap.get(i);
    
                    for(int k = 0 ; k < currentString.length(); k++){
                        
                        numCharHash.put(currentString.charAt(k), (numCharHash.get(currentString.charAt(k)) + 1));
                    }

                }
                
            }
    
            return numCharHash;
        }
    
        private HashMap<Character, Double> positionedLetterScoreFinder(int position){
            HashMap<Character, Double> numCharHash = charHashCleared();
            String currentString;
    
            for(int i = 0; i < this.currentValidWordMap.size(); i++){
                if(this.currentValidWordMap.get(i) != null){
                    currentString = this.currentValidWordMap.get(i);
    
                    numCharHash.put(currentString.charAt(position), numCharHash.get(currentString.charAt(position)) + 1);
                }
            }

            return numCharHash;
        }
    
        private boolean isKnownCharacter(char currentChar) {
            for(int i = 0; i < knownChars.length; i++){
                if(currentChar == knownChars[i]){
                    return true;
                }
            }
            return false;
        }

    //* Constructor Helper methods
    private ArrayList<String> baseDataRetriever() {
        File originFile = new File(this.originFileName);
        ArrayList<String> dataArray = new ArrayList<>();
        Scanner fScanner = null;

        String currentString = "";

        try {
             fScanner = new Scanner(originFile);

            while(fScanner.hasNext()){
                currentString = fScanner.next();

                if(wordIsValid(currentString)){
                    dataArray.add(currentString);
                }
                
                
            } 
            
            fScanner.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } 
        finally{
            if(fScanner != null){
                fScanner.close();
            }
        }
        
        
        
        return dataArray;
             
    }



    //* Misc. Methods
    private boolean isDuplicate(String word, int position){
        for(int i = 0; i < word.length(); i++){
            if(word.charAt(i) == word.charAt(position) && i != position){
                return true;
            }
        }

        return false;
    }

    private ArrayList<Character> possibleCharFiller(){
        ArrayList<Character> newPossibleChars = new ArrayList<>();

        for(int i = 0; i < 26; i++){
            switch (i) {
                case 0: newPossibleChars.add('A'); break;
                case 1: newPossibleChars.add('B'); break;
                case 2: newPossibleChars.add('C'); break;
                case 3: newPossibleChars.add('D'); break;
                case 4: newPossibleChars.add('E'); break;
                case 5: newPossibleChars.add('F'); break;
                case 6: newPossibleChars.add('G'); break;
                case 7: newPossibleChars.add('H'); break;
                case 8: newPossibleChars.add('I'); break;
                case 9: newPossibleChars.add('J'); break;
                case 10: newPossibleChars.add('K'); break;
                case 11: newPossibleChars.add('L'); break;
                case 12: newPossibleChars.add('M'); break;
                case 13: newPossibleChars.add('N'); break;
                case 14: newPossibleChars.add('O'); break;
                case 15: newPossibleChars.add('P'); break;
                case 16: newPossibleChars.add('Q'); break;
                case 17: newPossibleChars.add('R'); break;
                case 18: newPossibleChars.add('S'); break;
                case 19: newPossibleChars.add('T'); break;
                case 20: newPossibleChars.add('U'); break;
                case 21: newPossibleChars.add('V'); break;
                case 22: newPossibleChars.add('W'); break;
                case 23: newPossibleChars.add('X'); break;
                case 24: newPossibleChars.add('Y'); break;
                case 25: newPossibleChars.add('Z'); break;
                default: newPossibleChars.add('_'); break;
            }
        }

    return newPossibleChars;

    }

    private ArrayList<String> currentWordMapFiller(List<String> inputArray){
        ArrayList<String> currentWordMap = new ArrayList<>();

        for(int i = 0; i < inputArray.size(); i++){
            if(inputArray.get(i) != null){
                currentWordMap.add(inputArray.get(i));
            }
        }

        return currentWordMap;
    }

    private boolean trueBlack(int currentCharPos, String currWord, String currInfo) {
        char currentChar = currWord.charAt(currentCharPos);
        HashMap<Integer, Character> greenCharMap = new HashMap<>(greenCharacterFinder(currWord, currInfo));

        if(!isDuplicate(currWord, currentCharPos)){
            return true;
        }
        
        for(int i = 0; i < currWord.length(); i++){
            //If a future version of this same letter is green
            if((greenCharMap.get(i) != null && greenCharMap.get(i) == currentChar)){
                return false;
            }

            //If the char is already locked
            if(lockedChars[i] == currentChar){
                return false;
            }

            //if the char is already known
            if(knownChars[i] == currentChar){
                return false;
            }
            
        }

        return true;
    }

    private Map<Integer, Character> greenCharacterFinder(String currWord, String currInfo) {
        Map<Integer, Character> greenCharMap= new HashMap<>();

        for(int i = 0; i < currWord.length(); i++){
            if(currInfo.charAt(i) == 'G'){
                greenCharMap.put(i, currWord.charAt(i));
            }
        }

        return greenCharMap;
    }

    private HashMap<Character, Double> charHashCleared() {
        HashMap<Character, Double> numCharHash = new HashMap<>();
        numCharHash.put('A', 0d);
        numCharHash.put('B', 0d);
        numCharHash.put('C', 0d);
        numCharHash.put('D', 0d);
        numCharHash.put('E', 0d);
        numCharHash.put('F', 0d);
        numCharHash.put('G', 0d);
        numCharHash.put('H', 0d);
        numCharHash.put('I', 0d);
        numCharHash.put('J', 0d);
        numCharHash.put('K', 0d);
        numCharHash.put('L', 0d);
        numCharHash.put('M', 0d);
        numCharHash.put('N', 0d);
        numCharHash.put('O', 0d);
        numCharHash.put('P', 0d);
        numCharHash.put('Q', 0d);
        numCharHash.put('R', 0d);
        numCharHash.put('S', 0d);
        numCharHash.put('T', 0d);
        numCharHash.put('U', 0d);
        numCharHash.put('V', 0d);
        numCharHash.put('W', 0d);
        numCharHash.put('X', 0d);
        numCharHash.put('Y', 0d);
        numCharHash.put('Z', 0d);

        return numCharHash;
    }

    private Map<Character, Double> updateWordsCharIsInMap() {
        Map<Character, Double> dataMap = charHashCleared();

        for (String currentString : this.currentValidWordMap) {
            for(int i = 0; i < currentString.length(); i++){
                dataMap.put(currentString.charAt(i), dataMap.get(currentString.charAt(i)) + 1d);
            }
        }

        return dataMap;
    }

    //* Broken or invalid methods 
    //Should be empty :)



}

