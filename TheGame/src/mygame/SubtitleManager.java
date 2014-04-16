/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author atsingh
 */
public class SubtitleManager {
    
    public static boolean bInit = false;
    public static BitmapText text = null;
     
    public static void setSubtitle(String SubtitleString)
    {
        if(bInit == false)
        {
            BitmapFont myGuiFont = Global.gMyMain.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
            text = new BitmapText(myGuiFont, false);
            text.setSize(myGuiFont.getCharSet().getRenderedSize());
            Global.gMyMain.getGuiNode().attachChild(text);
            bInit = true;
        }
        text.setText(SubtitleString);
        text.setColor(SubtitleString, ColorRGBA.Magenta);
        System.out.println("camera width = "+(Global.gMyMain.getCamera().getWidth() - text.getLineWidth()) / 2+"camera height = "+Global.gMyMain.getCamera().getHeight());
        text.setLocalTranslation((Global.gMyMain.getCamera().getWidth() - text.getLineWidth()) / 2, Global.gMyMain.getCamera().getHeight(), 0);
    }

}
