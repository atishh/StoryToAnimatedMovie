//package com.chaoticity.dependensee;
package mygame;

import java.io.Serializable;

public class CDFEdge {

    public CDFNode source;
    public CDFNode target;
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
