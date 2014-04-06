package mygame;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.terrain.Terrain;
import java.util.HashMap;
import java.util.Map;

public class ActionNode {

    public static CDFNode ActionCDFNode;
    public static Map<String, Runnable> processActionMap = new HashMap<String, Runnable>();
    public static boolean ActionCompleted = false;
    public static float tpf;

    public ActionNode(CDFNode CDFNodeObj) {
        ActionCDFNode = CDFNodeObj;
    }
    //temporary
    public static int counter = 0;
    public static Vector3f camPos = new Vector3f(100, 150, 100);

    public static void processLookAroundBackground() {
        //TODO: add update code
        BackgroundNode BackgroundNode1 = ActionCDFNode.Background1;
        camPos.setX(camPos.getX() - 3 * tpf);
        camPos.setY(camPos.getY() - 4 * tpf);
        camPos.setZ(camPos.getZ() - 5 * tpf);
        Global.gMyMain.getCamera().setLocation(camPos);
        Global.gMyMain.getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        if (camPos.getX() < 10) {
            BackgroundNode1.bLookAroundBackground = true;
        }
        if (camPos.getY() < 10) {
            BackgroundNode1.bLookAroundBackground = true;
        }
        if (camPos.getZ() < 10) {
            BackgroundNode1.bLookAroundBackground = true;
        }

    }

    public static void processActionTypeWalk() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        Node Actor1Node = Actor1.Actor;
        Vector3f currLoc = Actor1Node.getLocalTranslation();
        BackgroundNode Background1 = ActionCDFNode.Background1;
        Terrain LakeTerrain = Background1.LakeTerrain;
        float height = LakeTerrain.getHeight(new Vector2f(currLoc.getX(), currLoc.getZ()));
        currLoc.setY(height + 4.6f);
        ActionCDFNode.Actor1.Actor.setLocalTranslation(currLoc);
        ActionCDFNode.Actor1.Actor.move(0, 0, tpf);
        counter++;
        if (counter > 100) {
            ActionCompleted = true;
        }
    }
    public static boolean bInitDone = false;

    public static void init(CDFNode CDFNodeObj) {
        ActionCDFNode = CDFNodeObj;

        if (bInitDone == true) {
            return;
        }
        bInitDone = true;
        processActionMap.put("drink", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("eat", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("fly", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("join", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("look", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("make", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("run", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("say", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("scramble", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("sit", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("sleep", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("stand", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("start", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("stop", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("swim", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("wake", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });
        processActionMap.put("walk", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });

    }

    public static void ProcessActionWrapper(String ActionType, float tpf_tmp) {

        tpf = tpf_tmp;
        boolean bActionNotSupported = false;
        ActionCompleted = false;

        //Look Around Background in parallel to action.
        if (ActionCDFNode.Background1.bLookAroundBackground == false) {
            processLookAroundBackground();
        }

        Runnable r = processActionMap.get(ActionType.toLowerCase());
        if (r != null) {
            r.run();
        } else {
            bActionNotSupported = true;
            System.out.println("Currently Action Type " + ActionType + " is not supported");
        }


    }
}
