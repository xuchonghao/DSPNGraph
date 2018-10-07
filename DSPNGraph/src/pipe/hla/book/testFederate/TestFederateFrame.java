
package pipe.hla.book.testFederate;

import hla.rti1516.*;
import pipe.hla.book.test_federate.dialog.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TestFederateFrame extends JFrame {

   public TestFederateFrame() {
    try  {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    this.setJMenuBar(mainMenuBar);
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    jPanel1.setLayout(borderLayout2);
    clearLog.setText("Clear Log");
    clearLog.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clearLog_actionPerformed(e);
      }
    });

     createMenuComponents();
     composeMenuComponents();
     activateMenuComponents();

     this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
     jScrollPane1.getViewport().add(logArea, null);
     this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
     jPanel1.add(clearLog, BorderLayout.EAST);

  }

   private void createMenuComponents() {
      fileMenu.setText("File");
      exitMenuItem.setText("Exit");

      federationManagementMenu.setText("Fed\'n");

      federationExecutionSubmenu.setText("Federation execution");
      createFedExMenuItem.setText("4.2 Create federation execution...");
      destroyFedExMenuItem.setText("4.3 Destroy federation execution...");

      federateSubmenu.setText("Federate");
      joinFederationExecutionMenuItem.setText("4.4 Join federation execution...");
      resignFederationExecutionSubmenu.setText("4.5 Resign federation execution and...");
      resignFederationExecutionMenuItem1.setText("Divest owned attributes");
      resignFederationExecutionMenuItem2.setText("Delete owned objects");
      resignFederationExecutionMenuItem3.setText("Cancel pending acquisitions");
      resignFederationExecutionMenuItem4.setText("Delete and Divest");
      resignFederationExecutionMenuItem5.setText("Cancel, Delete and Divest");
      resignFederationExecutionMenuItem6.setText("Do nothing");

      syncPointSubmenu.setText("Sync points");
      registerSyncPointMenuItem.setText("4.6 Register synchronization point...");
      registerSyncPointForFederatesMenuItem.setText("4.6 Register for federates...");
      syncPointAchievedMenuItem.setText("4.9 Synchronization point achieved...");

      saveFederationSubmenu.setText("Save");
      requestFederationSaveMenuItem.setText("4.11 Request federation save...");
      federateSaveBegunMenuItem.setText("4.13 Federate save begun");
      federateSaveCompleteMenuItem.setText("4.14 Federate save complete");
      federateSaveNotCompleteMenuItem.setText("4.14 Federate save not complete");
      queryFederationSaveStatusMenuItem.setText("4.16 Query federation save status");

      restoreFederationSubmenu.setText("Restore");
      requestFederationRestoreMenuItem.setText("4.16 Request federation restore...");
      federateRestoreCompleteMenuItem.setText("4.20 Federate restore complete");
      federateRestoreNotCompleteMenuItem.setText("4.20 Federate restore not complete");
      queryFederationRestoreStatusMenuItem.setText("4.24 Query federation restore status");

      declarationManagmentMenu.setText("Decl");

      publishSubmenu.setText("Publish");
      publishObjectClassMenuItem.setText("5.2 Publish object class...");
      unpublishObjectClassMenuItem.setText("5.3 Unpublish object class...");
      publishInteractionClassMenuItem.setText("5.4 Publish interaction class...");
      unpublishInteractionClassMenuItem.setText("5.5 Unpublish interaction class...");

      subscribeSubmenu.setText("Subscribe");
      subscribeObjectMenuItem.setText("5.6 Subscribe object class attributes...");
      subscribeObjectPassivelyMenuItem.setText("5.6 Subscribe attributes passively...");
      unsubscribeObjectClassMenuItem.setText("5.7 Unsubscribe object class...");
      subscribeInteractionMenuItem.setText("5.8 Subscribe interaction class...");
      subscribeInteractionPassivelyMenuItem.setText("5.8 Subscribe interaction class passively...");
      unsubscribeInteractionMenuItem.setText("5.9 Unsubscribe interaction class...");

      objectManagementMenu.setText("Obj");
      ownershipManagementMenu.setText("Own");
      timeManagementMenu.setText("Time");
      dataDistributionManagementMenu.setText("DDM");

      supportServicesMenu.setText("Support");

      jMenu23.setText("Object classes & attributes");
      jMenuItem49.setText("10.2 Get object class handle...");
      jMenuItem50.setText("10.3 Get object class name...");
      jMenuItem51.setText("10.4 Get attribute handle...");
      jMenuItem52.setText("10.5 Get attribute name...");

      jMenu24.setText("Interactions & parameters");
      jMenuItem53.setText("10.6 Get interaction class handle...");
      jMenuItem54.setText("10.7 Get interaction class name...");
      jMenuItem55.setText("10.8 Get parameter handle...");
      jMenuItem56.setText("10.9 Get parameter name...");
      
      jMenu30.setText("Object instances");
      jMenuItem57.setText("10.10 Get object instance handle...");
      jMenuItem58.setText("10.11 Get object instance name...");
      jMenuItem59.setText("10.17 Get object class...");
      jMenu25.setText("Spaces & dimensions");
      jMenuItem60.setText("10.12 Get routing space handle...");
      jMenuItem61.setText("10.13 Get routing space name...");
      jMenuItem62.setText("10.14 Get dimension handle...");
      jMenuItem63.setText("10.15 Get dimension name...");
      jMenu26.setText("Class relevance");
      jMenuItem64.setText("10.23 Enable class relevance advisory switch");
      jMenuItem65.setText("10.24 Disable class relevance advisory switch");
      jMenu27.setText("Interaction relevance");
      jMenuItem66.setText("10.29 Enable interaction relevance advisory switch");
      jMenuItem67.setText("10.30 Disable interaction relevance advisory switch");
      jMenu28.setText("Attribute scope");
      jMenuItem68.setText("10.27 Enable attribute scope advisory switch");
      jMenuItem69.setText("10.28 Disable attribute scope advisory switch");



    jMenu10.setText("Register");
    jMenuItem18.setText("6.2 Register object instance...");
    jMenuItem19.setText("6.2 Register object instance with name...");
    jMenu11.setText("Delete");
    jMenuItem20.setText("6.8 Delete object instance...");
    jMenuItem21.setText("6.8 Delete object instance with time...");
    jMenu12.setText("Send interaction");
    jMenuItem22.setText("6.6 Send interaction RO...");
    jMenuItem23.setText("6.6 Send interaction TSO...");
    jMenu13.setText("Update attributes");
    jMenuItem24.setText("6.4 Update attribute values RO...");
    jMenuItem25.setText("6.4 Update attribute values TSO...");

    jMenuItem26.setText("7.2 Unconditional attribute ownership divestiture...");
    jMenuItem27.setText("7.3 Negotiated attribute ownership divestiture...");
    jMenuItem28.setText("7.7 Attribute ownership acquisition...");
    jMenuItem29.setText("7.8 Attribute ownership acquisition if available...");
    jMenuItem30.setText("7.11 Attribute ownership release response...");
    jMenuItem31.setText("7.12 Cancel negotiated attribute ownership divestiture...");
    jMenuItem32.setText("7.13 Cancel attribute ownership acquisition...");
    jMenuItem33.setText("7.15 Query attribute ownership...");

    jMenu16.setText("Switches");
    jMenuItem34.setText("8.2 Enable time regulation...");
    jMenuItem35.setText("8.4 Disable time regulation");
    jMenuItem36.setText("8.5 Enable time constrained");
    jMenuItem37.setText("8.7 Disable time constrained");
    jMenu17.setText("Time advance");
    jMenuItem38.setText("8.8 Time advance request...");
    jMenuItem39.setText("8.9 Time advance request available...");
    jMenuItem40.setText("8.14 Enable asynchronous delivery");
    jMenuItem41.setText("8.16 Query LBTS");
    jMenuItem42.setText("8.17 Query federate time");
    jMenuItem43.setText("8.18 Query minimum next event time");
    jMenuItem44.setText("8.19 Modify lookahead...");
    jMenuItem45.setText("8.20 Query lookahead");

    jMenu19.setText("Regions");
    jMenuItem46.setText("9.2 Create region...");
    jMenu21.setText("Interactions");
    jMenuItem47.setText("9.10 Subscribe interaction class with region...");
    jMenuItem48.setText("9.12 Send interaction with region RO...");



    saveFederationSubmenu.setText("Save");
    requestFederationSaveMenuItem.setText("4.11 Request federation save...");
    federateSaveBegunMenuItem.setText("4.13 Federate save begun");
    federateSaveCompleteMenuItem.setText("4.14 Federate save complete");
    restoreFederationSubmenu.setText("Restore");
    requestFederationRestoreMenuItem.setText("4.16 Request federation restore...");
    federateRestoreCompleteMenuItem.setText("4.20 Federate restore complete");
    jMenuItem77.setText("6.10 Local delete object instance...");
    jMenuItem78.setText("6.11 Change attribute transportation type...");
    jMenuItem79.setText("6.12 Change interaction transportation type...");
    jMenuItem80.setText("6.15 Request class attribute value update...");
    jMenuItem81.setText("7.17 Is attribute owned by federate...");
    jMenuItem82.setText("8.10 Next event request...");
    jMenuItem83.setText("8.11 Next event request available...");
    jMenuItem84.setText("8.12 Flush queue request...");
    jMenuItem85.setText("8.15 Disable asynchronous delivery");
    jMenuItem86.setText("8.21 Retract...");
    jMenuItem87.setText("8.23 Change attribute order type...");
    jMenuItem88.setText("8.24 Change interaction order type...");
    jMenuItem89.setText("9.3 Modify region...");
    jMenuItem90.setText("9.4 Delete region...");
    jMenu32.setText("Objects & attributes");
    jMenuItem91.setText("9.5 Register object instance with region...");
    jMenuItem92.setText("9.5 Register instance with region & name...");
    jMenuItem93.setText("9.6 Associate region for updates...");
    jMenuItem94.setText("9.7 Unassociate region for updates...");
    jMenuItem95.setText("9.8 Subscribe object class attributes with region...");
    jMenuItem96.setText("9.9 Unsubscribe object class with region...");
    jMenuItem97.setText("9.11 Unsubscribe interaction class with region");
    jMenuItem98.setText("9.12 Send interaction with region TSO...");
    jMenuItem99.setText("9.13 Request attribute value update with region...");
    jMenuItem100.setText("10.16 Get attribute routing space handle...");
    jMenuItem101.setText("10.18 Get interaction routing space handle...");
    jMenu20.setText("Transportation & Ordering");
    jMenuItem102.setText("10.19 Get transportation handle...");
    jMenuItem103.setText("10.20 Get transportation name...");
    jMenuItem104.setText("10.21 Get ordering handle...");
    jMenuItem105.setText("10.22 Get ordering name...");
    jMenu33.setText("Attribute relevance");
    jMenuItem106.setText("10.25 Enable attribute relevance advisory switch");
    jMenuItem107.setText("10.26 Disable attribute relevance advisory switch");

    jMenuItem109.setText("6.15 Request instance attribute value update...");
    jMenuItem110.setText("9.8 Subscribe passively...");
    jMenuItem111.setText("9.10 Subscribe passively...");

   }

   private void activateMenuComponents() {
//      jMenuItem111.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          subscribeInteractionClassPassivelyWithRegion_actionPerformed(e);
//        }
//      });
//      jMenuItem110.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          subscribeObjectClassAttributesPassivelyWithRegion_actionPerformed(e);
//        }
//      });
//      jMenuItem109.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          requestInstanceAttributeValueUpdate_actionPerformed(e);
//        }
//      });
//      jMenuItem107.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          disableAttributeRelevanceAdvisorySwitch_actionPerformed(e);
//        }
//      });
//      jMenuItem106.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          enableAttributeRelevanceAdvisorySwitch_actionPerformed(e);
//        }
//      });
//      jMenuItem105.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getOrderingName_actionPerformed(e);
//        }
//      });
//      jMenuItem104.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getOrderingHandle_actionPerformed(e);
//        }
//      });
//      jMenuItem103.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getTransportationName_actionPerformed(e);
//        }
//      });
//      jMenuItem102.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getTransportationHandle_actionPerformed(e);
//        }
//      });
//      jMenuItem101.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getInteractionRoutingSpacehandle_actionPerformed(e);
//        }
//      });
//      jMenuItem100.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getAttributeRoutingSpaceHandle_actionPerformed(e);
//        }
//      });
//      jMenuItem99.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          requestAttributeValueUpdateWithRegion_actionPerformed(e);
//        }
//      });
//      jMenuItem98.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          sendInteractionWithRegionTSO_actionPerformed(e);
//        }
//      });
//      jMenuItem97.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          unsubscribeInteractionClassWithRegion_actionPerformed(e);
//        }
//      });
//      jMenuItem96.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          unsubscribeObjectClassWithRegion_actionPerformed(e);
//        }
//      });
//      jMenuItem95.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          subscribeObjectClassAttributesWithRegion_actionPerformed(e);
//        }
//      });
//      jMenuItem94.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          unassociateRegionForUpdates_actionPerformed(e);
//        }
//      });
//      jMenuItem93.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          associateRegionForUpdates_actionPerformed(e);
//        }
//      });
//      jMenuItem92.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          registerObjectInstanceWithRegionAndName_actionPerformed(e);
//        }
//      });
//      jMenuItem91.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          registerObjectInstanceWithRegion_actionPerformed(e);
//        }
//      });
//      jMenuItem90.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          deleteRegion_actionPerformed(e);
//        }
//      });
//      jMenuItem89.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          modifyRegion_actionPerformed(e);
//        }
//      });
//      jMenuItem88.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          changeInteractionOrderType_actionPerformed(e);
//        }
//      });
//      jMenuItem87.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          changeAttributeOrderType_actionPerformed(e);
//        }
//      });
//      jMenuItem86.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          retract_actionPerformed(e);
//        }
//      });
//      jMenuItem85.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          disableAsynchronousDelivery_actionPerformed(e);
//        }
//      });
//      jMenuItem84.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          flushQueueRequest_actionPerformed(e);
//        }
//      });
//      jMenuItem83.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          nextEventRequestAvailable_actionPerformed(e);
//        }
//      });
//      jMenuItem82.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          nextEventRequest_actionPerformed(e);
//        }
//      });
//      jMenuItem81.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          isAttributeOwnedByFederate_actionPerformed(e);
//        }
//      });
//      jMenuItem80.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          requestClassAttributeValueUpdate_actionPerformed(e);
//        }
//      });
//      jMenuItem79.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          changeInteractionTransportationType_actionPerformed(e);
//        }
//      });
//      jMenuItem78.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          changeAttributeTransportationType_actionPerformed(e);
//        }
//      });
//      jMenuItem77.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          localDeleteObjectInstance_actionPerformed(e);
//        }
//      });

      requestFederationRestoreMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          requestFederationRestore_actionPerformed(e);
        }
      });

      federateRestoreCompleteMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          federateRestoreComplete_actionPerformed(e);
        }
      });

      federateRestoreNotCompleteMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          federateRestoreNotComplete_actionPerformed(e);
        }
      });

      queryFederationRestoreStatusMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          queryFederationRestoreStatus_actionPerformed(e);
        }
      });

      requestFederationSaveMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            requestFederationSave_actionPerformed(e);
         }
      });

      federateSaveBegunMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          federateSaveBegun_actionPerformed(e);
        }
      });

      federateSaveCompleteMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          federateSaveComplete_actionPerformed(e);
        }
      });

      federateSaveNotCompleteMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            federateSaveNotComplete_actionPerformed(e);
         }
      });

      queryFederationSaveStatusMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            queryFederationSaveStatus_actionpPerformed(e);
         }
      });

      resignFederationExecutionMenuItem1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            resignDivestingAttributes_actionPerformed(e);
         }
      });

      resignFederationExecutionMenuItem2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            resignDeletingObjects_actionPerformed(e);
         }
      });

      resignFederationExecutionMenuItem3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            resignCancellingAcquistions_actionPerformed(e);
         }
      });

      resignFederationExecutionMenuItem4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            resignDeletingAndDivesting_actionPerformed(e);
         }
      });

       resignFederationExecutionMenuItem5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            resignCancelThenDeleteThenDivest_actionPerformed(e);
         }
      });

       resignFederationExecutionMenuItem6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            resignNoAction_actionPerformed(e);
         }
      });

//      jMenuItem69.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          disableAttributeScopeAdvisorySwitch_actionPerformed(e);
//        }
//      });
//      jMenuItem68.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          enableAttributeScopeAdvisorySwitch_actionPerformed(e);
//        }
//      });
//      jMenuItem67.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          disableInteractionRelevanceAdvisorySwitch_actionPerformed(e);
//        }
//      });
//      jMenuItem66.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          enableInteractionRelevanceAdvisorySwitch_actionPerformed(e);
//        }
//      });
//      jMenuItem65.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          disableClassRelevanceAdvisorySwitch_actionPerformed(e);
//        }
//      });
//      jMenuItem64.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          enableClassRelevanceAdvisorySwitch_actionPerformed(e);
//        }
//      });
//      jMenuItem63.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getDimensionName_actionPerformed(e);
//        }
//      });
//      jMenuItem62.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getDimensionHandle_actionPerformed(e);
//        }
//      });
//      jMenuItem61.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getRoutingSpaceName_actionPerformed(e);
//        }
//      });
//      jMenuItem60.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getRoutingSpaceHandle_actionPerformed(e);
//        }
//      });
//      jMenuItem59.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getObjectClass_actionPerformed(e);
//        }
//      });
//      jMenuItem58.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getObjectInstanceName_actionPerformed(e);
//        }
//      });
//      jMenuItem57.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getObjectInstanceHandle_actionPerformed(e);
//        }
//      });
//      jMenuItem56.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getParameterName_actionPerformed(e);
//        }
//      });
//      jMenuItem55.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getParameterHandle_actionPerformed(e);
//        }
//      });
//      jMenuItem54.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getInteractionClassName_actionPerformed(e);
//        }
//      });
//      jMenuItem53.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getInteractionClassHandle_actionPerformed(e);
//        }
//      });
//      jMenuItem52.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getAttributeName_actionPerformed(e);
//        }
//      });
//      jMenuItem51.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getAttributeHandle_actionPerformed(e);
//        }
//      });
//      jMenuItem50.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          getObjectClassName_actionPerformed(e);
//        }
//      });
//      jMenuItem49.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        getObjectClassHandle_actionPerformed(e);
//      }
//    });
//      jMenuItem48.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          sendInteractionWithRegionRO_actionPerformed(e);
//        }
//      });
//      jMenuItem47.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          subscribeInteractionClassWithRegion_actionPerformed(e);
//        }
//      });
//      jMenuItem46.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          createRegion_actionPerformed(e);
//        }
//      });
//      jMenuItem45.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          queryLookahead_actionPerformed(e);
//        }
//      });
//      jMenuItem44.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          modifyLookahead_actionPerformed(e);
//        }
//      });
//      jMenuItem43.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          queryMinimumNextEventTime_actionPerformed(e);
//        }
//      });
//      jMenuItem42.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          queryFederateTime_actionPerformed(e);
//        }
//      });
//      jMenuItem41.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          queryLBTS_actionPerformed(e);
//        }
//      });
//      jMenuItem40.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          enableAsynchronousDelivery_actionPerformed(e);
//        }
//      });
//      jMenuItem39.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          timeAdvanceRequestAvailable_actionPerformed(e);
//        }
//      });
//      jMenuItem38.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          timeAdvanceRequest_actionPerformed(e);
//        }
//      });
//      jMenuItem37.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          disableTimeConstrained_actionPerformed(e);
//        }
//      });
//      jMenuItem36.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          enableTimeConstrained_actionPerformed(e);
//        }
//      });
//      jMenuItem35.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          disableTimeRegulation_actionPerformed(e);
//        }
//      });
//      jMenuItem34.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          enableTimeRegulation_actionPerformed(e);
//        }
//      });
//      jMenuItem33.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          queryAttributeOwnership_actionPerformed(e);
//        }
//      });
//      jMenuItem32.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          cancelAttributeOwnershipAcquisition_actionPerformed(e);
//        }
//      });
//      jMenuItem31.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          cancelNegotiatedAttributeOwnershipDivenstiture_actionPerformed(e);
//        }
//      });
//      jMenuItem30.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          attributeOwnershipReleaseResponse_actionPerformed(e);
//        }
//      });
//      jMenuItem29.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          attributeOwnershipAcquisitionIfAvailable_actionPerformed(e);
//        }
//      });
//      jMenuItem28.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          attributeOwnershipAcquisition_actionPerformed(e);
//        }
//      });
//      jMenuItem27.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          negotiatedAttributeOwnershipDivestiture_actionPerformed(e);
//        }
//      });
//      jMenuItem26.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          unconditionalAttributeOwnershipDivestiture_actionPerformed(e);
//        }
//      });
//      jMenuItem25.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          updateAttributeValuesTSO_actionPerformed(e);
//        }
//      });
//      jMenuItem24.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          updateAttributeValuesRO_actionPerformed(e);
//        }
//      });
//      jMenuItem23.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          sendInteractionTSO_actionPerformed(e);
//        }
//      });
//      jMenuItem22.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          sendInteractionRO_actionPerformed(e);
//        }
//      });
//      jMenuItem21.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          deleteObjectInstanceWithTime_actionPerformed(e);
//        }
//      });
//      jMenuItem20.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          deleteObjectInstance_actionPerformed(e);
//        }
//      });
//      jMenuItem19.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          registerObjectInstanceWithName_actionPerformed(e);
//        }
//      });
//      jMenuItem18.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          registerObjectInstance_actionPerformed(e);
//        }
//      });
//      unsubscribeInteractionMenuItem.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          unsubscribeInteractionClass_actionPerformed(e);
//        }
//      });
//      subscribeInteractionPassivelyMenuItem.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          subscribeInteractionClassPassively_actionPerformed(e);
//        }
//      });
//      subscribeInteractionMenuItem.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          subscribeInteractionClass_actionPerformed(e);
//        }
//      });
//      unsubscribeObjectClassMenuItem.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          unsubscribeObjectClass_actionPerformed(e);
//        }
//      });
//      subscribeObjectPassivelyMenuItem.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        subscribeAttributesPassively_actionPerformed(e);
//      }
//    });
//      subscribeObjectMenuItem.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          subscribeObjectClassAttributes_actionPerformed(e);
//        }
//      });
//      unpublishInteractionClassMenuItem.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          unpublishInteractionClass_actionPerformed(e);
//        }
//      });
//      publishInteractionClassMenuItem.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          publishInteractionClass_actionPerformed(e);
//        }
//      });

//      unpublishObjectClassMenuItem.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            unpublishObjectClass_actionPerformed(e);
//         }
//      });

//      publishObjectClassMenuItem.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          publishObjectClass_actionPerformed(e);
//        }
//      });

      syncPointAchievedMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          synchronizationPointAchieved_actionPerformed(e);
        }
      });

      registerSyncPointForFederatesMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          registerForFederates_actionPerformed(e);
        }
      });

      registerSyncPointMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          registerSynchronizationPoint_actionPerformed(e);
        }
      });

      joinFederationExecutionMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            joinFederationExecution_Action(e);
         }
      });

      destroyFedExMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            destroyFedEx_Action(e);
         }
      });

      createFedExMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          createFedEx_Action(e);
        }
      });

      exitMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          exit_Action(e);
        }
      });
   }

   private void composeMenuComponents() {
      mainMenuBar.add(fileMenu);
      mainMenuBar.add(federationManagementMenu);
      mainMenuBar.add(declarationManagmentMenu);
      mainMenuBar.add(objectManagementMenu);
      mainMenuBar.add(ownershipManagementMenu);
      mainMenuBar.add(timeManagementMenu);
      mainMenuBar.add(dataDistributionManagementMenu);
      mainMenuBar.add(supportServicesMenu);

      fileMenu.add(exitMenuItem);
      federationManagementMenu.add(federationExecutionSubmenu);
      federationManagementMenu.add(federateSubmenu);
      federationManagementMenu.add(syncPointSubmenu);
      federationManagementMenu.add(saveFederationSubmenu);
      federationManagementMenu.add(restoreFederationSubmenu);
      federationExecutionSubmenu.add(createFedExMenuItem);
      federationExecutionSubmenu.add(destroyFedExMenuItem);
      federateSubmenu.add(joinFederationExecutionMenuItem);
      federateSubmenu.add(resignFederationExecutionSubmenu);
      syncPointSubmenu.add(registerSyncPointMenuItem);
      syncPointSubmenu.add(registerSyncPointForFederatesMenuItem);
      syncPointSubmenu.add(syncPointAchievedMenuItem);
      declarationManagmentMenu.add(publishSubmenu);
      declarationManagmentMenu.add(subscribeSubmenu);
      publishSubmenu.add(publishObjectClassMenuItem);
      publishSubmenu.add(unpublishObjectClassMenuItem);
      publishSubmenu.add(publishInteractionClassMenuItem);
      publishSubmenu.add(unpublishInteractionClassMenuItem);
      subscribeSubmenu.add(subscribeObjectMenuItem);
      subscribeSubmenu.add(subscribeObjectPassivelyMenuItem);
      subscribeSubmenu.add(unsubscribeObjectClassMenuItem);
      subscribeSubmenu.add(subscribeInteractionMenuItem);
      subscribeSubmenu.add(subscribeInteractionPassivelyMenuItem);
      subscribeSubmenu.add(unsubscribeInteractionMenuItem);
      objectManagementMenu.add(jMenu10);
      objectManagementMenu.add(jMenu13);
      objectManagementMenu.add(jMenu12);
      objectManagementMenu.add(jMenu11);
      objectManagementMenu.add(jMenuItem78);
      objectManagementMenu.add(jMenuItem79);
      objectManagementMenu.add(jMenuItem80);
      objectManagementMenu.add(jMenuItem109);
      jMenu10.add(jMenuItem18);
      jMenu10.add(jMenuItem19);
      jMenu11.add(jMenuItem20);
      jMenu11.add(jMenuItem21);
      jMenu11.add(jMenuItem77);
      jMenu12.add(jMenuItem22);
      jMenu12.add(jMenuItem23);
      jMenu13.add(jMenuItem24);
      jMenu13.add(jMenuItem25);
      ownershipManagementMenu.add(jMenuItem26);
      ownershipManagementMenu.add(jMenuItem27);
      ownershipManagementMenu.add(jMenuItem28);
      ownershipManagementMenu.add(jMenuItem29);
      ownershipManagementMenu.add(jMenuItem30);
      ownershipManagementMenu.add(jMenuItem31);
      ownershipManagementMenu.add(jMenuItem32);
      ownershipManagementMenu.add(jMenuItem33);
      ownershipManagementMenu.add(jMenuItem81);
      timeManagementMenu.add(jMenu16);
      timeManagementMenu.add(jMenu17);
      timeManagementMenu.add(jMenuItem40);
      timeManagementMenu.add(jMenuItem85);
      timeManagementMenu.add(jMenuItem41);
      timeManagementMenu.add(jMenuItem42);
      timeManagementMenu.add(jMenuItem43);
      timeManagementMenu.add(jMenuItem44);
      timeManagementMenu.add(jMenuItem45);
      timeManagementMenu.add(jMenuItem86);
      timeManagementMenu.add(jMenuItem87);
      timeManagementMenu.add(jMenuItem88);
      jMenu16.add(jMenuItem34);
      jMenu16.add(jMenuItem35);
      jMenu16.add(jMenuItem36);
      jMenu16.add(jMenuItem37);
      jMenu17.add(jMenuItem38);
      jMenu17.add(jMenuItem39);
      jMenu17.add(jMenuItem82);
      jMenu17.add(jMenuItem83);
      jMenu17.add(jMenuItem84);
      dataDistributionManagementMenu.add(jMenu19);
      dataDistributionManagementMenu.add(jMenu32);
      dataDistributionManagementMenu.add(jMenu21);
      jMenu19.add(jMenuItem46);
      jMenu19.add(jMenuItem89);
      jMenu19.add(jMenuItem90);
      jMenu21.add(jMenuItem47);
      jMenu21.add(jMenuItem111);
      jMenu21.add(jMenuItem97);
      jMenu21.add(jMenuItem48);
      jMenu21.add(jMenuItem98);
      supportServicesMenu.add(jMenu23);
      supportServicesMenu.add(jMenu24);
      supportServicesMenu.add(jMenu30);
      supportServicesMenu.add(jMenu25);
      supportServicesMenu.add(jMenu20);
      supportServicesMenu.add(jMenu26);
      supportServicesMenu.add(jMenu33);
      supportServicesMenu.add(jMenu28);
      supportServicesMenu.add(jMenu27);
      jMenu23.add(jMenuItem49);
      jMenu23.add(jMenuItem50);
      jMenu23.add(jMenuItem51);
      jMenu23.add(jMenuItem52);
      jMenu23.add(jMenuItem100);
      jMenu24.add(jMenuItem53);
      jMenu24.add(jMenuItem54);
      jMenu24.add(jMenuItem55);
      jMenu24.add(jMenuItem56);
      jMenu24.add(jMenuItem101);
      jMenu30.add(jMenuItem57);
      jMenu30.add(jMenuItem58);
      jMenu30.add(jMenuItem59);
      jMenu25.add(jMenuItem60);
      jMenu25.add(jMenuItem61);
      jMenu25.add(jMenuItem62);
      jMenu25.add(jMenuItem63);
      jMenu26.add(jMenuItem64);
      jMenu26.add(jMenuItem65);
      jMenu27.add(jMenuItem66);
      jMenu27.add(jMenuItem67);
      jMenu28.add(jMenuItem68);
      jMenu28.add(jMenuItem69);
      resignFederationExecutionSubmenu.add(resignFederationExecutionMenuItem1);
      resignFederationExecutionSubmenu.add(resignFederationExecutionMenuItem2);
      resignFederationExecutionSubmenu.add(resignFederationExecutionMenuItem3);
      resignFederationExecutionSubmenu.add(resignFederationExecutionMenuItem4);
      resignFederationExecutionSubmenu.add(resignFederationExecutionMenuItem5);
      resignFederationExecutionSubmenu.add(resignFederationExecutionMenuItem6);
      saveFederationSubmenu.add(requestFederationSaveMenuItem);
      saveFederationSubmenu.add(federateSaveBegunMenuItem);
      saveFederationSubmenu.add(federateSaveCompleteMenuItem);
      saveFederationSubmenu.add(federateSaveNotCompleteMenuItem);
      saveFederationSubmenu.add(queryFederationSaveStatusMenuItem);
      restoreFederationSubmenu.add(requestFederationRestoreMenuItem);
      restoreFederationSubmenu.add(federateRestoreCompleteMenuItem);
      restoreFederationSubmenu.add(federateRestoreNotCompleteMenuItem);
      restoreFederationSubmenu.add(queryFederationRestoreStatusMenuItem);
      jMenu32.add(jMenuItem91);
      jMenu32.add(jMenuItem92);
      jMenu32.add(jMenuItem93);
      jMenu32.add(jMenuItem94);
      jMenu32.add(jMenuItem95);
      jMenu32.add(jMenuItem110);
      jMenu32.add(jMenuItem96);
      jMenu32.add(jMenuItem99);
      jMenu20.add(jMenuItem102);
      jMenu20.add(jMenuItem103);
      jMenu20.add(jMenuItem104);
      jMenu20.add(jMenuItem105);
      jMenu33.add(jMenuItem106);
      jMenu33.add(jMenuItem107);
   }


   String _federateType; //null when not joined
  FederateHandle _federateHandle;
	String _fedexName = "";
	TestFederate _impl;
 	private FileBrowserFactory _fileBrowser;
	private InteractionBrowserFactory _interactionBrowser;
	private ObjectBrowserFactory _objectBrowser;
	private SpaceBrowserFactory _spaceBrowser;
  private static String _fileSeparator = System.getProperty("file.separator");
  private static String _userDirectory = System.getProperty("user.dir");
	private static String _newline = System.getProperty("line.separator");
  JMenuBar mainMenuBar = new JMenuBar();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea logArea = new JTextArea();
  JPanel jPanel1 = new JPanel();
  JButton clearLog = new JButton();
  BorderLayout borderLayout2 = new BorderLayout();
  JMenu fileMenu = new JMenu();
  JMenuItem exitMenuItem = new JMenuItem();
  JMenu federationManagementMenu = new JMenu();
  JMenu federationExecutionSubmenu = new JMenu();
  JMenuItem createFedExMenuItem = new JMenuItem();
  JMenuItem destroyFedExMenuItem = new JMenuItem();
  JMenu federateSubmenu = new JMenu();
  JMenuItem joinFederationExecutionMenuItem = new JMenuItem();
  JMenu syncPointSubmenu = new JMenu();
  JMenuItem registerSyncPointMenuItem = new JMenuItem();
  JMenuItem registerSyncPointForFederatesMenuItem = new JMenuItem();
  JMenuItem syncPointAchievedMenuItem = new JMenuItem();
  JMenu declarationManagmentMenu = new JMenu();
  JMenu publishSubmenu = new JMenu();
  JMenuItem publishObjectClassMenuItem = new JMenuItem();
  JMenuItem unpublishObjectClassMenuItem = new JMenuItem();
  JMenuItem publishInteractionClassMenuItem = new JMenuItem();
  JMenuItem unpublishInteractionClassMenuItem = new JMenuItem();
  JMenu subscribeSubmenu = new JMenu();
  JMenuItem subscribeObjectMenuItem = new JMenuItem();
  JMenuItem subscribeObjectPassivelyMenuItem = new JMenuItem();
  JMenuItem unsubscribeObjectClassMenuItem = new JMenuItem();
  JMenuItem subscribeInteractionMenuItem = new JMenuItem();
  JMenuItem subscribeInteractionPassivelyMenuItem = new JMenuItem();
  JMenuItem unsubscribeInteractionMenuItem = new JMenuItem();
  JMenu objectManagementMenu = new JMenu();
  JMenu jMenu10 = new JMenu();
  JMenuItem jMenuItem18 = new JMenuItem();
  JMenuItem jMenuItem19 = new JMenuItem();
  JMenu jMenu11 = new JMenu();
  JMenuItem jMenuItem20 = new JMenuItem();
  JMenuItem jMenuItem21 = new JMenuItem();
  JMenu jMenu12 = new JMenu();
  JMenuItem jMenuItem22 = new JMenuItem();
  JMenuItem jMenuItem23 = new JMenuItem();
  JMenu jMenu13 = new JMenu();
  JMenuItem jMenuItem24 = new JMenuItem();
  JMenuItem jMenuItem25 = new JMenuItem();
  JMenu ownershipManagementMenu = new JMenu();
  JMenuItem jMenuItem26 = new JMenuItem();
  JMenuItem jMenuItem27 = new JMenuItem();
  JMenuItem jMenuItem28 = new JMenuItem();
  JMenuItem jMenuItem29 = new JMenuItem();
  JMenuItem jMenuItem30 = new JMenuItem();
  JMenuItem jMenuItem31 = new JMenuItem();
  JMenuItem jMenuItem32 = new JMenuItem();
  JMenuItem jMenuItem33 = new JMenuItem();
  JMenu timeManagementMenu = new JMenu();
  JMenu jMenu16 = new JMenu();
  JMenuItem jMenuItem34 = new JMenuItem();
  JMenuItem jMenuItem35 = new JMenuItem();
  JMenuItem jMenuItem36 = new JMenuItem();
  JMenuItem jMenuItem37 = new JMenuItem();
  JMenu jMenu17 = new JMenu();
  JMenuItem jMenuItem38 = new JMenuItem();
  JMenuItem jMenuItem39 = new JMenuItem();
  JMenuItem jMenuItem40 = new JMenuItem();
  JMenuItem jMenuItem41 = new JMenuItem();
  JMenuItem jMenuItem42 = new JMenuItem();
  JMenuItem jMenuItem43 = new JMenuItem();
  JMenuItem jMenuItem44 = new JMenuItem();
  JMenuItem jMenuItem45 = new JMenuItem();
  JMenu dataDistributionManagementMenu = new JMenu();
  JMenu jMenu19 = new JMenu();
  JMenuItem jMenuItem46 = new JMenuItem();
  JMenu jMenu21 = new JMenu();
  JMenuItem jMenuItem47 = new JMenuItem();
  JMenuItem jMenuItem48 = new JMenuItem();
  JMenu supportServicesMenu = new JMenu();
  JMenu jMenu23 = new JMenu();
  JMenu jMenu24 = new JMenu();
  JMenuItem jMenuItem49 = new JMenuItem();
  JMenuItem jMenuItem50 = new JMenuItem();
  JMenuItem jMenuItem51 = new JMenuItem();
  JMenuItem jMenuItem52 = new JMenuItem();
  JMenuItem jMenuItem53 = new JMenuItem();
  JMenuItem jMenuItem54 = new JMenuItem();
  JMenuItem jMenuItem55 = new JMenuItem();
  JMenuItem jMenuItem56 = new JMenuItem();
  JMenu jMenu30 = new JMenu();
  JMenuItem jMenuItem57 = new JMenuItem();
  JMenuItem jMenuItem58 = new JMenuItem();
  JMenuItem jMenuItem59 = new JMenuItem();
  JMenu jMenu25 = new JMenu();
  JMenuItem jMenuItem60 = new JMenuItem();
  JMenuItem jMenuItem61 = new JMenuItem();
  JMenuItem jMenuItem62 = new JMenuItem();
  JMenuItem jMenuItem63 = new JMenuItem();
  JMenu jMenu26 = new JMenu();
  JMenuItem jMenuItem64 = new JMenuItem();
  JMenuItem jMenuItem65 = new JMenuItem();
  JMenu jMenu27 = new JMenu();
  JMenuItem jMenuItem66 = new JMenuItem();
  JMenuItem jMenuItem67 = new JMenuItem();
  JMenu jMenu28 = new JMenu();
  JMenuItem jMenuItem68 = new JMenuItem();
  JMenuItem jMenuItem69 = new JMenuItem();
  JMenu resignFederationExecutionSubmenu = new JMenu();
  JMenuItem resignFederationExecutionMenuItem1 = new JMenuItem();
  JMenuItem resignFederationExecutionMenuItem2 = new JMenuItem();
  JMenuItem resignFederationExecutionMenuItem3 = new JMenuItem();
  JMenuItem resignFederationExecutionMenuItem4 = new JMenuItem();
  JMenuItem resignFederationExecutionMenuItem5 = new JMenuItem();
  JMenuItem resignFederationExecutionMenuItem6 = new JMenuItem();
  JMenu saveFederationSubmenu = new JMenu();
  JMenuItem requestFederationSaveMenuItem = new JMenuItem();
  JMenuItem federateSaveBegunMenuItem = new JMenuItem();
  JMenuItem federateSaveCompleteMenuItem = new JMenuItem();
  JMenu restoreFederationSubmenu = new JMenu();
  JMenuItem requestFederationRestoreMenuItem = new JMenuItem();
  JMenuItem federateRestoreCompleteMenuItem = new JMenuItem();
  JMenuItem jMenuItem77 = new JMenuItem();
  JMenuItem jMenuItem78 = new JMenuItem();
  JMenuItem jMenuItem79 = new JMenuItem();
  JMenuItem jMenuItem80 = new JMenuItem();
  JMenuItem jMenuItem81 = new JMenuItem();
  JMenuItem jMenuItem82 = new JMenuItem();
  JMenuItem jMenuItem83 = new JMenuItem();
  JMenuItem jMenuItem84 = new JMenuItem();
  JMenuItem jMenuItem85 = new JMenuItem();
  JMenuItem jMenuItem86 = new JMenuItem();
  JMenuItem jMenuItem87 = new JMenuItem();
  JMenuItem jMenuItem88 = new JMenuItem();
  JMenuItem jMenuItem89 = new JMenuItem();
  JMenuItem jMenuItem90 = new JMenuItem();
  JMenu jMenu32 = new JMenu();
  JMenuItem jMenuItem91 = new JMenuItem();
  JMenuItem jMenuItem92 = new JMenuItem();
  JMenuItem jMenuItem93 = new JMenuItem();
  JMenuItem jMenuItem94 = new JMenuItem();
  JMenuItem jMenuItem95 = new JMenuItem();
  JMenuItem jMenuItem96 = new JMenuItem();
  JMenuItem jMenuItem97 = new JMenuItem();
  JMenuItem jMenuItem98 = new JMenuItem();
  JMenuItem jMenuItem99 = new JMenuItem();
  JMenuItem jMenuItem100 = new JMenuItem();
  JMenuItem jMenuItem101 = new JMenuItem();
  JMenu jMenu20 = new JMenu();
  JMenuItem jMenuItem102 = new JMenuItem();
  JMenuItem jMenuItem103 = new JMenuItem();
  JMenuItem jMenuItem104 = new JMenuItem();
  JMenuItem jMenuItem105 = new JMenuItem();
  JMenu jMenu33 = new JMenu();
  JMenuItem jMenuItem106 = new JMenuItem();
  JMenuItem jMenuItem107 = new JMenuItem();
  JMenuItem federateSaveNotCompleteMenuItem = new JMenuItem();
  JMenuItem federateRestoreNotCompleteMenuItem = new JMenuItem();
  JMenuItem queryFederationSaveStatusMenuItem = new JMenuItem();
  JMenuItem queryFederationRestoreStatusMenuItem = new JMenuItem();
  JMenuItem jMenuItem109 = new JMenuItem();
  javax.swing.JMenuItem jMenuItem110 = new javax.swing.JMenuItem();
  javax.swing.JMenuItem jMenuItem111 = new javax.swing.JMenuItem();

  public void finishConstruction(TestFederate impl)
	{
    setSize(500, 300);
    setNotJoined();
		_impl = impl;
		setTitle("Test Federate");
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //we'll handle it
    _fileBrowser = new FileBrowserFactory();
	}


	void createFedEx_Action(ActionEvent event)
	{
      StringsDialogProxy proxy = new StringsDialogProxy();
      String[] labels = {
         "Federation execution name:",
         "FED:"
      };
      String[] defaults = {
         "SushiRestaurant",
         "Restaurant.xml"};
      BrowserFactory[] browsers = {null, _fileBrowser};
      int result = proxy.queryUser(
              this,
              "Create Federation Execution",
              labels,
              defaults,
              browsers);
      if (result == StringsDialog.CANCEL) return;
      else if (result == StringsDialog.OK) {
         String[] stuff = proxy.results();
         _impl.createFederationExecution(stuff);
         _fedexName = stuff[0];
      }
      else {
         post("StringsDialog returned " + result);
      }
  }


	void destroyFedEx_Action(ActionEvent event)
	{
		StringsDialogProxy proxy = new StringsDialogProxy();
		String[] labels = {
		  "Federation execution name:"
		};
		String[] defaults = {
         _fedexName == null ? "" : _fedexName
      };
		BrowserFactory[] browsers = {null};
		int result = proxy.queryUser(
		  this,
		  "Destroy Federation Execution",
		  labels,
		  defaults,
		  browsers);
		if (result == StringsDialog.CANCEL) return;
		else if (result == StringsDialog.OK) {
		  String[] stuff = proxy.results();
  		_impl.destroyFederationExecution(stuff[0]);
  	}
  	else {
  	  post("StringsDialog returned " + result);
  	}
	}

	//called by impl when connected to to RTI object
	void enableRemote() {
	  createFedExMenuItem.setEnabled(true);
	  destroyFedExMenuItem.setEnabled(true);
	  joinFederationExecutionMenuItem.setEnabled(true);
	}

   //used by impl to keep UI current
	public void setJoined(String fedexName, FederateHandle federateHandle, String type) {
    joinFederationExecutionMenuItem.setEnabled(false);
    resignFederationExecutionSubmenu.setEnabled(true);
	  _federateType = type;
    _federateHandle = federateHandle;
	  _fedexName = fedexName;
	  setTitle("Test Federate [Joined to " + _fedexName + " as " + _federateHandle + "]");
	}

  //called by impl after successful load of FED info
  public void setFEDinfoAvailable() {
    _objectBrowser = new ObjectBrowserFactory();
    _interactionBrowser = new InteractionBrowserFactory();
    _spaceBrowser = new SpaceBrowserFactory();
  }

  //called upon resign, or if FED load unsuccessful
  public void setFEDinfoUnavailable() {
    _objectBrowser = null;
    _interactionBrowser = null;
    _spaceBrowser = null;
  }


	//used by impl to keep UI current
	public void setNotJoined() {
      _federateType = null;
      _federateHandle = null;
      setTitle("Test Federate [Not joined]");
      joinFederationExecutionMenuItem.setEnabled(true);
      resignFederationExecutionSubmenu.setEnabled(false);
	}

  void exit_Action(ActionEvent e) {
    if (_federateType == null) System.exit(0);
    String string1 = "Exit";
    String string2 = "Cancel";
    Object[] options = {string1, string2};
    Object[] message = {
      "This will exit the federate abruptly.    ",
      "Continue?"};
    int n = JOptionPane.showOptionDialog(
      this,
      message,
      "Test Federate - Exit",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.WARNING_MESSAGE,
      null,     //don't use a custom Icon
      options,  //the titles of buttons
      string2); //the title of the default button
    if (n == JOptionPane.YES_OPTION) System.exit(0);
  }

//  hla.rti.AttributeHandleSet parseAttributeHandles(String stuff) {
//    try {
//      hla.rti.AttributeHandleSet ahset = _impl._attributeHandleSetFactory.create();
//      StreamTokenizer tokenizer =
//        new StreamTokenizer(new StringReader(stuff));
//      tokenizer.whitespaceChars(' ', ' '); //separate attr handles with blanks
//      tokenizer.whitespaceChars(',', ','); //or commas
//      while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
//        if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
//          int num = (int)(tokenizer.nval);
//          if (num < 0) {
//            post("handle " + num + " is out of range.");
//            return null;
//          }
//          ahset.add(num);
//        }
//        else {
//          post("token " + tokenizer.sval + " is invalid.");
//          return null;
//        }
//      }
//      return ahset;
//    }
//    catch (Exception e) {
//      post("AH tokenizer exec " + e);
//      return null;
//    }
//  }

   FederateHandleSet parseFederateHandles(String stuff) {
      try {
         FederateHandleSet fhset = _impl._federateHandleSetFactory.create();
         StreamTokenizer tokenizer =
                 new StreamTokenizer(new StringReader(stuff));
         tokenizer.whitespaceChars(' ', ' '); //separate fed handles with blanks
         tokenizer.whitespaceChars(',', ','); //or commas
         while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
               int num = (int)(tokenizer.nval);
               //Todo Implement this part... Need to provide a number to FederateHandle mapping
            }
            else {
               post("token " + tokenizer.sval + " is invalid.");
               return null;
            }
         }
         return fhset;
      }
      catch (Exception e) {
         return null;
      }
   }

	public void post(String line) {
	  logArea.append(line + _newline);
	}

  void clearLog_actionPerformed(ActionEvent e) {
    logArea.setText("");
  }

   void registerSynchronizationPoint_actionPerformed(ActionEvent e) {
      StringsDialogProxy proxy = new StringsDialogProxy();
      String[] labels = {
         "Synchronization point label:",
         "User-supplied tag:"
      };
      String[] defaults = {"", ""};
      BrowserFactory[] browsers = {null, null};
      int result = proxy.queryUser(
              this,
              "Register Federation Synchronization Point",
              labels,
              defaults,
              browsers);
      if (result == StringsDialog.CANCEL) return;
      else if (result == StringsDialog.OK) {
         String[] stuff = proxy.results();
         _impl.registerFederationSynchronizationPoint(
                 stuff[0],
                 stuff[1].getBytes());
      }
      else {
         post("StringsDialog returned " + result);
      }
   }

   void registerForFederates_actionPerformed(ActionEvent e) {
      post("This service is not implemented yet.");
//      StringsDialogProxy proxy = new StringsDialogProxy();
//      String[] labels = {
//         "Synchronization point label:",
//         "User-supplied tag:",
//         "Federate handles:"
//      };
//      String[] defaults = {"", "", ""};
//      BrowserFactory[] browsers = {null, null, null};
//      int result = proxy.queryUser(
//              this,
//              "Register Federation Synchronization Point",
//              labels,
//              defaults,
//              browsers);
//      if (result == StringsDialog.CANCEL) return;
//      else if (result == StringsDialog.OK) {
//         String[] stuff = proxy.results();
//         FederateHandleSet fhset = parseFederateHandles(stuff[2]);
//         post("Got fed handles " + fhset);
//         if (fhset != null) {
//            _impl.registerFederationSynchronizationPoint(
//                    stuff[0],
//                    stuff[1].getBytes(),
//                    fhset);
//         }
//         else post("Invalid federate handles: " + stuff[2]);
//      }
//      else {
//         post("StringsDialog returned " + result);
//      }
   }

   void synchronizationPointAchieved_actionPerformed(ActionEvent e) {
      StringsDialogProxy proxy = new StringsDialogProxy();
      String[] labels = {"Synchronization point label:"};
      String[] defaults = {""};
      BrowserFactory[] browsers = {null};
      int result = proxy.queryUser(
              this,
              "Synchronization Point Achieved",
              labels,
              defaults,
              browsers);
      if (result == StringsDialog.CANCEL) return;
      else if (result == StringsDialog.OK) {
         String[] stuff = proxy.results();
         _impl.synchronizationPointAchieved(stuff[0]);
      }
      else {
         post("StringsDialog returned " + result);
      }
   }

//   void publishObjectClass_actionPerformed(ActionEvent e) {
//      StringsDialogProxy proxy = new StringsDialogProxy();
//      String[] labels = {
//         "Object class name or handle:",
//         "Attribute handles:"
//      };
//      String[] defaults = {"", ""};
//      BrowserFactory[] browsers = {_objectBrowser, null};
//      int result = proxy.queryUser(
//              this,
//              "Publish Object Class",
//              labels,
//              defaults,
//              browsers);
//      if (result == StringsDialog.CANCEL) return;
//      else if (result == StringsDialog.OK) {
//         String[] stuff = proxy.results();
//         AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//         if (ahset != null) {
//            _impl.publishObjectClass(
//                    stuff[0],
//                    ahset);
//         }
//         else post("Invalid attribute handles: " + stuff[1]);
//      }
//      else {
//         post("StringsDialog returned " + result);
//      }
//   }

//  void unpublishObjectClass_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object class name or handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {_objectBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Unpublish Object Class",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.unpublishObjectClass(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void publishInteractionClass_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class name or handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {_interactionBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Publish Interaction Class",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.publishInteractionClass(
//        stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void unpublishInteractionClass_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class name or handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {_interactionBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Unpublish Interaction Class",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.unpublishInteractionClass(
//        stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void subscribeObjectClassAttributes_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object class name or handle:",
//		  "Attribute handles:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_objectBrowser, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Subscribe Object Class Attributes",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.subscribeObjectClassAttributes(
//    		  stuff[0],
//          ahset);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void unsubscribeObjectClass_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object class name or handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {_objectBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Unsubscribe Object Class",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.unsubscribeObjectClass(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void subscribeInteractionClass_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class name or handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {_interactionBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Subscribe Interaction Class",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.subscribeInteractionClass(
//        stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void subscribeInteractionClassPassively_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class name or handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {_interactionBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Subscribe Interaction Class Passively",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.subscribeInteractionClassPassively(
//        stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void unsubscribeInteractionClass_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class name or handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {_interactionBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Unsubscribe Interaction Class",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.unsubscribeInteractionClass(
//        stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void registerObjectInstance_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object class name or handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {_objectBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Register Object Instance",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.registerObjectInstance(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void registerObjectInstanceWithName_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object class name or handle:",
//      "Object instance name:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_objectBrowser, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Register Object Instance With Name",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.registerObjectInstance(stuff[0], stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void deleteObjectInstance_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Object instance handle:",
//      "User-supplied tag:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Delete Object Instance",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.deleteObjectInstance(stuff[0], stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void deleteObjectInstanceWithTime_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Object instance handle:",
//      "User-supplied tag:",
//      "Time:"
//		};
//		String[] defaults = {"", "", ""};
//		BrowserFactory[] browsers = {null, null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Delete Object Instance With Time",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.deleteObjectInstanceWithTime(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void sendInteractionRO_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Interaction class:",
//      "Parameter handle 1:",
//      "Parameter value 1:",
//      "Parameter handle 2:",
//      "Parameter value 2:",
//      "Parameter handle 3:",
//      "Parameter value 3:",
//      "User-supplied tag:"
//		};
//		String[] defaults = {"", "", "", "", "", "", "", ""};
//		BrowserFactory[] browsers = {
//      _interactionBrowser,
//      _interactionBrowser,
//      null,
//      _interactionBrowser,
//      null,
//      _interactionBrowser,
//      null,
//      null};
//		int result = proxy.queryUser(
//		  this,
//		  "Send RO Interaction",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.sendROinteraction(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void sendInteractionTSO_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Interaction class:",
//      "Parameter handle 1:",
//      "Parameter value 1:",
//      "Parameter handle 2:",
//      "Parameter value 2:",
//      "Parameter handle 3:",
//      "Parameter value 3:",
//      "User-supplied tag:",
//      "Time:"
//		};
//		String[] defaults = {"", "", "", "", "", "", "", "", ""};
//		BrowserFactory[] browsers = {
//      _interactionBrowser,
//      _interactionBrowser,
//      null,
//      _interactionBrowser,
//      null,
//      _interactionBrowser,
//      null,
//      null,
//      null};
//		int result = proxy.queryUser(
//		  this,
//		  "Send TSO Interaction",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.sendTSOinteraction(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void updateAttributeValuesRO_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Object name or handle:",
//      "Attribute handle 1:",
//      "Attribute value 1:",
//      "Attribute handle 2:",
//      "Attribute value 2:",
//      "Attribute handle 3:",
//      "Attribute value 3:",
//      "User-supplied tag:"
//		};
//		String[] defaults = {"", "", "", "", "", "", "", ""};
//		BrowserFactory[] browsers = {
//      null,
//      _objectBrowser,
//      null,
//      _objectBrowser,
//      null,
//      _objectBrowser,
//      null,
//      null};
//		int result = proxy.queryUser(
//		  this,
//		  "Update Attribute Values RO",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.updateAttributeValuesRO(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void updateAttributeValuesTSO_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Object name or handle:",
//      "Attribute handle 1:",
//      "Attribute value 1:",
//      "Attribute handle 2:",
//      "Attribute value 2:",
//      "Attribute handle 3:",
//      "Attribute value 3:",
//      "User-supplied tag:",
//      "Time:"
//		};
//		String[] defaults = {"", "", "", "", "", "", "", "", ""};
//		BrowserFactory[] browsers = {
//      null,
//      _objectBrowser,
//      null,
//      _objectBrowser,
//      null,
//      _objectBrowser,
//      null,
//      null,
//      null};
//		int result = proxy.queryUser(
//		  this,
//		  "Update Attribute Values TSO",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.updateAttributeValuesTSO(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }

   void joinFederationExecution_Action(ActionEvent e) {
      StringsDialogProxy proxy = new StringsDialogProxy();
      String[] labels = {
         "Federation execution name:",
         "Federate type:"
      };
      String federation = _fedexName.equals("") ? "SushiRestaurant" : _fedexName;
      String[] defaults = {
         federation,
         "TestFederate"
      };
      BrowserFactory[] browsers = {null, null};
      int result = proxy.queryUser(
              this,
              "Join Federation Execution",
              labels,
              defaults,
              browsers);
      if (result == StringsDialog.CANCEL) return;
      else if (result == StringsDialog.OK) {
         String[] stuff = proxy.results();
         _impl.joinFederationExecution(stuff);
      }
      else {
         post("StringsDialog returned " + result);
      }
   }

//  void unconditionalAttributeOwnershipDivestiture_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//		  "Attribute handles:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Unconditional Attribute Ownership Divestiture",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.unconditionalAttributeOwnershipDivestiture(
//    		  stuff[0],
//          ahset);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void negotiatedAttributeOwnershipDivestiture_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//		  "Attribute handles:",
//      "User-supplied tag:"
//		};
//		String[] defaults = {"", "", ""};
//		BrowserFactory[] browsers = {null, null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Negotiated Attribute Ownership Divestiture",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.negotiatedAttributeOwnershipDivestiture(
//    		  stuff[0],
//          ahset,
//          stuff[2]);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void attributeOwnershipAcquisition_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//		  "Attribute handles:",
//      "User-supplied tag:"
//		};
//		String[] defaults = {"", "", ""};
//		BrowserFactory[] browsers = {null, null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Attribute Ownership Acquisition",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.attributeOwnershipAcquisition(
//    		  stuff[0],
//          ahset,
//          stuff[2]);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void attributeOwnershipAcquisitionIfAvailable_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//		  "Attribute handles:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Attribute Ownership Acquisition If Available",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.attributeOwnershipAcquisitionIfAvailable(
//    		  stuff[0],
//          ahset);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void attributeOwnershipReleaseResponse_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//		  "Attribute handles:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Attribute Ownership Release Response",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.attributeOwnershipReleaseResponse(
//    		  stuff[0],
//          ahset);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void cancelNegotiatedAttributeOwnershipDivenstiture_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//		  "Attribute handles:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Cancel Negotiated Attribute Ownership Divestiture",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.cancelNegotiatedAttributeOwnershipDivestiture(
//    		  stuff[0],
//          ahset);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void cancelAttributeOwnershipAcquisition_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//		  "Attribute handles:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Cancel Attribute Ownership Acquisition",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.cancelAttributeOwnershipAcquisition(
//    		  stuff[0],
//          ahset);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void queryAttributeOwnership_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//		  "Attribute handle:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Query Attribute Ownership",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.queryAttributeOwnership(stuff[1], stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void enableTimeRegulation_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "logical time:",
//		  "lookahead:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Enable Time Regulation",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.enableTimeRegulation(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void disableTimeRegulation_actionPerformed(ActionEvent e) {
//    _impl.disableTimeRegulation();
//  }
//
//  void enableTimeConstrained_actionPerformed(ActionEvent e) {
//    _impl.enableTimeConstrained();
//  }
//
//  void disableTimeConstrained_actionPerformed(ActionEvent e) {
//    _impl.disableTimeConstrained();
//  }
//
//  void timeAdvanceRequest_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "logical time:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Time Advance Request",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.timeAdvanceRequest(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void timeAdvanceRequestAvailable_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "logical time:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Time Advance Request Available",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.timeAdvanceRequestAvailable(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void enableAsynchronousDelivery_actionPerformed(ActionEvent e) {
//    _impl.enableAsynchronousDelivery();
//  }
//
//  void queryLBTS_actionPerformed(ActionEvent e) {
//     _impl.queryLBTS();
//  }
//
//  void queryFederateTime_actionPerformed(ActionEvent e) {
//     _impl.queryFederateTime();
//  }
//
//  void queryMinimumNextEventTime_actionPerformed(ActionEvent e) {
//     _impl.queryMinimumNextEventTime();
//  }
//
//  void modifyLookahead_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "new lookahead:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Modify Lookahead",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.modifyLookahead(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void queryLookahead_actionPerformed(ActionEvent e) {
//    _impl.queryLookahead();
//  }
//
//  void createRegion_actionPerformed(ActionEvent e) {
//    StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "local name:",
//      "Space name:",
//      "Dimension name A:",
//      "Dimension A lower bound:",
//      "Dimension A upper bound:",
//      "Dimension name B:",
//      "Dimension B lower bound:",
//      "Dimension B upper bound:",
//      "Dimension name C:",
//      "Dimension C lower bound:",
//      "Dimension C upper bound:"
//		};
//		String[] defaults = {"", "", "", "", "", "", "", "", "", "", ""};
//		BrowserFactory[] browsers = {null, null, null, null, null, null, null, null, null, null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Create Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.createRegion(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void subscribeInteractionClassWithRegion_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class name or handle:",
//      "Local name for region:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_interactionBrowser, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Subscribe Interaction Class With Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.subscribeInteractionClassWithRegion(
//        stuff[0],
//        stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void sendInteractionWithRegionRO_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Interaction class:",
//      "Parameter handle 1:",
//      "Parameter value 1:",
//      "Parameter handle 2:",
//      "Parameter value 2:",
//      "Parameter handle 3:",
//      "Parameter value 3:",
//      "User-supplied tag:",
//      "Local name of region:"
//		};
//		String[] defaults = {"", "", "", "", "", "", "", "", ""};
//		BrowserFactory[] browsers = {
//      _interactionBrowser,
//      _interactionBrowser,
//      null,
//      _interactionBrowser,
//      null,
//      _interactionBrowser,
//      null,
//      null,
//      null};
//		int result = proxy.queryUser(
//		  this,
//		  "Send RO Interaction With Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.sendROinteractionWithRegion(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getObjectClassHandle_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object class name:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {_objectBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Object Class Handle",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		int handle = _impl.getObjectClassHandle(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getObjectClassName_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object class handle:",
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Object Class Name",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getObjectClassName(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getAttributeHandle_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object class name or handle:",
//		  "Attribute name:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_objectBrowser, _objectBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Attribute Handle",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getAttributeHandle(stuff[1], stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getAttributeName_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object class name or handle:",
//		  "Attribute handle:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_objectBrowser, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Attribute Name",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getAttributeName(stuff[1], stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getInteractionClassHandle_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class name:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {_interactionBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Interaction Class Handle",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		int handle = _impl.getInteractionClassHandle(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getInteractionClassName_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class handle:",
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Interaction Class Name",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getInteractionClassName(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getParameterHandle_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class name:",
//		  "Parameter name:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_interactionBrowser, _interactionBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Parameter Handle",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getParameterHandle(stuff[1], stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getParameterName_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class name or handle:",
//		  "Parameter handle:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_interactionBrowser, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Parameter Name",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getParameterName(stuff[1], stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getObjectInstanceHandle_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Object instance name:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Object Instance Handle",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getObjectInstanceHandle(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getObjectInstanceName_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Object instance handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Object Instance Name",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getObjectInstanceName(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getObjectClass_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance name or handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Object Class",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getObjectClass(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getRoutingSpaceHandle_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Routing space name:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {_spaceBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Routing Space Handle",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		int handle = _impl.getRoutingSpaceHandle(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getRoutingSpaceName_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Routing space handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Routing Space Name",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		String name = _impl.getRoutingSpaceName(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getDimensionHandle_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Space name or handle",
//      "Dimension name:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_spaceBrowser, _spaceBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Dimension Handle",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getDimensionHandle(stuff[1], stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getDimensionName_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Space name or handle:",
//		  "Dimension handle:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_spaceBrowser, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Dimension Name",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getDimensionName(stuff[1], stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void enableClassRelevanceAdvisorySwitch_actionPerformed(ActionEvent e) {
//    _impl.enableClassRelevanceAdvisorySwitch();
//  }
//
//  void disableClassRelevanceAdvisorySwitch_actionPerformed(ActionEvent e) {
//    _impl.disableClassRelevanceAdvisorySwitch();
//  }
//
//  void enableInteractionRelevanceAdvisorySwitch_actionPerformed(ActionEvent e) {
//    _impl.enableInteractionRelevanceAdvisorySwitch();
//  }
//
//  void disableInteractionRelevanceAdvisorySwitch_actionPerformed(ActionEvent e) {
//    _impl.disableInteractionRelevanceAdvisorySwitch();
//  }
//
//  void enableAttributeScopeAdvisorySwitch_actionPerformed(ActionEvent e) {
//    _impl.enableAttributeScopeAdvisorySwitch();
//  }
//
//  void disableAttributeScopeAdvisorySwitch_actionPerformed(ActionEvent e) {
//    _impl.disableAttributeScopeAdvisorySwitch();
//  }
//
//  void subscribeAttributesPassively_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object class name or handle:",
//		  "Attribute handles:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_objectBrowser, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Subscribe Object Class Attributes Passively",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.subscribeObjectClassAttributesPassively(
//    		  stuff[0],
//          ahset);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void loadFEDinfo() {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {"Configuration path URL (blank is default):"};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get URL to FED",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      //more to do here
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }

  void this_windowClosing(WindowEvent e) {
    if (_federateType == null) System.exit(0);
    String string1 = "Exit";
    String string2 = "Cancel";
    Object[] options = {string1, string2};
    Object[] message = {
      "This will exit the federate abruptly.    ",
      "Continue?"};
    int n = JOptionPane.showOptionDialog(
      this,
      message,
      "Test Federate - Exit",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.WARNING_MESSAGE,
      null,     //don't use a custom Icon
      options,  //the titles of buttons
      string2); //the title of the default button
    if (n == JOptionPane.YES_OPTION) System.exit(0);
  }

   void resignDivestingAttributes_actionPerformed(ActionEvent e) {
      _impl.resignFederationExecution("UNCONDITIONALLY_DIVEST_ATTRIBUTES",
              ResignAction.UNCONDITIONALLY_DIVEST_ATTRIBUTES);
   }

   void resignDeletingObjects_actionPerformed(ActionEvent e) {
      _impl.resignFederationExecution("DELETE_OBJECTS", ResignAction.DELETE_OBJECTS);
   }

   private void resignCancellingAcquistions_actionPerformed(ActionEvent e) {
       _impl.resignFederationExecution("CANCEL_PENDING_OWNERSHIP_ACQUISITIONS",
               ResignAction.CANCEL_PENDING_OWNERSHIP_ACQUISITIONS);
   }

   void resignDeletingAndDivesting_actionPerformed(ActionEvent e) {
      _impl.resignFederationExecution("DELETE_OBJECTS_THEN_DIVEST",
              ResignAction.DELETE_OBJECTS_THEN_DIVEST);
   }

   private void resignCancelThenDeleteThenDivest_actionPerformed(ActionEvent e) {
       _impl.resignFederationExecution("CANCEL_THEN_DELETE_THEN_DIVEST",
               ResignAction.CANCEL_THEN_DELETE_THEN_DIVEST);
   }

   void resignNoAction_actionPerformed(ActionEvent e) {
      _impl.resignFederationExecution("NO_ACTION", ResignAction.NO_ACTION);
   }

  void requestFederationSave_actionPerformed(ActionEvent e) {
		StringsDialogProxy proxy = new StringsDialogProxy();
		String[] labels = {"Save label:", "Save time (optional):"};
		String[] defaults = {"", ""};
		BrowserFactory[] browsers = {null, null};
		int result = proxy.queryUser(
		  this,
		  "Request Federation Save",
		  labels,
		  defaults,
		  browsers);
		if (result == StringsDialog.CANCEL) return;
		else if (result == StringsDialog.OK) {
		  String[] stuff = proxy.results();
  		_impl.requestFederationSave(stuff);
  	}
  	else {
  	  post("StringsDialog returned " + result);
  	}
  }

  void federateSaveBegun_actionPerformed(ActionEvent e) {
    _impl.federateSaveBegun();
  }

  void federateSaveComplete_actionPerformed(ActionEvent e) {
    _impl.federateSaveComplete();
  }

   void requestFederationRestore_actionPerformed(ActionEvent e) {
      StringsDialogProxy proxy = new StringsDialogProxy();
      String[] labels = {"Save label:"};
      String[] defaults = {""};
      BrowserFactory[] browsers = {null};
      int result = proxy.queryUser(
              this,
              "Request Federation Restore",
              labels,
              defaults,
              browsers);
      if (result == StringsDialog.CANCEL) return;
      else if (result == StringsDialog.OK) {
         String[] stuff = proxy.results();
         _impl.requestFederationRestore(stuff);
      }
      else {
         post("StringsDialog returned " + result);
      }
   }
//   void requestFederationRestore_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {"Save label:"};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Request Federation Restore",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.requestFederationRestore(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }

   void federateRestoreComplete_actionPerformed(ActionEvent e) {
      _impl.federateRestoreComplete();
   }

//
//  void localDeleteObjectInstance_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {"object instance handle:"};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Local Delete Object Instance",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.localDeleteObjectInstance(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void changeAttributeTransportationType_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//		  "Attribute handles:",
//      "Transportation handle:"
//		};
//		String[] defaults = {"", "", ""};
//		BrowserFactory[] browsers = {null, null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Change Attribute Transportation Type",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.changeAttributeTransportationType(
//    		  stuff[0],
//          ahset,
//          stuff[2]);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void changeInteractionTransportationType_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction handle:",
//      "Transportation handle:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Change Interaction Transportation Type",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.changeInteractionTransportationType(
//        stuff[0], stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void isAttributeOwnedByFederate_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//      "Attribute handle:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Is Attribute Owned By Federate",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.isAttributeOwnedByFederate(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void nextEventRequest_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "logical time:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Next Event Request",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.nextEventRequest(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void nextEventRequestAvailable_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "logical time:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Next Event Request Available",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.nextEventRequestAvailable(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void flushQueueRequest_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "logical time:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Flush Queue Request",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.flushQueueRequest(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void disableAsynchronousDelivery_actionPerformed(ActionEvent e) {
//    _impl.disableAsynchronousDelivery();
//  }
//
//  void retract_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Local retraction handle:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Retract",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.retract(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void changeAttributeOrderType_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//		  "Attribute handles:",
//      "Order handle:"
//		};
//		String[] defaults = {"", "", ""};
//		BrowserFactory[] browsers = {null, null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Change Attribute Order Type",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.changeAttributeOrderType(
//    		  stuff[0],
//          ahset,
//          stuff[2]);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void changeInteractionOrderType_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction handle:",
//      "Order handle:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Change Interaction Order Type",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.changeInteractionOrderType(
//        stuff[0], stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void modifyRegion_actionPerformed(ActionEvent e) {
//    String[] stuff = null;
//    String[] currentValues = null;
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "local name:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Modify Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  stuff = proxy.results();
//  		currentValues = _impl.lookUpRegion(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//    if (currentValues == null) return;
//		String[] moreLabels = {
//      "local name:",
//      "Space name:",
//      "Dimension name A:",
//      "Dimension A lower bound:",
//      "Dimension A upper bound:",
//      "Dimension name B:",
//      "Dimension B lower bound:",
//      "Dimension B upper bound:",
//      "Dimension name C:",
//      "Dimension C lower bound:",
//      "Dimension C upper bound:"
//		};
//		BrowserFactory[] moreBrowsers = {null, null, null, null, null, null, null, null, null, null, null};
//		result = proxy.queryUser(
//		  this,
//		  "Modify Region",
//		  moreLabels,
//		  currentValues,
//		  moreBrowsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  stuff = proxy.results();
//  		_impl.modifyRegion(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void deleteRegion_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "local name:"
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Delete Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.deleteRegion(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void registerObjectInstanceWithRegion_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Object class name or handle:",
//      "First Attribute name or handle:",
//      "First Region local name:",
//      "Second Attribute name or handle:",
//      "Second Region local name:",
//      "Third Attribute name or handle:",
//      "Third Region local name:"
//		};
//		String[] defaults = {"", "", "", "", "", "", ""};
//		BrowserFactory[] browsers = {null, null, null, null, null, null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Register Object Instance With Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.registerObjectInstanceWithRegion(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void registerObjectInstanceWithRegionAndName_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Object class name or handle:",
//      "Object instance name:",
//      "First Attribute name or handle:",
//      "First Region local name:",
//      "Second Attribute name or handle:",
//      "Second Region local name:",
//      "Third Attribute name or handle:",
//      "Third Region local name:"
//		};
//		String[] defaults = {"", "", "", "", "", "", "", ""};
//		BrowserFactory[] browsers = {null, null, null, null, null, null, null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Register Object Instance With Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.registerObjectInstanceWithRegionAndName(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void associateRegionForUpdates_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Region local name:",
//      "Object instance handle:",
//		  "Attribute handles:"
//		};
//		String[] defaults = {"", "", ""};
//		BrowserFactory[] browsers = {null, null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Associate Region for Updates",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[2]);
//      if (ahset != null) {
//        _impl.associateRegionForUpdates(
//    		  stuff[0],
//          stuff[1],
//          ahset);
//      }
//      else post("Invalid attribute handles: " + stuff[2]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void unassociateRegionForUpdates_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Object instance handle:",
//		  "Region local name:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Unassociate Region for Updates",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.unassociateRegionForUpdates(stuff[0], stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void subscribeObjectClassAttributesWithRegion_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Region local name:",
//      "Object class name or handle:",
//		  "Attribute handles:"
//		};
//		String[] defaults = {"", "", ""};
//		BrowserFactory[] browsers = {null, null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Subscribe Object Class Attributes With Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[2]);
//      if (ahset != null) {
//        _impl.subscribeObjectClassAttributesWithRegion(
//    		  stuff[0],
//          stuff[1],
//          ahset);
//      }
//      else post("Invalid attribute handles: " + stuff[2]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void unsubscribeObjectClassWithRegion_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Object class name or handle:",
//		  "Region local name:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Unsubscribe Object Class With Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.unsubscribeObjectClassWithRegion(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void unsubscribeInteractionClassWithRegion_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Interaction class name or handle:",
//		  "Region local name:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Unsubscribe Interaction Class With Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.unsubscribeInteractionClassWithRegion(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void sendInteractionWithRegionTSO_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//      "Interaction class:",
//      "Parameter handle 1:",
//      "Parameter value 1:",
//      "Parameter handle 2:",
//      "Parameter value 2:",
//      "Parameter handle 3:",
//      "Parameter value 3:",
//      "User-supplied tag:",
//      "Local name of region:",
//      "Logical time:"
//		};
//		String[] defaults = {"", "", "", "", "", "", "", "", "", ""};
//		BrowserFactory[] browsers = {
//      _interactionBrowser,
//      _interactionBrowser,
//      null,
//      _interactionBrowser,
//      null,
//      _interactionBrowser,
//      null,
//      null,
//      null,
//      null};
//		int result = proxy.queryUser(
//		  this,
//		  "Send TSO Interaction With Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.sendROinteractionWithRegion(stuff);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void requestAttributeValueUpdateWithRegion_actionPerformed(ActionEvent e) {
//
//  }
//
//  void getAttributeRoutingSpaceHandle_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object class name or handle:",
//		  "Attribute handle:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_objectBrowser, _objectBrowser};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Attribute Routing Space Handle",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getAttributeRoutingSpaceHandle(stuff[1], stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getInteractionRoutingSpacehandle_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class handle:",
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Interaction Routing Space Handle",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getInteractionRoutingSpaceHandle(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getTransportationHandle_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Transportation name:",
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Transportation Handle",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getTransportationHandle(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getTransportationName_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Transportation handle:",
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Transportation Name",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getTransportationName(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getOrderingHandle_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Ordering name:",
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Ordering Handle",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getOrderingHandle(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void getOrderingName_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Ordering handle:",
//		};
//		String[] defaults = {""};
//		BrowserFactory[] browsers = {null};
//		int result = proxy.queryUser(
//		  this,
//		  "Get Ordering Name",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//  		_impl.getOrderingName(stuff[0]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void enableAttributeRelevanceAdvisorySwitch_actionPerformed(ActionEvent e) {
//    _impl.enableAttributeRelevanceAdvisorySwitch();
//  }
//
//  void disableAttributeRelevanceAdvisorySwitch_actionPerformed(ActionEvent e) {
//    _impl.disableAttributeRelevanceAdvisorySwitch();
//  }

   void federateSaveNotComplete_actionPerformed(ActionEvent e) {
      _impl.federateSaveNotComplete();
   }

   void queryFederationSaveStatus_actionpPerformed(ActionEvent e) {
      _impl.queryFederationSaveStatus();
   }

   void queryFederationRestoreStatus_actionPerformed(ActionEvent e) {
      _impl.queryFederationRestoreStatus();
   }

   void federateRestoreNotComplete_actionPerformed(ActionEvent e) {
      _impl.federateRestoreNotCompleteMenuItem();
   }

   void queryFederationRestoreStatus_actionpPerformed(ActionEvent e) {
      _impl.queryFederationRestoreStatus();
   }

//   void requestClassAttributeValueUpdate_actionPerformed(ActionEvent e) {
//      StringsDialogProxy proxy = new StringsDialogProxy();
//      String[] labels = {
//         "Object class handle:",
//         "Attribute handles:"
//      };
//      String[] defaults = {"", ""};
//      BrowserFactory[] browsers = {null, null};
//      int result = proxy.queryUser(
//              this,
//              "Request Attribute Class Value Update",
//              labels,
//              defaults,
//              browsers);
//      if (result == StringsDialog.CANCEL) return;
//      else if (result == StringsDialog.OK) {
//         String[] stuff = proxy.results();
//         hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//         if (ahset != null) {
//            _impl.requestClassAttributeValueUpdate(
//                    stuff[0],
//                    ahset);
//         }
//         else post("Invalid attribute handles: " + stuff[1]);
//      }
//      else {
//         post("StringsDialog returned " + result);
//      }
//   }

//  void requestInstanceAttributeValueUpdate_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Object instance handle:",
//		  "Attribute handles:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Request Attribute Instance Value Update",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[1]);
//      if (ahset != null) {
//        _impl.requestInstanceAttributeValueUpdate(
//    		  stuff[0],
//          ahset);
//      }
//      else post("Invalid attribute handles: " + stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void subscribeObjectClassAttributesPassivelyWithRegion_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Region local name:",
//      "Object class name or handle:",
//		  "Attribute handles:"
//		};
//		String[] defaults = {"", "", ""};
//		BrowserFactory[] browsers = {null, null, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Subscribe Object Class Attributes With Region Passively",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      hla.rti.AttributeHandleSet ahset = parseAttributeHandles(stuff[2]);
//      if (ahset != null) {
//        _impl.subscribeObjectClassAttributesPassivelyWithRegion(
//    		  stuff[0],
//          stuff[1],
//          ahset);
//      }
//      else post("Invalid attribute handles: " + stuff[2]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }
//
//  void subscribeInteractionClassPassivelyWithRegion_actionPerformed(ActionEvent e) {
//		StringsDialogProxy proxy = new StringsDialogProxy();
//		String[] labels = {
//		  "Interaction class name or handle:",
//      "Local name for region:"
//		};
//		String[] defaults = {"", ""};
//		BrowserFactory[] browsers = {_interactionBrowser, null};
//		int result = proxy.queryUser(
//		  this,
//		  "Subscribe Interaction Class Passively With Region",
//		  labels,
//		  defaults,
//		  browsers);
//		if (result == StringsDialog.CANCEL) return;
//		else if (result == StringsDialog.OK) {
//		  String[] stuff = proxy.results();
//      _impl.subscribeInteractionClassPassivelyWithRegion(
//        stuff[0],
//        stuff[1]);
//  	}
//  	else {
//  	  post("StringsDialog returned " + result);
//  	}
//  }

}

