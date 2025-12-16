package entity.npc;

import entity.Entity;
import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class NPC_Princess extends Entity {

    public NPC_Princess(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 0;
        
        getImage();
        setDialogue();
    }

    public void getImage() {
        try {
            // Ensure these files exist in your res/npc folder!
            up1 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/princess/tile049.png"));
            up2 = up1;
            down1 = up1;
            down2 = up1;
            left1 = up1;
            left2 = up1;
            right1 = up1;
            right2 = up1;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setDialogue() {
        dialogues[0] = "Oh, brave hero!";
        dialogues[1] = "Thank you for saving me.";
        dialogues[2] = "However, I cannot return \nyour love since... ";
        dialogues[3] = "I still love Siegfred more than you";
    }

    public void setAction() {
        
    }
    
    @Override
    public void speak() {
        
        // If we reach the end of the conversation
        if(dialogues[dialogueIndex] == null) {
            dialogueIndex = 0; 
            gp.gameState = gp.gameWinState; // <--- SWITCH TO WIN STATE INSTEAD OF PLAY
            gp.stopMusic();
            gp.playSE(8); // Play a victory sound (ensure you have one, or remove this)
        } 
        else {
            // Normal dialogue behavior
            gp.ui.currentDialogue = dialogues[dialogueIndex];
            dialogueIndex++;
            gp.gameState = gp.dialogueState;
            gp.playSE(3);
        }
        
        // Face the player
        switch(gp.player.direction) {
            case "up": direction = "down"; break;
            case "down": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;
        }
    }
}