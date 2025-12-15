package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public Entity currentLight;

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

        boolean moving = false;

        // 1. CHECK DIRECTION INPUT
        if(keyH.upPressed == true || keyH.downPressed == true || 
        keyH.leftPressed == true || keyH.rightPressed == true) {
            
            moving = true; // We are trying to move

            if(keyH.upPressed == true) direction = "up";
            if(keyH.downPressed == true) direction = "down";
            if(keyH.leftPressed == true) direction = "left";
            if(keyH.rightPressed == true) direction = "right";
        }

        // 2. CHECK COLLISION & INTERACTION (Always run this!)
        // We run this even if 'moving' is false, so we can interact while standing still.
        
        collisionOn = false;
        gp.cChecker.checkTile(this);

        int savedSpeed = speed;
        speed = 6;

        // CHECK OBJECTS (Chest, Door, Potions)
        int objIndex = gp.cChecker.checkObject(this, true);
        interactObject(objIndex); // Checks for 'E' press inside here

        speed = savedSpeed;

        // CHECK NPC
        int npcIndex = gp.cChecker.checkEntity(this, gp.npc[gp.currentMap]); 
        interactNPC(npcIndex);

        // CHECK MONSTER
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monsters[gp.currentMap]);
        interactMonster(monsterIndex);

        // 3. MOVE PLAYER (Only if moving key is held AND no collision)
        if(moving == true && collisionOn == false) {

            switch(direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }
        
        // 4. UPDATE SPRITE ANIMATION (Only if moving)
        // If we don't check 'moving', the legs will keep running while standing still!
        if(moving == true) {
            spriteCounter++;
            if(spriteCounter > 5) {
                if(spriteNum == 1) spriteNum = 2;
                else if(spriteNum == 2) spriteNum = 3;
                else if(spriteNum == 3) spriteNum = 1;
                spriteCounter = 0;
            }
        }
    }

    public void interactObject(int i) {
    if(i != 999) {
        
        // IF IT IS A CHEST (Checking Class type)
        if(gp.obj[gp.currentMap][i] instanceof obj.OBJ_Chest) {
            if(gp.keyH.ePressed == true) { 
                gp.gameState = gp.tradeState;
                gp.ui.npc = gp.obj[gp.currentMap][i]; 
                gp.playSE(2); 
            }
        }
        // NORMAL PICKUP LOGIC
        else if (! (gp.obj[gp.currentMap][i] instanceof obj.OBJ_Chest)) { 
            if(i != 999) {
                String text = "";
                if(inventory.size() != maxInventorySize) {
                    inventory.add(gp.obj[gp.currentMap][i]);
                    gp.playSE(1);
                    gp.ui.showMessage("Got a " + gp.obj[gp.currentMap][i].name + "!");
                    gp.obj[gp.currentMap][i] = null;
                } else {
                    gp.ui.showMessage("Inventory Full!");
                }
            }
        }
    }
}

    public void selectItem() {
        int itemIndex = gp.ui.getItemIndexOnSlot();

        if (itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            // 1. CONSUMABLE (Potions)
            if(selectedItem.type == type_consumable) {
                if(selectedItem.name.equals("Red Potion")) {
                     // Heal Logic
                     if(life < maxLife) {
                         life += 2; 
                         if(life > maxLife) life = maxLife;
                         inventory.remove(itemIndex);
                         gp.ui.showMessage("Drank Potion!");
                         gp.playSE(2);
                         
                         // Battle Logic: Using potion ends turn
                         if(gp.gameState == gp.battleState) {
                             gp.ui.battleSubState = 0;
                             gp.battlePhase = 1;
                             gp.ui.showMessage("Used turn to heal!");
                         }
                     } else {
                         gp.ui.showMessage("Life is full!");
                     }
                }
            }
            
            // 2. WEAPON
            // 2. WEAPON
            else if(selectedItem.type == type_sword || selectedItem.type == type_axe) {
                if(gp.gameState != gp.battleState) {
                    currentWeapon = selectedItem;
                    atk = getAttack(); // Recalculates Total Attack (Strength + Sword)
                    gp.ui.showMessage("Equipped " + selectedItem.name);
                } else {
                    gp.ui.showMessage("Can't equip in battle!");
                }
            }
            
            // 3. SHIELD
            else if(selectedItem.type == type_shield) {
                if(gp.gameState != gp.battleState) {
                    currentShield = selectedItem;
                    def = getDefense();
                    gp.ui.showMessage("Equipped " + selectedItem.name);
                } else {
                    gp.ui.showMessage("Can't equip in battle!");
                }
            }
            
            // 4. CRAFTING (Leaf + Water = Potion)
            else if(selectedItem.type == type_crafting) {
                 if(selectedItem.name.equals("Leaf")) {
                     int waterIndex = searchInventory("Water");
                     
                     if(waterIndex != -1) {
                         // Remove items (handle index shifting)
                         if(itemIndex < waterIndex) {
                             inventory.remove(waterIndex); 
                             inventory.remove(itemIndex);
                         } else {
                             inventory.remove(itemIndex);
                             inventory.remove(waterIndex);
                         }
                         
                         // Add Potion
                         inventory.add(new obj.OBJ_HealingPotion(gp));
                         gp.ui.showMessage("Crafted Potion!");
                         gp.playSE(1);
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

            gp.playSE(4);

            gp.stopMusic();

            gp.gameState = gp.transitionState;
            gp.transitionCounter = 0;

            gp.currentBattleMonsterIndex = i;
            gp.player.direction = "up";

            if(this.speed >= gp.monsters[gp.currentMap][i].speed) {
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

    public void checkLevelUp() {
        
        if(exp >= nextLevelExp) {
            
            // 1. LEVEL UP
            level++;
            
            // 2. CARRY OVER EXTRA EXP
            // (e.g. if you have 12 exp and needed 10, you keep 2)
            exp = exp - nextLevelExp; 
            
            // 3. INCREASE REQUIREMENT (Curve)
            // Next level needs 2x EXP (5 -> 10 -> 20 -> 40...)
            nextLevelExp = nextLevelExp * 2; 
            
            // 4. INCREASE STATS
            maxLife += 2;       // +1 Heart
            strength++;         // Hit harder
            dexterity++;        // Take less damage
            
            // 5. RECALCULATE ATK/DEF
            atk = getAttack();
            def = getDefense();
            
            // 6. HEAL & SOUND
            gp.playSE(1); // Play "Power Up" sound (Ensure you have a sound at index 1 or change this)
            gp.gameState = gp.dialogueState; // Optional: Pause to show message
            gp.ui.currentDialogue = "You are level " + level + " now!\n"
                                  + "You feel stronger!";
            gp.ui.drawDialogueWindow(); // Force draw text immediately if needed
            
            // Heal fully on level up (Classic RPG mechanic)
            life = maxLife;
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
