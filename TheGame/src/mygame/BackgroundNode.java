
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.water.SimpleWaterProcessor;
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
    public SimpleWaterProcessor waterProcessor;
    public BasicShadowRenderer bsr;

    public BackgroundNode(String lex, int idx) {
        this.Name = lex;
        this.label = lex + "-" + idx;
        this.idx = idx;
        attribute = "";
        createBackground();
    }

    public void AttachNodesToRoot() {

        if ((Background != null)) {
            System.out.println("Attaching to root node " + Background.getName());
            Global.gMyMain.getRootNode().attachChild(Background);
            if (waterProcessor != null) {
                Global.gMyMain.getViewPort().addProcessor(waterProcessor);
            }
            if (bsr != null) {
                Global.gMyMain.getViewPort().addProcessor(bsr);
            }
            if(sun != null)
                Global.gMyMain.getRootNode().addLight(sun);
        }

    }

    public void CreateLake() {
        Spatial lake = Global.gAssertManager.loadModel("Scenes/Lake/lake2.j3o");
        Background = new Node();
        Background.attachChild(lake);
        Node rootLake = lake.getParent();
        LakeTerrain = (Terrain) rootLake.getChild("terrain-lake2");
        rootLake.getChild("terrain-lake2").setShadowMode(RenderQueue.ShadowMode.Receive);
        TerrainLodControl lodControl = rootLake.getChild("terrain-lake2").getControl(TerrainLodControl.class);
        if (lodControl != null) {
            lodControl.setCamera(Global.gMyMain.getCamera());
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

        sun.setDirection((lightDir.normalizeLocal()));
        sun.setColor(ColorRGBA.White);
       // Background.addLight(sun);
    }

    public void createShadow() {
        bsr = new BasicShadowRenderer(Global.gAssertManager, 512);
        bsr.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());

    }

    public void createBackground() {
        if ((Global.gAssertManager != null) && (Global.gMyMain != null)) {
            CreateLake();
            PlantTree(LakeTerrain);
            CreateLight();
            CreateWater();
            createShadow();
        }
    }
}
