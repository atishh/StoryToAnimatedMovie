package mygame;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.terrain.Terrain;
import java.util.HashMap;
import java.util.Map;

public class PlaceActorNode {

    public static CDFNode ActionCDFNode;
    public static Map<String, Runnable> placeActorForActionMap = new HashMap<String, Runnable>();
    public static int nNoOfNearPoints = 5;
    public static Vector3f[] nearPoint = new Vector3f[nNoOfNearPoints];
    public static Vector3f middlePoint;
    public static int nCurrPointNo = 0;
    public static boolean bPlaceHigh = false;

    public PlaceActorNode(CDFNode CDFNodeObj) {
        ActionCDFNode = CDFNodeObj;
    }

    public static Vector3f getAPointNearAPoint(String PointName) {
        if (nCurrPointNo == 0) {
            middlePoint = PointsOnLake.getAPoint(PointName);
            if (middlePoint == null) {
                middlePoint = PointsOnLake.getAPointNearRoad();
            }
        }
        Vector3f returnPoint = middlePoint.add(nearPoint[nCurrPointNo]);

        nCurrPointNo++;
        if (nCurrPointNo >= nNoOfNearPoints) {
            nCurrPointNo = 0;
        }

        return returnPoint;
    }

    public static void placeActor1Node() {

        ActorNode Actor1 = ActionCDFNode.Actor1;
        //Actor1.createActor();
        nCurrPointNo = 0;
        if (Actor1 != null) {
            for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
                BackgroundNode Background1 = ActionCDFNode.Background1;
                Terrain LakeTerrain = Background1.LakeTerrain;

                ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];
                CurrActor.createActor();
                if (CurrActor.bPositionSet == false) {
                    String PointName = null;
                    //If the actor is not human, then just place the actor on the 
                    //location given by TalkString.
                    //For ex: The deer walk to the water. Here place the actor deer
                    //in the water itself.
                    if (Actor1.TotalActorNodeInThisNode[i].bPassiveActor == true) {
                        PointName = ActionCDFNode.TalkString;
                    }
                    if (Actor1.TotalActorNodeInThisNode[i].getIsHuman() == false) {
                        if (Actor1.getIsHuman() == false) {
                            PointName = ActionCDFNode.TalkString;
                        }
                    }
                    if (PointName == null) {
                        PointName = ActionCDFNode.TalkString.trim();
                    }
                    if (ActionCDFNode.label.equalsIgnoreCase("swim")) {
                        PointName = "water";
                    }
                    Vector3f Actor1PosTemp = getAPointNearAPoint(PointName);
                    System.out.println("Placing Actor " + CurrActor.Name + " On Road at " + Actor1PosTemp.toString());
                    float height = LakeTerrain.getHeight(new Vector2f(Actor1PosTemp.getX(), Actor1PosTemp.getZ()));
                    if (bPlaceHigh == true) {
                        //for action like birds fly, we have to place actors in the sky.
                        height = height + 50;
                    }
                    if (CurrActor.Name.trim().equalsIgnoreCase("sun")) {
                        //for objects like sun, place it high.
                        height = height + 200;
                    }
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

    public static void placeActorNodeForFly() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        bPlaceHigh = true;
        placeActor1Node();
    }

    public static void placeActorNodeForSwim() {
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

        nearPoint[0] = new Vector3f(0, 0, 0);
        nearPoint[1] = new Vector3f(-5, 0, 0);
        nearPoint[2] = new Vector3f(5, 0, 0);
        nearPoint[3] = new Vector3f(0, 0, -5);
        nearPoint[4] = new Vector3f(0, 0, 5);

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
                placeActorNodeForFly();
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
                placeActorNodeForSwim();
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
        bPlaceHigh = false;

        Runnable r = placeActorForActionMap.get(ActionType.toLowerCase());
        if (r != null) {
            r.run();
        } else {
            bActionNotSupported = true;
            System.out.println("Currently Action Type " + ActionType + " is not supported");
        }


    }
}
