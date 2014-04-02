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
    
    private final int nOfWalkablePoints = 10;
    public Vector2f centrePoint = new Vector2f(0, 0);
    public Vector2f [] walkablePoint = new Vector2f[nOfWalkablePoints];
    
    PointsOnLake()
    {
        walkablePoint[0] = new Vector2f(-50,-50);
        walkablePoint[1] = new Vector2f(-50,0);
        walkablePoint[2] = new Vector2f(-50,50);
        walkablePoint[3] = new Vector2f(0,-50);
        walkablePoint[4] = new Vector2f(0,0);
        walkablePoint[5] = new Vector2f(0,50);
        walkablePoint[6] = new Vector2f(50,-50);
        walkablePoint[7] = new Vector2f(50,0);
        walkablePoint[8] = new Vector2f(50,50);
    }
}
