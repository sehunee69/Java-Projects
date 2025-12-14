package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

    Clip clip;
    URL soundURL[] = new URL[30];

    public Sound() {
        // Load your sound files into the array
        // 0 = Background Music
        soundURL[0] = getClass().getResource("/res/sounds/Pixel 2.wav"); 
        soundURL[1] = getClass().getResource("/res/sounds/pick-upSFX.wav");
        
        // You can add sound effects later:
        // soundURL[1] = getClass().getResourceAsStream("/sound/coin.wav");
        // soundURL[2] = getClass().getResourceAsStream("/sound/powerup.wav");
        // soundURL[3] = getClass().getResourceAsStream("/sound/unlock.wav");
        // soundURL[4] = getClass().getResourceAsStream("/sound/fanfare.wav");
    }
    
    // Note: If getResourceAsStream doesn't work for Audio, try getResource:
    // soundURL[0] = getClass().getResource("/sound/BlueBoyAdventure.wav");
    // (Usually getClass().getResource() is safer for Sound URL arrays)

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

            // Make the music more quieter
            if(i == 0) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f); 
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}