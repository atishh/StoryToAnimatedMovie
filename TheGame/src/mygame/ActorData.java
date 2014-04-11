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
    float nScale;
    float nRotation;
    float nHeight;

    ActorData(String NameT, String PhysicalPathT, float nScaleT, float nRotationT, float nHeightT) {
        Name = NameT;
        PhysicalPath = PhysicalPathT;
        nScale = nScaleT;
        nRotation = nRotationT;
        nHeight = nHeightT;
    }
}
