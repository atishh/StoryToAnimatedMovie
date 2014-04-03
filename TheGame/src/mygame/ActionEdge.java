//package com.chaoticity.dependensee;
package mygame;

import java.io.Serializable;

public class ActionEdge {

    public ActionNode source;
    public ActionNode target;
    public String label;
    public int sourceIndex;
    public int targetIndex;
    public boolean visible = false;
    public int height;

    @Override
    public String toString() {
        return label + "[" + sourceIndex + "->" + targetIndex + "]";
    }
}
