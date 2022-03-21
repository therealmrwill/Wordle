import java.util.ArrayList;

public class Validity {
   public char[] lockedChars;
   public ArrayList<Character> knownChars;
   public static ArrayList<ArrayList<Character>> positionalInvalidChars;

   public Validity(){
        lockedChars = new char[5];
        knownChars = new ArrayList<>();
        positionalInvalidChars = new ArrayList<>();

        for(int position = 0; position < App.WORD_LENGTH; position++){
            lockedChars[position] = '$';
            positionalInvalidChars.add(new ArrayList<>());
        }
   }

   public boolean isValid(String word){
        for(int position = 0; position < App.WORD_LENGTH; position++){

            char currentChar = word.charAt(position);

            if(Character.isLetter(lockedChars[position]) && lockedChars[position] != currentChar){
                return false;
            }
            else if(positionalInvalidChars.get(position).contains(currentChar)){
                return false;
            }
        }

        return true;
   }

   public void addTestedWord(String word, String validityData){
        if(word.length() != validityData.length()){
            System.out.println("Error in Validity.addTestedWord( " + word + ", " + validityData + "): Word sizes do not match");
        }else{
            for(int position = 0; position < validityData.length(); position++){
                switch(validityData.charAt(position)){
                    case 'G': addGreenChar(word.charAt(position), position); break;
                    case 'Y': addYellowChar(word.charAt(position), position); break;
                    case 'B': addBlackChar(word.charAt(position), position); break;
                    case 'R':  ; break;
                    default: System.out.println("Error in Validity.addTestedWord: " + validityData.charAt(position) + " is not a valid response");
                }
            }
        }
   }

   private void addGreenChar(Character letter, int position){
        if(knownChars.contains(letter)){
            knownChars.remove(letter);
        }

        if(lockedChars[position] != letter){
            lockedChars[position] = letter;
        }

        for (char character : App.alphabet.toCharArray()) {
            if(character != letter){
                addBlackChar(character, position);
            }
        }

   }

   private void addYellowChar(Character letter, int position){
        if(!knownChars.contains(letter)){
            knownChars.add(letter);
        }

        addBlackChar(letter, position);
   }

   private void addBlackChar(Character letter, int position){
        if(!positionalInvalidChars.get(position).contains(letter)){
            positionalInvalidChars.get(position).add(letter);
        }
   }


   public String getValidityData(String word, String solution){
        if(word.length() != solution.length()){
            System.out.println("Error in Validity.getValiidityData(" + word + ", " + solution + "): Word sizes do not match");
            return "RRRRR";
        }

        String dataOut = "";
        ArrayList<Character> previousCharacters = new ArrayList<>();

        for(int position = 0; position < word.length(); position++){

            String charString = word.substring(position, position + 1);
            char currChar = word.charAt(position);

            if(currChar == solution.charAt(position)){
                dataOut += "G";
            }
            else if(solution.contains(charString) && !previousCharacters.contains(currChar) && solution.indexOf(charString) < position){
                dataOut += "Y";
            }
            else{
                dataOut += "B";
            }

            previousCharacters.add(currChar);
        }


        return dataOut;
   }


   public String getColoredValidity(String word){
        String dataOut = "";
        String validityData = getValidityData(word, lockedChars.toString());

        for(int position = 0; position < validityData.length(); position++){
            switch(validityData.charAt(position)){
                case 'G': dataOut += App.ANSI_GREEN + word.charAt(position) + App.ANSI_RESET; break;
                case 'Y': dataOut += App.ANSI_YELLOW + word.charAt(position) + App.ANSI_RESET; break;
                case 'R': dataOut += App.ANSI_RED + word.charAt(position) + App.ANSI_RESET; break; 
                case 'B': dataOut += word.charAt(position); break;
                default: dataOut += App.ANSI_RED + word.charAt(position) + App.ANSI_RESET; break;  
            }
        }


       return dataOut;
   }


   @Override
   public String toString(){
        String dataOut = "";

        dataOut += "\nCurrent Validity Data: ";

        dataOut += "\nLocked Characters: ";
        for (Character character : lockedChars) {
            if(Character.isLetter(character)){
                dataOut += character;
            }
            else{
                dataOut += "_";
            }
        }

        dataOut += "\nKnown Characters: ";
        if(knownChars.size() < 1){
            dataOut += "None";
        }

        for (Character letter : knownChars) {
            dataOut += letter + " ";
        }

        dataOut += "\nInvalid Characters: ";
        for (ArrayList<Character> charList : positionalInvalidChars) {
            dataOut += "\nPosition " + positionalInvalidChars.indexOf(charList) + ": ";
            for(Character letter : charList){
                dataOut += letter + " ";
            }
        }


        return dataOut;
   }
}
