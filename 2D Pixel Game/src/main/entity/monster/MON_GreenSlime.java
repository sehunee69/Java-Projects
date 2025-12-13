    package entity.monster;

    import java.util.Random;

    import javax.imageio.ImageIO;

    import entity.Entity;
    import main.GamePanel;

    public class MON_GreenSlime extends Entity {

        public MON_GreenSlime(GamePanel gp) {
            
        super(gp); // <--- ADD THIS LINE HERE. It must be the first line.

        type = 2; // 2 = Monster
        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;

        // BATTLE STATS
        atk = 2;  // Deals 1 heart damage (if player def is 0)
        def = 0; 
        exp = 2; 

        // HITBOX
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        }

        public void getImage() {
            // You need to create a "monster" folder in your res and add slime images
            // For now, if you don't have images, it will just be invisible or crash. 
            // Ideally:
            // up1 = setup("/monster/greenslime_down_1"); 
            // etc...
            
            // TEMPORARY: using player images so code doesn't crash if you lack assets
            // Replace this with your slime assets later!
            try {
                // Loading same logic as player for now
                // up1 = ImageIO.read(getClass().getResourceAsStream("/monster/slime_down_1.png"));
                up1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/slime/slime-back1.png"));
                up2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/slime/slime-back2.png"));
                down1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/slime/slime-front1.png"));
                down2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/slime/slime-front2.png"));
                left1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/slime/slime-left1.png"));
                left2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/slime/slime-left2.png"));
                right1 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/slime/slime-right1.png"));
                right2 = ImageIO.read(getClass().getResourceAsStream("/entity/monster/slime/slime-right2.png"));

            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void setAction() {

            actionLockCounter++;
            
            // Change direction every 2 seconds (120 frames)
            if(actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1; // pick 1 to 100

                if(i <= 25) {
                    direction = "up";
                }
                if(i > 25 && i <= 50) {
                    direction = "down";
                }
                if(i > 50 && i <= 75) {
                    direction = "left";
                }
                if(i > 75 && i <= 100) {
                    direction = "right";
                }

                actionLockCounter = 0;
            }
        }
        
        // We override update to include collision check for the monster
        public void update() {
            setAction();
            
            collisionOn = false;
            gp.cChecker.checkTile(this);
            // gp.cChecker.checkPlayer(this); // We will add this later for combat triggering

            if(collisionOn == false) {
                switch(direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            // Simple sprite animation logic
            spriteCounter++;
            if(spriteCounter > 12) {
                if(spriteNum == 1) spriteNum = 2;
                else if(spriteNum == 2) spriteNum = 1;
                spriteCounter = 0;
            }
        }
    }