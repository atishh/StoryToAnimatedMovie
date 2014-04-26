
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author aa496
 */
public class BackgroundNode {

    public String Name;
    public String label;
    public int idx;
    public String attribute;
    public Node Background = null;
    Terrain LakeTerrain;
    private Vector3f lightDir = new Vector3f(0, -1, 0);
    private DirectionalLight sun = new DirectionalLight();
    private AmbientLight ambient;
    public SimpleWaterProcessor waterProcessor;
    public BasicShadowRenderer bsr;
    public boolean bLookAroundBackgroundDone = false;
    public boolean bIsAttachedToRoot = false;
    Spatial skyMorning = null;
    Spatial skyNight = null;
    PointLight Nightlamp = null;
    Spatial sceneSpatial = null;
    public static Map<String, Node> gPassiveActorMap = new HashMap<String, Node>();

    public BackgroundNode(String lex, int idx) {
        this.Name = lex;
        this.label = lex + "-" + idx;
        this.idx = idx;
        attribute = "";
        if (lex.trim().equalsIgnoreCase("lake")) {
            createLakeBackground();
        } else if (lex.trim().equalsIgnoreCase("library")) {
            createLibraryBackground();
        } else {
            createLakeBackground();
        }
        placeStaticObjects();
        bLookAroundBackgroundDone = false;
        bIsAttachedToRoot = false;
    }

    public void placeStaticObjects() {

        SceneGraphVisitor visitor = new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                if ((spatial != null) && (spatial.getName() != null)) {
                    System.out.println(spatial.getName());
                    if (spatial.getName().equals("house")) {
                        ActorData ActorDataObj = SupportedBackground.getPathForStaticObjects("house");
                        String ObjectPath = ActorDataObj.PhysicalPath;
                        Vector3f spatialLocation = spatial.getLocalTranslation();
                        float height = LakeTerrain.getHeight(new Vector2f(spatialLocation.getX(), spatialLocation.getZ()));
                        Spatial tree = Global.gAssertManager.loadModel(ObjectPath);
                        tree.setLocalTranslation(spatialLocation.getX(), height + ActorDataObj.nHeight, spatialLocation.getZ());
                        float nScale = ActorDataObj.nScale;
                        tree.setLocalScale(nScale, nScale, nScale);
                        tree.setShadowMode(RenderQueue.ShadowMode.Cast);
                        Background.attachChild(tree);
                    }
                }
            }
        };
        sceneSpatial.depthFirstTraversal(visitor);
    }

    public void AttachNodesToRoot() {

        if ((Background != null) && (bIsAttachedToRoot == false)) {
            System.out.println("Attaching to root node " + Background.getName());
            Global.gMyMain.getRootNode().attachChild(Background);
            if (waterProcessor != null) {
                Global.gMyMain.getViewPort().addProcessor(waterProcessor);
            }
            if (bsr != null) {
                Global.gMyMain.getViewPort().addProcessor(bsr);
            }
            if (sun != null) {
                Global.gMyMain.getRootNode().addLight(sun);
            }
            if (ambient != null) {
                Global.gMyMain.getRootNode().addLight(ambient);
            }
            bIsAttachedToRoot = true;
        }

    }

    public void RemoveLightsFromRoot() {
        if (sun != null) {
            Global.gMyMain.getRootNode().removeLight(sun);
        }
        if (ambient != null) {
            Global.gMyMain.getRootNode().removeLight(ambient);
        }
    }

    public void AddLightsToRoot() {
        if (sun != null) {
            Global.gMyMain.getRootNode().addLight(sun);
        }
        if (ambient != null) {
            Global.gMyMain.getRootNode().addLight(ambient);
        }
    }

    public void CreateLake() {
        Spatial lake = Global.gAssertManager.loadModel("Scenes/Lake/lake5.j3o");
        sceneSpatial = lake;
        Background = new Node();
        Background.attachChild(lake);
        Node rootLake = lake.getParent();

        //Initialize points on lake;
        PointsOnLake.lake = rootLake;

        LakeTerrain = (Terrain) rootLake.getChild("terrain-lake5");
        rootLake.getChild("terrain-lake5").setShadowMode(RenderQueue.ShadowMode.Receive);
        TerrainLodControl lodControl = rootLake.getChild("terrain-lake5").getControl(TerrainLodControl.class);
        if (lodControl != null) {
            lodControl.setCamera(Global.gMyMain.getCamera());
        }
    }

    public void CreateLibrary() {
        Spatial library = Global.gAssertManager.loadModel("Scenes/Library/library1.j3o");
        sceneSpatial = library;
        Background = new Node();
        Background.attachChild(library);
        Node rootLake = library.getParent();

        //Initialize points on lake;
        PointsOnLake.lake = rootLake;

        LakeTerrain = (Terrain) rootLake.getChild("terrain-library1");
        rootLake.getChild("terrain-library1").setShadowMode(RenderQueue.ShadowMode.Receive);
        TerrainLodControl lodControl = rootLake.getChild("terrain-library1").getControl(TerrainLodControl.class);
        if (lodControl != null) {
            lodControl.setCamera(Global.gMyMain.getCamera());
        }
    }

    public void CreateSky() {
        Texture west = Global.gAssertManager.loadTexture("Scenes/Lake/bluesky_back.jpg");
        Texture east = Global.gAssertManager.loadTexture("Scenes/Lake/bluesky_front.jpg");
        Texture north = Global.gAssertManager.loadTexture("Scenes/Lake/bluesky_left.jpg");
        Texture south = Global.gAssertManager.loadTexture("Scenes/Lake/bluesky_right.jpg");
        Texture up = Global.gAssertManager.loadTexture("Scenes/Lake/bluesky_top.JPG");
        Texture down = Global.gAssertManager.loadTexture("Scenes/Lake/bluesky_top.JPG");

        skyMorning = SkyFactory.createSky(Global.gAssertManager, west, east, north, south, up, down, Vector3f.UNIT_XYZ);
        Background.attachChild(skyMorning);
        if (skyNight != null) {
            Background.detachChild(skyNight);
        }
    }

    public void CreateNightSky() {
        Texture west = Global.gAssertManager.loadTexture("Scenes/Lake/sky_night1.jpg");
        Texture east = Global.gAssertManager.loadTexture("Scenes/Lake/sky_night1.jpg");
        Texture north = Global.gAssertManager.loadTexture("Scenes/Lake/sky_night1.jpg");
        Texture south = Global.gAssertManager.loadTexture("Scenes/Lake/sky_night1.jpg");
        Texture up = Global.gAssertManager.loadTexture("Scenes/Lake/sky_night1.jpg");
        Texture down = Global.gAssertManager.loadTexture("Scenes/Lake/sky_night1.jpg");

        skyNight = SkyFactory.createSky(Global.gAssertManager, west, east, north, south, up, down, Vector3f.UNIT_XYZ);
        Background.attachChild(skyNight);
        if (skyMorning != null) {
            Background.detachChild(skyMorning);
        }
    }

    public void CreateNightLight() {
        /**
         * A white, spot light source.
         */
        Nightlamp = new PointLight();
        Nightlamp.setPosition(Vector3f.ZERO);
        Nightlamp.setColor(ColorRGBA.White);
        Global.gMyMain.getRootNode().addLight(Nightlamp);
    }

    public void RemoveNightLight() {
        /**
         * A white, spot light source.
         */
        if (Nightlamp != null) {
            Global.gMyMain.getRootNode().removeLight(Nightlamp);
        }
    }

    public void CreateNightBackground() {
        CreateNightSky();
        RemoveLightsFromRoot();
        CreateNightLight();
    }

    public void CreateDayBackground() {
        CreateSky();
        AddLightsToRoot();
        RemoveNightLight();
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
            Spatial tree = Global.gAssertManager.loadModel("Scenes/Plants/Cylinder.001.mesh.j3o");

            tree.setLocalTranslation(randomX, height + 0.6f, randomZ);
            tree.setShadowMode(RenderQueue.ShadowMode.Cast);

            float randomRotate = (float) (-3.14f + (rand.nextFloat() * ((3.14f + 3.14f) + 1)));
            //System.out.println("randomRotate = " + randomRotate);
            tree.rotate(0f, randomRotate, 0f);
            Background.attachChild(tree);
        }

    }

    public void CreateWater() {
        //Create water;
        waterProcessor =
                new SimpleWaterProcessor(Global.gAssertManager);
        waterProcessor.setReflectionScene(Background);
        waterProcessor.setLightPosition(lightDir.mult(-3000));
        //Global.gMyMain.getViewPort().addProcessor(waterProcessor);

        Spatial waterPlane =
                waterProcessor.createWaterGeometry(80, 80);
        waterPlane.setMaterial(waterProcessor.getMaterial());
        waterPlane.setLocalTranslation(-45, 1.5f, 65);
        Background.attachChild(waterPlane);
    }

    public void CreateLight() {

        /**
         * A white ambient light source.
         */
        ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        //  rootNode.addLight(ambient);

        sun.setDirection((lightDir.normalizeLocal()));
        sun.setColor(ColorRGBA.White);
        // Background.addLight(sun);
    }

    public void createShadow() {
        bsr = new BasicShadowRenderer(Global.gAssertManager, 512);
        bsr.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());

    }

    public Node getHouseNode() {
        Node ReturnNode = null;
        ReturnNode = gPassiveActorMap.get("cabin");
        return ReturnNode;
    }

    public Node createPassiveActor(String Name, String ObjectPath) {
        //This is the passive actor.
        Node Actor = (Node) Global.gAssertManager.loadModel(ObjectPath);
        Vector3f ActorLoc = PointsOnLake.getAPointForBuild(Name);
        float height = LakeTerrain.getHeight(new Vector2f(ActorLoc.getX(), ActorLoc.getZ()));
        Actor.setLocalTranslation(ActorLoc.getX(), height + 2, ActorLoc.getZ());
        Actor.setLocalScale(10, 10, 10);
        //Make culling off, since otherwise when the camera is inside the house 
        //it will be transparent.
        Actor.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                if (spatial instanceof Geometry) {
                    ((Geometry) spatial).getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
                }
            }
        });

        /**
         * A white, spot light source.
         */
        PointLight lamp = new PointLight();
        lamp.setPosition(Actor.getLocalTranslation().subtract(0, 5, 0));
        lamp.setColor(ColorRGBA.White);
        Background.addLight(lamp);
        //Material cube2Mat = Global.gAssertManager.loadMaterial("Materials/newMaterial.j3m");
        //Actor.setMaterial(cube2Mat);
        Background.attachChild(Actor);
        gPassiveActorMap.put(Name, Actor);
        return Actor;
    }

    public void createLakeBackground() {
        if ((Global.gAssertManager != null) && (Global.gMyMain != null)) {
            CreateLake();
            CreateSky();
            //CreateNightSky();
            //Temporarily commenting it for faster fps
            //PlantTree(LakeTerrain);
            CreateLight();
            //CreateWater();
            //createShadow();
        }
    }

    public void createLibraryBackground() {
        if ((Global.gAssertManager != null) && (Global.gMyMain != null)) {
            CreateLibrary();
            CreateSky();
            CreateLight();
        }
    }
}
