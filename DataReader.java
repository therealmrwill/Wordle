import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class DataReader {

   public static HashMap<String, TestWord> readFromFile(String fileName, boolean upperCase){
        HashMap<String, TestWord> dataOut = new HashMap<>();

        try {
            Scanner scnr = new Scanner(new File(fileName));
            
            while(scnr.hasNext()){

                String testWord = scnr.next();

                if(upperCase){
                    testWord = testWord.toUpperCase();
                }

                dataOut.put(testWord, new TestWord(testWord));

        
            }

            scnr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        return dataOut;
   } 
}
