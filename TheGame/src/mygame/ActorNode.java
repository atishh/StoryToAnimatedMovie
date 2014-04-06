/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;

/**
 *
 * @author aa496
 */
public class ActorNode {

    public String Name;
    public String label;
    public int idx;
    public String attribute;
    public Node Actor = null;
    public AnimChannel channel;
    public AnimControl control;

    public ActorNode(String lex, int idx) {
        this.Name = lex;
        this.label = lex + "-" + idx;
        this.idx = idx;
        attribute = "";
        createActor();

        channel = null;
        control = null;

    }

    public void createActor() {
        if (Global.gAssertManager != null) {
            Actor = (Node) Global.gAssertManager.loadModel("Models/Actors/Cube.mesh.j3o");
            control = Actor.getControl(AnimControl.class);

            channel = control.createChannel();
            channel.setAnim("walk");
            
        }
    }

    public void AttachNodesToRoot() {
        if ((Actor != null)) {
            System.out.println("Attaching to root node " + Actor.getName());
            Global.gMyMain.getRootNode().attachChild(Actor);
        }
    }
}
