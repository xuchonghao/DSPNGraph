
package pipe.hla.book.test_federate;

import java.util.*;
import java.net.*;
import hla.rti.*;
import se.pitch.prti.*;

public final class TestFederate
{
  //system properties used throughout
  private static String _fileSeparator = System.getProperty("file.separator");
  private static String _userDirectory = System.getProperty("user.dir");

  private TestFederateFrame _userInterface;
  private int _federateHandle; // -1 when not joined
  private FederateAmbassador _fedAmb;
  private String _fedexName;
  private Properties _properties;
  //key: local name (String); value: RegionRecord
  private Hashtable _regions;
  //index: local handle; value: EventRetractionHandle
  private Vector _eventRetractionHandles;
  private int _nextRetractionHandleLocalHandle;

  //things dependent on RTI implementation in use
  private RTIambassador _rti;
  public SuppliedParametersFactory _suppliedParametersFactory;
  public SuppliedAttributesFactory _suppliedAttributesFactory;
  public AttributeHandleSetFactory _attributeHandleSetFactory;
  public FederateHandleSetFactory _federateHandleSetFactory;

  public TestFederate(Properties props)
  {
		_userInterface = new TestFederateFrame();
        _userInterface.finishConstruction(this);
		_userInterface.show();
	
    _properties = props;
    System.out.println("host: " + props.get("RTI_HOST"));
    System.out.println("port: " + props.get("RTI_PORT"));
    System.out.println("config: " + props.get("CONFIG"));
    String lrcPort = (String)props.get("lrcPort");
    if (lrcPort != null)
      System.out.println("LRC port: " + lrcPort);
		//create RTI implementation
      try {
		  if (lrcPort == null) {
        _rti = RTI.getRTIambassador(
          (String)_properties.get("RTI_HOST"),
          Integer.parseInt((String)_properties.get("RTI_PORT")));
      }
      else {
        _rti = RTI.getRTIambassador(
          (String)_properties.get("RTI_HOST"),
          Integer.parseInt((String)_properties.get("RTI_PORT")),
          Integer.parseInt(lrcPort));
      }
        _userInterface.post("RTIambassador created");
        _fedAmb = new FedAmbImpl(this, _userInterface);
        _userInterface.enableRemote();
        _userInterface.setNotJoined();
        _federateHandle = -1;  //not joined

      //do other implementation-specific things
      _suppliedParametersFactory = RTI.suppliedParametersFactory();
      _suppliedAttributesFactory = RTI.suppliedAttributesFactory();
      _attributeHandleSetFactory = RTI.attributeHandleSetFactory();
      _federateHandleSetFactory = RTI.federateHandleSetFactory();

      _regions = new Hashtable(10);

      }catch (Exception e) {
		  _userInterface.post("TestFederate: RTIamb ctor failed: " + e);
		  _userInterface.post("You may as well exit.");
      }
  }

  void associateRegionForUpdates(String regionLocalName, String objectInstanceHandleString, AttributeHandleSet attributes)
  {
    int handle = -1;

    try {
      RegionRecord rr = (RegionRecord)_regions.get(regionLocalName);
      if (rr == null) throw new RegionNotKnown(
        "Region " + regionLocalName + " not defined.");
      handle = Integer.parseInt(objectInstanceHandleString);
      _rti.associateRegionForUpdates(
        rr._region,
        handle,
        attributes);
      _userInterface.post("9.6 Associate region for updates, instance " + handle
        + ", region " + regionLocalName
        + ", attributes " + attributes);
    }
    catch (Exception e) {
      _userInterface.post("EX 9.6 associateRegionForUpdates " + e);
      e.printStackTrace(System.out);
    }
  }

  void attributeOwnershipAcquisition(String objectString, AttributeHandleSet ahset, String tag)
  {
    int objectHandle;
    byte[] tagBytes;

    try {
      objectHandle = Integer.parseInt(objectString);
      tagBytes = tag.getBytes(); //cvt user-supplied tag to bytes
      _rti.attributeOwnershipAcquisition(
        objectHandle,
        ahset,
        tagBytes);
      _userInterface.post("7.7 Attribute ownership acquisition, instance: "
        + objectHandle + ", attrs: " + ahset + ", tag: " + tag);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 7.7 Invalid object instance handle: " + objectString);
    }
    catch (Exception e) {
      _userInterface.post("EX 7.7 Attribute ownership acquisition " + e);
      e.printStackTrace(System.out);
    }
  }


  void attributeOwnershipAcquisitionIfAvailable(String objectString, AttributeHandleSet ahset)
  {
    int objectHandle;

    try {
      objectHandle = Integer.parseInt(objectString);
      _rti.attributeOwnershipAcquisitionIfAvailable(
        objectHandle,
        ahset);
      _userInterface.post("7.8 Attribute ownership acquisition if avail, instance: "
        + objectHandle + ", attrs: " + ahset);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 7.8 Invalid object instance handle: " + objectString);
    }
    catch (Exception e) {
      _userInterface.post("EX 7.8 Attribute ownership acquisition if available " + e);
      e.printStackTrace(System.out);
    }
  }


  void attributeOwnershipReleaseResponse(String objectString, AttributeHandleSet ahset)
  {
    int objectHandle;

    try {
      objectHandle = Integer.parseInt(objectString);
      _rti.attributeOwnershipReleaseResponse(
        objectHandle,
        ahset);
      _userInterface.post("7.11 Attribute ownership release response, instance: "
        + objectHandle + ", attrs: " + ahset);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 7.11 Invalid object instance handle: " + objectString);
    }
    catch (Exception e) {
      _userInterface.post("EX 7.11 Attribute ownership release response " + e);
      e.printStackTrace(System.out);
    }
  }


  void cancelAttributeOwnershipAcquisition(String objectString, AttributeHandleSet ahset)
  {
    int objectHandle;

    try {
      objectHandle = Integer.parseInt(objectString);
      _rti.cancelAttributeOwnershipAcquisition(
        objectHandle,
        ahset);
      _userInterface.post("7.13 Cancel Attribute ownership acquisition, instance: "
        + objectHandle + ", attrs: " + ahset);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 7.13 Invalid object instance handle: " + objectString);
    }
    catch (Exception e) {
      _userInterface.post("EX 7.13 Cancel Attribute ownership acquisition " + e);
      e.printStackTrace(System.out);
    }
  }


  void cancelNegotiatedAttributeOwnershipDivestiture(String objectString, AttributeHandleSet ahset)
  {
    int objectHandle;

    try {
      objectHandle = Integer.parseInt(objectString);
      _rti.cancelNegotiatedAttributeOwnershipDivestiture(
        objectHandle,
        ahset);
      _userInterface.post("7.12 Cancel Negotiated Attribute ownership divestiture, instance: "
        + objectHandle + ", attrs: " + ahset);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 7.12 Invalid object instance handle: " + objectString);
    }
    catch (Exception e) {
      _userInterface.post("EX 7.12 Cancel Negotiated Attribute ownership divestiture " + e);
      e.printStackTrace(System.out);
    }
  }

  void changeAttributeOrderType(String objectString, AttributeHandleSet ahset, String orderString)
  {
    int objectHandle;
    int orderHandle;

    try {
      objectHandle = Integer.parseInt(objectString);
      orderHandle = Integer.parseInt(orderString);
      _rti.changeAttributeOrderType(
        objectHandle,
        ahset,
        orderHandle);
      _userInterface.post("8.23 Change Attribute Order Type, instance: "
        + objectHandle + ", attrs: " + ahset + ", order:" + orderHandle);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 8.23 Invalid handle: " + objectString
        + " or " + orderString);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.23 Change Attribute Order Type " + e);
      e.printStackTrace(System.out);
    }
  }

  void changeAttributeTransportationType(String objectString, AttributeHandleSet ahset, String transportationString)
  {
    int objectHandle;
    int transportationHandle;

    try {
      objectHandle = Integer.parseInt(objectString);
      transportationHandle = Integer.parseInt(transportationString);
      _rti.changeAttributeTransportationType(
        objectHandle,
        ahset,
        transportationHandle);
      _userInterface.post("6.11 Change Attribute Transportation Type, instance: "
        + objectHandle + ", attrs: " + ahset + ", transport:" + transportationHandle);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 6.11 Invalid handle: " + objectString
        + " or " + transportationString);
    }
    catch (Exception e) {
      _userInterface.post("EX 6.11 Change Attribute Transportation Type " + e);
      e.printStackTrace(System.out);
    }
  }

  void changeInteractionTransportationType(String interactionString, String transportationString)
  {
    int interactionHandle;
    int transportationHandle;

    try {
      interactionHandle = Integer.parseInt(interactionString);
      transportationHandle = Integer.parseInt(transportationString);
      _rti.changeInteractionTransportationType(interactionHandle, transportationHandle);
      _userInterface.post("6.12 Change Interaction Transportation Type, instance: "
        + interactionHandle + ", transport:" + transportationHandle);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 6.12 Invalid handle: " + interactionString
        + " or " + transportationString);
    }
    catch (Exception e) {
      _userInterface.post("EX 6.12 Change Interaction Transportation Type " + e);
      e.printStackTrace(System.out);
    }
  }

  void changeInteractionOrderType(String interactionString, String orderString)
  {
    int interactionHandle;
    int orderHandle;

    try {
      interactionHandle = Integer.parseInt(interactionString);
      orderHandle = Integer.parseInt(orderString);
      _rti.changeInteractionOrderType(interactionHandle, orderHandle);
      _userInterface.post("8.24 Change Interaction Order Type, instance: "
        + interactionHandle + ", order:" + orderHandle);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 8.24 Invalid handle: " + interactionString
        + " or " + orderString);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.24 Change Interaction Order Type " + e);
      e.printStackTrace(System.out);
    }
  }


  void createFederationExecution(String[] stuff) {
    try {
      URL fedURL;
      String urlString;
      if (stuff[1].equals("")) { //we're defaulting it
        urlString =
          (String)_properties.get("CONFIG")
          + _fileSeparator
          + stuff[0]
          + ".fed";
      }
      else urlString = stuff[1];
      fedURL = new URL(urlString);
      _userInterface.post("4.2 Starting to create federation execution with FED "
        + urlString + "...");
      _rti.createFederationExecution(stuff[0], fedURL);
      _userInterface.post("Done.");
    }
    catch (MalformedURLException e) {
      _userInterface.post("EX 4.2 CreateFederationExecution: FED path URL malformed:" + e);
    }
    catch (Exception e) {
      _userInterface.post("EX 4.2 CreateFederationExecution: " + e);
    }
  }

  /**
   * the 'class' string is construed as a handle if an integer
   */
  public String getParameterName(String pHandleString, String classString)
  {
    String name = null;
    int classHandle;
    int pHandle;
    try {
      try {
        pHandle = Integer.parseInt(pHandleString);
      }
      catch (NumberFormatException e) {
        _userInterface.post("Invalid parameter handle " + pHandleString);
        return null;
      }
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        name = _rti.getParameterName(pHandle, classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(classString);
        name = _rti.getParameterName(pHandle, classHandle);
      }
      _userInterface.post("10.9 " + name + " for parameter " + pHandleString
        + ", class " + classString + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.9 GetParameterName " + e);
      e.printStackTrace(System.out);
    }
    return name;
  }

  private String[] getRegionList() {
    String[] list = new String[_regions.size()];
    Enumeration keys = _regions.keys();
    int i = 0;
    while (keys.hasMoreElements()) {
      list[i++] = (String)keys.nextElement();
    }
    return list;
  }


  public void createRegion(String[] stuff)
  {
    long bound;
    RegionRecord rr;

    String localName = stuff[0];
    if (_regions.containsKey(localName)) {
      _userInterface.post("Region " + localName + " already defined.");
      return;
    }
    try {
      rr = new RegionRecord();
      rr._localName = stuff[0];
      rr._spaceName = stuff[1];
      rr._spaceHandle = _rti.getRoutingSpaceHandle(stuff[1]);
      if (!stuff[2].equals("")) {
        rr._dimensionNameA = stuff[2];
        rr._dimensionHandleA = _rti.getDimensionHandle(stuff[2], rr._spaceHandle);
        rr._region = _rti.createRegion(rr._spaceHandle, 1);
        bound = Integer.parseInt(stuff[3]);
        rr._region.setRangeLowerBound(0, rr._dimensionHandleA, bound);
        bound = Integer.parseInt(stuff[4]);
        rr._region.setRangeUpperBound(0, rr._dimensionHandleA, bound);
      }
      if (!stuff[5].equals("")) {
        rr._dimensionNameB = stuff[5];
        rr._dimensionHandleB = _rti.getDimensionHandle(stuff[5], rr._spaceHandle);
        bound = Integer.parseInt(stuff[6]);
        rr._region.setRangeLowerBound(0, rr._dimensionHandleB, bound);
        bound = Integer.parseInt(stuff[7]);
        rr._region.setRangeUpperBound(0, rr._dimensionHandleB, bound);
      }
      else rr._dimensionNameB = "";
      if (!stuff[8].equals("")) {
        rr._dimensionNameC = stuff[8];
        rr._dimensionHandleC = _rti.getDimensionHandle(stuff[8], rr._spaceHandle);
        bound = Integer.parseInt(stuff[9]);
        rr._region.setRangeLowerBound(0, rr._dimensionHandleC, bound);
        bound = Integer.parseInt(stuff[10]);
        rr._region.setRangeUpperBound(0, rr._dimensionHandleC, bound);
      }
      else rr._dimensionNameC = "";
      _rti.notifyOfRegionModification(rr._region);
      _regions.put(localName, rr);
      _userInterface.post("9.2 Created region " + localName + ", space " + rr._spaceName);
      _userInterface.post("    dim " + rr._dimensionNameA + "(" + rr._dimensionHandleA + ")"
        + " [" + rr._region.getRangeLowerBound(0, rr._dimensionHandleA)
        + "," +  rr._region.getRangeUpperBound(0, rr._dimensionHandleA) + ")");
      if (!rr._dimensionNameB.equals("")) {
        _userInterface.post("    dim " + rr._dimensionNameB + "(" + rr._dimensionHandleB + ")"
          + " [" + rr._region.getRangeLowerBound(0, rr._dimensionHandleB)
          + "," +  rr._region.getRangeUpperBound(0, rr._dimensionHandleB) + ")");
      }
      if (!rr._dimensionNameC.equals("")) {
        _userInterface.post("    dim " + rr._dimensionNameC + "(" + rr._dimensionHandleC + ")"
          + " [" + rr._region.getRangeLowerBound(0, rr._dimensionHandleC)
          + "," +  rr._region.getRangeUpperBound(0, rr._dimensionHandleC) + ")");
      }
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 9.2 Invalid bound ");
    }
    catch (Exception e) {
      _userInterface.post("EX 9.2 createRegion " + e);
      e.printStackTrace(System.out);
    }
  }

  void deleteObjectInstanceWithTime(String[] stuff) {
    int handle;
    byte[] tagBytes;
    LogicalTimeDouble time;

    try {
      try {
        handle = Integer.parseInt(stuff[0]);
        tagBytes = (stuff[1]).getBytes(); //cvt user-supplied tag to bytes
      }
      catch (NumberFormatException e) {
        _userInterface.post("Invalid object instance handle " + stuff[0]);
        return;
      }
      time = new LogicalTimeDouble(Double.valueOf(stuff[2]).doubleValue());
      EventRetractionHandle erh =
        _rti.deleteObjectInstance(handle, tagBytes, time);
      _userInterface.post("Deleted object instance " + handle + ", time: " + stuff[2]);
      int localHandle = registerRetractionHandle(erh);
      _userInterface.post("  local handle for event retraction: " + localHandle);
    }
    catch (Exception e) {
      _userInterface.post("deleteObjectInstance " + e);
      e.printStackTrace(System.out);
    }
  }

  void deleteObjectInstance(String handleString, String tag) {
    int handle;
    byte[] tagBytes;

    try {
      try {
        handle = Integer.parseInt(handleString);
        tagBytes = tag.getBytes(); //cvt user-supplied tag to bytes
      }
      catch (NumberFormatException e) {
        _userInterface.post("Invalid object instance handle " + handleString);
        return;
      }
      _rti.deleteObjectInstance(handle, tagBytes);
      _userInterface.post("6.8 Deleted object instance " + handle + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 6.8 deleteObjectInstance " + e);
      e.printStackTrace(System.out);
    }
  }

  void deleteRegion(String[] stuff) {
    RegionRecord rr = (RegionRecord)_regions.remove(stuff[0]);
    if (rr == null) {
      _userInterface.post("Region " + stuff[0] + " not defined.");
      return;
    }
    try {
      _rti.deleteRegion(rr._region);
      _userInterface.post("9.4 Deleted region " + stuff[0]);
    }
    catch (Exception e) {
      _userInterface.post("EX 9.4 deleteRegion " + e);
      e.printStackTrace(System.out);
    }
  }


  void destroyFederationExecution(String fedexName) {
    try {
      _rti.destroyFederationExecution(fedexName);
      _userInterface.post("4.3 Fed ex " + fedexName + " destroyed.");
    }
    catch (Exception e) {
      _userInterface.post("EX 4.3 DestroyFederationExecution: " + e);
    }
  }

  public void disableAsynchronousDelivery() {
    try {
      _rti.disableAsynchronousDelivery();
      _userInterface.post("8.15 asynchronous delivery disabled.");
    }
    catch (Exception e) {
      _userInterface.post("EX 8.15 disableAsynchronousDelivery " + e);
    }
  }

  public void disableAttributeRelevanceAdvisorySwitch() {
    try {
      _rti.disableAttributeRelevanceAdvisorySwitch();
      _userInterface.post("10.26 attribute relevance advisories disabled.");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.26 disableAttributeRelevanceAdvisorySwitch " + e);
    }
  }

  public void disableAttributeScopeAdvisorySwitch() {
    try {
      _rti.disableAttributeScopeAdvisorySwitch();
      _userInterface.post("10.28 attribute scope advisories disabled.");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.28 disableAttributeScopeAdvisorySwitch " + e);
    }
  }

  public void disableClassRelevanceAdvisorySwitch() {
    try {
      _rti.disableClassRelevanceAdvisorySwitch();
      _userInterface.post("10.24 Class relevance advisory switch disabled.");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.24 disableClassRelevanceAdvisorySwitch " + e);
    }
  }

  public void disableInteractionRelevanceAdvisorySwitch() {
    try {
      _rti.disableInteractionRelevanceAdvisorySwitch();
      _userInterface.post("10.30 Interaction relevance advisory switch disabled.");
    }
    catch (Exception e) {
      _userInterface.post("10.30 disableInteractionRelevanceAdvisorySwitch " + e);
    }
  }

  public void disableTimeConstrained() {
    try {
      _rti.disableTimeConstrained();
      _userInterface.post("8.7 disableTimeConstrained.");
    }
    catch (Exception e) {
      _userInterface.post("EX 8.7 disableTimeConstrained " + e);
    }
  }

  public void disableTimeRegulation() {
    try {
      _rti.disableTimeRegulation();
      _userInterface.post("8.4 disableTimeRegulating.");
    }
    catch (Exception e) {
      _userInterface.post("EX 8.4 disableTimeRegulating " + e);
    }
  }

  public void enableAsynchronousDelivery() {
    try {
      _rti.enableAsynchronousDelivery();
      _userInterface.post("8.14 asynchronous delivery enabled.");
    }
    catch (Exception e) {
      _userInterface.post("EX 8.14 enableAsynchronousDelivery " + e);
    }
  }

  public void enableAttributeRelevanceAdvisorySwitch() {
    try {
      _rti.enableAttributeRelevanceAdvisorySwitch();
      _userInterface.post("10.25 attribute relevance advisories enabled.");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.25 enableAttributeRelevanceAdvisorySwitch " + e);
    }
  }

  public void enableAttributeScopeAdvisorySwitch() {
    try {
      _rti.enableAttributeScopeAdvisorySwitch();
      _userInterface.post("10.27 attribute scope advisories enabled.");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.27 enableAttributeScopeAdvisorySwitch " + e);
    }
  }

  public void enableClassRelevanceAdvisorySwitch() {
    try {
      _rti.enableClassRelevanceAdvisorySwitch();
      _userInterface.post("10.23 Class relevance advisory switch enabled.");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.23 enableClassRelevanceAdvisorySwitch " + e);
    }
  }

  public void enableInteractionRelevanceAdvisorySwitch() {
    try {
      _rti.enableInteractionRelevanceAdvisorySwitch();
      _userInterface.post("10.29 Interaction relevance advisory switch enabled.");
    }
    catch (Exception e) {
      _userInterface.post("10.29 enableInteractionRelevanceAdvisorySwitch " + e);
    }
  }

  void enableTimeRegulation(String[] stuff) {
    LogicalTimeDouble time;
    LogicalTimeInterval lookahead;
    try {
      time = new LogicalTimeDouble(Double.valueOf(stuff[0]).doubleValue());
      lookahead =
        new LogicalTimeIntervalDouble(Double.valueOf(stuff[1]).doubleValue());
      _rti.enableTimeRegulation(time, lookahead);
      _userInterface.post("8.2 Enable time regulation, time: " + stuff[0]
        + ", lookahead: " + stuff[1]);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 8.2 Invalid time: " + stuff[0]
        + " or lookahead: " + stuff[1]);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.2 enableTimeRegulation " + e);
      e.printStackTrace(System.out);
    }
  }

  void enableTimeConstrained() {
    try {
      _rti.enableTimeConstrained();
      _userInterface.post("8.5 Enable time contraint");
    }
    catch (Exception e) {
      _userInterface.post("EX 8.5 enableTimeConstrained " + e);
      e.printStackTrace(System.out);
    }
  }

  public void federateRestoreComplete() {
    try {
      _rti.federateRestoreComplete();
      _userInterface.post("4.20 federate restore complete.");
    }
    catch (Exception e) {
      _userInterface.post("EX 4.20 federateRestoreComplete " + e);
    }
  }

  public void federateRestoreNotComplete() {
    try {
      _rti.federateRestoreNotComplete();
      _userInterface.post("4.20 federate restore not complete.");
    }
    catch (Exception e) {
      _userInterface.post("EX 4.20 federateRestoreNotComplete " + e);
    }
  }

  public void federateSaveBegun() {
    try {
      _rti.federateSaveBegun();
      _userInterface.post("4.13 federate save begun.");
    }
    catch (Exception e) {
      _userInterface.post("EX 4.13 federateSaveBegun " + e);
    }
  }

  public void federateSaveComplete() {
    try {
      _rti.federateSaveComplete();
      _userInterface.post("4.14 federate save complete.");
    }
    catch (Exception e) {
      _userInterface.post("EX 4.14 federateSaveComplete " + e);
    }
  }

  public void federateSaveNotComplete() {
    try {
      _rti.federateSaveNotComplete();
      _userInterface.post("4.14 federate save not complete.");
    }
    catch (Exception e) {
      _userInterface.post("EX 4.14 federateSaveNotComplete " + e);
    }
  }

  void flushQueueRequest(String[] stuff) {
    LogicalTimeDouble time;
    try {
      time = new LogicalTimeDouble(Double.valueOf(stuff[0]).doubleValue());
      _rti.flushQueueRequest(time);
      _userInterface.post("8.12 flushQueueRequest, time: " + stuff[0]);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 8.12 Invalid time: " + stuff[0]);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.12 flushQueueRequest " + e);
      e.printStackTrace(System.out);
    }
  }

  /**
   * the 'class' string is construed as a handle if an integer
   */
   public int getAttributeHandle(String attrName, String classString)
  {
    int handle = -1;
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        handle = _rti.getAttributeHandle(attrName, classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
        handle = _rti.getAttributeHandle(attrName, classHandle);
      }
      _userInterface.post("10.4 Handle " + handle + " for attribute " + attrName
        + ", class " + classString + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.4 GetAttributeHandle " + e);
      e.printStackTrace(System.out);
    }
    return handle;
  }
  /**
   * the 'class' string is construed as a handle if an integer
   */
  public String
  getAttributeName(String attrHandleString, String classString)
  {
    String name = null;
    int classHandle;
    int attrHandle;
    try {
      try {
        attrHandle = Integer.parseInt(attrHandleString);
      }
      catch (NumberFormatException e) {
        _userInterface.post("Invalid attribute handle " + attrHandleString);
        return null;
      }
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        name = _rti.getAttributeName(attrHandle, classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
        name = _rti.getAttributeName(attrHandle, classHandle);
      }
      _userInterface.post("10.5 " + name + " for attribute " + attrHandleString
        + ", class " + classString + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.5 GetAttributeName " + e);
      e.printStackTrace(System.out);
    }
    return name;
  }

  /**
   * the 'class' string is construed as a handle if an integer
   */
   public int getAttributeRoutingSpaceHandle(String attrHandleString, String classString)
  {
    String name = null;
    int classHandle;
    int attrHandle;
    int routingSpaceHandle = -1;
    try {
      try {
        attrHandle = Integer.parseInt(attrHandleString);
      }
      catch (NumberFormatException e) {
        _userInterface.post("Invalid attribute handle " + attrHandleString);
        return -1;
      }
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        routingSpaceHandle = _rti.getAttributeRoutingSpaceHandle(attrHandle, classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
        routingSpaceHandle = _rti.getAttributeRoutingSpaceHandle(attrHandle, classHandle);
      }
      _userInterface.post("10.16 Routing space " + routingSpaceHandle + " for attribute "
        + attrHandleString + ", class " + classString + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.16 GetAttributeName " + e);
      e.printStackTrace(System.out);
    }
    return routingSpaceHandle;
  }

  public int getInteractionClassHandle(String name)
  {
  	int handle = -1;
    try {
      handle = _rti.getInteractionClassHandle(name);
      _userInterface.post("10.6 Handle " + handle + " for interaction class " + name + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.6 GetInteractionClassHandle " + e);
      e.printStackTrace(System.out);
    }
    return handle;
  }

/**
 * the 'class' string is construed as a handle
 */
 public String getInteractionClassName(String iclass)
  {
    String name = null;
    int classHandle;
    try {
      //Is the String numeric?
      try {
        classHandle = Integer.parseInt(iclass);
        name = _rti.getInteractionClassName(classHandle);
        _userInterface.post("10.7 Name " + name + " for interaction class "
          + classHandle);
      }
      catch (NumberFormatException e) {
        _userInterface.post("EX 10.7 Invalid handle: " + iclass);
        return null;
      }
    }
    catch (Exception e) {
      _userInterface.post("EX 10.7 GetInteractionClassName " + e);
      e.printStackTrace(System.out);
    }
    return name;
  }

/**
 * the 'class' string is construed as a handle
 */
 public int getInteractionRoutingSpaceHandle(String iclass)
  {
    int routingSpaceHandle = -1;
    int classHandle;
    try {
      //Is the String numeric?
      try {
        classHandle = Integer.parseInt(iclass);
        routingSpaceHandle = _rti.getInteractionRoutingSpaceHandle(classHandle);
        _userInterface.post("10.18 Routing space handle " + routingSpaceHandle
          + " for interaction class " + classHandle);
      }
      catch (NumberFormatException e) {
        _userInterface.post("EX 10.18 Invalid handle: " + iclass);
        return routingSpaceHandle;
      }
    }
    catch (Exception e) {
      _userInterface.post("EX 10.18 GetInteractionClassName " + e);
      e.printStackTrace(System.out);
    }
    return routingSpaceHandle;
  }

  public void getObjectClass(String object) {
  	int instanceHandle;
    int classHandle;
    try {
      //Is the String numeric?
      try {
        instanceHandle = Integer.parseInt(object);
      }
      catch (NumberFormatException e) {
        //name supplied: ask RTI for instance handle
        instanceHandle = _rti.getObjectInstanceHandle(object);
      }
      classHandle = _rti.getObjectClass(instanceHandle);
      _userInterface.post("10.17 Instance " + object + " known as class "
        + classHandle);
    }
    catch (Exception e) {
      _userInterface.post("EX 10.17 GetObjectClass " + e);
      e.printStackTrace(System.out);
    }
  }

  public int getObjectClassHandle(String name)
  {
  	int handle = -1;
    try {
      handle = _rti.getObjectClassHandle(name);
      _userInterface.post("10.2 Handle " + handle + " for object class " + name + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.2 GetObjectClassHandle " + e);
      e.printStackTrace(System.out);
    }
    return handle;
  }

  /**
 * the 'class' string is construed as a handle
 */
 public String getObjectClassName(String oclass)
  {
    String name = null;
    int classHandle;
    try {
      //Is the String numeric?
      try {
        classHandle = Integer.parseInt(oclass);
        name = _rti.getObjectClassName(classHandle);
        _userInterface.post("10.3 Name " + name + " for object class "
          + classHandle);
      }
      catch (NumberFormatException e) {
        _userInterface.post("EX 10.3 Invalid handle: " + oclass);
        return null;
      }
    }
    catch (Exception e) {
      _userInterface.post("EX 10.3 GetObjectClassName " + e);
      e.printStackTrace(System.out);
    }
    return name;
  }

  public void getObjectInstanceName(String hstring) {
    String name = null;
    int handle;
    try {
      //Is the String numeric?
      try {
        handle = Integer.parseInt(hstring);
        name = _rti.getObjectInstanceName(handle);
        _userInterface.post("10.11 Name " + name + " for object instance "
          + hstring);
      }
      catch (NumberFormatException e) {
        _userInterface.post("EX 10.11 Invalid handle: " + hstring);
      }
    }
    catch (Exception e) {
      _userInterface.post("EX 10.11 GetObjectInstanceName " + e);
      e.printStackTrace(System.out);
    }
  }

  public void getObjectInstanceHandle(String name) {
  	int handle = -1;
    try {
      handle = _rti.getObjectInstanceHandle(name);
      _userInterface.post("10.10 Handle " + handle + " for object instance " + name + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.10 GetObjectInstanceHandle " + e);
      e.printStackTrace(System.out);
    }
  }

  public int getOrderingHandle(String name)
  {
  	int handle = -1;
  	try {
  	  handle = _rti.getOrderingHandle(name);
    	_userInterface.post("10.21 Handle " + handle + " for ordering " + name + ".");
  	}
  	catch (Exception e) {
  	  _userInterface.post("EX 10.21 getOrderingHandle " + e);
  	  e.printStackTrace(System.out);
  	}
  	return handle;
  }

  public String getOrderingName(String handleString)
  {
    String name = null;
    int orderingHandle;
    try {
      //Is the String numeric?
      try {
        orderingHandle = Integer.parseInt(handleString);
        name = _rti.getOrderingName(orderingHandle);
        _userInterface.post("10.22 Name " + name + " for ordering "
          + orderingHandle);
      }
      catch (NumberFormatException e) {
        _userInterface.post("EX 10.22 Invalid handle: " + handleString);
        return null;
      }
    }
    catch (Exception e) {
      _userInterface.post("EX 10.22 getOrderingName " + e);
      e.printStackTrace(System.out);
    }
    return name;
  }

  /**
   * the 'class' string is construed as a handle if an integer
   */
   public int getParameterHandle(String paramName, String classString)
  {
    int handle = -1;
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        handle = _rti.getParameterHandle(paramName, classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(classString);
        handle = _rti.getParameterHandle(paramName, classHandle);
      }
      _userInterface.post("10.8 Handle " + handle + " for parameter " + paramName
        + ", class " + classString + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.8 GetParameterHandle " + e);
      e.printStackTrace(System.out);
    }
    return handle;
  }

  private EventRetractionHandle getRetractionHandle(int localHandle)
  throws ArrayIndexOutOfBoundsException
  {
    return (EventRetractionHandle)_eventRetractionHandles.elementAt(localHandle);
  }


  public int getRoutingSpaceHandle(String name)
  {
  	int handle = -1;
  	try {
  	  handle = _rti.getRoutingSpaceHandle(name);
    	_userInterface.post("10.12 Handle " + handle + " for routing space " + name + ".");
  	}
  	catch (Exception e) {
  	  _userInterface.post("EX 10.12 GetRoutingSpaceHandle " + e);
  	  e.printStackTrace(System.out);
  	}
  	return handle;
  }


  public int getDimensionHandle(String name, String spaceName)
  {
    int handle = -1;
    int spaceHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        spaceHandle = Integer.parseInt(spaceName);
        handle = _rti.getDimensionHandle(name, spaceHandle);
      }
      catch (NumberFormatException e) {
        spaceHandle = _rti.getRoutingSpaceHandle(spaceName);
        handle = _rti.getDimensionHandle(name, spaceHandle);
      }
      _userInterface.post("10.14 Handle " + handle + " for dimension " + name
        + ", space " + spaceName + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.14 GetDimensionHandle " + e);
      e.printStackTrace(System.out);
    }
    return handle;
  }
  /**
   * the 'space' string is construed as a handle if an integer
   */
  public String
  getDimensionName(String dimensionHandleString, String spaceString)
  {
    String name = null;
    int spaceHandle;
    int dimensionHandle;
    try {
      try {
        dimensionHandle = Integer.parseInt(dimensionHandleString);
      }
      catch (NumberFormatException e) {
        _userInterface.post("EX 10.14 Invalid dimension handle " + dimensionHandleString);
        return null;
      }
      //Is the 'space' a handle or name?
      try {
        spaceHandle = Integer.parseInt(spaceString);
        name = _rti.getDimensionName(dimensionHandle, spaceHandle);
      }
      catch (NumberFormatException e) {
        spaceHandle = _rti.getRoutingSpaceHandle(spaceString);
        name = _rti.getDimensionName(dimensionHandle, spaceHandle);
      }
      _userInterface.post("10.14 " + name + " for dimension " + dimensionHandleString
        + ", space " + spaceString + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 10.14 GetDimensionName " + e);
      e.printStackTrace(System.out);
    }
    return name;
  }


  public String getRoutingSpaceName(String handleString)
  {
    String name = null;
    int spaceHandle;
    try {
      //Is the String numeric?
      try {
        spaceHandle = Integer.parseInt(handleString);
        name = _rti.getRoutingSpaceName(spaceHandle);
        _userInterface.post("10.13 Name " + name + " for routing space "
          + spaceHandle);
      }
      catch (NumberFormatException e) {
        _userInterface.post("EX 10.13 Invalid handle: " + handleString);
        return null;
      }
    }
    catch (Exception e) {
      _userInterface.post("EX 10.13 getRoutingSpaceName " + e);
      e.printStackTrace(System.out);
    }
    return name;
  }

  public int getTransportationHandle(String name)
  {
  	int handle = -1;
  	try {
  	  handle = _rti.getTransportationHandle(name);
    	_userInterface.post("10.19 Handle " + handle + " for transportation " + name + ".");
  	}
  	catch (Exception e) {
  	  _userInterface.post("EX 10.19 getTransportationHandle " + e);
  	  e.printStackTrace(System.out);
  	}
  	return handle;
  }

  public String getTransportationName(String handleString)
  {
    String name = null;
    int transportationHandle;
    try {
      //Is the String numeric?
      try {
        transportationHandle = Integer.parseInt(handleString);
        name = _rti.getTransportationName(transportationHandle);
        _userInterface.post("10.20 Name " + name + " for transportation "
          + transportationHandle);
      }
      catch (NumberFormatException e) {
        _userInterface.post("EX 10.20 Invalid handle: " + handleString);
        return null;
      }
    }
    catch (Exception e) {
      _userInterface.post("EX 10.20 getTransportationName " + e);
      e.printStackTrace(System.out);
    }
    return name;
  }

  void isAttributeOwnedByFederate(String[] stuff)
  {
    int objectHandle;
    int attributeHandle;

    try {
      objectHandle = Integer.parseInt(stuff[0]);
      attributeHandle = Integer.parseInt(stuff[1]);
      boolean indicator = _rti.isAttributeOwnedByFederate(
        objectHandle,
        attributeHandle);
      _userInterface.post("7.17 Attribute " + attributeHandle + " of object "
        + objectHandle + (indicator ? " is" : " is not") + " owned by federate.");
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 7.17 Invalid handle: " + stuff[0]
        + " or " + stuff[1]);
    }
    catch (Exception e) {
      _userInterface.post("EX 7.17 isAttributeOwnedByFederate " + e);
      e.printStackTrace(System.out);
    }
  }


  void joinFederationExecution(String[] args) {
    if (_federateHandle >= 0) {
      _userInterface.post("Federate is joined already.");
      return;
    }
    try {
      _federateHandle = _rti.joinFederationExecution(
        args[1], 
        args[0],
        _fedAmb);
      _userInterface.setJoined(args[0], _federateHandle, args[1]);
      _userInterface.post("4.4 Federate " + args[1] + " joined "
        + args[0] + " as " + _federateHandle);
      _fedexName = args[0];
      _nextRetractionHandleLocalHandle = 0;
      _eventRetractionHandles = new Vector(10);
    }
    catch (Exception e) {
      _userInterface.post("EX 4.4 JoinFederationExecution: " + e);
    }
  }

  void modifyLookahead(String[] stuff) {
    LogicalTimeIntervalDouble la;
    try {
      la = new LogicalTimeIntervalDouble(Double.valueOf(stuff[0]).doubleValue());
      _rti.modifyLookahead(la);
      _userInterface.post("8.19 modify lookahead, new lookahead: " + stuff[0]);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 8.19 Invalid lookahead: " + stuff[0]);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.19 modifyLookahead " + e);
      e.printStackTrace(System.out);
    }
  }

  void localDeleteObjectInstance(String[] stuff) {
    int handle;

    try {
      try {
        handle = Integer.parseInt(stuff[0]);
      }
      catch (NumberFormatException e) {
        _userInterface.post("Invalid object instance handle " + stuff[0]);
        return;
      }
      _rti.localDeleteObjectInstance(handle);
      _userInterface.post("6.10 Local delete object instance " + handle + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 6.10 localDeleteObjectInstance " + e);
      e.printStackTrace(System.out);
    }
  }

  public String[] lookUpRegion(String[] stuff) {
    RegionRecord rr = (RegionRecord)_regions.get(stuff[0]);
    if (rr == null) {
      _userInterface.post("EX 9.3 Modify region: region " + stuff[0] +
        " not defined.");
      return null;
    }
    String[] currentValues = new String[11];
    try {
      currentValues[0] = rr._localName;
      currentValues[1] = rr._spaceName;
      currentValues[2] = rr._dimensionNameA;
      currentValues[3] =
        Long.toString(rr._region.getRangeLowerBound(0, rr._dimensionHandleA));
      currentValues[4] =
        Long.toString(rr._region.getRangeUpperBound(0, rr._dimensionHandleA));
      currentValues[5] = rr._dimensionNameB;
      if (!rr._dimensionNameB.equals("")) {
        currentValues[6] =
          Long.toString(rr._region.getRangeLowerBound(0, rr._dimensionHandleB));
        currentValues[7] =
          Long.toString(rr._region.getRangeUpperBound(0, rr._dimensionHandleB));
      }
      currentValues[8] = rr._dimensionNameC;
      if (!rr._dimensionNameC.equals("")) {
        currentValues[9] =
          Long.toString(rr._region.getRangeLowerBound(0, rr._dimensionHandleC));
        currentValues[10] =
          Long.toString(rr._region.getRangeUpperBound(0, rr._dimensionHandleC));
      }
    }
    catch (Exception e) {
      _userInterface.post("EX 9.3 Modify region: " + e);
      currentValues = null;
    }
    return currentValues;
  }


  public static void main(String args[]) {
	  Properties props = parseArgs(args);
	  new TestFederate(props);
  }

  public void modifyRegion(String[] stuff)
  {
    long bound;
    RegionRecord rr = (RegionRecord)_regions.get(stuff[0]);

    try {
      bound = Integer.parseInt(stuff[3]);
      rr._region.setRangeLowerBound(0, rr._dimensionHandleA, bound);
      bound = Integer.parseInt(stuff[4]);
      rr._region.setRangeUpperBound(0, rr._dimensionHandleA, bound);
      if (!rr._dimensionNameB.equals("")) {
        bound = Integer.parseInt(stuff[6]);
        rr._region.setRangeLowerBound(0, rr._dimensionHandleB, bound);
        bound = Integer.parseInt(stuff[7]);
        rr._region.setRangeUpperBound(0, rr._dimensionHandleB, bound);
      }
      if (!rr._dimensionNameC.equals("")) {
        bound = Integer.parseInt(stuff[9]);
        rr._region.setRangeLowerBound(0, rr._dimensionHandleC, bound);
        bound = Integer.parseInt(stuff[10]);
        rr._region.setRangeUpperBound(0, rr._dimensionHandleC, bound);
      }
      _rti.notifyOfRegionModification(rr._region);
      _userInterface.post("9.3 Modified region " + rr._localName + ", space " + rr._spaceName);
      _userInterface.post("    dim " + rr._dimensionNameA + "(" + rr._dimensionHandleA + ")"
        + " [" + rr._region.getRangeLowerBound(0, rr._dimensionHandleA)
        + "," +  rr._region.getRangeUpperBound(0, rr._dimensionHandleA) + ")");
      if (!rr._dimensionNameB.equals("")) {
        _userInterface.post("    dim " + rr._dimensionNameB + "(" + rr._dimensionHandleB + ")"
          + " [" + rr._region.getRangeLowerBound(0, rr._dimensionHandleB)
          + "," +  rr._region.getRangeUpperBound(0, rr._dimensionHandleB) + ")");
      }
      if (!rr._dimensionNameC.equals("")) {
        _userInterface.post("    dim " + rr._dimensionNameC + "(" + rr._dimensionHandleC + ")"
          + " [" + rr._region.getRangeLowerBound(0, rr._dimensionHandleC)
          + "," +  rr._region.getRangeUpperBound(0, rr._dimensionHandleC) + ")");
      }
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 9.3 Invalid bound ");
    }
    catch (Exception e) {
      _userInterface.post("EX 9.3 modifyRegion " + e);
      e.printStackTrace(System.out);
    }
  }


  void negotiatedAttributeOwnershipDivestiture(
    String objectString,
    AttributeHandleSet ahset,
    String tag)
  {
    int objectHandle;
    byte[] tagBytes;

    try {
        objectHandle = Integer.parseInt(objectString);
        tagBytes = tag.getBytes(); //cvt user-supplied tag to bytes
        _rti.negotiatedAttributeOwnershipDivestiture(
          objectHandle,
          ahset,
          tagBytes);
        _userInterface.post("7.3 Negotiated attribute ownership divestiture, instance: "
          + objectHandle + ", attrs: " + ahset + ", tag: " + tag);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 7.3 Invalid object instance handle: " + objectString);
    }
    catch (Exception e) {
      _userInterface.post("EX 7.3 Negotiated attribute ownership divestiture " + e);
      e.printStackTrace(System.out);
    }
  }

  void nextEventRequest(String[] stuff) {
    LogicalTimeDouble time;
    try {
      time = new LogicalTimeDouble(Double.valueOf(stuff[0]).doubleValue());
      _rti.nextEventRequest(time);
      _userInterface.post("8.10 nextEventRequest, time: " + stuff[0]);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 8.10 Invalid time: " + stuff[0]);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.10 nextEventRequest " + e);
      e.printStackTrace(System.out);
    }
  }

  void nextEventRequestAvailable(String[] stuff) {
    LogicalTimeDouble time;
    try {
      time = new LogicalTimeDouble(Double.valueOf(stuff[0]).doubleValue());
      _rti.nextEventRequestAvailable(time);
      _userInterface.post("8.11 nextEventRequestAvailable, time: " + stuff[0]);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 8.11 Invalid time: " + stuff[0]);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.11 nextEventRequestAvailable " + e);
      e.printStackTrace(System.out);
    }
  }


  private static Properties parseArgs(String args[]) {
    Properties props = new Properties();
    //default values
    try {
      //host name for Central RTI Component
      String defaultRTIhost = InetAddress.getLocalHost().getHostName();
      String cmdHost = System.getProperty("RTI_HOST");
      //System.out.println("Cmd line had " + cmdHost);
      if (cmdHost == null) props.put("RTI_HOST", defaultRTIhost);
      else props.put("RTI_HOST", cmdHost);

      //port number for Central RTI component
      String defaultRTIport = "8989";
      String cmdPort = System.getProperty("RTI_PORT");
      //System.out.println("Cmd line had " + cmdPort);
      if (cmdPort == null) props.put("RTI_PORT", defaultRTIport);
      else props.put("RTI_PORT", cmdPort);

      //port number for Local RTI component: default is absence
      String lrcPort = System.getProperty("lrcPort");
      if (lrcPort != null) {
        props.put("lrcPort", lrcPort);
        System.out.println("LRC port is " + lrcPort);
      }

      //form URL
      String urlString = System.getProperty("CONFIG",
        "file:" + _userDirectory + _fileSeparator + "config");
      props.put("CONFIG", urlString);
      //System.out.println("Config URL: " + props.get("CONFIG"));
    }
    catch (UnknownHostException e) {
      System.out.println("TestFederate.parseArgs: default arguments failed: " + e);
      System.exit(1);
    }
    return props;
  }


  void publishInteractionClass(
    String classString)
  {
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.publishInteractionClass(classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(classString);
        _rti.publishInteractionClass(classHandle);
      }
      _userInterface.post("5.4 Published interaction class " + classHandle);
    }
    catch (Exception e) {
      _userInterface.post("EX 5.4 publishInteractionClass " + e);
      e.printStackTrace(System.out);
    }
  }


  void publishObjectClass(
    String classString,
    AttributeHandleSet ahset)
  {
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.publishObjectClass(classHandle, ahset);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
        _rti.publishObjectClass(classHandle, ahset);
      }
      _userInterface.post("5.2 Published class " + classHandle + " " + ahset);
    }
    catch (Exception e) {
      _userInterface.post("EX 5.2 PublishObjectClassAttributes " + e);
      e.printStackTrace(System.out);
    }
  }

  public void registerFederationSynchronizationPoint(
    String label,
    byte[] tag)
  {
    try {
      _rti.registerFederationSynchronizationPoint(
        label,
        tag);
      _userInterface.post("4.6 RegisterFederationSynchronizationPoint " + label
        + ", tag: " + new String(tag));
    }
    catch (Exception e) {
      _userInterface.post("EX 4.6 RegisterFederationSynchronizationPoint: " + e);
      e.printStackTrace(System.out);
    }
  }

  public void queryAttributeOwnership(String attrString, String objectString)
  {
    int attrHandle;
    int objectHandle;
    try {
      objectHandle = Integer.parseInt(objectString);
      attrHandle = Integer.parseInt(attrString);
      _rti.queryAttributeOwnership(objectHandle, attrHandle);
      _userInterface.post("7.15 Query ownership, instance " + objectHandle
        + " for attribute " + attrHandle);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 7.15 Invalid attribute handle (" + attrString
        + ") or instance handle (" +  objectString + ")");
    }
    catch (Exception e) {
      _userInterface.post("EX 7.15 queryAttributeOwnership " + e);
      e.printStackTrace(System.out);
    }
  }

  public void queryFederateTime() {
    try {
      LogicalTime time = _rti.queryFederateTime();
      _userInterface.post("8.17 Federate time:" + time);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.17 queryFederateTime " + e);
    }
  }

  public void queryLBTS() {
    try {
      LogicalTime lbts = _rti.queryLBTS();
      _userInterface.post("8.16 LBTS:" + lbts);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.16 queryLBTS " + e);
    }
  }

  public void queryLookahead() {
    try {
      LogicalTimeInterval la = _rti.queryLookahead();
      _userInterface.post("8.20 Lookahead:" + la);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.20 queryLookahead " + e);
    }
  }

  public void queryMinimumNextEventTime() {
    try {
      LogicalTime time = _rti.queryMinNextEventTime();
      _userInterface.post("8.18 Minimum next event time:" + time);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.18 queryMinimumNextEventTime " + e);
    }
  }

  public void registerFederationSynchronizationPoint(
    String label,
    byte[] tag,
    FederateHandleSet fhset)
  {
    try {
      _rti.registerFederationSynchronizationPoint(
        label,
        tag,
        fhset);
      _userInterface.post("4.6 RegisterFederationSynchronizationPoint " + label
        + " for federates " + fhset + ", tag: " + new String(tag));
    }
    catch (Exception e) {
      _userInterface.post("EX 4.6 RegisterFederationSynchronizationPoint: " + e);
      e.printStackTrace(System.out);
    }
  }

  //return local handle
  private int registerRetractionHandle(EventRetractionHandle erh) {
    _eventRetractionHandles.addElement(erh);
    return _nextRetractionHandleLocalHandle++;
  }

  void requestClassAttributeValueUpdate(
    String objectString,
    AttributeHandleSet ahset)
  {
    int objectHandle;

    try {
      objectHandle = Integer.parseInt(objectString);
      _rti.requestClassAttributeValueUpdate(
        objectHandle,
        ahset);
      _userInterface.post("6.15 requestClassAttributeValueUpdate, instance: "
        + objectHandle + ", attrs: " + ahset);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 6.15 Invalid handle: " + objectString);
    }
    catch (Exception e) {
      _userInterface.post("EX 6.15 requestClassAttributeValueUpdate " + e);
      e.printStackTrace(System.out);
    }
  }

  void requestInstanceAttributeValueUpdate(
    String objectString,
    AttributeHandleSet ahset)
  {
    int objectHandle;

    try {
      objectHandle = Integer.parseInt(objectString);
      _rti.requestObjectAttributeValueUpdate(
        objectHandle,
        ahset);
      _userInterface.post("6.15 requestInstanceAttributeValueUpdate, instance: "
        + objectHandle + ", attrs: " + ahset);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 6.15 Invalid handle: " + objectString);
    }
    catch (Exception e) {
      _userInterface.post("EX 6.15 requestInstanceAttributeValueUpdate " + e);
      e.printStackTrace(System.out);
    }
  }

  public void requestFederationRestore(String[] stuff) {
    try {
      _rti.requestFederationRestore(stuff[0]);
      _userInterface.post("4.16 request federation restore.");
    }
    catch (Exception e) {
      _userInterface.post("EX 4.16 requestFederationRestore " + e);
    }
  }

  void requestFederationSave(String[] stuff)
  {
    LogicalTimeDouble time;
    try {
      if (stuff[1].equals("")) {
        _rti.requestFederationSave(stuff[0]);
        _userInterface.post("4.11 request federation save, label: " + stuff[0]);
      }
      else {
        time = new LogicalTimeDouble(Double.valueOf(stuff[1]).doubleValue());
        _rti.requestFederationSave(stuff[0], time);
        _userInterface.post("4.11 request federation save, label: "
          + stuff[0] + ", time: " + stuff[1]);
      }
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 4.11 request federation save Invalid time: " + stuff[1]);
    }
    catch (Exception e) {
      _userInterface.post("EX 4.11 request federation save " + e);
      e.printStackTrace(System.out);
    }
  }

  void resignFederationExecution(int resignAction) {
    try {
      _rti.resignFederationExecution(resignAction);
      _userInterface.setFEDinfoUnavailable();
      _userInterface.setNotJoined();
      _federateHandle = -1;
      _regions.clear();  //regions shouldn't survive resignation
      _userInterface.post("4.5 Resigned with action " + resignAction);
    }
    catch (Exception e) {
      _userInterface.post("EX 4.5 ResignFederationExecution: " + e);
      e.printStackTrace(System.out);
    }
  }

  void registerObjectInstance(String classString)
  {
    int handle = -1;
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        handle = _rti.registerObjectInstance(classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
        handle = _rti.registerObjectInstance(classHandle);
      }
      _userInterface.post("6.2 Registered instance " + handle + " of class " + classString + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 6.2 RegisterObjectInstance " + e);
      e.printStackTrace(System.out);
    }
  }

  void registerObjectInstance
  (
    String classString,
    String name)
  {
    int handle = -1;
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        handle = _rti.registerObjectInstance(classHandle, name);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
        handle = _rti.registerObjectInstance(classHandle, name);
      }
      _userInterface.post("6.2 Registered instance " + handle
        + "(" + name + ") of class " + classString + ".");
    }
    catch (Exception e) {
      _userInterface.post("EX 6.2 RegisterObjectInstance " + e);
      e.printStackTrace(System.out);
    }
  }

  void registerObjectInstanceWithRegion(String[] stuff)
  {
    int handle = -1;
    int classHandle = -1;
    int numberOfRegionAttributePairs, attributeHandles[];
    Region[] regions;

    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(stuff[0]);
      }
      catch (NumberFormatException e) {
        numberOfRegionAttributePairs = _rti.getObjectClassHandle(stuff[0]);
      }
      numberOfRegionAttributePairs = 0;
      if (!stuff[1].equals("")) ++numberOfRegionAttributePairs;
      if (!stuff[3].equals("")) ++numberOfRegionAttributePairs;
      if (!stuff[5].equals("")) ++numberOfRegionAttributePairs;
      attributeHandles = new int[numberOfRegionAttributePairs];
      regions = new Region[numberOfRegionAttributePairs];
      int index = 0;
      if (!stuff[1].equals("")) {
        try {
          attributeHandles[index] = Integer.parseInt(stuff[1]);
        }
        catch (NumberFormatException e) {
          attributeHandles[index] = _rti.getAttributeHandle(stuff[1], classHandle);
        }
        RegionRecord rr = (RegionRecord)_regions.get(stuff[2]);
        if (rr == null) throw new RegionNotKnown(
          "Region " + stuff[1] + " not defined.");
        regions[index] = rr._region;
        ++index;
      }
      if (!stuff[3].equals("")) {
        try {
          attributeHandles[index] = Integer.parseInt(stuff[3]);
        }
        catch (NumberFormatException e) {
          attributeHandles[index] = _rti.getAttributeHandle(stuff[3], classHandle);
        }
        RegionRecord rr = (RegionRecord)_regions.get(stuff[4]);
        if (rr == null) throw new RegionNotKnown(
          "Region " + stuff[4] + " not defined.");
        regions[index] = rr._region;
        ++index;
      }
      if (!stuff[5].equals("")) {
        try {
          attributeHandles[index] = Integer.parseInt(stuff[5]);
        }
        catch (NumberFormatException e) {
          attributeHandles[index] = _rti.getAttributeHandle(stuff[5], classHandle);
        }
        RegionRecord rr = (RegionRecord)_regions.get(stuff[6]);
        if (rr == null) throw new RegionNotKnown(
          "Region " + stuff[6] + " not defined.");
        regions[index] = rr._region;
        ++index;
      }
      handle = _rti.registerObjectInstanceWithRegion(
        classHandle,
        attributeHandles,
        regions);
      _userInterface.post("9.5 Registered instance " + handle + " of class "
        + stuff[0] + " with " + numberOfRegionAttributePairs + " regions.");
    }
    catch (Exception e) {
      _userInterface.post("EX 9.5 RegisterObjectInstanceWithRegion " + e);
      e.printStackTrace(System.out);
    }
  }

  void registerObjectInstanceWithRegionAndName(String[] stuff)
  {
    int handle = -1;
    int classHandle = -1;
    int numberOfRegionAttributePairs, attributeHandles[];
    Region[] regions;

    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(stuff[0]);
      }
      catch (NumberFormatException e) {
        numberOfRegionAttributePairs = _rti.getObjectClassHandle(stuff[0]);
      }
      numberOfRegionAttributePairs = 0;
      if (!stuff[2].equals("")) ++numberOfRegionAttributePairs;
      if (!stuff[4].equals("")) ++numberOfRegionAttributePairs;
      if (!stuff[6].equals("")) ++numberOfRegionAttributePairs;
      attributeHandles = new int[numberOfRegionAttributePairs];
      regions = new Region[numberOfRegionAttributePairs];
      int index = 0;
      if (!stuff[1].equals("")) {
        try {
          attributeHandles[index] = Integer.parseInt(stuff[2]);
        }
        catch (NumberFormatException e) {
          attributeHandles[index] = _rti.getAttributeHandle(stuff[2], classHandle);
        }
        RegionRecord rr = (RegionRecord)_regions.get(stuff[3]);
        if (rr == null) throw new RegionNotKnown(
          "Region " + stuff[2] + " not defined.");
        regions[index] = rr._region;
        ++index;
      }
      if (!stuff[4].equals("")) {
        try {
          attributeHandles[index] = Integer.parseInt(stuff[4]);
        }
        catch (NumberFormatException e) {
          attributeHandles[index] = _rti.getAttributeHandle(stuff[4], classHandle);
        }
        RegionRecord rr = (RegionRecord)_regions.get(stuff[5]);
        if (rr == null) throw new RegionNotKnown(
          "Region " + stuff[5] + " not defined.");
        regions[index] = rr._region;
        ++index;
      }
      if (!stuff[6].equals("")) {
        try {
          attributeHandles[index] = Integer.parseInt(stuff[6]);
        }
        catch (NumberFormatException e) {
          attributeHandles[index] = _rti.getAttributeHandle(stuff[6], classHandle);
        }
        RegionRecord rr = (RegionRecord)_regions.get(stuff[7]);
        if (rr == null) throw new RegionNotKnown(
          "Region " + stuff[7] + " not defined.");
        regions[index] = rr._region;
        ++index;
      }
      handle = _rti.registerObjectInstanceWithRegion(
        classHandle,
        stuff[1],
        attributeHandles,
        regions);
      _userInterface.post("9.5 Registered instance " + handle + " of class "
        + stuff[0] + ", name " + stuff[1]
        + ", with " + numberOfRegionAttributePairs + " regions.");
    }
    catch (Exception e) {
      _userInterface.post("EX 9.5 RegisterObjectInstanceWithRegion " + e);
      e.printStackTrace(System.out);
    }
  }

  void sendROinteractionWithRegion(String[] stuff) {
    int h1 = -1;
    int h2 = -1;
    int h3 = -1;
    int classHandle;
    String regionName = stuff[8];
    try {
      RegionRecord rr = (RegionRecord)_regions.get(regionName);
      if (rr == null) throw new RegionNotKnown("Region " + regionName
        + " not defined");
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(stuff[0]);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(stuff[0]);
      }
      SuppliedParameters sp = _suppliedParametersFactory.create(3);
      if (!stuff[1].equals("")) {
        try {
          h1 = Integer.parseInt(stuff[1]);
        }
        catch (NumberFormatException e) {
          h1 = _rti.getParameterHandle(stuff[1], classHandle);
        }
        sp.add(h1, stuff[2].getBytes());
      }
      if (!stuff[3].equals("")) {
        try {
          h2 = Integer.parseInt(stuff[3]);
        }
        catch (NumberFormatException e) {
          h2 = _rti.getParameterHandle(stuff[3], classHandle);
        }
        sp.add(h2, stuff[4].getBytes());
      }
      if (!stuff[5].equals("")) {
        try {
          h3 = Integer.parseInt(stuff[5]);
        }
        catch (NumberFormatException e) {
          h3 = _rti.getParameterHandle(stuff[5], classHandle);
        }
        sp.add(h3, stuff[6].getBytes());
      }
      _rti.sendInteractionWithRegion(
        classHandle,
        sp,
        stuff[7].getBytes(),
        rr._region);
      _userInterface.post("9.12 Interaction sent, class " + classHandle
        + ", tag: " + stuff[7]);
    }
    catch (Exception e) {
      _userInterface.post("EX 9.12 SendROinteraction " + e);
      e.printStackTrace(System.out);
    }
  }

  void sendTSOinteractionWithRegion(String[] stuff) {
    int h1 = -1;
    int h2 = -1;
    int h3 = -1;
    int classHandle;
    LogicalTimeDouble time;
    String regionName = stuff[8];
    try {
      RegionRecord rr = (RegionRecord)_regions.get(regionName);
      if (rr == null) throw new RegionNotKnown("Region " + regionName
        + " not defined");
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(stuff[0]);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(stuff[0]);
      }
      SuppliedParameters sp = _suppliedParametersFactory.create(3);
      if (!stuff[1].equals("")) {
        try {
          h1 = Integer.parseInt(stuff[1]);
        }
        catch (NumberFormatException e) {
          h1 = _rti.getParameterHandle(stuff[1], classHandle);
        }
        sp.add(h1, stuff[2].getBytes());
      }
      if (!stuff[3].equals("")) {
        try {
          h2 = Integer.parseInt(stuff[3]);
        }
        catch (NumberFormatException e) {
          h2 = _rti.getParameterHandle(stuff[3], classHandle);
        }
        sp.add(h2, stuff[4].getBytes());
      }
      if (!stuff[5].equals("")) {
        try {
          h3 = Integer.parseInt(stuff[5]);
        }
        catch (NumberFormatException e) {
          h3 = _rti.getParameterHandle(stuff[5], classHandle);
        }
        sp.add(h3, stuff[6].getBytes());
      }
      time = new LogicalTimeDouble(Double.valueOf(stuff[9]).doubleValue());
      EventRetractionHandle erh =
        _rti.sendInteractionWithRegion(
          classHandle,
          sp,
          stuff[7].getBytes(),
          rr._region,
          time);
      _userInterface.post("9.12 Interaction sent TSO, class " + classHandle
        + ", tag: " + stuff[7] + ", region " + regionName
        + ", time " + time);
    }
    catch (Exception e) {
      _userInterface.post("EX 9.12 SendTSOinteraction " + e);
      e.printStackTrace(System.out);
    }
  }

  void retract(String[] stuff) {
    int localHandle = 0;

    try {
      localHandle = Integer.parseInt(stuff[0]);
      EventRetractionHandle erh = getRetractionHandle(localHandle);
      _rti.retract(getRetractionHandle(localHandle));
      _userInterface.post("8.21 retract " + localHandle + "(" + erh + ").");
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 8.21 Invalid retraction handle " + stuff[0]);
    }
    catch (ArrayIndexOutOfBoundsException e) {
      _userInterface.post("Local handle " + localHandle
        + " for event retraction handle not registered. ");
    }
    catch (Exception e) {
      _userInterface.post("EX 8.21 retract " + e);
    }
  }

  void sendROinteraction(String[] stuff) {
    int h1 = -1;
    int h2 = -1;
    int h3 = -1;
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(stuff[0]);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(stuff[0]);
      }
      SuppliedParameters sp = _suppliedParametersFactory.create(3);
      if (!stuff[1].equals("")) {
        try {
          h1 = Integer.parseInt(stuff[1]);
        }
        catch (NumberFormatException e) {
          h1 = _rti.getParameterHandle(stuff[1], classHandle);
        }
        //ugly hack to add a NUL to test MOM
        byte[] whatHeTyped = stuff[2].getBytes();
        byte[] nulledParam = new byte[whatHeTyped.length + 1];
        System.arraycopy(whatHeTyped, 0, nulledParam, 0, whatHeTyped.length);
        nulledParam[nulledParam.length-1] = 0;
        System.out.print("param value:");
        for (int i = 0; i < nulledParam.length; ++i) {
          System.out.print("x" + Integer.toHexString(((int)nulledParam[i])));
        }
        System.out.println("");
        sp.add(h1, nulledParam);
        //sp.add(h1, stuff[2].getBytes());
      }
      if (!stuff[3].equals("")) {
        try {
          h2 = Integer.parseInt(stuff[3]);
        }
        catch (NumberFormatException e) {
          h2 = _rti.getParameterHandle(stuff[3], classHandle);
        }
        sp.add(h2, stuff[4].getBytes());
      }
      if (!stuff[5].equals("")) {
        try {
          h3 = _rti.getParameterHandle(stuff[5], classHandle);
        }
        catch (NumberFormatException e) {
          h3 = _rti.getParameterHandle(stuff[5], classHandle);
        }
        sp.add(h3, stuff[6].getBytes());
      }
      _rti.sendInteraction(classHandle, sp, stuff[7].getBytes());
      _userInterface.post("6.6 Interaction sent, class " + classHandle
        + ", tag: " + stuff[7]);
    }
    catch (Exception e) {
      _userInterface.post("EX 6.6 SendROinteraction " + e);
      e.printStackTrace(System.out);
    }
  }

  void sendTSOinteraction(String[] stuff) {
    int h1 = -1;
    int h2 = -1;
    int h3 = -1;
    int classHandle;
    LogicalTimeDouble time;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(stuff[0]);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(stuff[0]);
      }
      SuppliedParameters sp = _suppliedParametersFactory.create(3);
      if (!stuff[1].equals("")) {
        try {
          h1 = Integer.parseInt(stuff[1]);
        }
        catch (NumberFormatException e) {
          h1 = _rti.getParameterHandle(stuff[1], classHandle);
        }
        sp.add(h1, stuff[2].getBytes());
      }
      if (!stuff[3].equals("")) {
        try {
          h2 = Integer.parseInt(stuff[3]);
        }
        catch (NumberFormatException e) {
          h2 = _rti.getParameterHandle(stuff[3], classHandle);
        }
        sp.add(h2, stuff[4].getBytes());
      }
      if (!stuff[5].equals("")) {
        try {
          h3 = Integer.parseInt(stuff[5]);
        }
        catch (NumberFormatException e) {
          h3 = _rti.getParameterHandle(stuff[5], classHandle);
        }
        sp.add(h3, stuff[6].getBytes());
      }
      time = new LogicalTimeDouble(Double.valueOf(stuff[8]).doubleValue());
      EventRetractionHandle erh =
        _rti.sendInteraction(classHandle, sp, stuff[7].getBytes(), time);
      _userInterface.post("6.6 Interaction sent, class " + classHandle
        + ", tag: " + stuff[7] + ", time: " + stuff[8]);
      int localHandle = registerRetractionHandle(erh);
      _userInterface.post("  local handle for event retraction: " + localHandle);
    }
    catch (NumberFormatException e) {
      _userInterface.post("Ex 6.6 Invalid time: " + stuff[7]);
    }
    catch (Exception e) {
      _userInterface.post("EX 6.6 SendTSOinteraction " + e);
      e.printStackTrace(System.out);
    }
  }


  void subscribeInteractionClassWithRegion(
    String classString,
    String regionName)
  {
    int classHandle;
    try {
      RegionRecord rr = (RegionRecord)_regions.get(regionName);
      if (rr == null) throw new RegionNotKnown("Region " + regionName
        + " not defined");
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.subscribeInteractionClass(classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(classString);
      }
      _rti.subscribeInteractionClassWithRegion(classHandle, rr._region);
      _userInterface.post("9.10 Subscribed interaction class " + classHandle
        + " with region " + regionName);
    }
    catch (Exception e) {
      _userInterface.post("EX 9.10 SubscribeInteractionClassWithRegion " + e);
      e.printStackTrace(System.out);
    }
  }

  void subscribeInteractionClassPassivelyWithRegion(
    String classString,
    String regionName)
  {
    int classHandle;
    try {
      RegionRecord rr = (RegionRecord)_regions.get(regionName);
      if (rr == null) throw new RegionNotKnown("Region " + regionName
        + " not defined");
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.subscribeInteractionClass(classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(classString);
      }
      _rti.subscribeInteractionClassPassivelyWithRegion(classHandle, rr._region);
      _userInterface.post("9.10 Subscribed interaction class passively " + classHandle
        + " with region " + regionName);
    }
    catch (Exception e) {
      _userInterface.post("EX 9.10 SubscribeInteractionClassPassivelyWithRegion " + e);
      e.printStackTrace(System.out);
    }
  }


  void subscribeInteractionClass(
    String classString)
  {
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.subscribeInteractionClass(classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(classString);
        _rti.subscribeInteractionClass(classHandle);
      }
      _userInterface.post("5.8 Subscribed interaction class " + classHandle);
    }
    catch (Exception e) {
      _userInterface.post("EX 5.8 SubscribeInteractionClass " + e);
      e.printStackTrace(System.out);
    }
  }


  void subscribeInteractionClassPassively(
    String classString)
  {
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.subscribeInteractionClassPassively(classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(classString);
        _rti.subscribeInteractionClassPassively(classHandle);
      }
      _userInterface.post("5.8 Subscribed interaction class passively" + classHandle);
    }
    catch (Exception e) {
      _userInterface.post("EX 5.8 SubscribeInteractionClassPassively " + e);
      e.printStackTrace(System.out);
    }
  }


  void subscribeObjectClassAttributes(
    String classString,
    AttributeHandleSet ahset)
  {
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.subscribeObjectClassAttributes(classHandle, ahset);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
        _rti.subscribeObjectClassAttributes(classHandle, ahset);
      }
      _userInterface.post("5.6 Subscribed class " + classHandle + " " + ahset);
    }
    catch (Exception e) {
      _userInterface.post("EX 5.6 SubscribeObjectClassAttributes " + e);
      e.printStackTrace(System.out);
    }
  }


  void subscribeObjectClassAttributesPassively(
    String classString,
    AttributeHandleSet ahset)
  {
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.subscribeObjectClassAttributesPassively(classHandle, ahset);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
        _rti.subscribeObjectClassAttributesPassively(classHandle, ahset);
      }
      _userInterface.post("5.6 Subscribed passively class " + classHandle + " " + ahset);
    }
    catch (Exception e) {
      _userInterface.post("EX 5.6 SubscribeObjectClassAttributes " + e);
      e.printStackTrace(System.out);
    }
  }

  void subscribeObjectClassAttributesWithRegion(
    String regionLocalName,
    String classString,
    AttributeHandleSet attributes)
  {
    int classHandle = -1;

    try {
      RegionRecord rr = (RegionRecord)_regions.get(regionLocalName);
      if (rr == null) throw new RegionNotKnown(
        "Region " + regionLocalName + " not defined.");
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
      }
      _rti.subscribeObjectClassAttributesWithRegion(
        classHandle,
        rr._region,
        attributes);
      _userInterface.post("9.8 Subscribe object class attributes with region, class "
        + classHandle
        + ", region " + regionLocalName
        + ", attributes " + attributes);
    }
    catch (Exception e) {
      _userInterface.post("EX 9.8 subscribeObjectClassAttributesWithRegion " + e);
      e.printStackTrace(System.out);
    }
  }

  void subscribeObjectClassAttributesPassivelyWithRegion(
    String regionLocalName,
    String classString,
    AttributeHandleSet attributes)
  {
    int classHandle = -1;

    try {
      RegionRecord rr = (RegionRecord)_regions.get(regionLocalName);
      if (rr == null) throw new RegionNotKnown(
        "Region " + regionLocalName + " not defined.");
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
      }
      _rti.subscribeObjectClassAttributesPassivelyWithRegion(
        classHandle,
        rr._region,
        attributes);
      _userInterface.post("9.8 Subscribe object class attributes passively with region, class "
        + classHandle
        + ", region " + regionLocalName
        + ", attributes " + attributes);
    }
    catch (Exception e) {
      _userInterface.post("EX 9.8 subscribeObjectClassAttributesPassivelyWithRegion " + e);
      e.printStackTrace(System.out);
    }
  }

  public void synchronizationPointAchieved(String label)
  {
    try {
      _rti.synchronizationPointAchieved(label);
      _userInterface.post("4.9 Synchronization point achieved " + label);
    }
    catch (Exception e) {
      _userInterface.post("EX 4.9 SynchronizationPointAchieved: " + e);
      e.printStackTrace(System.out);
    }
  }

  void timeAdvanceRequest(String[] stuff) {
    LogicalTimeDouble time;
    try {
      time = new LogicalTimeDouble(Double.valueOf(stuff[0]).doubleValue());
      _rti.timeAdvanceRequest(time);
      _userInterface.post("8.8 time advance request, time: " + stuff[0]);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 8.8 Invalid time: " + stuff[0]);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.8 timeAdvanceRequest " + e);
      e.printStackTrace(System.out);
    }
  }

  void timeAdvanceRequestAvailable(String[] stuff) {
    LogicalTimeDouble time;
    try {
      time = new LogicalTimeDouble(Double.valueOf(stuff[0]).doubleValue());
      _rti.timeAdvanceRequestAvailable(time);
      _userInterface.post("8.9 time advance request available, time: " + stuff[0]);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 8.9 Invalid time: " + stuff[0]);
    }
    catch (Exception e) {
      _userInterface.post("EX 8.9 timeAdvanceRequestAvailable " + e);
      e.printStackTrace(System.out);
    }
  }


  void unsubscribeInteractionClass(
    String classString)
  {
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.unsubscribeInteractionClass(classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(classString);
        _rti.unsubscribeInteractionClass(classHandle);
      }
      _userInterface.post("Unsubscribed interaction class " + classHandle);
    }
    catch (Exception e) {
      _userInterface.post("UnsubscribeInteractionClass " + e);
      e.printStackTrace(System.out);
    }
  }

  void unassociateRegionForUpdates(
    String objectInstanceHandleString,
    String regionLocalName)
  {
    int handle = -1;

    try {
      RegionRecord rr = (RegionRecord)_regions.get(regionLocalName);
      if (rr == null) throw new RegionNotKnown(
        "Region " + regionLocalName + " not defined.");
      handle = Integer.parseInt(objectInstanceHandleString);
      _rti.unassociateRegionForUpdates(
        rr._region,
        handle);
      _userInterface.post("9.7 Unassociate region for updates, instance " + handle
        + ", region " + regionLocalName);
    }
    catch (Exception e) {
      _userInterface.post("EX 9.7 unassociateRegionForUpdates " + e);
      e.printStackTrace(System.out);
    }
  }

  void unconditionalAttributeOwnershipDivestiture(
    String objectString,
    AttributeHandleSet ahset)
  {
    int objectHandle;
    try {
        objectHandle = Integer.parseInt(objectString);
        _rti.unconditionalAttributeOwnershipDivestiture(
          objectHandle,
          ahset);
        _userInterface.post("7.2 Unconditional attribute ownership divestiture, instance: "
          + objectHandle + ", attrs: " + ahset);
    }
    catch (NumberFormatException e) {
      _userInterface.post("EX 7.2 Invalid object instance handle: " + objectString);
    }
    catch (Exception e) {
      _userInterface.post("EX 7.2 Unconditional attribute ownership divestiture " + e);
      e.printStackTrace(System.out);
    }
  }


  void unpublishInteractionClass(
    String classString)
  {
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.unpublishInteractionClass(classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(classString);
        _rti.unpublishInteractionClass(classHandle);
      }
      _userInterface.post("5.5 Unpublished interaction class " + classHandle);
    }
    catch (Exception e) {
      _userInterface.post("EX 5.5 UnpublishInteractionClass " + e);
      e.printStackTrace(System.out);
    }
  }


  void unpublishObjectClass(
    String classString)
  {
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.unpublishObjectClass(classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
        _rti.unpublishObjectClass(classHandle);
      }
      _userInterface.post("5.3 Unpublished class " + classHandle);
    }
    catch (Exception e) {
      _userInterface.post("EX 5.3 unpublishObjectClass " + e);
      e.printStackTrace(System.out);
    }
  }

  void unsubscribeInteractionClassWithRegion(String[] stuff)
  {
    int classHandle = -1;

    try {
      RegionRecord rr = (RegionRecord)_regions.get(stuff[1]);
      if (rr == null) throw new RegionNotKnown(
        "Region " + stuff[1] + " not defined.");
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(stuff[0]);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getInteractionClassHandle(stuff[0]);
      }
      _rti.unsubscribeInteractionClassWithRegion(
        classHandle,
        rr._region);
      _userInterface.post("9.11 unsubscribe interaction class with region, class "
        + stuff[0]
        + ", region " + stuff[1]);
    }
    catch (Exception e) {
      _userInterface.post("EX 9.11 unsubscribeInteractionClassWithRegion " + e);
      e.printStackTrace(System.out);
    }
  }


  void unsubscribeObjectClass(
    String classString)
  {
    int classHandle;
    try {
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(classString);
        _rti.unsubscribeObjectClass(classHandle);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(classString);
        _rti.unsubscribeObjectClass(classHandle);
      }
      _userInterface.post("5.7 Unsubscribed class " + classHandle);
    }
    catch (Exception e) {
      _userInterface.post("EX 5.7 UnsubscribeObjectClass " + e);
      e.printStackTrace(System.out);
    }
  }

  void updateAttributeValuesTSO(String[] stuff) {
    int h1 = -1;
    int h2 = -1;
    int h3 = -1;
    int instanceHandle;
    int objectClass; //known class of instance
    LogicalTimeDouble time;
    try {
      //Is the 'object' a handle or name?
      try {
        instanceHandle = Integer.parseInt(stuff[0]);
      }
      catch (NumberFormatException e) {
        instanceHandle = _rti.getObjectInstanceHandle(stuff[0]);
      }
      objectClass = _rti.getObjectClass(instanceHandle);
      SuppliedAttributes sa = _suppliedAttributesFactory.create(3);
      if (!stuff[1].equals("")) {
        try {
          h1 = Integer.parseInt(stuff[1]);
        }
        catch (NumberFormatException e) {
          h1 = _rti.getAttributeHandle(stuff[1], objectClass);
        }
        sa.add(h1, stuff[2].getBytes());
      }
      if (!stuff[3].equals("")) {
        try {
          h2 = Integer.parseInt(stuff[3]);
        }
        catch (NumberFormatException e) {
          h2 = _rti.getAttributeHandle(stuff[3], objectClass);
        }
        sa.add(h2, stuff[4].getBytes());
      }
      if (!stuff[5].equals("")) {
        try {
          h3 = Integer.parseInt(stuff[5]);
        }
        catch (NumberFormatException e) {
          h3 = _rti.getAttributeHandle(stuff[5], objectClass);
        }
        sa.add(h3, stuff[6].getBytes());
      }
      time = new LogicalTimeDouble(Double.valueOf(stuff[8]).doubleValue());
      EventRetractionHandle erh =
        _rti.updateAttributeValues(instanceHandle, sa, stuff[7].getBytes(), time);
      _userInterface.post("6.4 Updated attributes of instance " + instanceHandle
        + ", tag: " + stuff[7] + ", time: " + stuff[8]);
      int localHandle = registerRetractionHandle(erh);
      _userInterface.post("  local handle for event retraction: " + localHandle);
    }
    catch (Exception e) {
      _userInterface.post("EX 6.4 updateAttributeValuesTSO " + e);
      e.printStackTrace(System.out);
    }
  }

  void unsubscribeObjectClassWithRegion(String[] stuff)
  {
    int classHandle = -1;

    try {
      RegionRecord rr = (RegionRecord)_regions.get(stuff[1]);
      if (rr == null) throw new RegionNotKnown(
        "Region " + stuff[1] + " not defined.");
      //Is the 'class' a handle or name?
      try {
        classHandle = Integer.parseInt(stuff[0]);
      }
      catch (NumberFormatException e) {
        classHandle = _rti.getObjectClassHandle(stuff[0]);
      }
      _rti.unsubscribeObjectClassWithRegion(
        classHandle,
        rr._region);
      _userInterface.post("9.9 unsubscribe object class with region, class "
        + stuff[0]
        + ", region " + stuff[1]);
    }
    catch (Exception e) {
      _userInterface.post("EX 9.9 unsubscribeObjectClassWithRegion " + e);
      e.printStackTrace(System.out);
    }
  }

  void updateAttributeValuesRO(String[] stuff) {
    int h1 = -1;
    int h2 = -1;
    int h3 = -1;
    int instanceHandle;
    int objectClass; //known class of instance
    try {
      //Is the 'object' a handle or name?
      try {
        instanceHandle = Integer.parseInt(stuff[0]);
      }
      catch (NumberFormatException e) {
        instanceHandle = _rti.getObjectInstanceHandle(stuff[0]);
      }
      objectClass = _rti.getObjectClass(instanceHandle);
      SuppliedAttributes sa = _suppliedAttributesFactory.create(3);
      if (!stuff[1].equals("")) {
        try {
          h1 = Integer.parseInt(stuff[1]);
        }
        catch (NumberFormatException e) {
          h1 = _rti.getAttributeHandle(stuff[1], objectClass);
        }
        sa.add(h1, stuff[2].getBytes());
      }
      if (!stuff[3].equals("")) {
        try {
          h2 = Integer.parseInt(stuff[3]);
        }
        catch (NumberFormatException e) {
          h2 = _rti.getAttributeHandle(stuff[3], objectClass);
        }
        sa.add(h2, stuff[4].getBytes());
      }
      if (!stuff[5].equals("")) {
        try {
          h3 = Integer.parseInt(stuff[5]);
        }
        catch (NumberFormatException e) {
          h3 = _rti.getAttributeHandle(stuff[5], objectClass);
        }
        sa.add(h3, stuff[6].getBytes());
      }
      _rti.updateAttributeValues(instanceHandle, sa, stuff[7].getBytes());
      _userInterface.post("6.4 Updated attributes of instance " + instanceHandle
        + ", tag: " + stuff[7]);
    }
    catch (Exception e) {
      _userInterface.post("EX 6.4 updateAttributeValuesRO " + e);
      e.printStackTrace(System.out);
    }
  }
}

