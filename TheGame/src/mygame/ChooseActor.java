package mygame;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author atsingh
 */
public class ChooseActor {

    //TODO change it to arraylist, since there may be more that one actor for given name
    public static Map<String, ActorData> NameToActorDataMap = new HashMap<String, ActorData>();
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
        ActorData ActorDataObj = new ActorData("swan", "Scenes/Birds/Sphere.mesh.j3o", 0.5f, 0, 5);
        NameToActorDataMap.put("swan", ActorDataObj);
        ActorDataObj = new ActorData("swan babies", "Scenes/Birds/Sphere.mesh.j3o", 0.25f, 0, 2.5f);
        NameToActorDataMap.put("swan babies", ActorDataObj);
        ActorDataObj = new ActorData("kingfisher", "Scenes/Birds/Sphere.mesh.j3o", 0.5f, 0, 5);
        NameToActorDataMap.put("kingfisher", ActorDataObj);
        ActorDataObj = new ActorData("fish", "Scenes/Fish/Sphere.006.mesh.j3o", 0.2f, 0, 1);
        NameToActorDataMap.put("fish", ActorDataObj);
        ActorDataObj = new ActorData("deer", "Scenes/Animals/Deer/Cube.001.mesh.j3o", 0.5f, 0, 1);
        NameToActorDataMap.put("deer", ActorDataObj);

        ActorDataObj = new ActorData("parent", "Models/Actors/Cube.mesh.j3o", 2, 0, 9.2f);
        NameToActorDataMap.put("parent", ActorDataObj);
        ActorDataObj = new ActorData("parents", "Models/Actors/Cube.mesh.j3o", 2, 0, 9.2f);
        NameToActorDataMap.put("parents", ActorDataObj);
        ActorDataObj = new ActorData("mothers", "Models/Actors/Cube.mesh.j3o", 2, 0, 9.2f);
        NameToActorDataMap.put("mothers", ActorDataObj);
        ActorDataObj = new ActorData("mother", "Models/Actors/Cube.mesh.j3o", 2, 0, 9.2f);
        NameToActorDataMap.put("mother", ActorDataObj);

        ActorDataObj = new ActorData("children", "Models/Actors/Cube.mesh.j3o", 1, 0, 4.6f);
        NameToActorDataMap.put("children", ActorDataObj);

        ActorDataObj = new ActorData("child", "Models/Actors/Cube.mesh.j3o", 1, 0, 4.6f);
        NameToActorDataMap.put("child", ActorDataObj);

        CollectiveActorToNoMap.put("parent", 2);
        CollectiveActorToNoMap.put("parents", 2);
        bInit = true;
    }

    static ActorData getActorData(String object) {
        if (bInit == false) {
            init();
        }
        return NameToActorDataMap.get(object);
    }

    static int getNoOfActor(String Actor) {
        if (bInit == false) {
            init();
        }
        return CollectiveActorToNoMap.get(Actor);
    }
}
