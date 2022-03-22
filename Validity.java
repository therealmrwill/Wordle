import java.util.ArrayList;

public class Validity {
   private int uniqueId;
   private char[] lockedChars;
   private ArrayList<Character> knownChars;
   private ArrayList<ArrayList<Character>> posInvalidChars;

   public Validity(){
        this.uniqueId = 0;
        this.lockedChars = new char[5];
        this.knownChars = new ArrayList<>();
        this.posInvalidChars = new ArrayList<>();

        for(int position = 0; position < App.WORD_LENGTH; position++){
            this.lockedChars[position] = '$';
            this.posInvalidChars.add(new ArrayList<>());
        }
   }

   public Validity(Validity validityData){
       this.uniqueId = validityData.uniqueId + 1;
       this.lockedChars = validityData.lockedChars;
       this.knownChars = validityData.knownChars;
       this.posInvalidChars = validityData.posInvalidChars;
   }

   public boolean isValid(String word){
        for(int position = 0; position < App.WORD_LENGTH; position++){

            char currentChar = word.charAt(position);

            if(Character.isLetter(lockedChars[position]) && lockedChars[position] != currentChar){
                return false;
            }
            else if(posInvalidChars.get(position).contains(currentChar)){
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
                    case 'B': addBlackChar(word.charAt(position)); break;
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
                addInvalidChar(character, position);
            }
        }

   }

   private void addYellowChar(Character letter, int position){
        if(!knownChars.contains(letter)){
            knownChars.add(letter);
        }

        addInvalidChar(letter, position);
   }

   private void addBlackChar(Character letter){
        for(int position = 0; position < App.WORD_LENGTH; position++){
            addInvalidChar(letter, position);
        }
   }


   private void addInvalidChar(Character letter, int position){
        if(!posInvalidChars.get(position).contains(letter)){
            posInvalidChars.get(position).add(letter);
        }

   }


   public char[] getLockedChars() {
       return lockedChars;
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
        for (ArrayList<Character> charList : posInvalidChars) {
            dataOut += "\nPosition " + posInvalidChars.indexOf(charList) + ": ";
            for(Character letter : charList){
                dataOut += letter + " ";
            }
        }


        return dataOut;
   }
}
