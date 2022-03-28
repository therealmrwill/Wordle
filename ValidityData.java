import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ValidityData {
    private boolean locked;
    private HashMap<Integer, WordData> pastData;
    private HashMap<String, Object> storedData;

    //* Only actual data that matters, all other data is based on this
    private HashMap<Character, CharacterData> baseDataMap;
    private HashMap<Integer, Character> lockedChars;

    //* Reminder to add all the constructors, once I have everything else built
    public ValidityData(){
        this.locked = false;
        this.pastData = new HashMap<>();
        this.storedData = new HashMap<>();

        this.baseDataMap = new HashMap<>();
        this.lockedChars = new HashMap<>();

        for (Character curChar : App.CHAR_LIST.toCharArray()) {
            baseDataMap.put(curChar, new CharacterData(curChar));
        }

        this.updateStoredData();
    }
    public ValidityData(HashMap<Integer, WordData> pastData){
        this.locked = false;
        this.pastData = pastData;

        

        ValidityData previousData = new ValidityData();

        this.storedData = previousData.storedData;

        for(Integer wordAdded : pastData.keySet()){
            previousData.addWord(pastData.get(wordAdded));
        }

        this.baseDataMap = new HashMap<>(previousData.baseDataMap);
        this.lockedChars = new HashMap<>(previousData.lockedChars);
        this.updateLockedChars();
    }


    //* Only Data Changing methods
    public void addWord(WordData data){
        if(data.isValid() && this.locked == false){
            this.pastData.put(pastData.size(), data);

            HashMap<Integer, Character> unFoundLockedChars = new HashMap<>(this.lockedChars);

            //* Start with green letters in the word
            HashMap<Character, Set<Integer>> greenData = new HashMap<>(data.getGreenInfo());
            for (Character greenChar : greenData.keySet()) {
                for(Integer position : greenData.get(greenChar)){
                    if(lockedChars.containsKey(position)){
                        if(lockedChars.get(position) != greenChar){
                            System.out.printf("Error in ValidityData.addWord(%s): Position %d has already been found", data.toString(), position);
                        }

                        //* Remove the character from unFoundLockedChars
                        unFoundLockedChars.remove(position);
                    }
                    else{
                        lockedChars.put(position, greenChar);
                        baseDataMap.get(greenChar).addGreen(position);

                        for(Character curChar : baseDataMap.keySet()){
                            if(curChar != greenChar){
                                baseDataMap.get(curChar).anotherGreenFound(position);
                            }
                        }

                    }
                }
            }
            greenData = null;


            //* Now have to figure out how many of each character could be positioned wrongly
            HashMap<Character, Integer> numPosWrong = new HashMap<>();
            for (Character curChar : unFoundLockedChars.values()) {
                if(numPosWrong.containsKey(curChar)){
                    numPosWrong.put(curChar, numPosWrong.get(curChar) + 1);
                }
                else{
                    numPosWrong.put(curChar, 1);
                }
            }
            

            //* Now we are free to check yellows
            //* Knowing that if the yellow is a character positioned wrongly then we add it as a black
            //* We pull in our blackData here too, so that we can da
            HashMap<Character, Set<Integer>> yellowData = new HashMap<>(data.getYellowInfo());
            HashMap<Character, Set<Integer>> blackData = new HashMap<>(data.getBlackInfo());

            

            for(Character yellowChar : yellowData.keySet()){
                int numOfPrevYellows = this.baseDataMap.get(yellowChar).getNumOfYellows();

                for(Integer position : yellowData.get(yellowChar)){
                    if(numPosWrong.containsKey(yellowChar) && numPosWrong.get(yellowChar) > 0){
                        //* Adds the data to the list of black characters 
                        if(blackData.containsKey(yellowChar) == false){
                            blackData.put(yellowChar, new HashSet<>());
                        }

                        blackData.get(yellowChar).add(position);
                        numPosWrong.put(yellowChar, numPosWrong.get(yellowChar) - 1);
                    }else{
                        if(numOfPrevYellows > 0){
                            if(blackData.containsKey(yellowChar) == false){
                                blackData.put(yellowChar, new HashSet<>());
                            }
    
                            blackData.get(yellowChar).add(position);
                            
                            numOfPrevYellows--;
                        }
                        else{
                            baseDataMap.get(yellowChar).addYellow(position);
                        }

                        
                    }

                }
            }

            //* Then we can add all the black characters after
            //* Do not need to do any checking with the black characters
            for(Character blackChar : blackData.keySet()){
                for(Integer position : blackData.get(blackChar)){
                    baseDataMap.get(blackChar).addBlack(position);
                }
            }

        }
        else if(locked == false){
            System.out.printf("Error in ValidityData.addWord(%s): Word is invalid %n", data.toString());
        }
        else{
            System.out.println("Error in ValidityData.addWord(%s): This version of validityData is locked");
        }
        
        this.updateLockedChars();
        this.updateStoredData();
    }
    public void setLocked(){
        this.locked = true;
        this.updateStoredData();
    }

    //* Data Checking Methods
    public boolean isValid(TestWord word){
        if(word.isValid() == false){
            return false;
        }

        HashMap<Character, Integer> numNeededByChar = this.getNumNeededByChar();

        for(Character curChar : word.getDuplicateData().keySet()){
            CharacterData curData = baseDataMap.get(curChar);
            int numberOfChars = curData.getNumOfGreens() + curData.getNumOfYellows();

            if(numNeededByChar.containsKey(curChar)){
                if(numberOfChars >= numNeededByChar.get(curChar)){
                    numNeededByChar.remove(curChar);
                }
            }


            for(Integer position : word.getDuplicateData().get(curChar)){
                if(lockedChars.containsKey(position)){
                    if(lockedChars.get(position) != curChar){
                        return false;
                    }
                }
                
                if(curData.getPosValidity().get(position) == false){
                    word.setIsValid(false);
                    return false;
                }
                else{
                    numberOfChars--;
                }
            }

            if(numberOfChars > 0){
                word.setIsValid(false);
                return false;
            }
        }

        if(numNeededByChar.size() > 0){
            return false;
        }


        return true;
    }

    //* Data Reading Methods
    public HashMap<Integer, HashMap<Character, Boolean>> getValidityMap(){
        HashMap<Integer, HashMap<Character, Boolean>> dataOut = new HashMap<>();

        for(Character curChar : baseDataMap.keySet()){

            for(Integer position : baseDataMap.get(curChar).getPosValidity().keySet()){
                if(dataOut.containsKey(position) == false){
                    dataOut.put(position, new HashMap<>());
                }

                dataOut.get(position).put(curChar, baseDataMap.get(curChar).getPosValidity().get(position));
            }
        }


        return dataOut;
    }
    public HashMap<Integer, Set<Character>> getValidByPos(){
        HashMap<Integer, HashMap<Character, Boolean>> validityMap = this.getValidityMap();
        HashMap<Integer, Set<Character>> dataOut = new HashMap<>();

        for(Integer position : validityMap.keySet()){
            dataOut.put(position, new HashSet<>());
            for(Character curChar : validityMap.get(position).keySet()){
                if(validityMap.get(position).get(curChar) == true){
                    dataOut.get(position).add(curChar);
                }
            }


        }


        return dataOut;
    }
    public HashMap<Integer, Integer> getNumValidByPos(){
        HashMap<Integer, Set<Character>> validByPos = this.getValidByPos();
        HashMap<Integer, Integer> dataOut = new HashMap<>();
        
        for(Integer position : validByPos.keySet()){
            dataOut.put(position, validByPos.get(position).size());
        }
        
        return dataOut;
    }

    public HashMap<Character, CharacterData> getBaseDataMap() {
        return baseDataMap;
    }
    public HashMap<Character, Integer> getNumValidByChar(){
        HashMap<Character, Integer> dataOut = new HashMap<>();
        
        for(Character curChar : this.baseDataMap.keySet()){
            int validPositions = 0;

            for (Integer position : this.baseDataMap.get(curChar).getPosValidity().keySet()) {
                if(this.baseDataMap.get(curChar).getPosValidity().get(position) == true){
                    validPositions++;
                }
            }

            dataOut.put(curChar, validPositions);
        }


        return dataOut;
    }
    public HashMap<Character, Integer> getNumTestableByChar(){
        HashMap<Character, Integer> dataOut = new HashMap<>();
        
        for(Character curChar : this.baseDataMap.keySet()){
            dataOut.put(curChar, this.baseDataMap.get(curChar).getTestablePositions().size());
        }
        
        return dataOut;
    }
    public HashMap<Character, Integer> getNumNeededByChar(){
        HashMap<Character, Integer> dataOut = new HashMap<>();

        for(Character curChar : this.baseDataMap.keySet()){
            int numberNeeded = this.baseDataMap.get(curChar).getNumOfGreens() + this.baseDataMap.get(curChar).getNumOfYellows();

            if(numberNeeded > 0){
                dataOut.put(curChar, numberNeeded);
            }
        }


        return dataOut;
    }



    public HashMap<Integer, Character> getLockedChars() {
        return lockedChars;
    }
    public Integer getNumLockedChars(){
        return this.lockedChars.size();
    }

    public HashMap<Integer, WordData> getPastData() {
        return pastData;
    }

    public void updateStoredData(){
        this.storedData = null;
        this.storedData = new HashMap<>();
        
        this.storedData.put("Validity Map", this.getValidityMap());
        this.storedData.put("Valid By Position", this.getValidByPos());
        this.storedData.put("# Valid By Position", this.getNumValidByPos());

        this.storedData.put("Base Data Map", this.getBaseDataMap());
        this.storedData.put("# Valid by Char", this.getNumValidByChar());
        this.storedData.put("# Testable by Char", this.getNumTestableByChar());

        this.storedData.put("Locked Chars", this.getLockedChars());
        this.storedData.put("# of Locked Chars", this.getNumLockedChars());
        this.storedData.put("Past Data", this.getPastData());
    }

    public String getColoredValidityMap(){
        HashMap<Integer, HashMap<Character, Boolean>> validityMap = this.getValidityMap();
        String dataOut = "";

        for(Integer position : validityMap.keySet()){
            dataOut += "\n" + position + ": ";
            for(Character curChar : validityMap.get(position).keySet()){
                if(validityMap.get(position).get(curChar) == true){
                    dataOut += ColorData.green + curChar + ColorData.reset;
                }
                else{
                    dataOut += ColorData.red + curChar + ColorData.reset;
                }
            }
        }

        return dataOut;
    }



    //* This method updates the locked chars variable using the baseDataMap
    public void updateLockedChars(){
        for(Character curChar : baseDataMap.keySet()){
            for(Integer position : baseDataMap.get(curChar).getSetPositions()){
                if(lockedChars.containsKey(position)){
                    if(lockedChars.get(position) != curChar){
                        System.out.printf("Error: Character(%c) assumed greens that were not accurate %n", curChar);
                    }
                }else{
                    lockedChars.put(position, curChar);
                    
                }
            }
        }
    }
}
