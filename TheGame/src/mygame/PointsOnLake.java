/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 *
 * @author atsingh
 */
public class PointsOnLake {

    private static final int nOfWalkablePoints = 10;
    private static final int nOfPointNearLake = 10;
    private static final int nOfPointOnRoad = 10;
    public static Vector2f centrePoint = new Vector2f(0, 0);
    public static Vector2f[] walkablePoint = new Vector2f[nOfWalkablePoints];
    public static Vector2f[] pointNearLake = new Vector2f[nOfPointNearLake];
    public static Vector2f[] pointNearRoad = new Vector2f[nOfPointOnRoad];
    public static int currPointNearLakeNo = 0;
    public static int currPointNearRoadNo = 0;
    public static boolean bInitialize = false;

    public static void initialize() {
        walkablePoint[0] = new Vector2f(-50, -50);
        walkablePoint[1] = new Vector2f(-50, 0);
        walkablePoint[2] = new Vector2f(-50, 50);
        walkablePoint[3] = new Vector2f(0, -50);
        walkablePoint[4] = new Vector2f(0, 0);
        walkablePoint[5] = new Vector2f(0, 50);
        walkablePoint[6] = new Vector2f(50, -50);
        walkablePoint[7] = new Vector2f(50, 0);
        walkablePoint[8] = new Vector2f(50, 50);

        pointNearLake[0] = new Vector2f(4, -1);
        pointNearLake[1] = new Vector2f(3, -2);
        pointNearLake[2] = new Vector2f(2, -3);
        pointNearLake[3] = new Vector2f(1, -4);
        pointNearLake[4] = new Vector2f(-2, 0);
        pointNearLake[5] = new Vector2f(-3, 1);
        pointNearLake[6] = new Vector2f(-4, 2);
        pointNearLake[7] = new Vector2f(-5, 3);
        pointNearLake[8] = new Vector2f(-6, 4);
        pointNearLake[9] = new Vector2f(-7, 5);

        pointNearRoad[0] = new Vector2f(50, -12);
        pointNearRoad[1] = new Vector2f(46, -9);
        pointNearRoad[2] = new Vector2f(42, -6);
        pointNearRoad[3] = new Vector2f(50, -3);
        pointNearRoad[4] = new Vector2f(46, 0);
        pointNearRoad[5] = new Vector2f(42, 3);
        pointNearRoad[6] = new Vector2f(50, 6);
        pointNearRoad[7] = new Vector2f(46, 9);
        pointNearRoad[8] = new Vector2f(42, 12);
        pointNearRoad[9] = new Vector2f(50, 15);

        bInitialize = true;
    }

    public static Vector2f getAPointNearLake() {
        if (bInitialize == false) {
            initialize();
        }
        if (currPointNearLakeNo >= nOfPointNearLake) {
            currPointNearLakeNo = 0;
        }
        return pointNearLake[currPointNearLakeNo++];
    }

    public static Vector2f getAPointNearRoad() {
        if (bInitialize == false) {
            initialize();
        }
        if (currPointNearRoadNo >= nOfPointOnRoad) {
            currPointNearRoadNo = 0;
        }
        return pointNearRoad[currPointNearRoadNo++];
    }
    //public static 
}
