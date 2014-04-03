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
public class ActionNode {

    public String label;
    public int idx;
    public String attribute;
    public List<ActionNode> children;
    public List<ActionEdge> outEdges;
    public ActionNode parent;

    public ActionNode(String lex, int idx) {
        this.label = lex + "-" + idx;

        this.idx = idx;

        children = new ArrayList<ActionNode>();
        outEdges = new ArrayList<ActionEdge>();
        attribute = "";
    }

    public void addChild(ActionNode c) {
        for (ActionNode node : children) {
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
        for (ActionEdge e : parent.outEdges) {
            if (e.target == this) {
                return e.label;
            }
        }
        return null;
    }
}
