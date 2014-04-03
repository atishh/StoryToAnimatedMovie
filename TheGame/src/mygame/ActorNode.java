

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author aa496
 */
public class ActorNode {

    public String Name;
    public String label;
    public int idx;
    public String attribute;

    public ActorNode(String lex, int idx) {
        this.Name = lex;
        this.label = lex + "-" + idx;
        this.idx = idx;
        attribute = "";
    }


}
