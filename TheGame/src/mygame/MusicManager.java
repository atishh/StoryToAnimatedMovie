
package mygame;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;

public class MusicManager {

    public static boolean bInit = false;
    public static AudioNode audioSource = null;

    public static void playMusic() {
        if (bInit == false) {
            audioSource = new AudioNode(Global.gMyMain.getAssetManager(), "Sounds/soft2.wav", false);
            //Global.gMyMain.getAssetManager().registerLocator(".", FileLocator.class);
            //audioSource = new AudioNode(Global.gMyMain.getAssetManager(), "atishoutput.wav.wav", false);
            //audioSource = new AudioNode(Global.gMyMain.getAssetManager(), "Sounds/rain1.ogg", false);
            audioSource.setVolume(3);
            audioSource.setPositional(false);
            audioSource.play();
            audioSource.setLooping(true);
            bInit = true;
        }
    }
}
