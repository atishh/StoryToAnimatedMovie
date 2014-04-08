/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector2f;

/**
 *
 * @author atsingh
 */
public class PointsArray {

    public int nOfPoints = 10;
    public Vector2f[] Points = null;
    public int currPointNo = 0;

    PointsArray(int nOfPointsTemp) {
        nOfPoints = nOfPointsTemp;
        Points = new Vector2f[nOfPoints];
        currPointNo = 0;
    }

    public Vector2f getAPoint() {

        if (currPointNo >= nOfPoints) {
            currPointNo = 0;
        }
        return Points[currPointNo++];
    }
}
