import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class ValidityData {
    private boolean locked;
    private HashMap<Integer, WordData> pastData;

    //* Only actual data that matters, all other data is based on this
    private HashMap<Character, CharacterData> baseDataMap;
    private HashMap<Integer, Character> lockedChars;

    //* Reminder to add all the constructors, once I have everything else built
    public ValidityData(){
        this.locked = false;
        this.pastData = new HashMap<>();

        this.baseDataMap = new HashMap<>();
        this.lockedChars = new HashMap<>();

        for (Character curChar : App.CHAR_LIST.toCharArray()) {
            baseDataMap.put(curChar, new CharacterData(curChar));
        }
    }
    public ValidityData(HashMap<Integer, WordData> pastData){
        this.locked = false;
        this.pastData = pastData;

        ValidityData previousData = new ValidityData();
        for(Integer wordAdded : pastData.keySet()){
            previousData.addWord(pastData.get(wordAdded));
        }

        this.baseDataMap = new HashMap<>(previousData.baseDataMap);
        this.lockedChars = new HashMap<>(previousData.lockedChars);

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
                for(Integer position : yellowData.get(yellowChar)){
                    if(numPosWrong.containsKey(yellowChar) && numPosWrong.get(yellowChar) > 0){
                        //* Adds the data to the list of black characters 
                        if(blackData.containsKey(yellowChar) == false){
                            blackData.put(yellowChar, new HashSet<>());
                        }

                        blackData.get(yellowChar).add(position);
                        numPosWrong.put(yellowChar, numPosWrong.get(yellowChar) - 1);
                    }else{
                        baseDataMap.get(yellowChar).addYellow(position);
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
    }
    public void setLocked(){
        this.locked = true;
    }

    //* Data Checking Methods
    public boolean isValid(TestWord word){
        if(word.isValid() == false){
            return false;
        }

        for(Character curChar : word.getDuplicateData().keySet()){
            CharacterData curData = baseDataMap.get(curChar);
            int numberOfChars = curData.getNumOfGreens() + curData.getNumOfYellows();

            for(Integer position : word.getDuplicateData().get(curChar)){
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


        return true;
    }

    //* Data Reading Methods
    



    //Todo: Need to have an update Locked chars method because we might find one inside the baseDataMap
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
