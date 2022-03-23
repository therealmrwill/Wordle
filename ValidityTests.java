import java.util.ArrayList;
import java.util.HashMap;

public class ValidityTests {
    //* A visual helper method to see data in a more logical way
    //* Outputs color values of a word instead of BGY strings
    public static String getColoredValidity(String word, String solution){
        String dataOut = "";

        String validityData = getFinalValidity(word, solution);

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

    //* Method created to find validity according to Wordle
    //* Will also be what I use for my code, because it should work correctly
    public static String getFinalValidity(String word, String solution){
        String dataOut = getBaseValidity(word, solution);

        HashMap<Character, HashMap<Integer, Character>> wordDuplicates = new HashMap<>();
        HashMap<Character, HashMap<Integer, Character>> solutionDuplicates = new HashMap<>(); 

        for(int position = 0; position < word.length(); position++){
            Character wordChar = word.charAt(position);
            Character solutionChar = solution.charAt(position);
            Character validityData = dataOut.charAt(position);
            

            if(validityData != 'B'){
                if(!wordDuplicates.containsKey(wordChar)){
                    wordDuplicates.put(wordChar, new HashMap<>());
                }
    
                wordDuplicates.get(wordChar).put(position, validityData);
            }

            if(!solutionDuplicates.containsKey(solutionChar)){
                solutionDuplicates.put(solutionChar, new HashMap<>());
            }

            Character gChar = 'G';
            solutionDuplicates.get(solutionChar).put(position, gChar);
        }
        
        //* Will only run on duplicates that are Y or G
        //* Meaning solutionDuplicates will have to have the letter somewhere in it
        for(Character charToFix : wordDuplicates.keySet()){
            if(wordDuplicates.get(charToFix).size() > 1){
                ArrayList<Integer> yellowPositions = new ArrayList<>();
                int greensLeft = solutionDuplicates.get(charToFix).size();

                for(Integer position : wordDuplicates.get(charToFix).keySet()){
                    if(wordDuplicates.get(charToFix).get(position) == 'G'){
                        greensLeft--;
                    }else{
                        yellowPositions.add(position);
                    }
                }

                for(Integer position : yellowPositions){
                    if(greensLeft == 0){
                        dataOut = dataOut.substring(0, position) + "B" + dataOut.substring(position + 1);
                    }else{
                        greensLeft--;
                    }
                }
            }
        }


       return dataOut;
    }

    //* Used to find the base of the validity data
    //* Used to support finalValidity
    public static String getBaseValidity(String word, String solution){
        if(word.length() != solution.length()){
            System.out.println("Error in Validity.getValidityData(" + word + ", " + solution + "): Word sizes do not match");
            return "RRRRR";
        }

        String dataOut = "";

        for(int position = 0; position < word.length(); position++){
            Character testChar = word.charAt(position);
            Character solutionChar = solution.charAt(position);

            if(testChar == solutionChar){
                dataOut += "G";
            }
            else if(solution.contains(testChar.toString())){
                dataOut += "Y";
            }
            else{
                dataOut += "B";
            }
        
        }

        return dataOut;
    }
}
