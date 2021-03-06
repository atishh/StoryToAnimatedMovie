package mygame;

import com.aurellem.capture.Capture;
import com.aurellem.capture.IsoTimer;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
//import com.jme3.app.state.VideoRecorderAppState;
//import com.jme3.app.state.VideoRecorderAppState.IsoTimer;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.water.SimpleWaterProcessor;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 import javax.speech.AudioException;
 //this import javax.speech should be used for speech synthesis later.
 //import javax.speech.
 import javax.speech.EngineCreate;
 import javax.speech.EngineException;
 import javax.speech.EngineList;
 import javax.speech.EngineStateError;
 import javax.speech.synthesis.Synthesizer;
 import javax.speech.synthesis.SynthesizerModeDesc;
 import javax.speech.synthesis.Voice;
 * */

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication implements AnimEventListener {

    //private Vector3f lightDir = new Vector3f(-0.39f, -0.32f, -0.74f);
    private Vector3f lightDir = new Vector3f(0, -1, 0);
    private DirectionalLight sun = new DirectionalLight();
    private Terrain LakeTerrain;
    private Node Actor1;
    private Node Actor2;
    private AnimChannel channel;
    private AnimControl control;
    public DesignUnit parserObj;
    //public VideoRecorderAppState vrap = null;

    public static void main(String[] args) {
        Main app = new Main();
        try {
            //settings.setAudioRenderer(AurellemSystemDelegate.SEND);
            //JmeSystem.setSystemDelegate(new AurellemSystemDelegate());
            File video = File.createTempFile("JME-video", ".mkv");
            File audio = File.createTempFile("JME-audio", ".wav");
            app.setTimer(new IsoTimer(30));

            Capture.captureVideo(app, video);
            Capture.captureAudio(app, audio);
            AppSettings settings = new AppSettings(true);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        app.setSettings(settings);
            System.out.println(video.getCanonicalPath());
            System.out.println(audio.getCanonicalPath());
        } catch (Exception e) {
        }
        app.start();
        //System.out.println(video.getCanonicalPath());
        //System.out.println(audio.getCanonicalPath());
    }

    public Node createActor() {
        Node Actor = (Node) assetManager.loadModel("Models/Actors/Cube.mesh.j3o");
        rootNode.attachChild(Actor);
        control = Actor.getControl(AnimControl.class);
        control.addListener(this);
        channel = control.createChannel();
        channel.setAnim("walk");
        float height = LakeTerrain.getHeight(new Vector2f(0, 0));
        Actor.setLocalTranslation(0, height + 4.6f, 0);
        Actor.setShadowMode(RenderQueue.ShadowMode.Cast);
        return Actor;
    }

    public void createWater() {
        //Create water;
        SimpleWaterProcessor waterProcessor =
                new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(rootNode);
        waterProcessor.setLightPosition(lightDir.mult(-3000));
        viewPort.addProcessor(waterProcessor);

        Spatial waterPlane =
                waterProcessor.createWaterGeometry(80, 80);
        waterPlane.setMaterial(waterProcessor.getMaterial());
        waterPlane.setLocalTranslation(-45, 1.5f, 65);
        rootNode.attachChild(waterPlane);
    }

    public void createLight() {
        /**
         * A white ambient light source.
         */
        /*
         AmbientLight ambient = new AmbientLight();
         ambient.setColor(ColorRGBA.White);
         rootNode.addLight(ambient);
         */
        /**
         * A white, directional light source
         */
        //DirectionalLight sun = new DirectionalLight();
        sun.setDirection((lightDir.normalizeLocal()));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
    }

    public void createShadow() {
        BasicShadowRenderer bsr = new BasicShadowRenderer(assetManager, 512);
        bsr.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        viewPort.addProcessor(bsr);
    }

    public void createBox() {
        /* A colored lit cube. Needs light source! */
        Box boxMesh = new Box(1f, 1f, 1f);
        Geometry boxGeo = new Geometry("Colored Box", boxMesh);
        Material boxMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        //RenderState rend = boxMat.getAdditionalRenderState();
        //rend.set
        boxMat.setBoolean("UseMaterialColors", true);
        boxMat.setColor("Ambient", ColorRGBA.Green);
        boxMat.setColor("Diffuse", ColorRGBA.Green);
        boxGeo.setMaterial(boxMat);
        boxGeo.setShadowMode(ShadowMode.Cast);
        boxGeo.setLocalTranslation(100, 3.6f, 100);
        rootNode.attachChild(boxGeo);
    }

    public void createScene() {
        Spatial lake = assetManager.loadModel("Scenes/Lake/lake2.j3o");
        rootNode.attachChild(lake);

        Node rootLake = lake.getParent();
        LakeTerrain = (Terrain) rootLake.getChild("terrain-lake2");
        rootLake.getChild("terrain-lake2").setShadowMode(RenderQueue.ShadowMode.Receive);
        //TerrainLodControl lodControl = new TerrainLodControl(LakeTerrain, getCamera());
        // LakeTerrain.addControl(lodControl);
        //LakeTerrain.
        TerrainLodControl lodControl = rootNode.getChild("terrain-lake2").getControl(TerrainLodControl.class);
        if (lodControl != null) {
            lodControl.setCamera(getCamera());
        }
    }

    public void PlantTree(Terrain LakeTerrain) {
        Random rand = new Random((long) 5f);

        for (int i = 0; i < 400; i++) {
            float randomX = (float) (-100 + (rand.nextFloat() * ((100 + 100) + 1)));
            float randomZ = (float) (-100 + (rand.nextFloat() * ((100 + 100) + 1)));
            if ((randomX < 25) && (randomX > -25)) {
                continue;
            }
            if ((randomZ < 25) && (randomZ > -25)) {
                continue;
            }
            //System.out.println("randomX = " + randomX);
            //System.out.println("randomY = " + randomZ);
            float height = LakeTerrain.getHeight(new Vector2f(randomX, randomZ));
            //System.out.println("height = " + height);
            Spatial tree = assetManager.loadModel("Scenes/Plants/Cylinder.001.mesh.j3o");
            //tree.setLodLevel(1);
            // Geometry geo = (Geometry)tree.;
//Logger.getLogger("test").severe("AFTER XML LOAD num of lod : " + geo.getMesh().getNumLodLevels());
            //tree.getMesh().getNumLodLevels();
            tree.setLocalTranslation(randomX, height + 0.6f, randomZ);
            tree.setShadowMode(RenderQueue.ShadowMode.Cast);
            //tree.setLocalTranslation(speed, speed, speed);
            //float randomRotate = (float) (-3.14f + (Math.random() * ((3.14f + 3.14f) + 1)));

            float randomRotate = (float) (-3.14f + (rand.nextFloat() * ((3.14f + 3.14f) + 1)));
            //System.out.println("randomRotate = " + randomRotate);
            tree.rotate(0f, randomRotate, 0f);
            rootNode.attachChild(tree);
        }


    }

    @Override
    public void simpleInitApp() {

        Global.gAssertManager = assetManager;
        Global.gMyMain = this;

        parserObj = new DesignUnit("C:\\Users\\atsingh\\Projects\\nlp\\source\\outfile.txt_bk_bk_bk");
        try {
            parserObj.processLineByLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * Load a model. Uses model and texture from jme3-test-data library!
         */
        // createScene();
        //PlantTree(LakeTerrain);
        // if(LakeTerrain != NULL)
        //createBox();
        //createLight();
        //createWater();
        //createShadow();

        /*
         Actor1 = createActor();
         Actor2 = createActor();

         PointsOnLake PointsOnLakeObj1 = new PointsOnLake();

         Vector2f point1 = PointsOnLakeObj1.walkablePoint[0];
         float height = LakeTerrain.getHeight(point1);
         Actor1.setLocalTranslation(point1.getX(), height + 4.6f, point1.getY());

         point1 = PointsOnLakeObj1.walkablePoint[1];
         height = LakeTerrain.getHeight(point1);
         Actor2.setLocalTranslation(point1.getX(), height + 4.6f, point1.getY());
         */
        // viewPort.setBackgroundColor(ColorRGBA.White);
        // Default speed is too slow
        flyCam.setMoveSpeed(20f);
        flyCam.setZoomSpeed(5f);
        //viewPort.setBackgroundColor(ColorRGBA.Blue);
        //voce.SpeechInterface.init();
        //commenting below code; to be used latter.

        // voce.SpeechInterface.init("C:\\Users\\atsingh\\Downloads\\voce-0.9.1.zip\\voce-0.9.1\\lib", true, false, "", "");
        // voce.SpeechInterface.synthesize("hello world hello world hello worlk");



        //voce.SpeechInterface.
        //System.setProperty("mbrola.base",);
        /*
         System.setProperty("mbrola.base", "C:\\Users\\atsingh\\Projects\\mbrola");
         String VoiceName = "mbrola_us1";
         VoiceManager vm = VoiceManager.getInstance();

         Voice voice;
         voice = vm.getVoice(VoiceName);

         System.out.println("VoicePitch = " + voice.getPitch());
         System.out.println("VoicePitchShift = " + voice.getPitchShift());
         System.out.println("VoicePitchRange = " + voice.getPitchRange());
         System.out.println("Voice string = " + voice.toString());
        
         voice.setPitch((float) 150.00);
         voice.setPitchShift((float) -100.905);
         voice.setPitchRange((float) 10.01);
         //"business", "casual", "robotic", "breathy"
         //voice.setStyle("business");
         voice.allocate();
         AudioPlayer audioPlayer = new SingleFileAudioPlayer("atishoutput.wav", Type.WAVE);
         //AudioPlayer audioPlayer = new SingleFileAudioPlayer();
         voice.setAudioPlayer(audioPlayer);
        
         voice.speak("atish kumar singh");
         voice.deallocate();
         audioPlayer.close();
         //System.exit(0);
         * */
        /*
         try {
         SynthesizerModeDesc generalDesc = new SynthesizerModeDesc(
         null, // engine name
         "general", // mode name
         Locale.US, // locale
         null, // running
         null);			// voice

         // Avoid using the JSAPI Central class (and the use of the speech.properties 
         // file) by using FreeTTSEngineCentral directly.
         FreeTTSEngineCentral central = new FreeTTSEngineCentral();
         EngineList list = central.createEngineList(generalDesc);


         EngineCreate creator = (EngineCreate) list.get(0);
         Synthesizer talker = (Synthesizer) creator.createEngine();


         //Synthesizer talker = Central.createSynthesizer(
         //        new SynthesizerModeDesc(null, "general", Locale.US, null, null));

         // Get it ready to speak
         talker.allocate();
         //  Voice theVoice = new Voice("hello I am atish kumar singh",
         //          Voice.GENDER_DONT_CARE, Voice.AGE_DONT_CARE, null);
         Voice theVoice = new Voice("mbrola_us1",
         Voice.GENDER_DONT_CARE, Voice.AGE_DONT_CARE, null);

         talker.getSynthesizerProperties().setVoice(theVoice);
         talker.resume();
         talker.speakPlainText("hi this is kumar ", null);
         //talker.
         //synthesize(" ");
         //talker.speak("Java is Wicked Cool, tell all your friends!", null);

         // Wait till speaking is done
         talker.waitEngineState(Synthesizer.QUEUE_EMPTY);

         // Clean up
         //talker.deallocate();
         } catch (IllegalArgumentException e) {
         } catch (EngineException e) {
         } catch (AudioException e) {
         } catch (EngineStateError e) {
         } catch (InterruptedException e) {
         } catch (Exception e) {
         }
         */

        //never pause the engine.
        this.setPauseOnLostFocus(false);
        //This is the main function for recording video/audio.
        //vrap = new VideoRecorderAppState();
        //stateManager.attach(vrap);

        /*
         //video/audio capture.
         try {
         //settings.setAudioRenderer(AurellemSystemDelegate.SEND);
         //JmeSystem.setSystemDelegate(new AurellemSystemDelegate());
         File video = File.createTempFile("JME-video", ".avi");
         File audio = File.createTempFile("JME-audio", ".wav");
         this.setTimer(new IsoTimer(30));
         Capture.captureVideo(this, video);
         Capture.captureAudio(this, audio);
         System.out.println(video.getCanonicalPath());
         System.out.println(audio.getCanonicalPath());
         } catch (Exception e) {
         }
         * */
    }

    public enum ActionState {

        STATE_ACTIONSTATEINITIAL, STATE_ACTIONSTATEMIDDLE, STATE_ACTIONSTATEEND,
        STATE_ACTIONSTATEEXIT
    };
    public ActionState mActionStateObj = ActionState.STATE_ACTIONSTATEINITIAL;
    public CDFNode pCurrActionCDFNode = null;
    BackgroundNode PrevBackgroundNode = null;
    BackgroundNode CurrBackgroundNode = null;

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        /*
         Vector3f currLoc = Actor1.getLocalTranslation();
         float height = LakeTerrain.getHeight(new Vector2f(currLoc.getX(), currLoc.getZ()));
         currLoc.setY(height + 4.6f);
         Actor1.setLocalTranslation(currLoc);
         Actor1.move(0,0,tpf);
         */
        //System.out.println("Inside simpleUpdate function");
        boolean bActionCompleted = false;
        switch (mActionStateObj) {
            case STATE_ACTIONSTATEINITIAL:
                System.out.println("In Simpleupdate , initializing action");
                pCurrActionCDFNode = parserObj.getNextCDFNodeTypeAction();
                ActionNode.bFirstTimeForThisAction = true;
                if (pCurrActionCDFNode == null) {
                    mActionStateObj = ActionState.STATE_ACTIONSTATEEXIT;
                } else {
                    CurrBackgroundNode = pCurrActionCDFNode.Background1;
                    if (CurrBackgroundNode != PrevBackgroundNode) {
                        System.out.println("In Simpleupdate , background changed");
                        pCurrActionCDFNode.PlaceActorsForThisBackground(CurrBackgroundNode);
                    }
                    PrevBackgroundNode = CurrBackgroundNode;

                    mActionStateObj = ActionState.STATE_ACTIONSTATEMIDDLE;
                }
                break;
            case STATE_ACTIONSTATEMIDDLE:
                bActionCompleted = pCurrActionCDFNode.PerformAction(tpf);
                if (bActionCompleted == true) {
                    mActionStateObj = ActionState.STATE_ACTIONSTATEEND;
                }
                break;
            case STATE_ACTIONSTATEEND:
                mActionStateObj = ActionState.STATE_ACTIONSTATEINITIAL;
                break;
            case STATE_ACTIONSTATEEXIT:
                //System.exit(0);
                //voce.SpeechInterface.destroy();
                //stateManager.detach(vrap);
                stop();
                break;
        }


    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
