/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;

/**
 *
 * @author atsingh
 */
public class PointsArray {

    public int nOfPoints = 10;
    public Vector3f[] Points = null;
    public int currPointNo = 0;
    public int currPointNoForBuild = 0;

    PointsArray(int nOfPointsTemp) {
        nOfPoints = nOfPointsTemp;
        Points = new Vector3f[nOfPoints];
        currPointNo = 0;
    }

    public Vector3f getAPoint() {

        if (currPointNo >= nOfPoints) {
            currPointNo = 0;
        }
        return Points[currPointNo++];
    }

    public Vector3f getAPointForBuild() {

        if (currPointNoForBuild >= nOfPoints) {
            currPointNoForBuild = 0;
        }
        return Points[currPointNoForBuild++];
    }
}
