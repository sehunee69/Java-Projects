package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Entity {

    public GamePanel gp; // (Optional: useful if Entity needs access to game settings)

    // COORDINATES AND SPEED
    public int worldX, worldY;
    public int speed;

    // IMAGES
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
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
    public int maxSpriteNum = 2;

    // CHARACTER STATUS
    public String name;
    public int maxLife;
    public int life;
    public int level;
    public int strength;
    public int dexterity;
    public int atk;
    public int def;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;

    // ITEM/OBJECTS ATTRIBUTES
    public String description = "";
    public int attackValue;
    public int defenseValue;

    // ITEM TYPES
    public int type; 
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;
    public final int type_crafting = 8;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    // METHODS
    public void setAction() {}
    
    public void update() {
        setAction();
        collisionOn = false;

        gp.cChecker.checkTile(this);
        
        if(collisionOn == false) {
            switch(direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        spriteCounter++;
        if(spriteCounter > 12) {

            // If we are at the max frame, reset to 1. Otherwise, increase by 1.
            if(spriteNum == maxSpriteNum) {
                spriteNum = 1;
            } else {
                spriteNum++;
            }

            spriteCounter = 0;
        }
    }
    

    // Move the draw method here so Monsters can use it too!
    public void draw(Graphics2D g2, GamePanel gp) {
        
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(gp.player.worldX < gp.player.screenX) screenX = worldX;
        if(gp.player.worldY < gp.player.screenY) screenY = worldY;
        
        int rightOffset = gp.screenWidth - gp.player.screenX;
        int bottomOffset = gp.screenHeight - gp.player.screenY;
        
        if(gp.player.worldX > gp.worldWidth - rightOffset) {
            screenX = gp.screenWidth - (gp.worldWidth - worldX);
        }
        if(gp.player.worldY > gp.worldHeight - bottomOffset) {
            screenY = gp.screenHeight - (gp.worldHeight - worldY);
        }

        if(screenX + gp.tileSize > 0 && screenX < gp.screenWidth && 
            screenY + gp.tileSize > 0 && screenY < gp.screenHeight) {
            
            switch(direction) {
            case "up":
                if(spriteNum == 1) image = up1;
                if(spriteNum == 2) image = up2;
                if(spriteNum == 3) image = up3; // Add this
                break;
            case "down":
                if(spriteNum == 1) image = down1;
                if(spriteNum == 2) image = down2;
                if(spriteNum == 3) image = down3; // Add this
                break;
            case "left":
                if(spriteNum == 1) image = left1;
                if(spriteNum == 2) image = left2;
                if(spriteNum == 3) image = left3; // Add this
                break;
            case "right":
                if(spriteNum == 1) image = right1;
                if(spriteNum == 2) image = right2;
                if(spriteNum == 3) image = right3; // Add this
                break;
        }
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}