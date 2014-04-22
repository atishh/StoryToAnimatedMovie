/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author atsingh
 */
public class PointsArray {

    public int nOfPoints = 10;
    public Vector3f[] Points = null;
    public int currPointNo = 0;
    public int currPointNoForBuild = 0;

    PointsArray(int nOfPointsTemp, List list) {
        nOfPoints = nOfPointsTemp;
        Points = new Vector3f[nOfPoints];
        if (list != null) {
            currPointNo = 0;
            for (Iterator<Vector3f> iter = list.iterator(); iter.hasNext();) {
                Vector3f element = iter.next();
                Points[currPointNo] = element;
                currPointNo++;
            }
        }
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

    public Vector3f getANearestPoint(Vector3f origPoint) {
        if (origPoint == null) {
            return null;
        }
        int nReturnPointNo = 0;
        float minDistance = origPoint.distanceSquared(Points[0]);
        for (int i = 1; i < nOfPoints; i++) {
            float minDistanceTemp = origPoint.distanceSquared(Points[i]);
            if (minDistanceTemp < minDistance) {
                minDistance = minDistanceTemp;
                nReturnPointNo = i;
            }
        }
        //Other wise all the objects may cluttered to a single point.
        if (minDistance < 6) {
            return Points[nReturnPointNo];
        } else {
            return getAPoint();
        }
    }
}
