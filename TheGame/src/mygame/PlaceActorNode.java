package mygame;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.terrain.Terrain;
import java.util.HashMap;
import java.util.Map;

public class PlaceActorNode {

    public static CDFNode ActionCDFNode;
    public static Map<String, Runnable> placeActorForActionMap = new HashMap<String, Runnable>();

    public PlaceActorNode(CDFNode CDFNodeObj) {
        ActionCDFNode = CDFNodeObj;
    }

    public static void placeActor1Node() {
        
        ActorNode Actor1 = ActionCDFNode.Actor1;
        //Actor1.createActor();
        if (Actor1 != null) {
            for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
                BackgroundNode Background1 = ActionCDFNode.Background1;
                Terrain LakeTerrain = Background1.LakeTerrain;

                ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];
                CurrActor.createActor();
                if (CurrActor.bPositionSet == false) {
                    Vector3f Actor1PosTemp = PointsOnLake.getAPointNearRoad();
                    System.out.println("Point On Road " + Actor1PosTemp.toString());
                    float height = LakeTerrain.getHeight(new Vector2f(Actor1PosTemp.getX(), Actor1PosTemp.getZ()));
                    CurrActor.Actor.setLocalTranslation(Actor1PosTemp.getX(), height + CurrActor.getHeight(), Actor1PosTemp.getZ());
                    CurrActor.bPositionSet = true;
                }
            }
            System.out.println("Totalno of actors1 " + Actor1.nTotalNoOfActorsInThisNode);
        }
    }

    public static void placeActorNodeForStart() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        placeActor1Node();
    }

    public static void placeActorNodeForMake() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        placeActor1Node();
    }

    public static void placeActorNodeForStand() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        placeActor1Node();
    }

    public static void placeActorNodeForSit() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        placeActor1Node();
    }

    public static void placeActorNodeForLook() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        placeActor1Node();
    }

    public static void placeActorNodeForWalk() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        placeActor1Node();
    }
    public static boolean bInitDone = false;

    public static void init(CDFNode CDFNodeObj) {
        ActionCDFNode = CDFNodeObj;

        if (bInitDone == true) {
            return;
        }
        bInitDone = true;

        placeActorForActionMap.put("drink", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });
        placeActorForActionMap.put("eat", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });
        placeActorForActionMap.put("fly", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });
        placeActorForActionMap.put("join", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });
        placeActorForActionMap.put("look", new Runnable() {
            public void run() {
                placeActorNodeForLook();
            }
        });
        placeActorForActionMap.put("make", new Runnable() {
            public void run() {
                placeActorNodeForMake();
            }
        });
        placeActorForActionMap.put("run", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });
        placeActorForActionMap.put("say", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });
        placeActorForActionMap.put("scramble", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });
        placeActorForActionMap.put("sit", new Runnable() {
            public void run() {
                placeActorNodeForSit();
            }
        });
        placeActorForActionMap.put("sleep", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });
        placeActorForActionMap.put("stand", new Runnable() {
            public void run() {
                placeActorNodeForStand();
            }
        });
        placeActorForActionMap.put("start", new Runnable() {
            public void run() {
                placeActorNodeForStart();
            }
        });
        placeActorForActionMap.put("stop", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });
        placeActorForActionMap.put("swim", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });
        placeActorForActionMap.put("wake", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });
        placeActorForActionMap.put("walk", new Runnable() {
            public void run() {
                placeActorNodeForWalk();
            }
        });

    }

    public static void PlaceActorWapper(String ActionType) {

        boolean bActionNotSupported = false;

        Runnable r = placeActorForActionMap.get(ActionType.toLowerCase());
        if (r != null) {
            r.run();
        } else {
            bActionNotSupported = true;
            System.out.println("Currently Action Type " + ActionType + " is not supported");
        }


    }
}
