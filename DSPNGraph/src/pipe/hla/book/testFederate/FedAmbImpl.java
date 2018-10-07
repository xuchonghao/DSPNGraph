
package pipe.hla.book.testFederate;

import hla.rti1516.*;

import java.util.*;

//testfed's implementation of FederateAmbassador

public final class FedAmbImpl implements FederateAmbassador {

  private TestFederate _fed;
  private TestFederateFrame _userInterface;

  public FedAmbImpl(
	TestFederate fed,
	TestFederateFrame ui)
  {
	_fed = fed;
	_userInterface = ui;
  }          
	//4.8
	public void announceSynchronizationPoint(
		String synchronizationPointLabel, 
		byte[] userSuppliedTag)
	throws
           FederateInternalError
	{
		_userInterface.post("�(4.8)announceSynchronizationPoint; label:"
			+ synchronizationPointLabel + " tag: " + renderTag(userSuppliedTag));
	}
	// 7.16 -> 7.17
   public void attributeIsNotOwned(
           ObjectInstanceHandle theObject,
           AttributeHandle theAttribute)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           FederateInternalError {
      _userInterface.post("�(7.17)Attribute is not owned, object: " + theObject
      + ", attribute: " + theAttribute);
   }

//	public void attributeIsNotOwned (
//		int theObject,
//		int theAttribute)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		FederateInternalError
//	{
//		_userInterface.post("�(7.16)Attribute is not owned, object: " + theObject
//      + ", attribute: " + theAttribute);
//	}

   // 7.16 -> 7.17
   public void attributeIsOwnedByRTI(
           ObjectInstanceHandle theObject,
           AttributeHandle theAttribute)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           FederateInternalError {
      _userInterface.post("�(7.17)Attribute owned by RTI, object: " + theObject
      + ", attribute: " + theAttribute);
   }

//	public void attributeOwnedByRTI (
//		int theObject,
//		int theAttribute)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		FederateInternalError
//	{
//		_userInterface.post("�(7.16)Attribute owned by RTI, object: " + theObject
//      + ", attribute: " + theAttribute);
//	}

	// 7.6 -> 7.7
   public void attributeOwnershipAcquisitionNotification(
           ObjectInstanceHandle theObject,
           AttributeHandleSet securedAttributes,
           byte[] userSuppliedTag)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeAcquisitionWasNotRequested,
           AttributeAlreadyOwned,
           AttributeNotPublished,
           FederateInternalError {
      _userInterface.post("�(7.7)attributeOwnershipAcquisitionNotification; object:"
			+ theObject
      + ", attrs: " + securedAttributes);
   }

//	public void attributeOwnershipAcquisitionNotification (
//		int                theObject,
//		AttributeHandleSet securedAttributes)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		AttributeAcquisitionWasNotRequested,
//		AttributeAlreadyOwned,
//		AttributeNotPublished,
//		FederateInternalError
//	{
//		_userInterface.post("�(7.6)attributeOwnershipAcquisitionNotification; object:"
//			+ theObject
//      + ", attrs: " + securedAttributes);
//	}

	// 7.5 Name Changed from "attributeOwnershipDivestitureNotification" 
   public void requestDivestitureConfirmation(
           ObjectInstanceHandle theObject,
           AttributeHandleSet offeredAttributes)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotOwned,
           AttributeDivestitureWasNotRequested,
           FederateInternalError {
      _userInterface.post("�(7.5)requestDivestitureConfirmation; object:"
			+ theObject
      + ", attrs: " + offeredAttributes);
   }

//	public void attributeOwnershipDivestitureNotification (
//		int                theObject,
//		AttributeHandleSet releasedAttributes)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		AttributeNotOwned,
//		AttributeDivestitureWasNotRequested,
//		FederateInternalError
//	{
//		_userInterface.post("�(7.5)attributeOwnershipDivestitureNotification; object:"
//			+ theObject
//      + ", attrs: " + releasedAttributes);
//	}

   // 7.9 -> 7.10
   public void attributeOwnershipUnavailable(
           ObjectInstanceHandle theObject,
           AttributeHandleSet theAttributes)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeAlreadyOwned,
           AttributeAcquisitionWasNotRequested,
           FederateInternalError {
      _userInterface.post("�(7.10)attributeOwnershipUnavailable; object:"
			+ theObject
      + ", attrs: " + theAttributes);
   }

//	public void attributeOwnershipUnavailable (
//		int                theObject,
//		AttributeHandleSet theAttributes)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		AttributeAlreadyOwned,
//		AttributeAcquisitionWasNotRequested,
//		FederateInternalError
//	{
//		_userInterface.post("�(7.9)attributeOwnershipUnavailable; object:"
//			+ theObject
//      + ", attrs: " + theAttributes);
//	}

	// 6.13 -> 6.15
   public void attributesInScope(
           ObjectInstanceHandle theObject,
           AttributeHandleSet theAttributes)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotSubscribed,
           FederateInternalError {
      _userInterface.post("�(6.15)attributesInScope; object:"
			+ theObject
      + ", attrs: " + theAttributes);
   }

//	public void attributesInScope (
//		int                theObject,
//		AttributeHandleSet theAttributes)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		FederateInternalError
//	{
//		_userInterface.post("�(6.15)attributesInScope; object:"
//			+ theObject
//      + ", attrs: " + theAttributes);
//	}

	// 6.14 -> 6.16
   public void attributesOutOfScope(
           ObjectInstanceHandle theObject,
           AttributeHandleSet theAttributes)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotSubscribed,
           FederateInternalError {
      _userInterface.post("�(6.16)attributesOutOfScope; object:"
			+ theObject
      + ", attrs: " + theAttributes);
   }

//	public void attributesOutOfScope (
//		int                theObject,
//		AttributeHandleSet theAttributes)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		FederateInternalError
//	{
//		_userInterface.post("�(6.16)attributesOutOfScope; object:"
//			+ theObject
//      + ", attrs: " + theAttributes);
//	}

	// 7.14 -> 7.15
   public void confirmAttributeOwnershipAcquisitionCancellation(
           ObjectInstanceHandle theObject,
           AttributeHandleSet theAttributes)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeAlreadyOwned,
           AttributeAcquisitionWasNotCanceled,
           FederateInternalError {
      _userInterface.post("�(7.15)confirmAttributeOwnershipAcquisitionCancellation; object:"
			+ theObject
      + ", attrs: " + theAttributes);
   }

//	public void confirmAttributeOwnershipAcquisitionCancellation (
//		   int                theObject,
//		   AttributeHandleSet theAttributes)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		AttributeAlreadyOwned,
//		AttributeAcquisitionWasNotCanceled,
//		FederateInternalError
//	{
//		_userInterface.post("�(7.14)confirmAttributeOwnershipAcquisitionCancellation; object:"
//			+ theObject
//      + ", attrs: " + theAttributes);
//	}

	// 6.5
   public void discoverObjectInstance(
           ObjectInstanceHandle theObject,
           ObjectClassHandle theObjectClass,
           String objectName)
   throws
           CouldNotDiscover,
           ObjectClassNotRecognized,
           FederateInternalError {
      _userInterface.post("�(6.5)discoverObjectInstance; object:"
              + theObject + "(" + objectName + "), class: " + theObjectClass);
   }

//	public void discoverObjectInstance (
//		int    theObject,
//		int    theObjectClass,
//		String objectName)
//	throws
//		CouldNotDiscover,
//		ObjectClassNotKnown,
//		FederateInternalError
//	{
//		_userInterface.post("�(6.5)discoverObjectInstance; object:"
//			+ theObject + "(" + objectName + "), class: " + theObjectClass);
//	}

	// 4.21
   public void federationNotRestored(
           RestoreFailureReason reason)
   throws
           FederateInternalError {
		_userInterface.post("�(4.21)Federation not restored.");
   }

   // 4.25
   public void federationRestoreStatusResponse(
           FederateHandleRestoreStatusPair[] response)
   throws
           FederateInternalError {
      //Todo Implement
   }

//	public void federationNotRestored ()
//	throws
//		FederateInternalError
//	{
//		_userInterface.post("�(4.21)Federation not restored.");
//	}

	// 4.15
   public void federationNotSaved(
           SaveFailureReason reason)
   throws
           FederateInternalError {
      _userInterface.post("�(4.15)Federation not saved.");
   }

   // 4.17
   public void federationSaveStatusResponse(
           FederateHandleSaveStatusPair[] response)
   throws FederateInternalError {
      //Todo Implement
   }

//	public void federationNotSaved ()
//	throws
//		FederateInternalError
//	{
//		_userInterface.post("�(4.15)Federation not saved.");
//	}

	// 4.18
	public void federationRestoreBegun ()
	throws
		FederateInternalError
	{
		_userInterface.post("�(4.18)Federation restore begun.");
	}

   // 4.21
   public void initiateFederateRestore(
           String label,
           FederateHandle federateHandle)
   throws
           SpecifiedSaveLabelDoesNotExist,
           CouldNotInitiateRestore,
           FederateInternalError {
      //Todo Implement
   }

   // 4.21
	public void federationRestored ()
	throws
		FederateInternalError
	{
		_userInterface.post("�(4.21)Federation restored.");
	}

	// 4.15
	public void federationSaved ()
	throws
		FederateInternalError
	{
		_userInterface.post("�(4.15)Federation saved.");
	}

	//4.10
	public void federationSynchronized(
		String synchronizationPointLabel)
	throws
		FederateInternalError
	{
		_userInterface.post("�(4.10)federationSynchronized at:"
			+ synchronizationPointLabel);
	}

   // 7.16 -> 7.17
   public void informAttributeOwnership(
           ObjectInstanceHandle theObject,
           AttributeHandle theAttribute,
           FederateHandle theOwner)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           FederateInternalError {
      _userInterface.post("�(7.16)informAttributeOwnership; object:"
			+ theObject
      + ", attr: " + theAttribute
      + ", owner: " + theOwner);
   }

//	public void informAttributeOwnership (
//		int theObject,
//		int theAttribute,
//		int theOwner)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		FederateInternalError
//	{
//		_userInterface.post("�(7.16)informAttributeOwnership; object:"
//			+ theObject
//      + ", attr: " + theAttribute
//      + ", owner: " + theOwner);
//	}

	// 4.19
   public void initiateFederateSave(
           String label,
           LogicalTime time)
   throws
           InvalidLogicalTime,
           UnableToPerformSave,
           FederateInternalError {
      _userInterface.post("�(4.19)Initiate federate restore.");
   }

//	public void initiateFederateRestore (
//		String label,
//		int    federateHandle)
//	throws
//		SpecifiedSaveLabelDoesNotExist,
//		CouldNotRestore,
//		FederateInternalError
//	{
//		_userInterface.post("�(4.19)Initiate federate restore.");
//	}

	//4.12
	public void initiateFederateSave(
		String label)
	throws
		UnableToPerformSave,
		FederateInternalError
	{
		_userInterface.post("�(4.12)Initiate federate save.");
	}

   // 6.18
   public void provideAttributeValueUpdate(
           ObjectInstanceHandle theObject,
           AttributeHandleSet theAttributes,
           byte[] userSuppliedTag)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotOwned,
           FederateInternalError {
      _userInterface.post("�(6.18)Provide attribute value update, object: "
      + theObject + ", attributes: " + theAttributes);
   }

//	public void provideAttributeValueUpdate (
//		int                theObject,
//		AttributeHandleSet theAttributes)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		AttributeNotOwned,
//		FederateInternalError
//	{
//		_userInterface.post("�(6.18)Provide attribute value update, object: "
//      + theObject + ", attributes: " + theAttributes);
//	}

	// 6.9
   public void receiveInteraction(
           InteractionClassHandle interactionClass,
           ParameterHandleValueMap theParameters,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport)
   throws
           InteractionClassNotRecognized,
           InteractionParameterNotRecognized,
           InteractionClassNotSubscribed,
           FederateInternalError {
      String order = (sentOrdering.equals(OrderType.RECEIVE))
              ? "receive"
              : "timestamp";
      _userInterface.post("�(6.9)receiveInteraction " + interactionClass
              + ", order: " + order + ", transportation: " + theTransport
              + ", tag: " + renderTag(userSuppliedTag));

      Set handles = theParameters.keySet();
      for(Iterator i=handles.iterator(); i.hasNext(); ) {
         ParameterHandle theHandle = (ParameterHandle) i.next();
         _userInterface.post("  param: " + theHandle
                 + " value: " + theParameters.get(theHandle));
      }
   }

//	public void receiveInteraction (
//		int                 interactionClass,
//		ReceivedInteraction theInteraction,
//		byte[]              userSuppliedTag)
//	throws
//		InteractionClassNotKnown,
//		InteractionParameterNotKnown,
//		FederateInternalError
//	{
//    try {
//      String order = (theInteraction.getOrderType() == RTI.OrderType_Receive)
//        ? "receive"
//        : "timestamp";
//      _userInterface.post("�(6.9)receiveInteraction " + interactionClass
//        + ", order: " + order + ", transportation: " + theInteraction.getTransportType()
//        + ", tag: " + renderTag(userSuppliedTag));
//      int paramCount = theInteraction.size();
//      for (int p = 0; p < paramCount; ++p) {
//        _userInterface.post("  param: " + theInteraction.getParameterHandle(p)
//          + " value: " + new String(theInteraction.getValueReference(p)));
//      }
//    }
//    catch (ArrayIndexOutOfBounds e) {
//      throw new InteractionParameterNotKnown(e.toString());
//    }
//	}

	// 6.9
   public void receiveInteraction(
           InteractionClassHandle interactionClass,
           ParameterHandleValueMap theParameters,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport,
           RegionHandleSet sentRegions)
   throws
           InteractionClassNotRecognized,
           InteractionParameterNotRecognized,
           InteractionClassNotSubscribed,
           FederateInternalError {
      String order = (sentOrdering.equals(OrderType.RECEIVE))
              ? "receive"
              : "timestamp";
      _userInterface.post("�(6.9)receiveInteraction " + interactionClass
              + ", order: " + order + ", transportation: " + theTransport
              + ", tag: " + renderTag(userSuppliedTag));

      Set handles = theParameters.keySet();
      for(Iterator i=handles.iterator(); i.hasNext(); ) {
         ParameterHandle theHandle = (ParameterHandle) i.next();
         _userInterface.post("  param: " + theHandle
                 + " value: " + theParameters.get(theHandle));
      }
   }

   // 6.9
   public void receiveInteraction(
           InteractionClassHandle interactionClass,
           ParameterHandleValueMap theParameters,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport,
           LogicalTime theTime,
           OrderType receivedOrdering)
   throws
           InteractionClassNotRecognized,
           InteractionParameterNotRecognized,
           InteractionClassNotSubscribed,
           FederateInternalError {
      String order = (sentOrdering.equals(OrderType.RECEIVE))
              ? "receive"
              : "timestamp";
      _userInterface.post("�(6.9)receiveInteraction " + interactionClass
              + ", order: " + order + ", transportation: " + theTransport
              + ", tag: " + renderTag(userSuppliedTag));

      Set handles = theParameters.keySet();
      for(Iterator i=handles.iterator(); i.hasNext(); ) {
         ParameterHandle theHandle = (ParameterHandle) i.next();
         _userInterface.post("  param: " + theHandle
                 + " value: " + theParameters.get(theHandle));
      }
   }

   // 6.9
   public void receiveInteraction(
           InteractionClassHandle interactionClass,
           ParameterHandleValueMap theParameters,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport,
           LogicalTime theTime,
           OrderType receivedOrdering,
           RegionHandleSet regions)
   throws
           InteractionClassNotRecognized,
           InteractionParameterNotRecognized,
           InteractionClassNotSubscribed,
           FederateInternalError {
      String order = (sentOrdering.equals(OrderType.RECEIVE))
              ? "receive"
              : "timestamp";
      _userInterface.post("�(6.9)receiveInteraction " + interactionClass
              + ", order: " + order + ", transportation: " + theTransport
              + ", tag: " + renderTag(userSuppliedTag));

      Set handles = theParameters.keySet();
      for(Iterator i=handles.iterator(); i.hasNext(); ) {
         ParameterHandle theHandle = (ParameterHandle) i.next();
         _userInterface.post("  param: " + theHandle
                 + " value: " + theParameters.get(theHandle));
      }
   }

   // 6.9
   public void receiveInteraction(
           InteractionClassHandle interactionClass,
           ParameterHandleValueMap theParameters,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport,
           LogicalTime theTime,
           OrderType receivedOrdering,
           MessageRetractionHandle messageRetractionHandle)
   throws
           InteractionClassNotRecognized,
           InteractionParameterNotRecognized,
           InteractionClassNotSubscribed,
           InvalidLogicalTime,
           FederateInternalError {
      String order = (sentOrdering.equals(OrderType.RECEIVE))
              ? "receive"
              : "timestamp";
      _userInterface.post("�(6.9)receiveInteraction " + interactionClass
              + ", order: " + order + ", transportation: " + theTransport
              + ", tag: " + renderTag(userSuppliedTag));
      _userInterface.post("  time: " + theTime + ", retraction: " + messageRetractionHandle);
      Set handles = theParameters.keySet();
      for(Iterator i=handles.iterator(); i.hasNext(); ) {
         ParameterHandle theHandle = (ParameterHandle) i.next();
         _userInterface.post("  param: " + theHandle
                 + " value: " + theParameters.get(theHandle));
      }
   }

//   public void receiveInteraction (
//		int                 interactionClass,
//		ReceivedInteraction theInteraction,
//		byte[]              userSuppliedTag,
//		LogicalTime         theTime,
//		EventRetractionHandle eventRetractionHandle)
//	throws
//		InteractionClassNotKnown,
//		InteractionParameterNotKnown,
//		InvalidFederationTime,
//		FederateInternalError
//	{
//    try {
//      String order = (theInteraction.getOrderType() == RTI.OrderType_Receive)
//        ? "receive"
//        : "timestamp";
//      _userInterface.post("�(6.9)receiveInteraction " + interactionClass
//        + ", order: " + order + ", transportation: " + theInteraction.getTransportType()
//        + ", tag: " + renderTag(userSuppliedTag));
//      _userInterface.post("  time: " + theTime + ", retraction: " +  eventRetractionHandle);
//      int paramCount = theInteraction.size();
//      for (int p = 0; p < paramCount; ++p) {
//        _userInterface.post("  param: " + theInteraction.getParameterHandle(p)
//          + " value: " + new String(theInteraction.getValueReference(p)));
//      }
//    }
//    catch (ArrayIndexOutOfBounds e) {
//      throw new InteractionParameterNotKnown(e.toString());
//    }
//	}

   // 6.9
   public void receiveInteraction(
           InteractionClassHandle interactionClass,
           ParameterHandleValueMap theParameters,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport,
           LogicalTime theTime,
           OrderType receivedOrdering,
           MessageRetractionHandle messageRetractionHandle,
           RegionHandleSet sentRegions)
      throws
           InteractionClassNotRecognized,
           InteractionParameterNotRecognized,
           InteractionClassNotSubscribed,
           InvalidLogicalTime,
           FederateInternalError {
      //Todo Implement
   }

   // 6.7
   public void reflectAttributeValues(
           ObjectInstanceHandle theObject,
           AttributeHandleValueMap theAttributes,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotSubscribed,
           FederateInternalError {

      _userInterface.post("�(6.7)reflectAttributeValues of obj " + theObject
        + ", tag: " + renderTag(userSuppliedTag));

      Set theHandles = theAttributes.keySet();
      for (Iterator i=theHandles.iterator(); i.hasNext(); ) {
         AttributeHandle theHandle = (AttributeHandle) i.next();
         _userInterface.post("  attr: " + theHandle
          + " value: " + theAttributes.get(theHandle));
      }
   }

//   public void reflectAttributeValues (
//		int                 theObject,
//		ReflectedAttributes theAttributes,
//		byte[]              userSuppliedTag)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		FederateOwnsAttributes,
//		FederateInternalError
//	{
//    try {
//      _userInterface.post("�(6.7)reflectAttributeValues of obj " + theObject
//        + ", tag: " + renderTag(userSuppliedTag));
//      int attrCount = theAttributes.size();
//      for (int a = 0; a < attrCount; ++a) {
//        _userInterface.post("  attr: " + theAttributes.getAttributeHandle(a)
//          + " value: " + new String(theAttributes.getValueReference(a)));
//      }
//    }
//    catch (ArrayIndexOutOfBounds e) {
//      throw new AttributeNotKnown(e.toString());
//    }
//	}

   // 6.7
   public void reflectAttributeValues(
           ObjectInstanceHandle theObject,
           AttributeHandleValueMap theAttributes,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport,
           RegionHandleSet sentRegions)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotSubscribed,
           FederateInternalError {
      //Todo Implement
   }

   // 6.7
   public void reflectAttributeValues(
           ObjectInstanceHandle theObject,
           AttributeHandleValueMap theAttributes,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport,
           LogicalTime theTime,
           OrderType receivedOrdering)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotSubscribed,
           FederateInternalError {
      //Todo Implement
   }

   // 6.7
   public void reflectAttributeValues(
           ObjectInstanceHandle theObject,
           AttributeHandleValueMap theAttributes,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport,
           LogicalTime theTime,
           OrderType receivedOrdering,
           RegionHandleSet sentRegions)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotSubscribed,
           FederateInternalError {
      //Todo Implement
   }

   // 6.7
   public void reflectAttributeValues(
           ObjectInstanceHandle theObject,
           AttributeHandleValueMap theAttributes,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport,
           LogicalTime theTime,
           OrderType receivedOrdering,
           MessageRetractionHandle retractionHandle)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotSubscribed,
           InvalidLogicalTime,
           FederateInternalError {
      _userInterface.post("�(6.7)reflectAttributeValues of obj " + theObject
        + ", tag: " + renderTag(userSuppliedTag));
      _userInterface.post("  time: " + theTime + ", retraction: " + retractionHandle);
      Set theHandles = theAttributes.keySet();
      for (Iterator i=theHandles.iterator(); i.hasNext(); ) {
         AttributeHandle theHandle = (AttributeHandle) i.next();
         _userInterface.post("  attr: " + theHandle
          + " value: " + theAttributes.get(theHandle));
      }
   }

//	public void reflectAttributeValues (
//		int                 theObject,
//		ReflectedAttributes theAttributes,
//		byte[]              userSuppliedTag,
//		LogicalTime         theTime,
//		EventRetractionHandle retractionHandle)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		FederateOwnsAttributes,
//		InvalidFederationTime,
//		FederateInternalError
//	{
//    try {
//      _userInterface.post("�(6.7)reflectAttributeValues of obj " + theObject
//        + ", tag: " + renderTag(userSuppliedTag));
//      _userInterface.post("  time: " + theTime + ", retraction: " +  retractionHandle);
//      int attrCount = theAttributes.size();
//      for (int a = 0; a < attrCount; ++a) {
//        _userInterface.post("  attr: " + theAttributes.getAttributeHandle(a)
//          + " value: " + new String(theAttributes.getValueReference(a)));
//      }
//    }
//    catch (ArrayIndexOutOfBounds e) {
//      throw new AttributeNotKnown(e.toString());
//    }
//	}

   // 6.7
   public void reflectAttributeValues(
           ObjectInstanceHandle theObject,
           AttributeHandleValueMap theAttributes,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           TransportationType theTransport,
           LogicalTime theTime,
           OrderType receivedOrdering,
           MessageRetractionHandle retractionHandle,
           RegionHandleSet sentRegions)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotSubscribed,
           InvalidLogicalTime,
           FederateInternalError {
      //Todo Implement
   }



	// 6.11
   public void removeObjectInstance(
           ObjectInstanceHandle theObject,
           byte[] userSuppliedTag,
           OrderType sentOrdering)
   throws
           ObjectInstanceNotKnown,
           FederateInternalError {
      _userInterface.post("�(6.11)removeObjectInstance; object:"
			+ theObject + ", tag: " + renderTag(userSuppliedTag));
   }

//	public void removeObjectInstance (
//		int    theObject,
//		byte[] userSuppliedTag)
//	throws
//		ObjectNotKnown,
//		FederateInternalError
//	{
//		_userInterface.post("�(6.11)removeObjectInstance; object:"
//			+ theObject + ", tag: " + renderTag(userSuppliedTag));
//	}

   // 6.11
   public void removeObjectInstance(
           ObjectInstanceHandle theObject,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           LogicalTime theTime,
           OrderType receivedOrdering)
   throws
           ObjectInstanceNotKnown,
           FederateInternalError {
      //Todo Implement
   }

	// 6.11
   public void removeObjectInstance(
           ObjectInstanceHandle theObject,
           byte[] userSuppliedTag,
           OrderType sentOrdering,
           LogicalTime theTime,
           OrderType receivedOrdering,
           MessageRetractionHandle retractionHandle)
   throws
           ObjectInstanceNotKnown,
           InvalidLogicalTime,
           FederateInternalError {
      _userInterface.post("�(6.11)removeObjectInstance; object:"
              + theObject + ", tag: " + renderTag(userSuppliedTag));
      _userInterface.post("  time: " + theTime + ", retraction: " +  retractionHandle);
   }

//	public void removeObjectInstance (
//		int            theObject,
//		byte[]         userSuppliedTag,
//		LogicalTime    theTime,
//		EventRetractionHandle retractionHandle)
//	throws
//		ObjectNotKnown,
//		InvalidFederationTime,
//		FederateInternalError
//	{
//		_userInterface.post("�(6.11)removeObjectInstance; object:"
//			+ theObject + ", tag: " + renderTag(userSuppliedTag));
//    _userInterface.post("  time: " + theTime + ", retraction: " +  retractionHandle);
//	}

	// 7.4
   public void requestAttributeOwnershipAssumption(
           ObjectInstanceHandle theObject,
           AttributeHandleSet offeredAttributes,
           byte[] userSuppliedTag)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeAlreadyOwned,
           AttributeNotPublished,
           FederateInternalError {
      _userInterface.post("�(7.4)requestAttributeOwnershipAssumption; object:"
              + theObject
              + ", attrs: " + offeredAttributes
              + ", tag: " + renderTag(userSuppliedTag));
   }

//	public void requestAttributeOwnershipAssumption (
//		int                theObject,
//		AttributeHandleSet offeredAttributes,
//		byte[]             userSuppliedTag)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		AttributeAlreadyOwned,
//		AttributeNotPublished,
//		FederateInternalError
//	{
//		_userInterface.post("�(7.4)requestAttributeOwnershipAssumption; object:"
//			+ theObject
//      + ", attrs: " + offeredAttributes
//      + ", tag: " + renderTag(userSuppliedTag));
//	}

   // 7.10 -> 7.11
   public void requestAttributeOwnershipRelease(
           ObjectInstanceHandle theObject,
           AttributeHandleSet candidateAttributes,
           byte[] userSuppliedTag)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotOwned,
           FederateInternalError {
      _userInterface.post("�(7.11)requestAttributeOwnershipRelease; object:"
              + theObject
              + ", attrs: " + candidateAttributes
              + ", tag: " + renderTag(userSuppliedTag));
   }

//	public void requestAttributeOwnershipRelease (
//		int                theObject,
//		AttributeHandleSet candidateAttributes,
//		byte[]             userSuppliedTag)
//	throws
//		ObjectNotKnown,
//		AttributeNotKnown,
//		AttributeNotOwned,
//		FederateInternalError
//	{
//		_userInterface.post("�(7.10)requestAttributeOwnershipRelease; object:"
//			+ theObject
//      + ", attrs: " + candidateAttributes
//      + ", tag: " + renderTag(userSuppliedTag));
//	}

	// 4.17
   public void requestFederationRestoreFailed(
           String label)
   throws
           FederateInternalError {
      _userInterface.post("�(4.17)Request federation restore failed, label: " + label);
   }

//	public void requestFederationRestoreFailed (
//		String label,
//		String reason)
//	throws
//		FederateInternalError
//	{
//		_userInterface.post("�(4.17)Request federation restore failed, label: "
//      + label + ", reason: " + reason);
//	}

	// 4.17
	public void requestFederationRestoreSucceeded (
		String label)
	throws
		FederateInternalError
	{
		_userInterface.post("�(4.17)Request federation restore succeeded, label: "
      + label);
	}

   // 8.22
   public void requestRetraction(
           MessageRetractionHandle theHandle)
   throws
           FederateInternalError {
      _userInterface.post("�(8.22)Request retraction, handle: " + theHandle);
   }

//	public void requestRetraction (
//		EventRetractionHandle theHandle)
//	throws
//		EventNotKnown,
//		FederateInternalError
//	{
//		_userInterface.post("�(8.22)Request retraction, handle: " + theHandle);
//	}

	// 5.10
   public void startRegistrationForObjectClass(
           ObjectClassHandle theClass)
   throws
           ObjectClassNotPublished,
           FederateInternalError {
      _userInterface.post("�(5.10)startRegistrationForObjectClass:" + theClass);
   }

//	public void startRegistrationForObjectClass (
//		int theClass)
//	throws
//		ObjectClassNotPublished,
//		FederateInternalError
//	{
//		_userInterface.post("�(5.10)startRegistrationForObjectClass:"
//			+ theClass);
//	}

	// 5.11
   public void stopRegistrationForObjectClass(
           ObjectClassHandle theClass)
   throws
           ObjectClassNotPublished,
           FederateInternalError {
      _userInterface.post("�(5.11)stopRegistrationForObjectClass:" + theClass);
   }

//	public void stopRegistrationForObjectClass (
//		int theClass)
//	throws
//		ObjectClassNotPublished,
//		FederateInternalError
//	{
//		_userInterface.post("�(5.11)stopRegistrationForObjectClass:"
//			+ theClass);
//	}

	//4.7
   public void synchronizationPointRegistrationFailed(
           String synchronizationPointLabel,
           SynchronizationPointFailureReason reason)
   throws
           FederateInternalError {
      _userInterface.post("�(4.7)synchronizationPointRegistrationFailed; label: "
              + synchronizationPointLabel + "; reason: " + reason);
   }

//	public void synchronizationPointRegistrationFailed(
//		String synchronizationPointLabel)
//	throws
//		FederateInternalError
//	{
//		_userInterface.post("�(4.7)synchronizationPointRegistrationFailed; label: "
//			+ synchronizationPointLabel);
//	}

	//4.7
	public void synchronizationPointRegistrationSucceeded(
		String synchronizationPointLabel)
	throws
		FederateInternalError
	{
		_userInterface.post("�(4.7)synchronizationPointRegistrationSucceeded; label: "
			+ synchronizationPointLabel);
	}

	// 8.13
	public void timeAdvanceGrant (
		LogicalTime theTime) 
	throws
           FederateInternalError
	{
		_userInterface.post("�(8.13)time adv grant, time: " + theTime);
	}

	// 8.6
	public void timeConstrainedEnabled (
		LogicalTime theFederateTime) 
	throws
           FederateInternalError
	{
		_userInterface.post("�(8.6)timeConstrainedEnabled, time: "
			+ theFederateTime);
	}

	// 8.3
	public void timeRegulationEnabled (
		LogicalTime theFederateTime)
	throws
           FederateInternalError
	{
		_userInterface.post("�(8.3)timeRegulationEnabled, time: "
			+ theFederateTime);
	}

	// 5.13
   public void turnInteractionsOff(
           InteractionClassHandle theHandle)
   throws
           InteractionClassNotPublished,
           FederateInternalError {
      _userInterface.post("�(5.13)turnInteractionsOff:"
			+ theHandle);
   }

   // 6.3
   public void objectInstanceNameReservationSucceeded(String objectName) throws UnknownName, FederateInternalError {
   }

   public void objectInstanceNameReservationFailed(String objectName) throws UnknownName, FederateInternalError {
   }

//	public void turnInteractionsOff (
//		int theHandle)
//	throws
//		InteractionClassNotPublished,
//		FederateInternalError
//	{
//		_userInterface.post("�(5.13)turnInteractionsOff:"
//			+ theHandle);
//	}

   // 5.12
   public void turnInteractionsOn(
           InteractionClassHandle theHandle)
   throws
           InteractionClassNotPublished,
           FederateInternalError {
      _userInterface.post("�(5.12)turnInteractionsOn:"
			+ theHandle);
   }

//	public void turnInteractionsOn (
//		int theHandle)
//	throws
//		InteractionClassNotPublished,
//		FederateInternalError
//	{
//		_userInterface.post("�(5.12)turnInteractionsOn:"
//			+ theHandle);
//  }

	// 6.20
   public void turnUpdatesOffForObjectInstance(
           ObjectInstanceHandle theObject,
           AttributeHandleSet theAttributes)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotOwned,
           FederateInternalError {
      _userInterface.post("�(6.20)Turn updates off for object instance: "
      + theObject + ", attributes: " + theAttributes);
   }

//	public void turnUpdatesOffForObjectInstance (
//		int                theObject,
//		AttributeHandleSet theAttributes)
//	throws
//		ObjectNotKnown,
//		AttributeNotOwned,
//		FederateInternalError
//	{
//		_userInterface.post("�(6.20)Turn updates off for object instance: "
//      + theObject + ", attributes: " + theAttributes);
//	}

	// 6.19
   public void turnUpdatesOnForObjectInstance(
           ObjectInstanceHandle theObject,
           AttributeHandleSet theAttributes)
   throws
           ObjectInstanceNotKnown,
           AttributeNotRecognized,
           AttributeNotOwned,
           FederateInternalError {
      _userInterface.post("�(6.19)Turn updates on for object instance: "
      + theObject + ", attributes: " + theAttributes);
   }

//	public void turnUpdatesOnForObjectInstance (
//		int                theObject,
//		AttributeHandleSet theAttributes)
//	throws
//		ObjectNotKnown,
//		AttributeNotOwned,
//		FederateInternalError
//	{
//		_userInterface.post("�(6.19)Turn updates on for object instance: "
//      + theObject + ", attributes: " + theAttributes);
//	}

  private String renderTag(byte[] tag) {
    return (tag == null) ? "[null]" : new String(tag);
  }
}
