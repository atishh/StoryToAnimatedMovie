/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.scene.Node;

/**
 *
 * @author aa496
 */
public class ActorNode {

    public String Name;
    public String label;
    public int idx;
    public String attribute;
    public Node Actor = null;
    public AnimChannel channel;
    public AnimControl control;
    public boolean bPositionSet = false;
    //bOtherActor means that it is not the main actor
    //Somehow nlp could not able to find this actor.
    public boolean bOtherActor = false;
    //Total no. of actors belonging to this node
    public int nTotalNoOfActorsInThisNode = 0;
    //Currently this is 10, to change it later.
    public ActorNode[] TotalActorNodeInThisNode = new ActorNode[10];
    public static ActorNode[] gActorNodes = new ActorNode[50];
    public static int gNoOfActors = 0;
    public boolean bAttachedToRoot = false;
    public CDFNode CDFNodeObj = null;
    public boolean bPassiveActor = false;
    public ActorData ActorDataObj;
    public boolean bActorCreated = false;

    //This function will populate total no. of actors based on synonyms.
    //For ex; Martin, Annie and Martha are children. children walks to the lake.
    //Here we have 4 ActorNode, but children is actually referred to Martin, Annie
    public static void PopulateTotalActorNodeInThisNode() {
        System.out.println("Inside PopulateTotalActorNodeInThisNode");
        for (int i = 0; i < gNoOfActors; i++) {
            if (gActorNodes[i].bOtherActor == true) {
                //Try to find if other actors attribute matches.
                for (int j = 0; j < gNoOfActors; j++) {
                    if (i != j) {
                        if (gActorNodes[j].attribute.toLowerCase().contains(gActorNodes[i].Name.toLowerCase())) {
                            gActorNodes[i].TotalActorNodeInThisNode[gActorNodes[i].nTotalNoOfActorsInThisNode] = gActorNodes[j];
                            gActorNodes[i].nTotalNoOfActorsInThisNode++;
                            System.out.println("Found " + gActorNodes[j].Name + " with attribute "
                                    + gActorNodes[j].attribute);
                        }
                    }
                }
            }
            //There should be atleast one actor.
            if (gActorNodes[i].nTotalNoOfActorsInThisNode == 0) {
                gActorNodes[i].nTotalNoOfActorsInThisNode = 1;
            }
        }
    }

    public ActorNode(String lex, int idx, boolean bOther, CDFNode CDFNodeTemp) {
        this.Name = lex;
        this.label = lex + "-" + idx;
        this.idx = idx;
        this.bOtherActor = bOther;
        attribute = "";
        bPositionSet = false;
        CDFNodeObj = CDFNodeTemp;
        bPassiveActor = false;

        channel = null;
        control = null;

        gActorNodes[gNoOfActors] = this;
        gNoOfActors++;
        TotalActorNodeInThisNode[nTotalNoOfActorsInThisNode] = this;
        //It is intentionally kept zero here. So that other actor can be populated.
        //Ideally this should be 1;
        nTotalNoOfActorsInThisNode = 0;
        bAttachedToRoot = false;
        ActorDataObj = null;
        bActorCreated = false;
        //createActor();
    }

    public void createActor() {
        if (bActorCreated == true) {
            return;
        }

        if (Global.gAssertManager != null) {
            //First try to load non-actor like cabin. The action can be 
            //"The cabin stand little far aways.
            if (bOtherActor == true) {
                String ObjectPath = SupportedBackground.getPathForStaticObjects(Name);
                if (ObjectPath != null) {
                    System.out.println("Inside createActor name " + Name + ObjectPath);
                    Actor = CDFNodeObj.Background1.createPassiveActor(Name, ObjectPath);
                    CDFNodeObj.passiveActorMap.put(Name, ObjectPath);
                    bAttachedToRoot = true;
                    bPassiveActor = true;
                    bPositionSet = true;
                }
            }

            if (Actor == null) {
                //This is the real actor.

                //choose actor from library.
                //Might be required to change
                //First try with attribute, if unable then go for name. This will take care of 2 cases.
                //1) William is a children. Children go to the lake. //Here children is attribute.
                //2) Deer walk towards water. //Here deer is animal.
                ActorData ActorDataTemp = ChooseActor.getActorData(attribute.toLowerCase().trim());
                if (ActorDataTemp == null) {
                    ActorDataTemp = ChooseActor.getActorData(Name.toLowerCase().trim());
                }
                if (ActorDataTemp != null) {
                    ActorDataObj = ActorDataTemp;
                    Actor = (Node) Global.gAssertManager.loadModel(ActorDataObj.PhysicalPath);
                    Actor.setLocalScale(ActorDataObj.nScale);
                    control = Actor.getControl(AnimControl.class);
                    if (control != null) {
                        channel = control.createChannel();
                        channel.setAnim("walk");
                    }
                }
            }

            //This case ideally should not happen, If it happens just load anything.

            if (Actor == null) {
                //This is the real actor.
                Actor = (Node) Global.gAssertManager.loadModel("Models/Actors/Cube.mesh.j3o");

                control = Actor.getControl(AnimControl.class);
                if (control != null) {
                    channel = control.createChannel();
                    channel.setAnim("walk");
                }
            }
            bActorCreated = true;
        }
    }

    public void AttachNodesToRoot() {
        if (bAttachedToRoot == false) {
            /*
             if ((Actor != null)) {
             System.out.println("Attaching to root node " + Actor.getName());
             Global.gMyMain.getRootNode().attachChild(Actor);
             }
             */

            //if (bOtherActor == true) {
            for (int i = 0; i < nTotalNoOfActorsInThisNode; i++) {

                if (TotalActorNodeInThisNode[i].Actor != null) {
                    System.out.println("Attaching to root node " + TotalActorNodeInThisNode[i].Actor.getName());
                    Global.gMyMain.getRootNode().attachChild(TotalActorNodeInThisNode[i].Actor);
                }

            }
            //}
            bAttachedToRoot = true;
        }
    }

    public float getHeight() {
        float nHeight = 0;
        if (ActorDataObj != null) {
            nHeight = ActorDataObj.nHeight;
        }
        return nHeight;
    }
}
