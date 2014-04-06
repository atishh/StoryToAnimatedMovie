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
    private static final int nOfPointNearLake = 3;
    public static Vector2f centrePoint = new Vector2f(0, 0);
    public static Vector2f[] walkablePoint = new Vector2f[nOfWalkablePoints];
    public static Vector2f[] pointNearLake = new Vector2f[nOfPointNearLake];
    public static int currPointNearLakeNo = 0;
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

        pointNearLake[0] = new Vector2f(10, 0);
        pointNearLake[1] = new Vector2f(0, 10);
        pointNearLake[2] = new Vector2f(10, 10);

        bInitialize = true;
    }

    public static Vector2f getAPointNearLake() {
        if (bInitialize == false) {
            initialize();
        }
        return pointNearLake[currPointNearLakeNo++];
    }
    //public static 
}
