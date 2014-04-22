/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author atsingh
 */
public class PointsOnLake {

    private static final int nOfWalkablePoints = 10;
    private static int nOfPointNearLake = 10;
    private static int nOfPointOnRoad = 10;
    private static int nOfPointOnWater = 6;
    private static final int nOfPointOnSky = 3;
    private static final int nOfPointOnTree = 3;
    private static int nOfPointForHouse = 3;
    public static PointsArray walkablePoint = new PointsArray(nOfWalkablePoints, null);
    // public static PointsArray pointNearLake = new PointsArray(nOfPointNearLake);
    public static PointsArray pointNearLake = null;
    //public static PointsArray pointNearRoad = new PointsArray(nOfPointOnRoad);
    public static PointsArray pointNearRoad = null;
    // public static PointsArray pointOnWater = new PointsArray(nOfPointOnWater);
    public static PointsArray pointOnWater = null;
    public static PointsArray pointOnSky = new PointsArray(nOfPointOnSky, null);
    public static PointsArray pointOnTree = new PointsArray(nOfPointOnTree, null);
    //public static PointsArray pointForHouse = new PointsArray(nOfPointForHouse);
    public static PointsArray pointForHouse = null;
    public static boolean bInitialize = false;
    public static Map<String, PointsArray> pointsMap = new HashMap<String, PointsArray>();
    public static Node lake = null;

    public static void searchTraverseWorld() {
        final List listLake = new ArrayList<Vector3f>();
        final List listWater = new ArrayList<Vector3f>();
        final List listRoad = new ArrayList<Vector3f>();
        final List listHouse = new ArrayList<Vector3f>();
        SceneGraphVisitor visitor = new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                if ((spatial != null) && (spatial.getName() != null)) {
                    System.out.println(spatial.getName());
                    if (spatial.getName().equals("lake")) {
                        listLake.add(spatial.getLocalTranslation());
                    } else if (spatial.getName().equals("water")) {
                        listWater.add(spatial.getLocalTranslation());
                    } else if (spatial.getName().equals("road")) {
                        listRoad.add(spatial.getLocalTranslation());
                    } else if (spatial.getName().equals("cabin")) {
                        listHouse.add(spatial.getLocalTranslation());
                    }
                }
            }
        };
        lake.depthFirstTraversal(visitor);
        
        nOfPointNearLake = listLake.size();
        nOfPointOnWater = listWater.size();
        nOfPointOnRoad = listRoad.size();
        nOfPointForHouse = listHouse.size();

        pointNearLake = new PointsArray(nOfPointNearLake, listLake);
        pointNearRoad = new PointsArray(nOfPointOnRoad, listRoad);
        pointOnWater = new PointsArray(nOfPointOnWater, listWater);
        pointForHouse = new PointsArray(nOfPointForHouse, listHouse);
    }

    public static void initialize() {

        searchTraverseWorld();

        walkablePoint.Points[0] = new Vector3f(-50, 0, -50);
        walkablePoint.Points[1] = new Vector3f(-50, 0, 0);
        walkablePoint.Points[2] = new Vector3f(-50, 0, 50);
        walkablePoint.Points[3] = new Vector3f(0, 0, -50);
        walkablePoint.Points[4] = new Vector3f(0, 0, 0);
        walkablePoint.Points[5] = new Vector3f(0, 0, 50);
        walkablePoint.Points[6] = new Vector3f(50, 0, -50);
        walkablePoint.Points[7] = new Vector3f(50, 0, 0);
        walkablePoint.Points[8] = new Vector3f(50, 0, 50);

        /*
         pointNearLake.Points[0] = new Vector3f(4, 0, -1);
         pointNearLake.Points[1] = new Vector3f(3, 0, -2);
         pointNearLake.Points[2] = new Vector3f(2, 0, -3);
         pointNearLake.Points[3] = new Vector3f(1, 0, -4);
         pointNearLake.Points[4] = new Vector3f(-2, 0, 0);
         pointNearLake.Points[5] = new Vector3f(-3, 0, 1);
         pointNearLake.Points[6] = new Vector3f(-4, 0, 2);
         pointNearLake.Points[7] = new Vector3f(-5, 0, 3);
         pointNearLake.Points[8] = new Vector3f(-6, 0, 4);
         pointNearLake.Points[9] = new Vector3f(-7, 0, 5);

         pointNearRoad.Points[0] = new Vector3f(50, 0, -12);
         pointNearRoad.Points[1] = new Vector3f(46, 0, -9);
         pointNearRoad.Points[2] = new Vector3f(42, 0, -6);
         pointNearRoad.Points[3] = new Vector3f(50, 0, -3);
         pointNearRoad.Points[4] = new Vector3f(46, 0, 0);
         pointNearRoad.Points[5] = new Vector3f(42, 0, 3);
         pointNearRoad.Points[6] = new Vector3f(50, 0, 6);
         pointNearRoad.Points[7] = new Vector3f(46, 0, 9);
         pointNearRoad.Points[8] = new Vector3f(42, 0, 12);
         pointNearRoad.Points[9] = new Vector3f(50, 0, 15);

         pointOnWater.Points[0] = new Vector3f(-22, 0, 15);
         pointOnWater.Points[1] = new Vector3f(15, 0, 15);
         pointOnWater.Points[2] = new Vector3f(15, 0, 45);
         pointOnWater.Points[3] = new Vector3f(-22, 0, 45);
         pointOnWater.Points[4] = new Vector3f(-30, 0, 30);
         pointOnWater.Points[5] = new Vector3f(0, 0, 30);
         */

        pointOnSky.Points[0] = new Vector3f(0, 50, 0);
        pointOnSky.Points[1] = new Vector3f(0, 60, 0);
        pointOnSky.Points[2] = new Vector3f(0, 40, 0);

        pointOnTree.Points[0] = new Vector3f(10, 10, 10);
        pointOnTree.Points[1] = new Vector3f(10, 15, -10);
        pointOnTree.Points[2] = new Vector3f(-10, 12, 10);

        /*
         pointForHouse.Points[0] = new Vector3f(50, 10, -25);
         pointForHouse.Points[1] = new Vector3f(50, 10, -25);
         pointForHouse.Points[2] = new Vector3f(-50, 10, 25);
         */

        pointsMap.put("walk", walkablePoint);
        pointsMap.put("lake", pointNearLake);
        pointsMap.put("road", pointNearRoad);
        pointsMap.put("water", pointOnWater);
        pointsMap.put("sky", pointOnSky);
        pointsMap.put("tree", pointOnTree);
        pointsMap.put("trees", pointOnTree);
        pointsMap.put("cabin", pointForHouse);
        pointsMap.put("house", pointForHouse);

        bInitialize = true;
    }

    public static Vector3f getAPoint(String sPosition, Vector3f origPoint) {
        if (bInitialize == false) {
            initialize();
        }
        if (sPosition == null) {
            return null;
        }
        String sPositionTemp = sPosition.toLowerCase().trim();

        //since window in house is not implemented, for now run to the window
        //is similar to run to the road.
        if ("window".equalsIgnoreCase(sPositionTemp)) {
            sPositionTemp = "road";
        }

        PointsArray PointsArrayObj = pointsMap.get(sPositionTemp);
        if (PointsArrayObj != null) {
            if (origPoint != null) {
                return PointsArrayObj.getANearestPoint(origPoint);
            }
            return PointsArrayObj.getAPoint();
        } else {
            System.out.println("getAPoint returns null. means not supported");
            return null;
        }
    }

    public static Vector3f getAPoint(String sPosition) {
        return getAPoint(sPosition, null);
    }

    public static Vector3f getAPointForBuild(String sPosition) {
        if (bInitialize == false) {
            initialize();
        }
        String sPositionTemp = sPosition.toLowerCase().trim();
        PointsArray PointsArrayObj = pointsMap.get(sPositionTemp);
        return PointsArrayObj.getAPointForBuild();
    }

    public static Vector3f getAPointNearLake() {
        if (bInitialize == false) {
            initialize();
        }
        return pointNearLake.getAPoint();
    }

    public static Vector3f getAPointNearRoad() {
        if (bInitialize == false) {
            initialize();
        }
        return pointNearRoad.getAPoint();
    }

    public static Vector3f getAPointOnWater() {
        if (bInitialize == false) {
            initialize();
        }
        return pointOnWater.getAPoint();
    }
    //public static 
}
