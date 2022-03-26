import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TestWord implements Comparable<TestWord>{
    //* Data protection parameters
    private Boolean locked;

    //* Parameters
    private String name;
    private Double score;
    private Boolean isValid;
    private HashMap<Character, Set<Integer>> duplicateData;

    //* Constructors
    public TestWord(String name){
        this.locked = false;
        this.name = String.format("%S", name);
        this.score = 0d;
        this.isValid = true;
        this.duplicateData = new HashMap<>();

        for(int position = 0; position < this.name.length(); position++){
            Character curChar = this.name.charAt(position);

            if(App.CHAR_LIST.contains(curChar.toString()) == false){
                this.isValid = false;
            }

            if(duplicateData.containsKey(curChar)){
                duplicateData.get(curChar).add(position);
            }
            else{
                duplicateData.put(curChar, new HashSet<>());
                duplicateData.get(curChar).add(position);
            }

        }

        if(this.name.length() != App.WORD_LENGTH){
            this.isValid = false;
        }

 
    }

    public TestWord(TestWord oldData){
        this.locked = oldData.locked;
        this.name = String.format("%S", oldData.name);
        this.score = oldData.score + 0;
        this.isValid = oldData.isValid;
        this.duplicateData = new HashMap<>(oldData.duplicateData);
    }
    
    //* All the getters for this class
    public String getName() {
        return name;
    }
    public Double getScore() {
        return score;
    }
    public Boolean isValid() {
        return isValid;
    }
    public HashMap<Character, Set<Integer>> getDuplicateData() {
        return duplicateData;
    }
    public TestWord copy(){
        return new TestWord(this);
    }
    public String getInfo(String infoLevel){
        String dataOut = "";

        switch(infoLevel){
            case "toString": dataOut += String.format("%S: %B - %.4f", this.name, this.isValid, this.score); break;
            case "Colored Name": 
                for (Character curChar : this.name.toUpperCase().toCharArray()) {
                    if(duplicateData.get(curChar).size() > 1){
                        dataOut += ColorData.blue + curChar + ColorData.reset;
                    }else{
                        dataOut += curChar;
                    }
                } 

                dataOut += String.format(": %B - %.4f", this.isValid, this.score); break;
            default: dataOut += "Error, invalid infoLevel: " + infoLevel; break;

        }

        return dataOut;

    }

    //* All the setters for this class
    public void setScore(Double newScore){
        if(!locked){
            this.score = newScore;
        }
        else{
            System.out.printf("Error in TestWord(%s).setScore(%f): Attempting to edit locked Data", this.name, newScore);
        }
        
    }
    public void setIsValid(Boolean valid){
        if(!locked){
            this.isValid = valid;
        }
        else{
            System.out.printf("Error in TestWord(%s).setIsValid(%b): Attempting to edit locked Data", this.name, valid);
        }
    }
    public void lockData(){
        this.locked = true;
    }


    //* Override Methods
    @Override
    public String toString(){   
             
        return this.getInfo("toString");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        TestWord other = (TestWord) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int compareTo(TestWord otherWord) {
        int compare = otherWord.score.compareTo(this.score);
        if(compare == 0){
            compare = this.name.compareTo(otherWord.name);
        }
        return compare;
        
    }

}
