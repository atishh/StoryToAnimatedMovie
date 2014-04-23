package mygame;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.terrain.Terrain;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ActionNode {

    public static CDFNode ActionCDFNode;
    public static CDFNode PastActionCDFNode;
    public static CDFNode FutureActionCDFNode;
    public static Map<String, Runnable> processActionMap = new HashMap<String, Runnable>();
    public static boolean ActionCompleted = false;
    public static float tpf;
    public static boolean bFirstTimeForThisAction = true;
    public static Random rand = new Random((long) 5f);
    public static boolean bCamInUse = false;
    public static int nCurrActionNo = 0;
    public static int nAccelerateUptoActionNo = 10;
    public static float nAccel = 1;
    public static int nNoOfActor2 = 10;
    public static Vector3f[] Actor2Pos = new Vector3f[nNoOfActor2];
    public static Vector3f Actor1LookAt = new Vector3f(0, 0, 0);
    public static int nSubtitleDuration = 5;
    public static int nNoOfNearPoints = 5;
    public static Vector3f[] nearPoint = new Vector3f[nNoOfNearPoints];
    public static Vector3f middlePoint;
    public static int nCurrPointNo = 0;

    public static Vector3f getAPointNearAPoint(String PointName, Vector3f origPoint) {
        if (nCurrPointNo == 0) {
            middlePoint = PointsOnLake.getAPoint(PointName, origPoint);
        }
        //if middlePoint is null, try to find if there is an actor with this name.
        if (middlePoint == null) {
            ActorNode ActorNodeObj = ActorNode.GetAnActorNamed(PointName);
            if (ActorNodeObj != null) {
                Vector3f Position = ActorNodeObj.TotalActorNodeInThisNode[0].Actor.getLocalTranslation();
                middlePoint = Position.add(10, 3, 10);
            }
        }
        if (middlePoint == null) {
            return null;
        }

        Vector3f returnPoint = middlePoint.add(nearPoint[nCurrPointNo]);

        nCurrPointNo++;
        if (nCurrPointNo >= nNoOfNearPoints) {
            nCurrPointNo = 0;
        }
        return returnPoint;
    }

    public ActionNode(CDFNode CDFNodeObj) {
        ActionCDFNode = CDFNodeObj;
    }
    //temporary
    public static int counter = 0;
    public static Vector3f camPos = new Vector3f(100, 150, 100);

    public static void perplexCamera() {
        //Move the camera little bit, since stationary camera looks dull.
        ActorNode Actor1 = ActionCDFNode.Actor1;
        Vector3f camLoc = Global.gMyMain.getCamera().getLocation();
        camLoc.setX(camLoc.getX() + tpf);
        camLoc.setY(camLoc.getY() + tpf);
        camLoc.setZ(camLoc.getZ() + tpf);
        Global.gMyMain.getCamera().setLocation(camLoc);
        Node Actor1Node = Actor1.TotalActorNodeInThisNode[0].Actor;
        Vector3f currLoc = Actor1Node.getLocalTranslation();
        Global.gMyMain.getCamera().lookAt(currLoc, Vector3f.UNIT_Y);
    }

    public static void positionCamera() {
        ActorNode Actor1 = ActionCDFNode.Actor1;
        Node Actor1Node = Actor1.TotalActorNodeInThisNode[0].Actor;
        Vector3f currLoc = Actor1Node.getLocalTranslation();

        float camX = Actor2Pos[0].getX() > currLoc.getX() ? Actor2Pos[0].getX() + 35
                : Actor2Pos[0].getX() - 35;
        float camZ = Actor2Pos[0].getZ() > currLoc.getZ() ? Actor2Pos[0].getZ() + 35
                : Actor2Pos[0].getZ() - 35;
        float camY = Actor2Pos[0].getY() + 10;
        System.out.println("Camera Pos x " + camX + " camera pos z " + camZ);
        camPos.setX(camX);
        camPos.setY(camY);
        camPos.setZ(camZ);
        Global.gMyMain.getCamera().setLocation(camPos);
        Global.gMyMain.getCamera().lookAt(currLoc, Vector3f.UNIT_Y);
    }

    public static void processCounter(int nCountLimit) {
        counter++;
        if (counter * nAccel > nCountLimit) {
            ActionCompleted = true;
            bCamInUse = false;
        }
    }

    public static void handleCamera(Vector3f myCamLoc) {
        //handle camera
        if (myCamLoc == null) {
            handleCamera();
        } else {
            if (bCamInUse == false) {
                Vector3f CamLocTemp = new Vector3f(myCamLoc);
                CamLocTemp = CamLocTemp.add(0, 10, 0);
                Global.gMyMain.getCamera().setLocation(CamLocTemp);
                Global.gMyMain.getCamera().lookAt(myCamLoc, Vector3f.UNIT_Y);
                bCamInUse = true;
            } else {
                //Move the camera little bit, since stationary camera looks dull.
                perplexCamera();
            }
        }
    }

    public static void handleCamera() {
        //handle camera
        if (bCamInUse == false) {
            positionCamera();
            bCamInUse = true;
        } else {
            //Move the camera little bit, since stationary camera looks dull.
            perplexCamera();
        }
    }

    public static void processLookAroundBackground() {
        //TODO: add update code
        bCamInUse = true;
        BackgroundNode BackgroundNode1 = ActionCDFNode.Background1;
        camPos.setX(camPos.getX() - 10 * nAccel * tpf);
        camPos.setY(camPos.getY() - 12 * nAccel * tpf);
        camPos.setZ(camPos.getZ() - 14 * nAccel * tpf);
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

        if (BackgroundNode1.bLookAroundBackgroundDone == true) {
            bCamInUse = false;
        }

    }

    public static ActorNode createPassiveActorForThisAction(ActorNode CurrActor, String sEatString) {
        ActorNode NewActor = new ActorNode(sEatString, 0, true, ActionCDFNode);
        NewActor.createActor();
        NewActor.AttachNodesToRoot();
        if (NewActor.bPositionSet == false) {
            Vector3f Actor1PosTemp = CurrActor.Actor.getLocalTranslation();
            Actor1PosTemp = Actor1PosTemp.add(12, 3, 10);
            System.out.println("Placing Actor " + sEatString + " at " + Actor1PosTemp.toString());
            NewActor.Actor.setLocalTranslation(Actor1PosTemp.getX(), Actor1PosTemp.getY(), Actor1PosTemp.getZ());
            CurrActor.bPositionSet = true;
        }
        return NewActor;
    }

    public static void processActionTypeWake() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        Vector3f myCamLoc = null;

        if (bFirstTimeForThisAction == true) {

            //Since it is sleep, it is most likely to be night, so create night sky.
            ActionCDFNode.Background1.CreateDayBackground();
            ActionCDFNode.Actor1.DetachNodesFromRoot();
            ActionCDFNode.Actor1.AttachNodesToRoot();
            ActionCDFNode.PlaceActorsForThisBackground(ActionCDFNode.Background1);
            if (ActionCDFNode.Actor2 != null) {
                ActionCDFNode.Actor2.AttachNodesToRoot();
            }
            for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
                if (Actor1.TotalActorNodeInThisNode[i].getIsHuman()) {
                    ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];
                    Node Actor1Node = CurrActor.Actor;
                    Actor1Node.rotate((FastMath.PI) / 2, 0, 0);
                }
                myCamLoc = Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation();
            }

            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }

            //Look around background.
            //camPos = new Vector3f(100, 150, 100);
            camPos.set(100, 150, 100);
            ActionCDFNode.Background1.bLookAroundBackgroundDone = false;
            bFirstTimeForThisAction = false;
            counter = 0;
        }

        //handle camera
        if (ActionCDFNode.Background1.bLookAroundBackgroundDone == true) {
            myCamLoc = Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation();
            handleCamera(myCamLoc);
            processCounter(50);
        }
    }

    public static void processActionTypeSleep() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        Vector3f myCamLoc = null;

        if (bFirstTimeForThisAction == true) {

            //Since it is sleep, it is most likely to be night, so create night sky.
            ActionCDFNode.Background1.CreateNightBackground();
            ActionCDFNode.Actor1.DetachNodesFromRoot();
            ActionCDFNode.Actor1.AttachNodesToRoot();
            if (ActionCDFNode.Actor2 != null) {
                ActionCDFNode.Actor2.AttachNodesToRoot();
            }
            Node HouseNode = ActionCDFNode.Background1.getHouseNode();
            if (HouseNode != null) {
                Vector3f HouseLoc = HouseNode.getLocalTranslation();
                for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
                    if (Actor1.TotalActorNodeInThisNode[i].getIsHuman()) {
                        ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];
                        Node Actor1Node = Actor1.TotalActorNodeInThisNode[i].Actor;
                        Actor1Node.setLocalTranslation(HouseLoc.add(-5 + i * 3, 0, 0));
                        Actor1Node.rotate((FastMath.PI) / 2, 0, 0);
                        Vector3f currLoc = new Vector3f(Actor1Node.getLocalTranslation());
                        //Look very high.
                        currLoc.setY(100);
                        Actor1LookAt.set(currLoc);
                        if (CurrActor.bPassiveActor == false) {
                            Actor1Node.lookAt(Actor1LookAt, Vector3f.UNIT_Y);
                        }
                        //Actor1Node.move(0, 0, tpf);
                    }
                }
                myCamLoc = Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation();
            }

            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }

            //Look around background.
            //camPos = new Vector3f(100, 150, 100);
            camPos.set(100, 150, 100);
            ActionCDFNode.Background1.bLookAroundBackgroundDone = false;
            bFirstTimeForThisAction = false;
            counter = 0;
        }

        //handle camera
        if (ActionCDFNode.Background1.bLookAroundBackgroundDone == true) {
            myCamLoc = Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation();
            handleCamera(myCamLoc);
            processCounter(50);
        }
    }

    public static void processActionTypeFly() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }
            bFirstTimeForThisAction = false;
            counter = 0;
        }

        //handle camera
        handleCamera();
        processCounter(10);
    }

    public static void processActionTypeDrink() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }
            bFirstTimeForThisAction = false;
            counter = 0;
        }

        //handle camera
        handleCamera();
        processCounter(5);
    }

    public static void processActionTypeEat() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            nCurrPointNo = 0;
            String sEatString = ActionCDFNode.TalkString;

            for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
                System.out.println("Eat String is " + sEatString);
                ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];
                ActorNode NewActor = createPassiveActorForThisAction(CurrActor, sEatString);
                if (NewActor != null) {
                    Actor2Pos[i].set(NewActor.Actor.getLocalTranslation());
                }
            }

            bFirstTimeForThisAction = false;
            counter = 0;
        }
        boolean bReachedTarget = true;

        for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
            ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];

            Node Actor1Node = Actor1.TotalActorNodeInThisNode[i].Actor;
            Vector3f currLoc = Actor1Node.getLocalTranslation();

            //Look towards Actor2.
            //Vector3f Actor1LookAt = new Vector3f(Actor2Pos[i]);
            Actor1LookAt.set(Actor2Pos[i]);
            //Actor1LookAt.setY(5);
            //Don't change anything if actor is passive for ex:
            //The cabin looks very beautiful. Here cabin is passive.
            if (CurrActor.bPassiveActor == false) {
                Actor1Node.lookAt(Actor1LookAt, Vector3f.UNIT_Y);
            }
            //Actor1Node.move(0, 0, tpf);
        }
        //handle camera
        handleCamera();

        processCounter(20);

    }

    public static void processActionTypeSwim() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        int nSwimDuration = 5;
        if (bFirstTimeForThisAction == true) {
            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }
            bFirstTimeForThisAction = false;
            counter = 0;
        }
        //for human rotate 90 degress for swim

        for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
            if (Actor1.TotalActorNodeInThisNode[i].getIsHuman()) {
                ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];

                Node Actor1Node = Actor1.TotalActorNodeInThisNode[i].Actor;
                Actor1Node.rotate((FastMath.PI) / 2, 0, 0);
                Vector3f currLoc = Actor1Node.getLocalTranslation();
                currLoc.setY(0);
                Actor1LookAt.set(currLoc);
                if (CurrActor.bPassiveActor == false) {
                    Actor1Node.lookAt(Actor1LookAt, Vector3f.UNIT_Y);
                }
                nSwimDuration = 50;
                //Actor1Node.move(0, 0, tpf);
            }
        }
        //handle camera
        handleCamera();
        processCounter(nSwimDuration);
    }

    public static void processActionTypeSay() {

        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;
        if (PastActionCDFNode != null) {
            System.out.println("PastCDFNode name = " + PastActionCDFNode.label);
        }
        if (FutureActionCDFNode != null) {
            System.out.println("FutureCDFNode name = " + FutureActionCDFNode.label);
        }

        if (bFirstTimeForThisAction == true) {
            nSubtitleDuration = 5;
            //Adding some sort of intelligence to find to whom this actor is 
            //saying. The other actor can be found from past or future cdf node.
            if (Actor2 == null) {
                if (PastActionCDFNode != null) {
                    if (PastActionCDFNode.label.equals("say")) {
                        Actor2 = PastActionCDFNode.Actor1.TotalActorNodeInThisNode[0];
                    }
                }
            }
            if (Actor2 == null) {
                if (FutureActionCDFNode != null) {
                    if (FutureActionCDFNode.label.equals("say")) {
                        Actor2 = FutureActionCDFNode.Actor1.TotalActorNodeInThisNode[0];
                    }
                }
            }

            //still if actor2 is null, then direcly assign actor2 to actor1 of previous action
            if (Actor2 == null) {
                if (PastActionCDFNode != null) {
                    Actor2 = PastActionCDFNode.Actor1.TotalActorNodeInThisNode[0];
                }
            }

            if (Actor2 == null) {
                if (FutureActionCDFNode != null) {
                    Actor2 = FutureActionCDFNode.Actor1.TotalActorNodeInThisNode[0];
                }
            }

            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }

            if (Actor2 != null) {
                for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
                    ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];
                    Node Actor1Node = CurrActor.Actor;
                    Vector3f Actor2Loc = Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation();
                    Actor1Node.lookAt(Actor2Loc, Vector3f.UNIT_Y);
                }
            }

            String sTalkString = ActionCDFNode.TalkString;
            if (sTalkString != null) {
                int nStringCount = sTalkString.length();
                SubtitleManager.setSubtitle(sTalkString);
                nSubtitleDuration = nStringCount;
                //This is voice speaker. I don't know how it works yet.
                //We have to initialize it first.
                //voce.SpeechInterface.init();
                //commenting this code; to be used latter.
                //voce.SpeechInterface.synthesize(sTalkString);
            }
            bFirstTimeForThisAction = false;
            counter = 0;
        }

        //System.out.println("Totalno of actors1 " + Actor1.nTotalNoOfActorsInThisNode);

        //handle camera
        handleCamera();

        processCounter(nSubtitleDuration);

        if (ActionCompleted == true) {
            SubtitleManager.setSubtitle("");
        }
    }

    public static void processActionTypeStop() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }
            bFirstTimeForThisAction = false;
            counter = 0;
        }

        //System.out.println("Totalno of actors1 " + Actor1.nTotalNoOfActorsInThisNode);

        //handle camera
        handleCamera();

        processCounter(2);

    }

    public static void processActionTypeScramble() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }
            bFirstTimeForThisAction = false;
            counter = 0;
        }

        System.out.println("Action scramble is currently partially supported");

        //handle camera
        handleCamera();

        processCounter(2);

    }

    public static void processActionTypeStart() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }
            bFirstTimeForThisAction = false;
            counter = 0;
        }

        System.out.println("Totalno of actors1 " + Actor1.nTotalNoOfActorsInThisNode);

        //handle camera
        handleCamera();

        processCounter(5);

    }

    public static void processActionTypeMake() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }
            bFirstTimeForThisAction = false;
            counter = 0;
        }

        //handle camera
        handleCamera();

        processCounter(5);

    }

    public static void processActionTypeStand() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }
            bFirstTimeForThisAction = false;
            counter = 0;
        }

        //handle camera
        handleCamera();

        processCounter(20);


    }

    public static void processActionTypeSit() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            } else {
                Actor2Pos[0].set(Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
            }
            bFirstTimeForThisAction = false;
            counter = 0;
        }

        //handle camera
        handleCamera();

        processCounter(20);


    }

    public static void processActionTypeLook() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            nCurrPointNo = 0;
            if (Actor2 != null) {
                for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
                    Actor2Pos[i].set(Actor2.Actor.getLocalTranslation());
                }
            } else {
                //Here Actor2 is null; this means that we need to find where 
                //to walk;
                String sPosition = ActionCDFNode.TalkString;

                for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
                    System.out.println("sPosition String is " + sPosition);
                    Vector3f Actor2PosTemp = getAPointNearAPoint(sPosition,
                            Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
                    if (Actor2PosTemp != null) {
                        System.out.println("Point Near Lake " + Actor2PosTemp.toString());
                        Actor2Pos[i].set(Actor2PosTemp);
                    } else {
                        if (FutureActionCDFNode != null) {
                            Actor2 = FutureActionCDFNode.Actor1.TotalActorNodeInThisNode[0];
                            System.out.println("Future cdf node " + FutureActionCDFNode.label);
                            if (Actor2 != null) {
                                Actor2Pos[i].set(Actor2.Actor.getLocalTranslation());
                            }
                        }
                        if (Actor2 == null) {
                            //Get a future action node.
                            System.out.println("Failed to set Actor2Pos since we couldn't able to find"
                                    + "a point to place it");
                            ActionCompleted = true;
                            bCamInUse = false;
                            return;
                        }
                    }
                }

            }
            bFirstTimeForThisAction = false;
            counter = 0;
        }
        boolean bReachedTarget = true;

        for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
            ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];

            Node Actor1Node = Actor1.TotalActorNodeInThisNode[i].Actor;
            Vector3f currLoc = Actor1Node.getLocalTranslation();

            //Look towards Actor2.
            //Vector3f Actor1LookAt = new Vector3f(Actor2Pos[i]);
            Actor1LookAt.set(Actor2Pos[i]);
            Actor1LookAt.setY(Actor1LookAt.getY() + Actor1.getHeight());
            //Don't change anything if actor is passive for ex:
            //The cabin looks very beautiful. Here cabin is passive.
            if (CurrActor.bPassiveActor == false) {
                Actor1Node.lookAt(Actor1LookAt, Vector3f.UNIT_Y);
            }
            //Actor1Node.move(0, 0, tpf);
        }
        //handle camera
        handleCamera();

        processCounter(20);


    }

    public static void processActionTypeRun() {
        //Run is similar to walk just with faster speed.
        //To change it latter since animation of "walk" and "run" are different.
        float nPrevAccel = nAccel;
        nAccel = 2 * nAccel;
        processActionTypeWalk();
        nAccel = nPrevAccel;
    }

    public static void processActionTypeJoin() {
        //For now just call walk action.
        processActionTypeWalk();
    }

    public static void processActionTypeWalk() {
        //TODO: add update code
        ActorNode Actor1 = ActionCDFNode.Actor1;
        ActorNode Actor2 = ActionCDFNode.Actor2;

        if (bFirstTimeForThisAction == true) {
            nCurrPointNo = 0;
            if (Actor2 != null) {
                Actor2Pos[0].set(Actor2.Actor.getLocalTranslation());
            } else {

                //Here Actor2 is null; this means that we need to find where 
                //to walk;
                String sPosition = ActionCDFNode.TalkString;

                for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
                    System.out.println("sPosition String is " + sPosition);
                    Vector3f Actor2PosTemp = getAPointNearAPoint(sPosition,
                            Actor1.TotalActorNodeInThisNode[0].Actor.getLocalTranslation());
                    if (Actor2PosTemp != null) {
                        System.out.println("Point Near Lake " + Actor2PosTemp.toString());
                        Actor2Pos[i].set(Actor2PosTemp);
                    } else {
                        System.out.println("Failed to set Actor2Pos since we couldn't able to find"
                                + "a point to place it");
                        ActionCompleted = true;
                        bCamInUse = false;
                        return;
                    }
                    //Look towards Actor2.
                    //Vector3f Actor1LookAt = new Vector3f(Actor2Pos[i]);
                    ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];
                    Node Actor1Node = CurrActor.Actor;
                    Actor1LookAt.set(Actor2Pos[i]);
                    Actor1LookAt.setY(Actor1LookAt.getY() + CurrActor.getHeight());
                    Actor1Node.lookAt(Actor1LookAt, Vector3f.UNIT_Y);
                }

            }
            bFirstTimeForThisAction = false;
        }
        boolean bReachedTarget = true;
        //System.out.println("Totalno of actors1 " + Actor1.nTotalNoOfActorsInThisNode);

        for (int i = 0; i < Actor1.nTotalNoOfActorsInThisNode; i++) {
            BackgroundNode Background1 = ActionCDFNode.Background1;
            Terrain LakeTerrain = Background1.LakeTerrain;

            ActorNode CurrActor = Actor1.TotalActorNodeInThisNode[i];

            Node Actor1Node = CurrActor.Actor;
            Vector3f currLoc = Actor1Node.getLocalTranslation();



            //walk towards Actor2Loc, which can be actor or point on plane.
            float randomX = (float) (0 + (rand.nextFloat() * ((0.2))));
            //System.out.println("Random translation " + randomX);
            float xMov = currLoc.getX() + nAccel * (Actor2Pos[i].getX() > currLoc.getX() ? tpf + randomX : -tpf - randomX);
            float zMov = currLoc.getZ() + nAccel * (Actor2Pos[i].getZ() > currLoc.getZ() ? tpf + randomX : -tpf - randomX);

            if (Math.abs(Actor2Pos[i].getX() - currLoc.getX()) > 5) {
                bReachedTarget = false;
            } else {
                //This is done so that xMov will not flicker because of randomX
                xMov = currLoc.getX();
            }

            if (Math.abs(Actor2Pos[i].getZ() - currLoc.getZ()) > 5) {
                bReachedTarget = false;
            } else {
                //This is done so that zMov will not flicker because of randomX
                zMov = currLoc.getZ();
            }

            float height = LakeTerrain.getHeight(new Vector2f(xMov, zMov));
            currLoc.setY(height + CurrActor.getHeight());
            currLoc.setX(xMov);
            currLoc.setZ(zMov);
            Actor1Node.setLocalTranslation(currLoc);

            //Actor1Node.move(0, 0, tpf);
        }
        //handle camera
        handleCamera();

        /*
         counter++;
         if (counter > 100) {
         ActionCompleted = true;
         }
         */
        if (bReachedTarget) {
            ActionCompleted = true;
            bCamInUse = false;
        }
    }
    public static boolean bInitDone = false;

    public static void init(CDFNode PastCDFNodeObj, CDFNode CDFNodeObj, CDFNode FutureCDFNodeObj) {
        ActionCDFNode = CDFNodeObj;
        PastActionCDFNode = PastCDFNodeObj;
        FutureActionCDFNode = FutureCDFNodeObj;

        if (bInitDone == true) {
            return;
        }
        bInitDone = true;

        nearPoint[0] = new Vector3f(0, 0, 0);
        nearPoint[1] = new Vector3f(-5, 0, 0);
        nearPoint[2] = new Vector3f(5, 0, 0);
        nearPoint[3] = new Vector3f(0, 0, -5);
        nearPoint[4] = new Vector3f(0, 0, 5);

        for (int i = 0; i < nNoOfActor2; i++) {
            Actor2Pos[i] = new Vector3f(0, 0, 0);
        }

        processActionMap.put("drink", new Runnable() {
            public void run() {
                processActionTypeDrink();
            }
        });
        processActionMap.put("eat", new Runnable() {
            public void run() {
                processActionTypeEat();
            }
        });
        processActionMap.put("fly", new Runnable() {
            public void run() {
                processActionTypeFly();
            }
        });
        processActionMap.put("join", new Runnable() {
            public void run() {
                processActionTypeJoin();
            }
        });
        processActionMap.put("look", new Runnable() {
            public void run() {
                processActionTypeLook();
            }
        });
        processActionMap.put("make", new Runnable() {
            public void run() {
                processActionTypeMake();
            }
        });
        processActionMap.put("run", new Runnable() {
            public void run() {
                processActionTypeRun();
            }
        });
        processActionMap.put("say", new Runnable() {
            public void run() {
                processActionTypeSay();
            }
        });
        processActionMap.put("scramble", new Runnable() {
            public void run() {
                processActionTypeScramble();
            }
        });
        processActionMap.put("sit", new Runnable() {
            public void run() {
                processActionTypeSit();
            }
        });
        processActionMap.put("sleep", new Runnable() {
            public void run() {
                processActionTypeSleep();
            }
        });
        processActionMap.put("stand", new Runnable() {
            public void run() {
                processActionTypeStand();
            }
        });
        processActionMap.put("start", new Runnable() {
            public void run() {
                processActionTypeStart();
            }
        });
        processActionMap.put("stop", new Runnable() {
            public void run() {
                processActionTypeStop();
            }
        });
        processActionMap.put("swim", new Runnable() {
            public void run() {
                processActionTypeSwim();
            }
        });
        processActionMap.put("wake", new Runnable() {
            public void run() {
                processActionTypeWake();
            }
        });
        processActionMap.put("walk", new Runnable() {
            public void run() {
                processActionTypeWalk();
            }
        });

    }

    public static void ProcessActionWrapper(String ActionType, float tpf_tmp) {

        //  System.out.println("tpf = " + tpf_tmp);
        //currently making tpf to 0.1, we have to see latter what to do with it.
        tpf = 0.1f;
        boolean bActionNotSupported = false;
        ActionCompleted = false;

        if (bFirstTimeForThisAction == true) {
            nCurrActionNo++;
            System.out.println("Processing action " + ActionType);
        }

        if (nCurrActionNo <= nAccelerateUptoActionNo) {
            nAccel = 6;
        } else {
            nAccel = 1;
        }
        //System.out.println("nCurrActionNo = " + nCurrActionNo);
        //System.out.println("nAccelerateUptoActionNo = " + nAccelerateUptoActionNo);
        //System.out.println("nAccel = " + nAccel);

        //Look Around Background in parallel to action.
        if (ActionCDFNode.Background1.bLookAroundBackgroundDone == false) {
            bCamInUse = true;
            processLookAroundBackground();

        } else {
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
