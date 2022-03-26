import java.util.ArrayList;
import java.util.HashMap;

/**
 * App
 */
public class App {
    public static final int WORD_LENGTH = 5;
    public static final int NUM_OF_ROUNDS = 6;
    public static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //* Valid Debug Levels: 1. Testing, 2. Production
    public static final int DEBUG_LEVEL = 1;
    

    

    public static void main(String[] args) {

        WordData data = new WordData("TARES", "DEPOT");
        System.out.println(data.getInfo(true));

        WordData data2 = new WordData("HEIST", "DEPOT");
        System.out.println(data2.getInfo(true));

        WordData data3 = new WordData("DEVOT", "DEPOT");
        System.out.println(data3.getInfo(true));
        


        
        
    }




    

}


