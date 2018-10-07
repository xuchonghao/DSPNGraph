package pipe.hla.book.messages;
import hla.rti.*;
import se.pitch.prti.FederateAmbassador_Impl;
import se.pitch.prti.RTI;

import java.net.*;
import java.io.*;

public final class InstantMessages extends FederateAmbassador_Impl {
  RTIambassador _rtiAmbassador;

  public static void main(String[] args) {
    new InstantMessages().run();
  }

  public void run() {
    int _interactionClassYouHaveAMessage;
    int _parameter_message;

    try {
      _rtiAmbassador = RTI.getRTIambassador(
        InetAddress.getLocalHost().getHostAddress(),
        8989);

      try {
        _rtiAmbassador.createFederationExecution("MessagesFederation",
           new URL("file:/c:/mydocu~1/hla/hla.org.book/code/config/MessagesFederation.fed"));
      } catch (FederationExecutionAlreadyExists ignored) {}

      _rtiAmbassador.joinFederationExecution("Messenger", "MessagesFederation", this);

      _interactionClassYouHaveAMessage = _rtiAmbassador.getInteractionClassHandle(
        "InteractionRoot.YouHaveAMessage");
      _parameter_message = _rtiAmbassador.getParameterHandle(
        "message",
        _interactionClassYouHaveAMessage);

      _rtiAmbassador.subscribeInteractionClass(_interactionClassYouHaveAMessage);
      _rtiAmbassador.publishInteractionClass(_interactionClassYouHaveAMessage);

      System.out.println("Type a message to send it. Type \"quit\" to quit.");
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      while (true) {
        System.out.print("message: ");
        String message = in.readLine();
        if (message.equals("quit")) break;
        SuppliedParameters parameters = se.pitch.prti.RTI.suppliedParametersFactory().create(1);

        byte[] value = message.getBytes();
        parameters.add(_parameter_message, value);
        _rtiAmbassador.sendInteraction(_interactionClassYouHaveAMessage, parameters, null);
	    }
      _rtiAmbassador.resignFederationExecution(
        ResignAction.DELETE_OBJECTS_AND_RELEASE_ATTRIBUTES);
	    try {
        _rtiAmbassador.destroyFederationExecution("MessagesFederation");
	    }
      catch (FederatesCurrentlyJoined ignored) {}
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void receiveInteraction(int interactionClass,
    ReceivedInteraction theInteraction,
    byte[] userSuppliedTag)
  throws InteractionClassNotKnown,
    InteractionParameterNotKnown,
    FederateInternalError
  {
    try {
      String message = new String(theInteraction.getValueReference(0));
      System.out.println("\nReceived: " + message);
      System.out.print("message: ");
    }
    catch (ArrayIndexOutOfBounds ignored) {}
  }
}

