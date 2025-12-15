package entity.monster;

import entity.Entity;
import main.GamePanel;
import java.util.Random;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MON_GreatSoldier extends Entity {

    GamePanel gp;

    public MON_GreatSoldier(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Skeleton";
        speed = 2; // Faster than slime (1) but slower than player (3-4)
        maxLife = 10;
        life = maxLife;
        
        // --- STATS ---
        atk = 6;  
        def = 1;  
        exp = 5;  
        // -------------

        // HITBOX (Adjust values if your sprite is smaller/larger)
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        try {
            // Ensure these files exist in res/monster/
            // Using skeleton_down_1.png for all directions if you don't have 4-way sprites yet
            up1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile093.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile094.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile095.png")); // Frame 3

            down1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile057.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile058.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile059.png")); // Frame 3

            left1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile069.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile070.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile071.png")); // Frame 3

            right1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile081.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile082.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/greatSoldier/tile083.png")); // Frame 3
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setAction() {
        actionLockCounter++;
        
        if(actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100)+1;

            if(i <= 25) direction = "up";
            if(i > 25 && i <= 50) direction = "down";
            if(i > 50 && i <= 75) direction = "left";
            if(i > 75 && i <= 100) direction = "right";

            actionLockCounter = 0;
        }
    }
}