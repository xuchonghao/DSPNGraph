package pipe.hla.SimDSPNModule.unuse;

import hla.rti1516.RTIexception;

public class ArrayIndexOutOfBounds extends RTIexception {

    /**
     * @param reason    String to be carried with exception
     */
    public ArrayIndexOutOfBounds(String reason) {
        super(reason);
    }

}
