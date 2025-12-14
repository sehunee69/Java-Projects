package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import obj.OBJ_HealingPotion;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    // The Bag
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 18; // limit slots

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp); // Calls Entity constructor
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        // PLAYER STATS
        level = 1;
        maxLife = 6;
        life = maxLife;
        strength = 1; // More str = more dmg
        dexterity = 1; // More dex = less dmg taken
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        
        // TODO: Create a sword class and equip it here later
        // currentWeapon = new OBJ_Sword_Normal(gp);
        // currentShield = new OBJ_Shield_Wood(gp);
        
        atk = getAttack();
        def = getDefense();

    }

    public int getAttack() {
        int attackArea = 0; // Base punch damage = 0? Or 1?
        if(currentWeapon != null) {
            attackArea = currentWeapon.attackValue;
        }
        return strength + attackArea;
    }

    public int getDefense() {
        int defenseArea = 0;
        if(currentShield != null) {
            defenseArea = currentShield.defenseValue;
        }
        return dexterity + defenseArea;
    }

    public void getPlayerImage() {

        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile036.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile037.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile038.png"));

            down1 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile000.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile001.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile002.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile012.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile013.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile014.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile024.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile025.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/res/player/tile026.png"));

        } catch(IOException e) {

            e.printStackTrace();
        }
    }

    public void update() {

        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {

            if(keyH.upPressed == true) {
            direction = "up";
        } else if(keyH.downPressed == true) {
            direction = "down";
        } else if(keyH.leftPressed == true) {
            direction = "left";
        } else if(keyH.rightPressed == true) {
            direction = "right";
        } 

        //CHECK TILE COLLISION
        collisionOn = false;
        gp.cChecker.checkTile(this);

        // CHECK MONSTER COLLISION (NEW CODE)
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monsters);
        interactMonster(monsterIndex);

        // CHECK OBJECT COLLISION
        int objIndex = gp.cChecker.checkObject(this, true); // We need to create this method next
        pickUpObject(objIndex);

        // CHECK NPC COLLISION
        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        interactNPC(npcIndex);

        //IF FALSE, PLAYER CAN MOVE
        if(collisionOn == false) {

            switch(direction) {

                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
                
            }
        }

        spriteCounter++;
        if(spriteCounter > 5) {

            if(spriteNum == 1) {
                spriteNum = 2;
            }
            else if(spriteNum == 2) {
                spriteNum = 3;
            }
            else if(spriteNum == 3) {
                spriteNum = 1;
            }

            spriteCounter = 0;
        }
        }
    }

    public void pickUpObject(int i) {
        if(i != 999) {
            String text = "";
            if(inventory.size() != maxInventorySize) {
                inventory.add(gp.obj[i]);
                gp.playSE(1);
                gp.ui.showMessage("Got a " + gp.obj[i].name + "!");
                gp.obj[i] = null;
            } else {
                gp.ui.showMessage("Inventory Full!");
            }
        }
    }

    public void selectItem() {
        int itemIndex = gp.ui.getItemIndexOnSlot();

        if (itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            // 1. CONSUMABLE
            if(selectedItem.type == type_consumable) {
                if(selectedItem.name.equals("Red Potion")) {
                     life += 2; 
                     if(life > maxLife) life = maxLife;
                     inventory.remove(itemIndex);
                     gp.ui.showMessage("Drank Potion!");
                }
            }
            // 2. WEAPON
            else if(selectedItem.type == type_sword || selectedItem.type == type_axe) {
                currentWeapon = selectedItem;
                atk = getAttack();
                gp.ui.showMessage("Equipped " + selectedItem.name);
            }
            // 3. SHIELD
            else if(selectedItem.type == type_shield) {
                currentShield = selectedItem;
                def = getDefense();
                gp.ui.showMessage("Equipped " + selectedItem.name);
            }
            // 4. CRAFTING
            else if(selectedItem.type == type_crafting) {
                 if(selectedItem.name.equals("Leaf")) {
                     int waterIndex = searchInventory("Water");
                     if(waterIndex != -1) {
                         if(itemIndex < waterIndex) {
                             inventory.remove(waterIndex); 
                             inventory.remove(itemIndex);
                         } else {
                             inventory.remove(itemIndex);
                             inventory.remove(waterIndex);
                         }
                         inventory.add(new obj.OBJ_HealingPotion(gp));
                         gp.ui.showMessage("Crafted Potion!");
                     } else {
                         gp.ui.showMessage("Need Water!");
                     }
                 }
            }
        }
    }

    public int searchInventory(String itemName) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).name.equals(itemName)) {
                return i;
            }
        }
        return -1;
    }

    public void interactMonster(int i) {
        if(i != 999) {
            gp.gameState = gp.transitionState;
            gp.transitionCounter = 0;

            gp.currentBattleMonsterIndex = i;
            gp.player.direction = "up";

            if(this.speed >= gp.monsters[i].speed) {
                gp.battlePhase = 0; // Player goes first
                gp.ui.showMessage("You are faster! Your turn.");
            } else {
                gp.battlePhase = 1; // Enemy goes first
                gp.ui.showMessage("Enemy is faster! Enemy turn.");
            }
        }
    }

    public void interactNPC(int i) {
    if(i != 999) {
        // If index is not 999, we are touching an NPC.
        // If we are touching them, we don't want to walk through.
        
        // However, usually checkEntity sets 'collisionOn' to true automatically inside CollisionChecker.
        // So just by calling checkEntity above, physical collision should work!
        
        // This method is mostly for if you want to trigger dialogue by bumping into them
        // (Optional: bumping triggers dialogue)
        if(gp.keyH.enterPressed == true) {
            // gp.gameState = gp.dialogueState;
            // gp.npc[i].speak();
        }
    }
}

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        // Create temporary variables for drawing position
        int x = screenX; // Default to center
        int y = screenY; // Default to center

        // --- ADJUST PLAYER POSITION AT EDGES ---
        
        // 1. LEFT & TOP
        // If we are at the edge, use our actual world coordinate instead of 'center'
        if(worldX < screenX) {
            x = worldX;
        }
        if(worldY < screenY) {
            y = worldY;
        }

        // 2. RIGHT & BOTTOM
        int rightOffset = gp.screenWidth - screenX;
        int bottomOffset = gp.screenHeight - screenY;

        if(worldX > gp.worldWidth - rightOffset) {
            x = gp.screenWidth - (gp.worldWidth - worldX);
        }
        if(worldY > gp.worldHeight - bottomOffset) {
            y = gp.screenHeight - (gp.worldHeight - worldY);
        }

        switch(direction) {
        case "up":
            if(spriteNum == 1) image = up1;
            if(spriteNum == 2) image = up2;
            if(spriteNum == 3) image = up3; // ADD THIS
            break;
        case "down":
            if(spriteNum == 1) image = down1;
            if(spriteNum == 2) image = down2;
            if(spriteNum == 3) image = down3; // ADD THIS
            break;
        case "left":
            if(spriteNum == 1) image = left1;
            if(spriteNum == 2) image = left2;
            if(spriteNum == 3) image = left3; // ADD THIS
            break;
        case "right":
            if(spriteNum == 1) image = right1;
            if(spriteNum == 2) image = right2;
            if(spriteNum == 3) image = right3; // ADD THIS
            break;
    }

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}
