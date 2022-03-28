import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WordData {
    private String word;
    private boolean valid;
    private HashMap<Character, Set<Integer>> greenInfo;
    private HashMap<Character, Set<Integer>> yellowInfo;
    private HashMap<Character, Set<Integer>> blackInfo;


    //* Rules for using this method
    //* 1. Words must be the same size
    //* 2. Data must be in the form GYB
    public WordData(String word, char[] data){
        this.word = word;
        this.valid = true;
        this.greenInfo = new HashMap<>();
        this.yellowInfo = new HashMap<>();
        this.blackInfo = new HashMap<>();


        if(word.length() != data.length){
            System.out.printf("Error in WordData(%s , %s): Word Size (%d) doesn't match data size (%d)", word, data.toString(), word.length(), data.length);
            valid = false;
        }

        for(int position = 0; position < word.length(); position++){
            Character currentChar = word.charAt(position);
            Character currentData = data[position];

            switch(currentData){
                case 'G': 
                    if(greenInfo.containsKey(currentChar) == false)
                        greenInfo.put(currentChar, new HashSet<>());
                        
                    greenInfo.get(currentChar).add(position); break;
                case 'Y': 
                    if(yellowInfo.containsKey(currentChar) == false)
                        yellowInfo.put(currentChar, new HashSet<>());
                
                    yellowInfo.get(currentChar).add(position); break;
                case 'B': 
                    if(blackInfo.containsKey(currentChar) == false)
                        blackInfo.put(currentChar, new HashSet<>());
                
                    blackInfo.get(currentChar).add(position); break;
                case 'R': ; break;
                default: 
                System.out.printf("Error in WordData(%s , %s): %c not valid data Type (G,Y,B) %n", word, data, currentData);
                this.valid = false; break;
            }

        }


    }

    public WordData(String word, String solution){
        WordData realData = new WordData(word, getSolution(word, solution));
        this.word = word;
        this.valid = realData.valid;
        this.greenInfo = new HashMap<>(realData.greenInfo);
        this.yellowInfo = new HashMap<>(realData.yellowInfo);
        this.blackInfo = new HashMap<>(realData.blackInfo);
    }

    private char[] getSolution(String testWord, String solution) {
        char[] dataOut = new char[testWord.length()];

        if(testWord.length() != solution.length()){
            System.out.printf("Error in WordData.getSolution(%s, %s): TestWord Size (%d) and solution size (%d) do not match");
            for(int i = 0; i < testWord.length(); i++){
                dataOut[i] = 'E';
            }
            return dataOut;
        }

        //* However many times a letter shows up in the solution is the amount of times it can show in data
        //* If a position is green it gets priority on taking from that list
        //* Then the first yellows in the word get priority 

        //* This is storing how many times each word shows up in the solution
        HashMap<Character, Integer> solutionData = new HashMap<>();
             
        //* 3 HashMaps one for each type of Data (G, Y, B)
        HashMap<Character, Set<Integer>> greenData = new HashMap<>();
        HashMap<Character, Set<Integer>> yellowData = new HashMap<>(); 
        HashMap<Character, Set<Integer>> blackData = new HashMap<>();

        for(int position = 0; position < testWord.length(); position++){
            Character curChar = testWord.charAt(position);
            Character solutionChar = solution.charAt(position);

            if(curChar == solutionChar){
                if(greenData.containsKey(curChar) == false)
                    greenData.put(curChar, new HashSet<>());
                greenData.get(curChar).add(position);
            }
            else if(solution.contains(curChar.toString())){
                if(yellowData.containsKey(curChar) == false)
                    yellowData.put(curChar, new HashSet<>());
                yellowData.get(curChar).add(position); 
            }
            else{
                if(blackData.containsKey(curChar) == false)
                    blackData.put(curChar, new HashSet<>());
                blackData.get(curChar).add(position);
            }

            if(solutionData.containsKey(solutionChar) == false){
                solutionData.put(solutionChar, 1);
            }
            else{
                solutionData.put(solutionChar, solutionData.get(solutionChar) + 1);
            }

        }

        //* First loop through all the green locations - these are 100% part of solution
        for (Character curChar : greenData.keySet()) {
            for(Integer position : greenData.get(curChar)){
                if(solutionData.get(curChar) > 0){
                    solutionData.put(curChar, solutionData.get(curChar) - 1);
                    dataOut[position] = 'G';
                }else{
                    System.out.printf("Error in WordData.getSolution(%s , $s): Unexpected Green Found %n", testWord, solution);
                    dataOut[position] = 'E';
                }
            }
        }

        //* Then loop through all of the yellow locations 
        //* Loop through them in order of how they appear
        //* Yellow means the word has to be in the solution data somewhere
        for(Character curChar : yellowData.keySet()){

            ArrayList<Integer> orderedPositions = new ArrayList<>();
            for(Integer position : yellowData.get(curChar)){
                orderedPositions.add(position);
            }
            
            Collections.sort(orderedPositions);

            for(Integer position : orderedPositions) {
                if(solutionData.get(curChar) > 0){
                    solutionData.put(curChar, solutionData.get(curChar) - 1);
                    dataOut[position] = 'Y'; 
                }
                else{
                    dataOut[position] = 'B';
                }
            }
                
        }

        //* Then finally loop through all of the black locations
        //* They should not need any checking, just get sent straight through
        for(Character curChar : blackData.keySet()){
            for(Integer position : blackData.get(curChar)){
                dataOut[position] = 'B';
            }
        }

        return dataOut;
    }

    public String getInfo(boolean colored){
        String dataOut = this.word + ": ";

        if(colored){
            HashMap<Integer, Character> validityData = getValidity();
            for(int position = 0; position < this.word.length(); position++){
                Character curChar = this.word.charAt(position);

                switch(validityData.get(position)){
                    case 'G': dataOut += ColorData.green + curChar + ColorData.reset; break;
                    case 'Y': dataOut += ColorData.yellow + curChar + ColorData.reset; break;
                    case 'B': dataOut += ColorData.silver + curChar + ColorData.reset; break;
                    case 'E': dataOut += ColorData.black + curChar + ColorData.reset; break;
                }
            }

        }
        else{
            HashMap<Integer, Character> validityData = getValidity();
            for(int position = 0; position < this.word.length(); position++){
                dataOut += validityData.get(position);
            }
        }



        return dataOut;
    }

    public HashMap<Integer, Character> getValidity(){
        HashMap<Integer, Character> totalData = new HashMap<>();

            for (Character curChar : greenInfo.keySet()) {
                for (Integer position : greenInfo.get(curChar)) {
                    totalData.put(position, 'G');
                }
            }

            for(Character curChar : yellowInfo.keySet()){
                for (Integer position : yellowInfo.get(curChar)){
                    totalData.put(position, 'Y');
                }
            }

            for(Character curChar : blackInfo.keySet()){
                for(Integer position : blackInfo.get(curChar)){
                    totalData.put(position, 'B');
                }
            }

        return totalData;
    }

    public String getWord() {
        return word;
    }
    public boolean isValid() {
        return valid;
    }
    public HashMap<Character, Set<Integer>> getGreenInfo() {
        return greenInfo;
    }
    public HashMap<Character, Set<Integer>> getYellowInfo() {
        return yellowInfo;
    }
    public HashMap<Character, Set<Integer>> getBlackInfo() {
        return blackInfo;
    }

    @Override
    public String toString(){
        return this.getInfo(false);
    }

}
