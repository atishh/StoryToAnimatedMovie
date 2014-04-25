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

    public static Map<String, String> LogicalToPhysicalMap = new HashMap<String, String>();
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
        LogicalToPhysicalMap.put("cabin", "Scenes/House/example_house.j3o");
        bInit = true;
    }

    static String getPathForStaticObjects(String object) {
        if (bInit == false) {
            init();
        }
        return LogicalToPhysicalMap.get(object);
    }
}
