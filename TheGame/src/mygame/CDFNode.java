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
    public List<CDFNode> children;
    public List<CDFEdge> outEdges;
    public CDFNode parent;

    public CDFNode(String lex, int idx) {
        this.label = lex + "-" + idx;

        this.idx = idx;

        children = new ArrayList<CDFNode>();
        outEdges = new ArrayList<CDFEdge>();
        attribute = "";
    }

    public void addChild(CDFNode c) {
        for (CDFNode node : children) {
            if (node.label.equalsIgnoreCase(c.label)) {
                return;
            }
        }
        children.add(c);
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
