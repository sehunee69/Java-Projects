package entity.npc; // or package entity.npc;

import java.util.Random;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import main.GamePanel;
import entity.Entity;

public class NPC_OldMan extends Entity {
    
    // Boundaries for the restricted area
    private int minX, minY, maxX, maxY;

    public NPC_OldMan(GamePanel gp, int x, int y) {
        super(gp);

        direction = "down";
        speed = 1;
        
        // Define the area (5 tiles around spawn point)
        int range = 2 * gp.tileSize;
        this.minX = x - range;
        this.minY = y - range;
        this.maxX = x + range;
        this.maxY = y + range;

        getImage();
        setDialogue();
    }

    public void getImage() {
        // Load images (Using player images temporarily)
        // Replace with your NPC sprites!
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile045.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile046.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile047.png")); // Added frame 3 for consistency
            
            down1 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile009.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile010.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile011.png")); // Added frame 3

            left1 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile021.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile022.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile023.png")); // Added frame 3

            right1 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile033.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile034.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/entity/npc/npc_res/oldman/tile035.png")); // Added frame 3
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setDialogue() {
        dialogues[0] = "Hello, lad.";
        dialogues[1] = "I am too old to adventure.";
        dialogues[2] = "I just walk inside this small area.";
        dialogues[3] = "Good luck on your journey!";
    }

    public void setAction() {
        
        actionLockCounter++;
        
        if(actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; // 1 to 100

            // INTELLIGENT MOVEMENT (Area Restriction)
            // If the NPC is too far Left, force them Right, etc.
            
            if(i <= 25) { 
                direction = "up"; 
                if(worldY < minY) direction = "down"; // Turn back
            }
            if(i > 25 && i <= 50) { 
                direction = "down"; 
                if(worldY > maxY) direction = "up"; // Turn back
            }
            if(i > 50 && i <= 75) { 
                direction = "left"; 
                if(worldX < minX) direction = "right"; // Turn back
            }
            if(i > 75 && i <= 100) { 
                direction = "right"; 
                if(worldX > maxX) direction = "left"; // Turn back
            }

            actionLockCounter = 0;
        }
    }
}