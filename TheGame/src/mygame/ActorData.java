/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author atsingh
 */
public class ActorData {

    String Name;
    String PhysicalPath;
    int nScale;
    float nRotation;

    ActorData(String NameT, String PhysicalPathT, int nScaleT, float nRotationT) {
        Name = NameT;
        PhysicalPath = PhysicalPathT;
        nScale = nScaleT;
        nRotation = nRotationT;
    }
}
