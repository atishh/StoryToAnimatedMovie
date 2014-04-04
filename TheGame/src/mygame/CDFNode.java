/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.ArrayList;
import java.util.List;

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
    }


    public String getRelationToParent() {
        String rel = null;
        if (parent == null) {
            return null;
        }
        for (CDFEdge e : parent.outEdges) {
            if (e.target == this) {
                return e.label;
            }
        }
        return null;
    }
}
