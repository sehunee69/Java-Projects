package entity.monster;

import java.util.Random;
import entity.Entity;
import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MON_Soldier extends Entity {

    public MON_Soldier(GamePanel gp) {
        super(gp);

        type = type_monster;
        name = "Soldier";
        speed = 3;
        maxLife = 13;
        life = maxLife;

        level = 3;
        atk = 6;  // Stronger than slime (which was 2)
        def = 2;  // Harder to hit
        exp = 20; // Gives more XP
        
        // Hitbox settings
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        maxSpriteNum = 3;

        getImage();
    }

    public void getImage() {
        try {
            // LOAD ALL 3 FRAMES PER DIRECTION
            up1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile093.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile094.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile095.png")); // Frame 3

            down1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile057.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile058.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile059.png")); // Frame 3

            left1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile069.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile070.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile071.png")); // Frame 3

            right1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile081.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile082.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/soldier/tile083.png")); // Frame 3

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setAction() {
        // Simple AI: Move random direction every 2 seconds
        actionLockCounter++;
        
        if(actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; // pick 1 to 100

            if(i <= 25) direction = "up";
            if(i > 25 && i <= 50) direction = "down";
            if(i > 50 && i <= 75) direction = "left";
            if(i > 75 && i <= 100) direction = "right";

            actionLockCounter = 0;
        }
    }
}