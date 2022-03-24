import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class DataReader {

   public static ArrayList<TestWord> readFromFile(String fileName){
        ArrayList<TestWord> dataOut = new ArrayList<>();

        try {
            Scanner fscnr = new Scanner(new File(fileName));
            
            while(fscnr.hasNext()){
                String testWord = fscnr.next();
                testWord = testWord.toUpperCase();

                if(testWord.length() != App.WORD_LENGTH){
                    System.out.println("Error in DataReader.readFromFile( " + fileName + "): Invalid Word - " + testWord);
                }else{
                    dataOut.add(new TestWord(testWord));
                }


            }

            fscnr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        return dataOut;
   } 
}
