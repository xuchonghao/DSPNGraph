package pipe.hla.SimDSPNModule.unuse;

import hla.rti1516.*;

import pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary.Constant;
import se.pitch.prti1516.Encoder;
import se.pitch.prti1516.FederateAmbassadorImpl;
import se.pitch.prti1516.LogicalTimeDouble;
import se.pitch.prti1516.RTI;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

class Chat extends FederateAmbassadorImpl {
    private RTIambassador _rtiAmbassador;
    private final String[] _args;
    private InteractionClassHandle _messageId;
    private ParameterHandle _parameterIdText;
    private ParameterHandle _parameterIdSender;
    private ObjectClassHandle _participantId;
    private ObjectInstanceHandle _userId;
    private AttributeHandle _attributeIdName;
    private String _username;

    private volatile boolean _reservationComplete;
    private volatile boolean _reservationSucceeded;
    private final Object _reservationSemaphore = new Object();

    private static final int CRC_PORT = 8989;
    private static final String FEDERATION_NAME = "ChatRoom";

    private static class Participant {
        private String _name;

        Participant(String name)
        {
            _name = name;
        }

        public String toString()
        {
            return _name;
        }
    }
    private final Map _knownObjects = new HashMap();

    public static void main(String[] args)
    {
        new Chat(args).run();
    }

    private Chat(String[] args)
    {
        _args = args;
    }

    private void run()
    {
        try {
            //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            String rtiHost = "";

            if (_args.length > 0) {
                rtiHost = _args[0];
            } else {
                System.out.print("Enter the IP address of the CRC host [localhost]: ");
                //rtiHost = in.readLine();
                if (rtiHost.length() == 0) {
                    rtiHost = Constant.HOSTNAME;
                }
            }

            try {
                _rtiAmbassador = RTI.getRTIambassador("192.168.206.136", CRC_PORT);
            } catch (Exception e) {
                System.out.println("Unable to connect to CRC on " + rtiHost + ":" + CRC_PORT);
                return;
            }

            try {
                _rtiAmbassador.destroyFederationExecution(FEDERATION_NAME);
            } catch (FederatesCurrentlyJoined ignored) {
            } catch (FederationExecutionDoesNotExist ignored) {
            }

            try {
                File fddFile = new File("Chat.xml");
                _rtiAmbassador.createFederationExecution(FEDERATION_NAME, fddFile.toURL());
            } catch (FederationExecutionAlreadyExists ignored) {
            }

            _rtiAmbassador.joinFederationExecution("Chat", FEDERATION_NAME, this, null);

            // Subscribe and publish interactions
            _messageId = _rtiAmbassador.getInteractionClassHandle("Communication");
            _parameterIdText = _rtiAmbassador.getParameterHandle(_messageId, "Message");
            _parameterIdSender = _rtiAmbassador.getParameterHandle(_messageId, "Sender");

            _rtiAmbassador.subscribeInteractionClass(_messageId);
            _rtiAmbassador.publishInteractionClass(_messageId);

            // Subscribe and publish objects
            _participantId = _rtiAmbassador.getObjectClassHandle("Participant");
            _attributeIdName = _rtiAmbassador.getAttributeHandle(_participantId, "Name");

            AttributeHandleSetFactory _attributeHandleSetFactory = _rtiAmbassador.getAttributeHandleSetFactory();
            AttributeHandleSet attributeSet = _attributeHandleSetFactory.create();
            attributeSet.add(_attributeIdName);

            _rtiAmbassador.subscribeObjectClassAttributes(_participantId, attributeSet);
            _rtiAmbassador.publishObjectClassAttributes(_participantId, attributeSet);


            LogicalTimeDouble infinity = new LogicalTimeDouble(Long.MAX_VALUE/10);
            //System.out.println( infinity.isFinal());
            _rtiAmbassador.nextMessageRequest(infinity);

            // Reserve object instance name and register object instance
            do {
                System.out.print("Enter your name: ");
               // _username = in.readLine();

                try {
                    _reservationComplete = false;
                    _rtiAmbassador.reserveObjectInstanceName(_username);

                    synchronized (_reservationSemaphore) {
                        // Wait for response from RTI
                        while (!_reservationComplete) {
                            try {
                                _reservationSemaphore.wait();
                            } catch (InterruptedException ignored) {

                            }
                        }
                    }
                    if (!_reservationSucceeded) {
                        System.out.println("Name already taken, try again.");
                    }
                } catch (IllegalName e) {
                    System.out.println("Illegal name. Try again.");
                } catch (RTIexception e) {
                    System.out.println("RTI exception when reserving name: " + e.getMessage());
                    return;
                }
            } while (!_reservationSucceeded);

            _userId = _rtiAmbassador.registerObjectInstance(_participantId, _username);

            System.out.println("Type messages you want to send. To exit, type . <ENTER>");
            while (true) {
                System.out.print("> ");
                String message = "fsadfs";//in.readLine();

                if (message.equals(".")) {
                    break;
                }

                ParameterHandleValueMap parameters = _rtiAmbassador.getParameterHandleValueMapFactory().create(1);

                parameters.put(_parameterIdText, Encoder.makeHLAunicodeString(message));
                parameters.put(_parameterIdSender, Encoder.makeHLAunicodeString(_username));

                _rtiAmbassador.sendInteraction(_messageId, parameters, null);
            }

            _rtiAmbassador.resignFederationExecution(ResignAction.DELETE_OBJECTS_THEN_DIVEST);
            try {
                _rtiAmbassador.destroyFederationExecution(FEDERATION_NAME);
            } catch (FederatesCurrentlyJoined ignored) {
            }
            _rtiAmbassador = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void receiveInteraction(InteractionClassHandle interactionClass,
                                         ParameterHandleValueMap theParameters,
                                         byte[] userSuppliedTag,
                                         OrderType sentOrdering,
                                         TransportationType theTransport)
    {
        if (interactionClass.equals(_messageId)) {
            if (!theParameters.containsKey(_parameterIdText)) {
                System.out.println("Bad message received: No text.");
                return;
            }
            if (!theParameters.containsKey(_parameterIdSender)) {
                System.out.println("Bad message received: No sender.");
                return;
            }
            String message = Encoder.stringFromHLAunicodeString((byte[]) theParameters.get(_parameterIdText));
            String sender = Encoder.stringFromHLAunicodeString((byte[]) theParameters.get(_parameterIdSender));

            System.out.println(sender + ": " + message);
            System.out.print("> ");
        }
    }

    public final void objectInstanceNameReservationSucceeded(String objectName)
    {
        synchronized (_reservationSemaphore) {
            _reservationComplete = true;
            _reservationSucceeded = true;
            _reservationSemaphore.notify();
        }
    }

    public final void objectInstanceNameReservationFailed(String objectName)
    {
        synchronized (_reservationSemaphore) {
            _reservationComplete = true;
            _reservationSucceeded = false;
            _reservationSemaphore.notify();
        }
    }


    public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering)
    {
        Participant member = (Participant) _knownObjects.remove(theObject);
        if (member != null) {
            System.out.println("[" + member + " has left]");
        }
    }

    public final void reflectAttributeValues(ObjectInstanceHandle theObject,
                                             AttributeHandleValueMap theAttributes,
                                             byte[] userSuppliedTag,
                                             OrderType sentOrdering,
                                             TransportationType theTransport)
    {
        if (!_knownObjects.containsKey(theObject)) {
            if (theAttributes.containsKey(_attributeIdName)) {
                String memberName = Encoder.stringFromHLAunicodeString((byte[]) theAttributes.get(_attributeIdName));
                Participant member = new Participant(memberName);
                System.out.println("[" + member + " has joined]");
                System.out.print("> ");
                _knownObjects.put(theObject, member);
            }
        }
    }

    public final void provideAttributeValueUpdate(ObjectInstanceHandle theObject,
                                                  AttributeHandleSet theAttributes,
                                                  byte[] userSuppliedTag)
    {
        if (theObject.equals(_userId) && theAttributes.contains(_attributeIdName)) {
            new Thread() {
                public void run()
                {
                    updateMyName();
                }
            }.start();
        }
    }

    private void updateMyName()
    {
        try {
            AttributeHandleValueMap attributeValues = _rtiAmbassador.getAttributeHandleValueMapFactory().create(1);
            attributeValues.put(_attributeIdName, Encoder.makeHLAunicodeString(_username));
            _rtiAmbassador.updateAttributeValues(_userId, attributeValues, null);
        } catch (RTIexception ignored) {
        }
    }

}
