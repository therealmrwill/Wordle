import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CharacterData {
    private boolean locked;
    private Character character;
    private boolean valid;
    private int numOfYellows;
    private int numOfGreens;
    
    private Set<Integer> testablePositions;
    private Set<Integer> setPositions;

    private HashMap<Integer, Boolean> posValidity;

    public CharacterData(Character curChar){
        this.locked = false;
        this.character = curChar;
        this.valid = true;
        this.numOfYellows = 0;
        this.numOfGreens = 0;

        this.testablePositions = new HashSet<>();
        this.setPositions = new HashSet<>();
        this.posValidity = new HashMap<>();

        for(int position = 0; position < App.WORD_LENGTH; position++){
            this.posValidity.put(position, true);
            this.testablePositions.add(position);
        }
    }

    public void addBlack(int position){
        //* Needs to keep previous data safe
        //* Sources plugging in to this should also keep data safe
        if(setPositions.contains(position)){
            System.out.printf("Error in CharacterData(%c).addBlack(%d): Entry Contradicts Previous Data %n", this.character, position);
            return;
        }


        //* If letter has never before been proven green or yellow
        //* Relies on the fact that yellows and greens will be added first
        if(numOfGreens == 0 && numOfYellows == 0){
            this.locked = true;
            this.valid = false;
            this.testablePositions.clear();

            for(Integer posToRemove : posValidity.keySet()){
                posValidity.put(posToRemove, false);
            }
        }
        //* This means that there has already been a yellow or green located, meaning the letter is valid
        //* We remove this letter from the list of testable positions
        //* And set positional validity to false
        else{
            if(this.testablePositions.contains(position)){
                this.testablePositions.remove(position);
            }

            this.posValidity.put(position, false);

            this.checkYellows();
        }
    }

    public void addYellow(int position){
        //* A Yellow is for all intents and purposes a black
        //* But it adds 1 to the number of yellows
        //* Need to make sure that it is not a yellow just because we don't have a green in it's spot
        //*  - This is checked by current ValidityData function
        if(testablePositions.size() < 1){
            System.out.printf("Error in CharacterData(%c).addYellow(%d): Entry Contradicts Previous Data %n", this.character, position);
        }

        numOfYellows += 1;
        addBlack(position);
    }

    public void addGreen(int position){
        if(posValidity.get(position) == false){
            System.out.printf("Error in CharacterData(%c).addGreen(%d): Entry Contradicts Previous data %n", this.character, position);
            return;
        }


        //* Needs to check and make sure this isn't a green we already know about
        if(setPositions.contains(position) == false){
            //* Adds one to the number of greens
            numOfGreens += 1;

            //* Subtracts one from the number of yellows
            if(numOfYellows > 0){
                numOfYellows -= 1;
            }
            

            //* Adds green to setPositions
            setPositions.add(position);

            //* Removes letter from testable positions
            if(testablePositions.contains(position)){
                testablePositions.remove(position);
            }

            //* Removes the character from validity data if it is no longer testable
            for (Integer posToRemove : posValidity.keySet()) {
                if(testablePositions.contains(posToRemove) == false && setPositions.contains(posToRemove) == false){
                    posValidity.put(posToRemove, false);
                }
            }

            //* Checks Yellows to see if they can be solved
            this.checkYellows();
        }
    }

    public void checkYellows(){
        if(numOfYellows != 0 && numOfYellows == testablePositions.size()){
            Set<Integer> positionToFlip = new HashSet<>(testablePositions);

            for(Integer position : positionToFlip){
                addGreen(position);
            }
            
        }
    }

    public void anotherGreenFound(int position){
        if(testablePositions.contains(position)){
            testablePositions.remove(position);
        }

        if(posValidity.get(position) == true){
            posValidity.put(position, false);
        }
    }

    //* Override Methods - To String is mine, all others are computer generated
    @Override
    public String toString(){
        return this.getData("toString");
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((character == null) ? 0 : character.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CharacterData other = (CharacterData) obj;
        if (character == null) {
            if (other.character != null)
                return false;
        } else if (!character.equals(other.character))
            return false;
        return true;
    }

    //* All Getters for this class
    public boolean isLocked() {
        return locked;
    }
    public Character getCharacter() {
        return character;
    }
    public boolean isValid() {
        return valid;
    }
    public int getNumOfYellows() {
        return numOfYellows;
    }
    public int getNumOfGreens() {
        return numOfGreens;
    }
    public Set<Integer> getTestablePositions() {
        return testablePositions;
    }
    public Set<Integer> getSetPositions() {
        return setPositions;
    }
    public HashMap<Integer, Boolean> getPosValidity() {
        return posValidity;
    }
    public Integer getNumPositionsStillValid(){
        Integer dataOut = 0;

        for(Integer position : this.posValidity.keySet()){
            if(this.posValidity.get(position) == true){
                dataOut += 1;
            }
        }

        return dataOut;
    }


    public String getData(String type){
        String dataOut = character + ": ";

        switch(type){
            case "toString": 
                for(int position = 0; position < App.WORD_LENGTH; position++){
                    if(setPositions.contains(position)){
                        dataOut += 'S';
                    }
                    else if(testablePositions.contains(position)){
                        dataOut += 'T';
                    }
                    else{
                        dataOut += 'U';
                    }
                }
                break;
            case "Colored":
                String data = this.getData("toString");
                for(Character curChar : data.toCharArray()){
                    switch(curChar){
                        case 'S': dataOut += ColorData.green + curChar + ColorData.reset; break;
                        case 'T': dataOut += ColorData.blue + curChar + ColorData.reset; break;
                        case 'U': dataOut += ColorData.silver + curChar + ColorData.reset; break;
                    }
                }
                break;
            case "Validity":
                for (Integer position : this.posValidity.keySet()) {
                    if(this.posValidity.get(position) == true){
                        dataOut += 'T';
                    }else{
                        dataOut += 'F';
                    }
                }
                break;
            case "Colored Validity":
                String validity = this.getData("Validity");
                for (Character curChar : validity.toCharArray()) {
                    if(curChar == 'T'){
                        dataOut += ColorData.green + curChar + ColorData.reset;
                    }else if (curChar == 'F'){ 
                        dataOut += ColorData.red + curChar + ColorData.reset;
                    }
                }

            default: this.getData("toString");
            
        }

        return dataOut;
    }

    

    
    

}
