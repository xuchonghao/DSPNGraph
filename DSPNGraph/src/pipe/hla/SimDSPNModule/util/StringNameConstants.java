package pipe.hla.SimDSPNModule.util;

public class StringNameConstants {
    public final static String AU = "AU";
    public final static String Par = "Par";
    private static int AUSequence;
    public static int getAUSequence(){
        if(AUSequence == 0){
            AUSequence += 1;
            return 0;
        }
        else {
            return (AUSequence++);
        }
    }
}
