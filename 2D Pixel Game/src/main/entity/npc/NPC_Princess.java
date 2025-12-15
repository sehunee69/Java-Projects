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
        dialogues[1] = "Please be careful...";
        dialogues[2] = "The dungeon in the other world is\ncrawling with Skeletons.";
        dialogues[3] = "They are much stronger than the Slimes!";
    }

    public void setAction() {
        
    }
    
    public void speak() {
        super.speak();
    }
}