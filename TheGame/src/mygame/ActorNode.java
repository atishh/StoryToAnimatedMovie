/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue;
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
    //bOtherActor means that it is not the main actor
    //Somehow nlp could not able to find this actor.
    public boolean bOtherActor = false;
    //Total no. of actors belonging to this node
    public int nTotalNoOfActorsInThisNode = 0;
    //Currently this is 10, to change it later.
    public ActorNode[] TotalActorNodeInThisNode = new ActorNode[10];
    public static ActorNode[] gActorNodes = new ActorNode[50];
    public static int gNoOfActors = 0;

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
                        if (gActorNodes[j].attribute.contains(gActorNodes[i].Name)) {
                            gActorNodes[i].TotalActorNodeInThisNode[gActorNodes[i].nTotalNoOfActorsInThisNode] = gActorNodes[j];
                            gActorNodes[i].nTotalNoOfActorsInThisNode++;
                            System.out.println("Found " + gActorNodes[j].Name + " with attribute "
                                    + gActorNodes[j].attribute);
                        }
                    }
                }
            }
            //There should be atleast one actor.
            if(gActorNodes[i].nTotalNoOfActorsInThisNode == 0)
                gActorNodes[i].nTotalNoOfActorsInThisNode = 1;
        }
    }

    public ActorNode(String lex, int idx, boolean bOther) {
        this.Name = lex;
        this.label = lex + "-" + idx;
        this.idx = idx;
        this.bOtherActor = bOther;
        attribute = "";
        createActor();

        channel = null;
        control = null;

        gActorNodes[gNoOfActors] = this;
        gNoOfActors++;
        TotalActorNodeInThisNode[nTotalNoOfActorsInThisNode] = this;
        //It is intentionally kept zero here. So that other actor can be populated.
        //Ideally this should be 1;
        nTotalNoOfActorsInThisNode = 0; 
        
    }

    public void createActor() {
        if (Global.gAssertManager != null) {
            Actor = (Node) Global.gAssertManager.loadModel("Models/Actors/Cube.mesh.j3o");
            control = Actor.getControl(AnimControl.class);

            channel = control.createChannel();
            channel.setAnim("walk");

        }
    }

    public void AttachNodesToRoot() {
        if ((Actor != null)) {
            System.out.println("Attaching to root node " + Actor.getName());
            Global.gMyMain.getRootNode().attachChild(Actor);
        }

        if (bOtherActor == true) {
            for (int i = 0; i < nTotalNoOfActorsInThisNode; i++) {

                if (TotalActorNodeInThisNode[i].Actor != null) {
                    System.out.println("Attaching to root node " + TotalActorNodeInThisNode[i].Actor.getName());
                    Global.gMyMain.getRootNode().attachChild(TotalActorNodeInThisNode[i].Actor);
                }

            }
        }
    }
}
