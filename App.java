import java.util.HashMap;

/**
 * App
 */
public class App {
    public static final int WORD_LENGTH = 5;
    public static final int NUM_OF_ROUNDS = 6;
    public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int DEBUG_LEVEL = 4;

    //Yes I know the yellow is not yellow - I just didn't want to blind myself while testing
    public static final String ANSI_YELLOW = "\u001b[35m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_RESET = "\u001b[0m";

    

    public static void main(String[] args) {
        ValidityData vData = new ValidityData();
        vData.addTestedWord("TARES", "BYGGY");

        //* Cases for quick reference: Locked Only, Invalid Only, Valid Only, Valid and Invalid, All, All Colored
        System.out.println(vData.saveInfo("Locked Only"));
        System.out.println(vData.saveInfo("Invalid Only"));
        System.out.println(vData.saveInfo("Valid Only"));
        System.out.println(vData.saveInfo("Valid and Invalid"));
        System.out.println(vData.saveInfo("All"));
        System.out.println(vData.saveInfo("All Colored"));

    }




    

}


   