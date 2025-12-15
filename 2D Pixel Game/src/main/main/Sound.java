package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

    Clip[] clips;
    URL[] soundURL = new URL[30];

    public Sound() {
        
        // 1. SETUP FILE PATHS
        soundURL[0] = getClass().getResource("/res/sounds/Pixel 2.wav"); 
        soundURL[1] = getClass().getResource("/res/sounds/pick-upSFX.wav");
        soundURL[2] = getClass().getResource("/res/sounds/open-bag-sound-39216.wav");
        soundURL[3] = getClass().getResource("/res/sounds/8-bit-game-sfx-sound-26-269962.wav");
        soundURL[4] = getClass().getResource("/res/sounds/enemy-encounter-undertale.wav");
        soundURL[5] = getClass().getResource("/res/sounds/Pixel 5.wav");
        soundURL[6] = getClass().getResource("/res/sounds/attack-slash.wav");

        // 2. INITIALIZE CLIPS ARRAY
        clips = new Clip[soundURL.length];
        
        // 3. PRE-LOAD ALL SOUNDS INTO MEMORY NOW
        for(int i = 0; i < soundURL.length; i++) {
            if(soundURL[i] != null) {
                loadSound(i);
            }
        }
    }
    
    public void loadSound(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clips[i] = AudioSystem.getClip();
            clips[i].open(ais);
            
            // ADJUST VOLUME FOR MUSIC (Index 0)
            if(i == 0 || i == 5) {
                FloatControl gainControl = (FloatControl) clips[i].getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);
            }
            if(i == 6) { 
                FloatControl gainControl = (FloatControl) clips[i].getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-15.0f);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void play(int i) {
        if(clips[i] == null) return;
        
        // Stop it if it's already playing (allows rapid fire)
        if(clips[i].isRunning()) {
            clips[i].stop();
        }
        
        // Rewind and Play
        clips[i].setFramePosition(0);
        clips[i].start();
    }

    public void loop(int i) {
        if(clips[i] == null) return;
        clips[i].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(int i) {
        if(clips[i] == null) return;
        clips[i].stop();
    }
    
    // Helper to stop ALL sounds (useful for battle transitions)
    public void stopAll() {
        for(int i = 0; i < clips.length; i++) {
            if(clips[i] != null) {
                clips[i].stop();
            }
        }
    }
}