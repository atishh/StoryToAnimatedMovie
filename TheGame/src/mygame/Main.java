package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.water.SimpleWaterProcessor;
import java.util.Random;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private Vector3f lightDir = new Vector3f(-0.39f, -0.32f, -0.74f);

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
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
            System.out.println("randomX = " + randomX);
            System.out.println("randomY = " + randomZ);
            float height = LakeTerrain.getHeight(new Vector2f(randomX, randomZ));
            System.out.println("height = " + height);
            Spatial tree = assetManager.loadModel("Scenes/Plants/Cylinder.001.mesh.j3o");
            //tree.setLodLevel(1);
            // Geometry geo = (Geometry)tree.;
//Logger.getLogger("test").severe("AFTER XML LOAD num of lod : " + geo.getMesh().getNumLodLevels());
            //tree.getMesh().getNumLodLevels();
            tree.setLocalTranslation(randomX, height + 0.6f, randomZ);
            tree.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            //tree.setLocalTranslation(speed, speed, speed);
            //float randomRotate = (float) (-3.14f + (Math.random() * ((3.14f + 3.14f) + 1)));

            float randomRotate = (float) (-3.14f + (rand.nextFloat() * ((3.14f + 3.14f) + 1)));
            System.out.println("randomRotate = " + randomRotate);
            tree.rotate(0f, randomRotate, 0f);
            rootNode.attachChild(tree);
        }


    }

    @Override
    public void simpleInitApp() {
        /**
         * Load a model. Uses model and texture from jme3-test-data library!
         */
        Spatial lake = assetManager.loadModel("Scenes/Lake/lake2.j3o");
        rootNode.attachChild(lake);

        Node rootNode = lake.getParent();
        Terrain LakeTerrain = (Terrain) rootNode.getChild("terrain-lake2");
        rootNode.getChild("terrain-lake2").setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        //TerrainLodControl lodControl = new TerrainLodControl(LakeTerrain, getCamera());
        // LakeTerrain.addControl(lodControl);
        //LakeTerrain.
        TerrainLodControl lodControl = rootNode.getChild("terrain-lake2").getControl(TerrainLodControl.class);
        if (lodControl != null) {
            lodControl.setCamera(getCamera());
        }
        PlantTree(LakeTerrain);




        // if(LakeTerrain != NULL)

        /**
         * A white ambient light source.
         */
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);

        /**
         * A white, directional light source
         */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((lightDir.normalizeLocal()));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

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


        //Shadow
        DirectionalLightShadowRenderer dlsr =
                new DirectionalLightShadowRenderer(assetManager, 1024, 2);
        dlsr.setLight(sun);

        //viewPort.addProcessor(dlsr);
        DirectionalLightShadowFilter dlsf =
                new DirectionalLightShadowFilter(assetManager, 1024, 2);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
        rootNode.setShadowMode(RenderQueue.ShadowMode.Off);
        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        // viewPort.setBackgroundColor(ColorRGBA.White);
        // Default speed is too slow
        flyCam.setMoveSpeed(20f);
        flyCam.setZoomSpeed(5f);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
