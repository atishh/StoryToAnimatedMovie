package mygame;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.terrain.Terrain;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ActionNode {

    public static CDFNode ActionCDFNode;
    public static Map<String, Runnable> processActionMap = new HashMap<String, Runnable>();
    public static boolean ActionCompleted = false;
    public static float tpf;
    public static boolean bFirstTimeForThisAction = true;
    public static Random rand = new Random((long) 5f);

    public ActionNode(CDFNode CDFNodeObj) {
        ActionCDFNode = CDFNodeObj;
    }
    //temporary
    public static int counter = 0;
    public static Vector3f camPos = new Vector3f(100, 150, 100);

    public static void processLookAroundBackground() {
        //TODO: add update code
        BackgroundNode BackgroundNode1 = ActionCDFNode.Background1;
        camPos.setX(camPos.getX() - 10 * tpf);
        camPos.setY(camPos.getY() - 12 * tpf);
        camPos.setZ(camPos.getZ() - 14 * tpf);
        Global.gMyMain.getCamera().setLocation(camPos);
        Global.gMyMain.getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        if (camPos.getX() < 10) {
            BackgroundNode1.bLookAroundBackgroundDone = true;
        }
        if (camPos.getY() < 10) {
            BackgroundNode1.bLookAroundBackgroundDone = true;
        }
        if (camPos.getZ() < 10) {
            BackgroundNode1.bLookAroundBackgroundDone = true;
        }

    }
    public static int nNoOfActor2 = 10;
    public static Vector3f[] Actor2Pos = new Vector3f[nNoOfActor2];

    public static void processActionTypeWalk() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.Actor.getLocalTranslation());
            } else {
                //Here Actor2 is null; this means that we need to find where 
                //to walk;
                for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
                    Vector2f Actor2Pos2D = PointsOnLake.getAPointNearLake();
                    System.out.println("Point Near Lake " + Actor2Pos2D.toString());
                    Actor2Pos[i].set(Actor2Pos2D.getX(), 0, Actor2Pos2D.getY());
                }

            }
            bFirstTimeForThisAction = false;
        }
        boolean bReachedTarget = true;

        for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
            BackgroundNode Background1 = ActionCDFNode.Background1;
            Terrain LakeTerrain = Background1.LakeTerrain;

            ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];
            if (CurrActor.bPositionSet == false) {
                Vector2f Actor1Pos2D = PointsOnLake.getAPointNearRoad();
                System.out.println("Point On Road " + Actor1Pos2D.toString());
                float height = LakeTerrain.getHeight(new Vector2f(Actor1Pos2D.getX(), Actor1Pos2D.getY()));
                CurrActor.Actor.setLocalTranslation(Actor1Pos2D.getX(), height, Actor1Pos2D.getY());
                CurrActor.bPositionSet = true;
            }
            Node Actor1Node = Actor1.TotalActorNodeInThisNode[i].Actor;
            Vector3f currLoc = Actor1Node.getLocalTranslation();

            //walk towards Actor2Loc, which can be actor or point on plane.
            float randomX = (float) (0 + (rand.nextFloat() * ((0.2))));
            System.out.println("Random translation " + randomX);
            float xMov = currLoc.getX() + (Actor2Pos[i].getX() > currLoc.getX() ? tpf + randomX : -tpf - randomX);
            float zMov = currLoc.getZ() + (Actor2Pos[i].getZ() > currLoc.getZ() ? tpf + randomX : -tpf - randomX);

            if (Math.abs(Actor2Pos[i].getX() - currLoc.getX()) > 1) {
                bReachedTarget = false;
            }
            if (Math.abs(Actor2Pos[i].getZ() - currLoc.getZ()) > 1) {
                bReachedTarget = false;
            }

            float height = LakeTerrain.getHeight(new Vector2f(xMov, xMov));
            currLoc.setY(height + 4.6f);
            currLoc.setX(xMov);
            currLoc.setZ(zMov);
            Actor1Node.setLocalTranslation(currLoc);
            //Actor1Node.move(0, 0, tpf);
        }
        /*
         counter++;
         if (counter > 100) {
         ActionCompleted = true;
         }
         */
        if(bReachedTarget)
            ActionCompleted = true;
    }
    public static boolean bInitDone = false;

    public static void init(CDFNode CDFNodeObj) {
        ActionCDFNode = CDFNodeObj;

        if (bInitDone == true) {
            return;
        }
        bInitDone = true;
        for (int i = 0; i < nNoOfActor2; i++) {
            Actor2Pos[i] = new Vector3f(0, 0, 0);
        }

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
        if (ActionCDFNode.Background1.bLookAroundBackgroundDone == false) {
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
