
package pipe.hla.book.restaurant.transport;


import hla.rti.*;
import se.pitch.prti.*;

//Transport's implementation of FederateAmbassador

public final class FedAmbImpl implements FederateAmbassador {

  private TransportFrame _userInterface;
  private Transport _fed;
  private Barrier _enableTimeConstrainedBarrier = null;
  private Barrier _enableTimeRegulationBarrier = null;
  private Barrier _synchronizationPointRegistrationSucceededBarrier = null;
  private Barrier _federationSynchronizedBarrier = null;


  public void setEnableTimeConstrainedBarrier(Barrier b) {
    _enableTimeConstrainedBarrier = b;
  }

  public void setEnableTimeRegulationBarrier(Barrier b) {
    _enableTimeRegulationBarrier = b;
  }

  public void setFederationSynchronizedBarrier(Barrier b) {
    _federationSynchronizedBarrier = b;
  }

  public void setSynchronizationPointRegistrationSucceededBarrier(Barrier b) {
    _synchronizationPointRegistrationSucceededBarrier = b;
  }

  public FedAmbImpl(
    Transport fed,
    TransportFrame ui)
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
		_fed.recordSynchronizationPointAnnouncement(synchronizationPointLabel);
	}
	// 7.16
	public void attributeIsNotOwned (
		int theObject,    
		int theAttribute) 
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		FederateInternalError
	{
		_userInterface.post("�(7.16)Attribute is not owned, object: " + theObject
      + ", attribute: " + theAttribute);
	}
	// 7.16
	public void attributeOwnedByRTI (
		int theObject,    
		int theAttribute) 
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		FederateInternalError
	{
		_userInterface.post("�(7.16)Attribute owned by RTI, object: " + theObject
      + ", attribute: " + theAttribute);
	}
	// 7.6
	public void attributeOwnershipAcquisitionNotification (
		int                theObject,         
		AttributeHandleSet securedAttributes)
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		AttributeAcquisitionWasNotRequested,
		AttributeAlreadyOwned,
		AttributeNotPublished,
		FederateInternalError
	{
		_fed.queueAttributeOwnershipAcquisitionNotificationCallback(theObject, securedAttributes);
	}
	// 7.5
	public void attributeOwnershipDivestitureNotification (
		int                theObject,          
		AttributeHandleSet releasedAttributes)
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		AttributeNotOwned,
		AttributeDivestitureWasNotRequested,
		FederateInternalError
	{
		_fed.queueAttributeOwnershipDivestitureNotificationCallback(
      theObject,
      releasedAttributes);
	}
	// 7.9
	public void attributeOwnershipUnavailable (
		int                theObject,         
		AttributeHandleSet theAttributes)
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		AttributeAlreadyOwned,
		AttributeAcquisitionWasNotRequested,
		FederateInternalError
	{
		_fed.queueAttributeOwnershipUnavailableCallback(
      theObject,
      theAttributes);
	}
	// 6.13
	public void attributesInScope (
		int                theObject,     
		AttributeHandleSet theAttributes) 
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		FederateInternalError
	{
		_userInterface.post("�(6.15)attributesInScope; object:"
			+ theObject
      + ", attrs: " + theAttributes);
	}
	// 6.14
	public void attributesOutOfScope (
		int                theObject,     
		AttributeHandleSet theAttributes) 
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		FederateInternalError
	{
		_userInterface.post("�(6.16)attributesOutOfScope; object:"
			+ theObject
      + ", attrs: " + theAttributes);
	}
	// 7.14
	public void confirmAttributeOwnershipAcquisitionCancellation (
		   int                theObject,         
		   AttributeHandleSet theAttributes)
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		AttributeAlreadyOwned,
		AttributeAcquisitionWasNotCanceled,
		FederateInternalError
	{
		_userInterface.post("�(7.14)confirmAttributeOwnershipAcquisitionCancellation; object:"
			+ theObject
      + ", attrs: " + theAttributes);
	}

	// 6.5
	public void discoverObjectInstance (
		int    theObject,
		int    theObjectClass,
		String objectName) 
	throws
		CouldNotDiscover,
		ObjectClassNotKnown,
		FederateInternalError
	{
		_fed.queueDiscoverObjectInstanceCallback(
      theObject,
      theObjectClass,
      objectName);
	}
	// 4.21
	public void federationNotRestored ()
	throws
		FederateInternalError
	{
		_userInterface.post("�(4.21)Federation not restored.");
	}
	// 4.15
	public void federationNotSaved ()
	throws
		FederateInternalError
	{
		_userInterface.post("�(4.15)Federation not saved.");
	}
	// 4.18
	public void federationRestoreBegun ()
	throws
		FederateInternalError
	{
		_userInterface.post("�(4.18)Federation restore begun.");
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
		if (_federationSynchronizedBarrier != null) {
      if (_federationSynchronizedBarrier.getSuppliedValue().equals(synchronizationPointLabel)) {
        _federationSynchronizedBarrier.lower(null);
      }
      else {
		_userInterface.post("�(4.10)federationSynchronized at:"
  			+ synchronizationPointLabel);
      }
    }
    else {
      _userInterface.post("ERROR: federationSynchronized with no barrier set");
    }
	}
	// 7.16
	public void informAttributeOwnership (
		int theObject,    
		int theAttribute, 
		int theOwner)     
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		FederateInternalError
	{
		_userInterface.post("�(7.16)informAttributeOwnership; object:"
			+ theObject
      + ", attr: " + theAttribute
      + ", owner: " + theOwner);
	}
	// 4.19
	public void initiateFederateRestore (
		String label,
		int    federateHandle)
	throws
		SpecifiedSaveLabelDoesNotExist,
		CouldNotRestore,
		FederateInternalError
	{
		_userInterface.post("�(4.19)Initiate federate restore.");
	}
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
	public void provideAttributeValueUpdate (
		int                theObject,     
		AttributeHandleSet theAttributes) 
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		AttributeNotOwned,
		FederateInternalError
	{
		_fed.queueProvideAttributeValueUpdateCallback(theObject, theAttributes);
	}
	// 6.9
	public void receiveInteraction (
		int                 interactionClass, 
		ReceivedInteraction theInteraction,  
		byte[]              userSuppliedTag)         
	throws
		InteractionClassNotKnown,
		InteractionParameterNotKnown,
		FederateInternalError
	{
    _fed.queueReceiveInteractionCallback(interactionClass, theInteraction);
	}

	// 6.9
	public void receiveInteraction (
		int                 interactionClass, 
		ReceivedInteraction theInteraction,  
		byte[]              userSuppliedTag,
		LogicalTime         theTime,
		EventRetractionHandle                 eventRetractionHandle)
	throws
		InteractionClassNotKnown,
		InteractionParameterNotKnown,
		InvalidFederationTime,
		FederateInternalError
	{
    try {
      String order = (theInteraction.getOrderType() == RTI.OrderType_Receive)
        ? "receive"
        : "timestamp";
      _userInterface.post("�(6.9)receiveInteraction " + interactionClass
        + ", order: " + order + ", transportation: " + theInteraction.getTransportType()
        + ", tag: " + renderTag(userSuppliedTag));
      _userInterface.post("  time: " + theTime + ", retraction: " +  eventRetractionHandle);
      int paramCount = theInteraction.size();
      for (int p = 0; p < paramCount; ++p) {
        _userInterface.post("  param: " + theInteraction.getParameterHandle(p)
          + " value: " + new String(theInteraction.getValueReference(p)));
      }
    }
    catch (ArrayIndexOutOfBounds e) {
      throw new InteractionParameterNotKnown(e.toString());
    }
	}
	// 6.7
	public void reflectAttributeValues (
		int                 theObject,
		ReflectedAttributes theAttributes,
		byte[]              userSuppliedTag)        
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		FederateOwnsAttributes,
		FederateInternalError
	{
    try {
      _userInterface.post("�(6.7)reflectAttributeValues of obj " + theObject
        + ", tag: " + renderTag(userSuppliedTag));
      int attrCount = theAttributes.size();
      for (int a = 0; a < attrCount; ++a) {
        _userInterface.post("  attr: " + theAttributes.getAttributeHandle(a)
          + " value: " + new String(theAttributes.getValueReference(a)));
      }
    }
    catch (ArrayIndexOutOfBounds e) {
      throw new AttributeNotKnown(e.toString());
    }
	}
	// 6.7
	public void reflectAttributeValues (
		int                 theObject,     
		ReflectedAttributes theAttributes, 
		byte[]              userSuppliedTag,        
		LogicalTime         theTime,
		EventRetractionHandle                 retractionHandle)
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		FederateOwnsAttributes,
		InvalidFederationTime,
		FederateInternalError
	{
    _fed.queueReflectAttributeValuesEvent(
      theTime,
      theObject,
      theAttributes,
      userSuppliedTag);
	}
	// 6.11
	public void removeObjectInstance (
		int    theObject, 
		byte[] userSuppliedTag)
	throws
		ObjectNotKnown,
		FederateInternalError
	{
		_userInterface.post("�(6.11)removeObjectInstance; object:"
			+ theObject + ", tag: " + renderTag(userSuppliedTag));
	}
	// 6.9
	public void removeObjectInstance (
		int            theObject,
		byte[]         userSuppliedTag,
		LogicalTime    theTime,
		EventRetractionHandle            retractionHandle)
	throws
		ObjectNotKnown,
		InvalidFederationTime,
		FederateInternalError
	{
		_fed.queueRemoveObjectInstanceEvent(theObject);
	}

	// 7.4
	public void requestAttributeOwnershipAssumption (
		int                theObject,         
		AttributeHandleSet offeredAttributes,
		byte[]             userSuppliedTag)            
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		AttributeAlreadyOwned,
		AttributeNotPublished,
		FederateInternalError
	{
		_fed.queueRequestAttributeOwnershipAssumptionCallback(
			theObject,
      offeredAttributes,
      userSuppliedTag);
	}
	// 7.10
	public void requestAttributeOwnershipRelease (
		int                theObject,
		AttributeHandleSet candidateAttributes,
		byte[]             userSuppliedTag)
	throws
		ObjectNotKnown,
		AttributeNotKnown,
		AttributeNotOwned,
		FederateInternalError
	{
		_userInterface.post("�(7.10)requestAttributeOwnershipRelease; object:"
			+ theObject
      + ", attrs: " + candidateAttributes
      + ", tag: " + renderTag(userSuppliedTag));
	}
	// 4.17
	public void requestFederationRestoreFailed (
		String label,
		String reason)
	throws
		FederateInternalError
	{
		_userInterface.post("�(4.17)Request federation restore failed, label: "
      + label + ", reason: " + reason);
	}
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
	public void requestRetraction (
		EventRetractionHandle theHandle) 
	throws
		EventNotKnown,
		FederateInternalError
	{
		_userInterface.post("�(8.22)Request retraction, handle: " + theHandle);
	}

	// 5.10
	public void startRegistrationForObjectClass (
		int theClass)      
	throws
		ObjectClassNotPublished,
		FederateInternalError
	{
		_userInterface.post("�(5.10)startRegistrationForObjectClass:"
			+ theClass);
	}
	// 5.11
	public void stopRegistrationForObjectClass (
		int theClass)      
	throws
		ObjectClassNotPublished,
		FederateInternalError
	{
		_userInterface.post("�(5.11)stopRegistrationForObjectClass:"
			+ theClass);
	}

	//4.7
	public void synchronizationPointRegistrationFailed(
		String synchronizationPointLabel)
	throws
		FederateInternalError
	{
		_userInterface.post("�(4.7)synchronizationPointRegistrationFailed; label: "
			+ synchronizationPointLabel);
	}
	//4.7
	public void synchronizationPointRegistrationSucceeded(
		String synchronizationPointLabel)
	throws
		FederateInternalError
	{
		if (_synchronizationPointRegistrationSucceededBarrier != null) {
      if (_synchronizationPointRegistrationSucceededBarrier.getSuppliedValue().equals(synchronizationPointLabel)) {
        _synchronizationPointRegistrationSucceededBarrier.lower(null);
      }
      else {
      _userInterface.post("�(4.7)synchronizationPointRegistrationSucceeded; label: "
  			+ synchronizationPointLabel);
      }
    }
    else {
      _userInterface.post("ERROR: synchronizationPointRegistrationSucceeded with no barrier set");
    }
	}
	// 8.13
	public void timeAdvanceGrant (
		LogicalTime theTime)
	throws
		InvalidFederationTime,
		TimeAdvanceWasNotInProgress,
		FederateInternalError
	{
    _fed.queueGrantEvent(theTime);
	}

	// 8.6
	public void timeConstrainedEnabled (
		LogicalTime theFederateTime) 
	throws
		InvalidFederationTime,
		EnableTimeConstrainedWasNotPending,
		FederateInternalError
	{
		if (_enableTimeConstrainedBarrier != null) {
      Object[] returnedTime = {theFederateTime};
      _enableTimeConstrainedBarrier.lower(returnedTime);
    }
    else {
      _userInterface.post("ERROR: timeConstrainedEnabled with no barrier set");
    }
	}

	// 8.3
	public void timeRegulationEnabled (
		LogicalTime theFederateTime)
	throws
		InvalidFederationTime,
		EnableTimeRegulationWasNotPending,
		FederateInternalError
	{
		if (_enableTimeRegulationBarrier != null) {
      Object[] returnedTime = {theFederateTime};
      _enableTimeRegulationBarrier.lower(returnedTime);
    }
    else {
      _userInterface.post("ERROR: timeRegulationEnabled with no barrier set");
    }
	}
	// 5.13
	public void turnInteractionsOff (
		int theHandle)
	throws
		InteractionClassNotPublished,
		FederateInternalError
	{
		_userInterface.post("�(5.13)turnInteractionsOff:"
			+ theHandle);
	}
	// 5.12
	public void turnInteractionsOn (
		int theHandle) 
	throws
		InteractionClassNotPublished,
		FederateInternalError
	{
		_userInterface.post("�(5.12)turnInteractionsOn:"
			+ theHandle);
  }
	// 6.20
	public void turnUpdatesOffForObjectInstance (
		int                theObject,      
		AttributeHandleSet theAttributes) 
	throws
		ObjectNotKnown,
		AttributeNotOwned,
		FederateInternalError
	{
		_userInterface.post("�(6.20)Turn updates off for object instance: "
      + theObject + ", attributes: " + theAttributes);
	}
	// 6.19
	public void turnUpdatesOnForObjectInstance (
		int                theObject,     
		AttributeHandleSet theAttributes) 
	throws
		ObjectNotKnown,
		AttributeNotOwned,
		FederateInternalError
	{
		_userInterface.post("�(6.19)Turn updates on for object instance: "
      + theObject + ", attributes: " + theAttributes);
	}

  private String renderTag(byte[] tag) {
    return (tag == null) ? "[null]" : new String(tag);
  }
}
