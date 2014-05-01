/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aa496
 */
public class CDFNode {

    public String label;
    public int idx;
    public String attribute;
    //public List<CDFNode> children;
    public CDFNode children;
    public List<CDFEdge> outEdges;
    public CDFNode parent;
    public CDFType cdfType;
    //TODO the actors can be more;
    public ActorNode Actor1;
    public ActorNode Actor2;
    public String TalkString;
    public BackgroundNode Background1;
    public boolean bFirstTime = true;
    public boolean bFirstTimePlaceActor = true;
    public Map<String, String> passiveActorMap = new HashMap<String, String>();

    public String sPotentialBackgroundName = null;
    
    public String sAudioFileName = null;
    
    public void AttachNodesToRoot() {

        if ((Background1 != null)) {
            Background1.AttachNodesToRoot();
        }

        if ((Actor1 != null)) {
            Actor1.AttachNodesToRoot();
        }
        if ((Actor2 != null)) {
            Actor2.AttachNodesToRoot();
        }



    }

    //public void CreatePassiveActors() {
    //    if (Background1 != null) {
    //        for (Map.Entry<String, String> entry : passiveActorMap.entrySet()) {
    //            System.out.println(entry.getKey() + "/" + entry.getValue());
    //            Node passiveActor = Background1.createPassiveActor(entry.getKey(), entry.getValue());
    //            if (Actor1 != null) {
    //                //Might be wrong.
    //                Actor1.Actor = passiveActor;
    //            }
    //        }
    //    }
    //}
    public CDFNode(String lex, int idx) {
        this.label = lex;

        this.idx = idx;

        children = null;
        parent = null;
        outEdges = new ArrayList<CDFEdge>();
        attribute = "";
        cdfType = CDFType.CDF_ACTION;

        Actor1 = null;
        Actor2 = null;
        TalkString = "";

        Background1 = null;
        bFirstTime = true;
        bFirstTimePlaceActor = true;
    }

    public boolean PerformAction(float tpf) {
        if (bFirstTime) {
            ActionNode.init(DesignUnit.mPastCDFNodeTypeAction, this, DesignUnit.mFutureCDFNodeTypeAction);
            bFirstTime = false;
        }
        ActionNode.ProcessActionWrapper(label, tpf);
        return ActionNode.ActionCompleted;
    }

    public void PlaceActorsForThisBackground(BackgroundNode CurrBackgroundNode) {

        if (bFirstTimePlaceActor) {
            PlaceActorNode.init(this);
            bFirstTimePlaceActor = false;
        }
        PlaceActorNode.PlaceActorWapper(label);
        if (children != null) {
            children.PlaceActorsForThisBackground(CurrBackgroundNode);
        }
        //CreatePassiveActors();
        //Upto now all the nodes must have been populated.
        AttachNodesToRoot();
    }
}
