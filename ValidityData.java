import java.util.ArrayList;
import java.util.HashMap;

public class ValidityData {
    //* Data protection parameters
    private boolean finalData;

    //* Needed Parameters
    private char[] lockedChars;
    private HashMap<Integer, HashMap<Character, Boolean>> posCharData;
    private ArrayList<Character> knownChars;

    //* Validity already creates a new non-linkable object every time, so there is never a reason to copy it
    //*  - Other than for data storage purposes 
    //*     - Which is covered in this class 
    public ValidityData(){
        this.finalData = false;
        this.lockedChars = new char[5];
        this.knownChars = new ArrayList<>();
        this.posCharData = new HashMap<>();

        for(int position = 0; position < App.WORD_LENGTH; position++){
            this.lockedChars[position] = '_';
            this.posCharData.put(position, charDataFiller(App.alphabet));
        }
    }


    //* Constructor that is only used for making copies of previous validityData
    //* Checked - no linkage issues found
    public ValidityData(char[] lockedChars, HashMap<Integer, HashMap<Character, Boolean>> posCharData, ArrayList<Character> knownChars){
        this.finalData = true;
        this.lockedChars = new char[5];

        for(int position = 0; position < lockedChars.length; position++){
            this.lockedChars[position] = lockedChars[position];
        }

        this.posCharData = new HashMap<>(posCharData);
        this.knownChars = new ArrayList<>(knownChars);
    }

    //* Method used to create a new (and not linked) ValidityData to store the values at that moment
    public ValidityData finalCopy(){
        return new ValidityData(this.lockedChars, this.posCharData, this.knownChars);
    }

    //* Creates a string of data to store for later usage 
    //* Super helpful for debugging when we have bigger classes using this
    //* Cases for quick reference: Locked Only, Invalid Only, Valid Only, Valid and Invalid, All, All Colored
    public String saveInfo(String dataType){
        String dataOut = "";

        if(!dataType.equals("toString")){
            dataOut += "\nValidityData: ";
        }
        

        switch(dataType){
            case "toString":
                for(Character curChar : lockedChars){
                    dataOut += curChar;
                }; 
                break; 
            case "Locked Only":
                for(Character curChar : lockedChars){
                    dataOut += curChar;
                }; 
                break;
            case "Invalid Only":
                dataOut += "Invalid: ";
                for(Integer position : posCharData.keySet()){
                    dataOut += "\n(Pos: " + position + ":";
                    for(Character curChar : posCharData.get(position).keySet()){
                        if(posCharData.get(position).get(curChar).booleanValue() == false){
                            dataOut += " " + curChar;
                        }
                    }
                    dataOut += ")";
                } 
                break;
            case "Valid Only":
                dataOut += "Valid ";
                    for(Integer position : posCharData.keySet()){
                        dataOut += "\n(Pos: " + position + ":";
                        for(Character curChar : posCharData.get(position).keySet()){
                            if(posCharData.get(position).get(curChar).booleanValue() == true){
                                dataOut += " " + curChar;
                            }
                        }
                        dataOut += ")";
                    } 
                    break;
            case "Valid and Invalid":
                dataOut += this.saveInfo("Valid Only") + "\n";
                dataOut += this.saveInfo("Invalid Only") + "\n";
                break;
            case "All":
                dataOut += "All Data ";
                for(Integer position : posCharData.keySet()){
                    dataOut += "\n(Pos: " + position + ":";
                    for(Character curChar : posCharData.get(position).keySet()){
                        dataOut += " " + curChar + ":" + posCharData.get(position).get(curChar).booleanValue();
                    }
                    dataOut += ")";
                } 
                break; 
            case "All Colored":
                dataOut += "Colored Data ";
                for(Integer position : posCharData.keySet()){
                    dataOut += "\n(Pos: " + position + ":";
                    for(Character curChar : posCharData.get(position).keySet()){
                        if(posCharData.get(position).get(curChar).booleanValue() == true){
                            dataOut += " " + App.ANSI_GREEN + curChar + App.ANSI_RESET;
                        }else{
                            dataOut += " " + App.ANSI_RED + curChar + App.ANSI_RESET;
                        }
                        
                    }
                    dataOut += ")";
                }
                break;
            default:
                System.out.println("Error in ValidityData.saveInfo(): Invalid data type (" + dataType + ")");
                dataOut += this.saveInfo("All");
        }

        return dataOut;
    }

    //* Prints out the saveInfo of the current data
    @Override
    public String toString(){
        return saveInfo("toString");
    }

    //* Checks if word is valid given current ValidityData
    //* Needs to be clean and precise - is run a lot of times by higher level classes
    public boolean isValid(Word word){
        if(word.isValid() == false)
            return false;
        
        String wordString = word.getName();
        ArrayList<Character> neededChars = new ArrayList<>(knownChars);

        for(int position = 0; position < wordString.length(); position++){

            char currentChar = wordString.charAt(position);

            //* This method really isn't needed, all information in lockedChars is stored in posCharData anyways
            //* I'm just leaving it here so that if there are issues you can try turning it back on
            // if(Character.isLetter(lockedChars[position]) && lockedChars[position] != currentChar){
            //     return false;
            // }
            
            if(posCharData.get(position).containsKey(currentChar)){
                if(posCharData.get(position).get(currentChar).booleanValue() == false){
                    return false;
                }
            }
            else{
                System.out.println("Error in ValidityData.isValid() with word " + word + ": contains unknown character: " + currentChar);
            }

            if(neededChars.contains(currentChar)){
                neededChars.remove(neededChars.lastIndexOf(currentChar));
            }
        }

        if(neededChars.size() > 0){
            return false;
        }

        return true;
    }

    //* Protects Object from any future changes
    //* Allows us to check if the object we are trying to change is final or not
    public void makeFinal() {
        this.finalData = true;
    }
    public boolean isFinalData() {
        return finalData;
    }

    //* Adds new data to the validity parameters
    //* Allows for more removal of words in the future
    public void addTestedWord(String word, String wordValidity){
        if(finalData){
            System.out.println("Error in ValidityData.addTestedWord(" + word + "): Data is marked as final and cannot be changed");
            return;
        }

        if(word.length() != wordValidity.length()){
            System.out.println("Error in ValidityData.addTestedWord( " + word + ", " + wordValidity + "): Word sizes do not match");
            return;
        }
        
        
        for(int position = 0; position < wordValidity.length(); position++){
            switch(wordValidity.charAt(position)){
                case 'G': addGreenChar(word.charAt(position), position); break;
                case 'Y': addYellowChar(word.charAt(position), position); break;
                case 'B': addBlackChar(word.charAt(position), position); break;
                case 'R': ; break;
                default: System.out.println("Error in ValidityData.addTestedWord: " + wordValidity.charAt(position) + " is not a valid response");
            }
            }
        
    }

    //* Adds a 'Green' Character to data
    private void addGreenChar(Character letter, int position){
        //* Checking first if we are attempting to add invalid data
        //* Then checking if we already know the data
        //* If neither of those are true, then we add the data
        if(Character.isLetter(lockedChars[position])){
            if(lockedChars[position] != letter){
                System.out.println("Error in ValidityData.addTestedWord.addGreenChar(" + letter + "): Attempting to change a value that has already been found");
                return;
            }
            else{
                return;
            }
        }
        
        //* Adds data to lockedChars[]    
        lockedChars[position] = letter;
        
        //* Checks if we located a previously Yellow Character
        //* If we did remove it from the yellow character list
        if(knownChars.contains(letter)){
            knownChars.remove(knownChars.lastIndexOf(letter));
        }

        //* Set every value other than letter to false at this position
        for(Character curChar : posCharData.get(position).keySet()){
            if(curChar != letter){
                addInvalidChar(curChar, position);
            }
        }
        

    }

    //* Adds a 'Yellow' Character to data
    private void addYellowChar(Character letter, int position){
        //* Only adds a known character if it isn't already in the list
        //* Means that words with multiple of the same letter being yellow will be solved slower
        //*  - I don't think this will cause issues, but try to actually write tests to see if it does
        if(!knownChars.contains(letter)){
            knownChars.add(letter);
        }
        
        //* Should be fine to add it regardless - it will check if letter is already not allowed
        addInvalidChar(letter, position);
    }

    //* Adds a 'Black' Character to data
    private void addBlackChar(Character letter, int position){
        addInvalidChar(letter, position);

        for(int testPos = 0; testPos < App.WORD_LENGTH; testPos++){
            //* Adding rules to help with 'Fake blacks' - or blacks or a letter that is already known
            //* If position in lockedChars doesn't equal our value
            //* And knownChars doesn't contain our letter
            if(lockedChars[testPos] != letter && !knownChars.contains(letter)){
                addInvalidChar(letter, testPos);
            }
        } 
    }

    //* Adds an Invalid Character to posInvalidChars
    private void addInvalidChar(Character letter, int position){
        if(posCharData.get(position).containsKey(letter)){
            if(posCharData.get(position).get(letter).booleanValue()){
                posCharData.get(position).put(letter, false);
            }
        }
        else{
            System.out.println("Error in ValidityData.addInvalidChar(): " + letter + " not a known character");
        }


        
        

    }

    private HashMap<Character, Boolean> charDataFiller(String characters){
        HashMap<Character, Boolean> dataOut = new HashMap<>();
        for (Character character : characters.toCharArray()) {
            dataOut.put(character, true);
        }
        return dataOut;
    }
    
  
}
