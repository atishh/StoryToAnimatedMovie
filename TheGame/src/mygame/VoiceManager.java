package mygame;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource.Status;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;
import javax.sound.sampled.AudioFileFormat.Type;

public class VoiceManager {

    public static AudioNode audioSource = null;
    public static boolean bCreateAudioInit = false;
    public static boolean bSpeakInit = false;

    public static void speak(String sAudioFileName) {
        if (bSpeakInit == false) {
            Global.gMyMain.getAssetManager().registerLocator(".", FileLocator.class);
            bSpeakInit = true;
        }
        audioSource = new AudioNode(Global.gMyMain.getAssetManager(), sAudioFileName, false);
        audioSource.setVolume(3);
        audioSource.setPositional(false);
        audioSource.play();
        audioSource.setLooping(false);
        //audioSource.get
    }

    public static boolean IsAudioStopped() {
        if (audioSource == null) {
            return true;
        }

        if (audioSource.getStatus() == Status.Playing) {
            return false;
        }
        return true;
    }

    public static void createAudio(CDFNode cdfNodeObj, ActorNode actorNodeObj) {
        if (bCreateAudioInit == false) {
            System.setProperty("mbrola.base", "C:\\Users\\atsingh\\Projects\\mbrola");
            bCreateAudioInit = true;
        }

        String sAudioFileName = "";
        sAudioFileName += Global.nAudioFileCounter;
        cdfNodeObj.sAudioFileName = sAudioFileName;
        cdfNodeObj.sAudioFileName += ".wav";
        Global.nAudioFileCounter++;
        String VoiceName = "mbrola_us1";
        com.sun.speech.freetts.VoiceManager vm = com.sun.speech.freetts.VoiceManager.getInstance();

        Voice voice;
        voice = vm.getVoice(VoiceName);

        System.out.println("VoicePitch = " + voice.getPitch());
        System.out.println("VoicePitchShift = " + voice.getPitchShift());
        System.out.println("VoicePitchRange = " + voice.getPitchRange());
        System.out.println("Voice string = " + voice.toString());

        //voice.setPitch((float) 150.00);
        //voice.setPitchShift((float) -100.905);
        //voice.setPitchRange((float) 10.01);
        //"business", "casual", "robotic", "breathy"
        //voice.setStyle("business");
        voice.allocate();
        AudioPlayer audioPlayer = new SingleFileAudioPlayer(sAudioFileName, Type.WAVE);
        voice.setAudioPlayer(audioPlayer);

        voice.speak(cdfNodeObj.TalkString);
        voice.deallocate();
        audioPlayer.close();
    }
}
