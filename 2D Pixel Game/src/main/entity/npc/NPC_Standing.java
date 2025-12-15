package entity.npc;

import javax.imageio.ImageIO;
import main.GamePanel;
import entity.Entity;

public class NPC_Standing extends Entity {

    public NPC_Standing(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 0; // ZERO SPEED = No movement
        
        getImage();
        setDialogue();
    }

    public void getImage() {
        try {
            // Load the ONE image you want for this NPC
            // We set every direction to this same image so they never appear to turn
            
            
            up1 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/standing/tile055.png")); 
            up2 = up1;
            up3 = up1;
            
            down1 = up1;
            down2 = up1;
            down3 = up1;
            
            left1 = up1;
            left2 = up1;
            left3 = up1;
            
            right1 = up1;
            right2 = up1;
            right3 = up1;

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setDialogue() {
        dialogues[0] = "I am a stationary guard.";
        dialogues[1] = "I do not move from this spot.";
        dialogues[2] = "Press Enter to see the next line...";
        dialogues[3] = "And now the conversation ends.";
    }

    public void setAction() {
        // LEAVE THIS EMPTY
        // Because it is empty, the NPC will never try to move.
    }
}