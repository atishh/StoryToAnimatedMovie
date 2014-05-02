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
    public static int nChildrenVoiceCounter = 0;
    public static int nMaleVoiceCounter = 0;
    public static int nFemaleVoiceCounter = 0;

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

        if (actorNodeObj.bVoiceDataSet == false) {
            if (actorNodeObj.TotalActorNodeInThisNode[0].attribute.toLowerCase().contains("children")) {
                actorNodeObj.sVoiceName = "mbrola_us1";
                actorNodeObj.pitchShift = getNewChildrenPitchShift();
            } else if (actorNodeObj.bIsMale) {
                actorNodeObj.sVoiceName = "mbrola_us2";
                actorNodeObj.pitchShift = getNewMalePitchShift();
            } else {
                //If nothing then female voice;
                actorNodeObj.sVoiceName = "mbrola_us1";
                actorNodeObj.pitchShift = getNewFemalePitchShift();
            }
            actorNodeObj.bVoiceDataSet = true;
        }

        String sAudioFileName = "";
        sAudioFileName += Global.nAudioFileCounter;
        cdfNodeObj.sAudioFileName = sAudioFileName;
        cdfNodeObj.sAudioFileName += ".wav";
        Global.nAudioFileCounter++;
        //String VoiceName = "mbrola_us1";
        String VoiceName = actorNodeObj.sVoiceName;
        com.sun.speech.freetts.VoiceManager vm = com.sun.speech.freetts.VoiceManager.getInstance();

        Voice voice;
        voice = vm.getVoice(VoiceName);

        System.out.println("VoicePitch = " + voice.getPitch());
        System.out.println("VoicePitchShift = " + voice.getPitchShift());
        System.out.println("VoicePitchRange = " + voice.getPitchRange());
        System.out.println("Voice string = " + voice.toString());

        if (actorNodeObj.pitchShift < 100) {
            voice.setPitch(voice.getPitch() + actorNodeObj.pitchShift);
        } else {
            voice.setPitchShift(voice.getPitchShift() + actorNodeObj.pitchShift);
        }
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

    public static float getNewFemalePitchShift() {
        float nNewPitchShift = 0;
        if (nFemaleVoiceCounter == 0) {
            nNewPitchShift = 0;
        } else if (nFemaleVoiceCounter == 1) {
            nNewPitchShift = 50;
        } else if (nFemaleVoiceCounter == 2) {
            nNewPitchShift = -50;
        } else if (nFemaleVoiceCounter == 3) {
            nNewPitchShift = 70;
        } else if (nFemaleVoiceCounter == 4) {
            nNewPitchShift = -70;
        }
        nFemaleVoiceCounter++;
        return nNewPitchShift;
    }

    public static float getNewMalePitchShift() {
        float nNewPitchShift = 0;
        if (nMaleVoiceCounter == 0) {
            nNewPitchShift = 0;
        } else if (nMaleVoiceCounter == 1) {
            nNewPitchShift = 50;
        } else if (nMaleVoiceCounter == 2) {
            nNewPitchShift = -50;
        } else if (nMaleVoiceCounter == 3) {
            nNewPitchShift = 70;
        } else if (nMaleVoiceCounter == 4) {
            nNewPitchShift = -70;
        }
        nMaleVoiceCounter++;
        return nNewPitchShift;
    }

    public static float getNewChildrenPitchShift() {
        float nNewPitchShift = 50;
        if (nChildrenVoiceCounter == 0) {
            nNewPitchShift += 0;
        } else if (nChildrenVoiceCounter == 1) {
            nNewPitchShift += 10;
        } else if (nChildrenVoiceCounter == 2) {
            nNewPitchShift += -10;
        } else if (nChildrenVoiceCounter == 3) {
            nNewPitchShift += 20;
        } else if (nChildrenVoiceCounter == 4) {
            nNewPitchShift += -20;
        }
        nChildrenVoiceCounter++;
        return nNewPitchShift;
    }
}
