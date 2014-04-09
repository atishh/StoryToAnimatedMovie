package mygame;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author atsingh
 */
public class ActorType {

    public static Map<String, String> LogicalToPhysicalMap = new HashMap<String, String>();
    public static Map<String, Integer> CollectiveActorToNoMap = new HashMap<String, Integer>();
    public static boolean bInit = false;

    static boolean IsSupported(String s) {
        if (s.equalsIgnoreCase("parent")) {
            return true;
        }
        if (s.equalsIgnoreCase("parents")) {
            return true;
        }
        return false;
    }

    static void init() {
        LogicalToPhysicalMap.put("parent", "Models/Actors/Cube.mesh.j3o");
        LogicalToPhysicalMap.put("parents", "Models/Actors/Cube.mesh.j3o");

        CollectiveActorToNoMap.put("parent", 2);
        CollectiveActorToNoMap.put("parents", 2);
        bInit = true;
    }

    static String getPathForActors(String object) {
        if (bInit == false) {
            init();
        }
        return LogicalToPhysicalMap.get(object);
    }

    static int getNoOfActor(String Actor) {
        if (bInit == false) {
            init();
        }
        return CollectiveActorToNoMap.get(Actor);
    }
}
