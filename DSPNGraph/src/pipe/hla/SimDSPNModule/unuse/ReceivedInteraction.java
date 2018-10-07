package pipe.hla.SimDSPNModule.unuse;

/**
 * This packages the information supplied to the federate for
 * receiveInteraction. The parameters are conceptually an array
 * with an initial capacity and the ability to grow.
 * You enumerate by stepping index from 0 to size()-1.
 *
 */
public interface ReceivedInteraction {

    /**
     * Return order type

     */
    public int getOrderType ();
    /**
     * Return parameter handle at index position.

     *
     */
    public int getParameterHandle ( int index) throws ArrayIndexOutOfBounds;
    /**
     * Return Region out of which interaction was received.
     */
   // public Region getRegion ();
    /**
     * Return transport type
     * @return int transport type
     */
    public int getTransportType ();
    /**
     * Return copy of value at index position.

     */
    public byte[] getValue ( int index) throws ArrayIndexOutOfBounds;
    /**
     * Return length of value at index position.
     * @return int value length
     * @param index int
     * @exception hla.rti.ArrayIndexOutOfBounds
     */
    public int getValueLength ( int index) throws ArrayIndexOutOfBounds;
    /**
     * Get the reference of the value at position index (not a clone)

     */
    public byte[] getValueReference ( int index) throws ArrayIndexOutOfBounds;
    /**
     * @return int Number of parameter handle-value pairs
     */
    public int size ( );
}
