/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author atsingh
 */
public class SupportedBackground {

    public static Map<String, ActorData> LogicalToPhysicalMap = new HashMap<String, ActorData>();
    public static boolean bInit = false;

    static boolean IsSupported(String s) {
        if (s.equalsIgnoreCase("camp")) {
            return true;
        }
        else if (s.equalsIgnoreCase("lake")) {
            return true;
        }
        else if (s.equalsIgnoreCase("library")) {
            return true;
        }
        return false;
    }

    static void init() {
        //ActorData(String NameT, String PhysicalPathT, float nScaleT, float nRotationT, float nHeightT
        ActorData ActorDataObj = new ActorData("cabin", "Scenes/House/example_house.j3o", 10, 0, 2);
        LogicalToPhysicalMap.put("cabin", ActorDataObj);
        LogicalToPhysicalMap.put("house", ActorDataObj);
       // LogicalToPhysicalMap.put("cabin", "Scenes/House/example_house.j3o");
       // LogicalToPhysicalMap.put("house", "Scenes/House/example_house.j3o");
        bInit = true;
    }

    static ActorData getPathForStaticObjects(String object) {
        if (bInit == false) {
            init();
        }
        return LogicalToPhysicalMap.get(object);
    }
}
