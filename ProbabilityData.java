import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class ProbabilityData {
    //* Again this is just a baseLine where I build all the important parts first, then the rest later
    private ValidityData validityData;

    private HashMap<Integer, HashMap<Character, Double>> greenChance;
    private HashMap<Integer, Double> greenTotals;

    private HashMap<Integer, HashMap<Character, Double>> yellowChance;
    private HashMap<Integer, Double> yellowTotals;

    

    
    //* Constructors 
    public ProbabilityData(ValidityData validData){
        this.validityData = validData;
        this.greenChance = updateGreenChance();
        this.greenTotals = getTotals(this.greenChance);
        this.yellowChance = updateYellowChance();
        this.yellowTotals = getTotals(this.yellowChance);
        


    }


    private HashMap<Integer, Double> getTotals(HashMap<Integer, HashMap<Character, Double>> dataIn){
        HashMap<Integer, Double> dataOut = new HashMap<>();

        for(Integer position : dataIn.keySet()){
            Double total = 0d;

            for(Character curChar : dataIn.get(position).keySet()){
                total += dataIn.get(position).get(curChar);
            }

            dataOut.put(position, total);
        }

        return dataOut;
    }

    private HashMap<Integer, HashMap<Character, Double>> updateGreenChance(){
        HashMap<Integer, HashMap<Character, Double>> dataOut = new HashMap<>();

        

        for(int position = 0; position < App.WORD_LENGTH; position++){

            Double totalRemovalPercent = 0.0;
            Set<Character> validCharacters = new HashSet<>();
            dataOut.put(position, new HashMap<>());
            
            for(Character curChar : this.validityData.getBaseDataMap().keySet()){
                //* Checking if the character is even valid in the first place
                if(this.validityData.getValidityMap().get(position).get(curChar) == true){
                    validCharacters.add(curChar);
                }
            }

            for(Character curChar : this.validityData.getBaseDataMap().keySet()){
                double chanceOfGreen = 0.0;

                //* Using previous data to check if its valid
                if(validCharacters.contains(curChar)){

                    chanceOfGreen += 1.0 / this.validityData.getNumValidByPos().get(position);

                    //* Now we check if the character is has any known yellows
                    CharacterData currentCharData = this.validityData.getBaseDataMap().get(curChar);
                    if(currentCharData.getNumOfYellows() > 0 && currentCharData.getTestablePositions().contains(position)){
                        //* Number of yellows / Positions those yellows could be at
                        Double chanceAddedByYellow = currentCharData.getNumOfYellows() / (double)currentCharData.getTestablePositions().size();

                        chanceOfGreen += chanceAddedByYellow;

                        //* This line might need to be turned on, honestly this part hurts my brain
                        //chanceOfGreen += (double)chanceAddedByYellow / (double)validCharacters.size();

                        totalRemovalPercent += chanceAddedByYellow;
                    }
                }

                dataOut.get(position).put(curChar, chanceOfGreen);
            }

            for(Character curChar : validCharacters){
                Double newPercentage = dataOut.get(position).get(curChar) - (totalRemovalPercent / validCharacters.size() );
                dataOut.get(position).put(curChar, newPercentage);
            }




        }


        return dataOut;
    }

    private HashMap<Integer, HashMap<Character, Double>> updateYellowChance(){
        HashMap<Integer, HashMap<Character, Double>> dataOut = new HashMap<>();

        Set<Integer> positionsTestable = new HashSet<>();
        for(int position = 0; position < App.WORD_LENGTH; position++){
            if(this.validityData.getLockedChars().containsKey(position) == false){
                positionsTestable.add(position);
            }
        }
        
        for(int position = 0; position < App.WORD_LENGTH; position++){
            dataOut.put(position, new HashMap<>());

            for(Character curChar : this.greenChance.get(position).keySet()){
                double percentYellow = 0.0;

                




                dataOut.get(position).put(curChar, percentYellow);
            }


        }


        
        return dataOut;
    }

   
}
