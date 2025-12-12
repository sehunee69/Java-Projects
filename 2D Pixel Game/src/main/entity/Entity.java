package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Entity {

    GamePanel gp; // (Optional: useful if Entity needs access to game settings)

    // COORDINATES AND SPEED
    public int worldX, worldY;
    public int speed;

    // IMAGES
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction = "down";

    // ANIMATION COUNTERS
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // COLLISION
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    // THESE WERE MISSING AND CAUSING ERRORS IN MONSTER CLASS:
    public int solidAreaDefaultX, solidAreaDefaultY; 
    public boolean collisionOn = false;
    
    // AI / COUNTERS
    public int actionLockCounter = 0;

    // TYPE (0 = player, 1 = npc, 2 = monster)
    // THIS WAS THE MISSING VARIABLE CAUSING YOUR ERROR
    public int type; 

    // METHODS
    public void setAction() {}
    
    public void update() {
        setAction();
        collisionOn = false;
        // Logic will be handled by specific classes or overridden
    }

    // Move the draw method here so Monsters can use it too!
    public void draw(Graphics2D g2, GamePanel gp) {
        
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
           worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
           worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
           worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            
            switch(direction) {
                case "up":
                    if(spriteNum == 1) image = up1;
                    if(spriteNum == 2) image = up2;
                    break;
                case "down":
                    if(spriteNum == 1) image = down1;
                    if(spriteNum == 2) image = down2;
                    break;
                case "left":
                    if(spriteNum == 1) image = left1;
                    if(spriteNum == 2) image = left2;
                    break;
                case "right":
                    if(spriteNum == 1) image = right1;
                    if(spriteNum == 2) image = right2;
                    break;
            }
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}