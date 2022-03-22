import java.util.ArrayList;

public class ValidityData {
    

    public static String getColoredValidity(String word, String solution){
        String dataOut = "";

        String currentSolution = solution;

        String validityData = getValidityData(word, currentSolution);

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


   public static String getValidityData(String word, String solution){
    if(word.length() != solution.length()){
        System.out.println("Error in Validity.getValidityData(" + word + ", " + solution + "): Word sizes do not match");
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
        else if(solution.contains(charString) ){
            dataOut += "Y";
            previousCharacters.add(currChar);
        }
        else{
            dataOut += "B";
        }

        
    }


    return dataOut;
}
}
