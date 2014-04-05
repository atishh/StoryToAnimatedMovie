/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author atsingh
 */
public class SupportedBackground {

    static boolean IsSupported(String s) {
        if (s.equalsIgnoreCase("camp")) {
            return true;
        }
        if (s.equalsIgnoreCase("lake")) {
            return true;
        }
        return false;
    }
}
