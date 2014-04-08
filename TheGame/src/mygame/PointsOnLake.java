/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.HashMap;
import java.util.Map;
import static mygame.ActionNode.processActionMap;

/**
 *
 * @author atsingh
 */
public class PointsOnLake {

    private static final int nOfWalkablePoints = 10;
    private static final int nOfPointNearLake = 10;
    private static final int nOfPointOnRoad = 10;
    private static final int nOfPointOnWater = 3;
    public static PointsArray walkablePoint = new PointsArray(nOfWalkablePoints);
    public static PointsArray pointNearLake = new PointsArray(nOfPointNearLake);
    public static PointsArray pointNearRoad = new PointsArray(nOfPointOnRoad);
    public static PointsArray pointOnWater = new PointsArray(nOfPointOnWater);
    public static int currPointNearLakeNo = 0;
    public static int currPointNearRoadNo = 0;
    public static boolean bInitialize = false;
    public static Map<String, PointsArray> pointsMap = new HashMap<String, PointsArray>();

    public static void initialize() {
        walkablePoint.Points[0] = new Vector2f(-50, -50);
        walkablePoint.Points[1] = new Vector2f(-50, 0);
        walkablePoint.Points[2] = new Vector2f(-50, 50);
        walkablePoint.Points[3] = new Vector2f(0, -50);
        walkablePoint.Points[4] = new Vector2f(0, 0);
        walkablePoint.Points[5] = new Vector2f(0, 50);
        walkablePoint.Points[6] = new Vector2f(50, -50);
        walkablePoint.Points[7] = new Vector2f(50, 0);
        walkablePoint.Points[8] = new Vector2f(50, 50);

        pointNearLake.Points[0] = new Vector2f(4, -1);
        pointNearLake.Points[1] = new Vector2f(3, -2);
        pointNearLake.Points[2] = new Vector2f(2, -3);
        pointNearLake.Points[3] = new Vector2f(1, -4);
        pointNearLake.Points[4] = new Vector2f(-2, 0);
        pointNearLake.Points[5] = new Vector2f(-3, 1);
        pointNearLake.Points[6] = new Vector2f(-4, 2);
        pointNearLake.Points[7] = new Vector2f(-5, 3);
        pointNearLake.Points[8] = new Vector2f(-6, 4);
        pointNearLake.Points[9] = new Vector2f(-7, 5);

        pointNearRoad.Points[0] = new Vector2f(50, -12);
        pointNearRoad.Points[1] = new Vector2f(46, -9);
        pointNearRoad.Points[2] = new Vector2f(42, -6);
        pointNearRoad.Points[3] = new Vector2f(50, -3);
        pointNearRoad.Points[4] = new Vector2f(46, 0);
        pointNearRoad.Points[5] = new Vector2f(42, 3);
        pointNearRoad.Points[6] = new Vector2f(50, 6);
        pointNearRoad.Points[7] = new Vector2f(46, 9);
        pointNearRoad.Points[8] = new Vector2f(42, 12);
        pointNearRoad.Points[9] = new Vector2f(50, 15);

        pointOnWater.Points[0] = new Vector2f(-20, 1);
        pointOnWater.Points[1] = new Vector2f(-20, 3);
        pointOnWater.Points[2] = new Vector2f(-20, 5);

        pointsMap.put("walk", walkablePoint);
        pointsMap.put("lake", pointNearLake);
        pointsMap.put("road", pointNearRoad);
        pointsMap.put("water", pointOnWater);

        bInitialize = true;
    }

    public static Vector2f getAPointNearLake() {
        if (bInitialize == false) {
            initialize();
        }
        return pointNearLake.getAPoint();
    }

    public static Vector2f getAPointNearRoad() {
        if (bInitialize == false) {
            initialize();
        }
        return pointNearRoad.getAPoint();
    }

    public static Vector2f getAPointOnWater() {
        if (bInitialize == false) {
            initialize();
        }
        return pointOnWater.getAPoint();
    }
    //public static 
}
