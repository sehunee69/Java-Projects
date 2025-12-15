package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import entity.Entity;
import java.awt.image.BufferedImage;
import java.io.IOException;            
import javax.imageio.ImageIO;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40;
    Font arial_80;

    Color windowColor;
    Color borderColor;
    BasicStroke borderStroke;
    Font dialogueFont;

    BufferedImage battleBackground;
    BufferedImage slashEffect;
    public boolean showingSlash = false;
    int slashCounter = 0;

    public Entity npc;
    
    // VARIABLES
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public int commandNum = 0;
    
    // INVENTORY
    public int slotCol = 0;
    public int slotRow = 0;
    
    // SUBSTATE: 0 = Menu Select, 1 = Status, 2 = Bag
    public int subState = 0; 
    public int battleSubState = 0;

    // DIALOGUE
    public String currentDialogue = "";

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);

        dialogueFont = new Font("Arial", Font.PLAIN, 28);
        windowColor = new Color(0, 0, 0, 210); // Transparent Black
        borderColor = new Color(255, 255, 255); // White
        borderStroke = new BasicStroke(5);

        try {
            battleBackground = ImageIO.read(getClass().getResourceAsStream("/battleScenes/grass-battleScene.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            battleBackground = ImageIO.read(getClass().getResourceAsStream("/battleScenes/grass-battleScene.png"));
            
            // OPTIONAL: Load a real slash image if you have one
            // slashEffect = ImageIO.read(getClass().getResourceAsStream("/res/effects/slash.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        // PLAY STATE
        if(gp.gameState == gp.playState) {
            drawPlayerLife();
        }
        // CHARACTER STATE (Menu)
        if(gp.gameState == gp.characterState) {
            drawCharacterScreen();
        }
        // BATTLE STATE
        if(gp.gameState == gp.battleState) {
            drawPlayerLife();
            drawBattleScreen();
        }
        //TRANSITION STATE
        if(gp.gameState == gp.transitionState) {
            drawTransition();
        }

        if(gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }

        // MESSAGE OVERLAY
        if(messageOn == true) {
            g2.setFont(g2.getFont().deriveFont(30F));
            g2.drawString(message, gp.tileSize/2, gp.tileSize*5);
            messageCounter++;
            if(messageCounter > 120) {
                messageCounter = 0;
                messageOn = false;
            }
        }

        if(gp.gameState == gp.dialogueState) {
            drawDialogueWindow();
        }

        if(gp.gameState == gp.tradeState) {
            drawTradeScreen();
        }
    }

    public void drawCharacterScreen() {
        
        // CHECK SUBSTATE
        if(subState == 0) {
            drawMenuWindow();
        }
        else if(subState == 1) {
            drawStatusWindow();
        }
        else if(subState == 2) {
            drawInventoryWindow();
        }
    }

    public void drawMenuWindow() {
        // --- POKEMON STYLE RIGHT-SIDE MENU ---
        
        // Dimensions
        int frameWidth = gp.tileSize * 3;
        int frameHeight = gp.tileSize * 4;
        int frameX = gp.screenWidth - frameWidth - gp.tileSize; // 1 tile from right edge
        int frameY = gp.tileSize; // 1 tile from top
        
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        // Text Settings
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(28F));
        
        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        int lineHeight = 40;
        
        // OPTIONS
        g2.drawString("Status", textX, textY);
        if(commandNum == 0) g2.drawString(">", textX - 15, textY);
        
        textY += lineHeight;
        g2.drawString("Bag", textX, textY);
        if(commandNum == 1) g2.drawString(">", textX - 15, textY);
        
        textY += lineHeight;
        g2.drawString("Save", textX, textY);
        if(commandNum == 2) g2.drawString(">", textX - 15, textY);
        
        textY += lineHeight;
        g2.drawString("Close", textX, textY);
        if(commandNum == 3) g2.drawString(">", textX - 15, textY);
    }

    public void drawStatusWindow() {
        
        // Center the Status Window
        int frameWidth = gp.tileSize * 10;
        int frameHeight = gp.tileSize * 10;
        int frameX = (gp.screenWidth - frameWidth) / 2;
        int frameY = gp.tileSize;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(30F));
        
        int textX = frameX + 30;
        int textY = frameY + gp.tileSize;
        int lineHeight = 40;

        g2.drawString("Level", textX, textY);
        g2.drawString(String.valueOf(gp.player.level), textX + 150, textY);
        textY += lineHeight;

        g2.drawString("Life", textX, textY);
        g2.drawString(gp.player.life + "/" + gp.player.maxLife, textX + 150, textY);
        textY += lineHeight;
        
        g2.drawString("Strength", textX, textY);
        g2.drawString(String.valueOf(gp.player.strength), textX + 150, textY);
        textY += lineHeight;
        
        g2.drawString("Dexterity", textX, textY);
        g2.drawString(String.valueOf(gp.player.dexterity), textX + 150, textY);
        textY += lineHeight;
        
        g2.drawString("Attack", textX, textY);
        g2.drawString(String.valueOf(gp.player.atk), textX + 150, textY);
        textY += lineHeight;
        
        g2.drawString("Defense", textX, textY);
        g2.drawString(String.valueOf(gp.player.def), textX + 150, textY);
        textY += lineHeight;
        
        g2.drawString("Exp", textX, textY);
        g2.drawString(String.valueOf(gp.player.exp), textX + 150, textY);
        textY += lineHeight;
        
        g2.drawString("Coin", textX, textY);
        g2.drawString(String.valueOf(gp.player.coin), textX + 150, textY);
        textY += lineHeight + 20;
        
        g2.drawString("Weapon", textX, textY);
        if(gp.player.currentWeapon != null) {
            g2.drawString(gp.player.currentWeapon.name, textX + 150, textY);
        } else {
             g2.drawString("None", textX + 150, textY);
        }
        textY += lineHeight;
        
        g2.drawString("Shield", textX, textY);
        if(gp.player.currentShield != null) {
            g2.drawString(gp.player.currentShield.name, textX + 150, textY);
        } else {
             g2.drawString("None", textX + 150, textY);
        }

        // Back Hint
        g2.setFont(g2.getFont().deriveFont(20F));
        g2.drawString("[Press ESC to Return]", frameX + 20, frameY + frameHeight - 20);
    }

    public void drawInventoryWindow() {
        
        // 1. CALCULATE WINDOW SIZE (9x2 Grid)
        // 9 items wide needs about 10-11 tiles of width
        // 2 items high needs about 3-4 tiles of height
        int frameWidth = gp.tileSize * 11; 
        int frameHeight = gp.tileSize * 4; 
        int frameX = (gp.screenWidth - frameWidth) / 2; // Center X
        int frameY = gp.tileSize; // Top Y

        // 2. DRAW THE WINDOW BACKGROUND
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // 3. SLOT ALIGNMENT
        // spacing between slots
        int slotSize = gp.tileSize + 3; 
        
        // Math to center the grid inside the window:
        // Window Width - (9 slots * size) / 2 = Left Margin
        int totalSlotWidth = 9 * slotSize;
        int startXpadding = (frameWidth - totalSlotWidth) / 2;
        
        final int slotXstart = frameX + startXpadding;
        final int slotYstart = frameY + gp.tileSize; // Padding from top
        
        int itemSize = (int)(gp.tileSize * 0.9);
        int offset = (gp.tileSize - itemSize) / 2; // Math to center it

        int slotX = slotXstart;
        int slotY = slotYstart;

        // 4. DRAW SLOTS & ITEMS
        for(int i = 0; i < gp.player.maxInventorySize; i++) {
            
            // A. DRAW EMPTY SLOT OUTLINE
            g2.setColor(new Color(255, 255, 255, 120)); 
            g2.setStroke(new BasicStroke(1)); 
            g2.drawRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);

            // B. HIGHLIGHT CURSOR
            if(i == getItemIndexOnSlot()) {
                 g2.setColor(new Color(240,190,90));
                 g2.setStroke(new BasicStroke(3)); 
                 g2.drawRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }
            
            // C. DRAW ITEM
            if(i < gp.player.inventory.size()) {
                if(gp.player.inventory.get(i) != null) {
                    if(gp.player.inventory.get(i).down1 != null) {
                        g2.drawImage(gp.player.inventory.get(i).down1, slotX + offset, slotY + offset, itemSize, itemSize, null);
                    } else {
                        g2.setColor(Color.white);
                        g2.setFont(g2.getFont().deriveFont(20F));
                        g2.drawString("?", slotX + 12, slotY + 30);
                    }
                }
            }
            
            // D. MOVE TO NEXT SLOT
            slotX += slotSize;
            
            // E. GRID BREAK (After 9 items, reset X and go down Y)
            if(i % 9 == 8) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }
        
        // 5. DESCRIPTION BOX
        int itemIndex = getItemIndexOnSlot();
        if(itemIndex < gp.player.inventory.size()) {
            
            int dFrameY = frameY + frameHeight + 10;
            int dFrameHeight = gp.tileSize * 3;
            
            drawSubWindow(frameX, dFrameY, frameWidth, dFrameHeight);
            
            int textX = frameX + 20;
            int textY = dFrameY + 40;
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(20F));
            
            Entity item = gp.player.inventory.get(itemIndex);
            
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
            g2.drawString(item.name, textX, textY);
            textY += 30;
            
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
            for(String line : item.description.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += 25;
            }
        }
    }
    
    // Kept for structure
    public void drawInventory(Graphics2D g2) { } 

    public int getItemIndexOnSlot() {
        int itemIndex = slotCol + (slotRow * 9);
        return itemIndex;
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        // Black Background
        g2.setColor(windowColor);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        // White Border
        g2.setColor(borderColor);
        g2.setStroke(borderStroke);
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }
    
    public void drawPlayerLife() {
        // Keep your existing drawPlayerLife code here
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int width = gp.tileSize * 5; 
        int height = 20; 

        g2.setColor(new Color(35, 35, 35));
        g2.fillRect(x, y, width, height);

        g2.setColor(new Color(255, 0, 30));
        int currentBarWidth = (int)(((double)gp.player.life / (double)gp.player.maxLife) * width);
        g2.fillRect(x, y, currentBarWidth, height);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, width, height);
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
        g2.drawString("HP: " + gp.player.life + "/" + gp.player.maxLife, x, y - 5);
    }

    public void drawBattleScreen() {
        
        // 1. DRAW BACKGROUND
        if (battleBackground != null) {
            g2.drawImage(battleBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            g2.setColor(new Color(0,0,0));
            g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
        }

        // 2. DRAW MONSTER IMAGE
        int monsterIndex = gp.currentBattleMonsterIndex;
        if(monsterIndex != 999 && gp.monsters[gp.currentMap][monsterIndex] != null) {
            
            Entity monster = gp.monsters[gp.currentMap][monsterIndex];

            int offsetX = 0;
            int offsetY = 0;

            if(monster.shakeCounter > 0) {
                // Generate random shake between -10 and 10 pixels
                offsetX = (int)(Math.random() * 20) - 10;
                offsetY = (int)(Math.random() * 20) - 10;
                
                // Reduce the counter so it eventually stops shaking
                monster.shakeCounter--; 
            }

            int monsterX = (gp.tileSize * 2) + offsetX; 
            int monsterY = (gp.tileSize * 1) + offsetY;
            
            
            g2.drawImage(monster.down2, monsterX, monsterY, gp.tileSize*4, gp.tileSize*4, null);

            if(showingSlash == true) {
                
                // If you have an image:
                // g2.drawImage(slashEffect, monsterX, monsterY, gp.tileSize*4, gp.tileSize*4, null);
                
                // If you DON'T have an image (Draw a Red Scratch):
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(4f));
                g2.drawLine(monsterX, monsterY, monsterX + 150, monsterY + 150); // Slash 1
                g2.drawLine(monsterX + 150, monsterY, monsterX, monsterY + 150); // Slash 2 (X shape)
                
                // Animation Timer
                slashCounter++;
                if(slashCounter > 20) { // Animation lasts 20 frames (0.3 seconds)
                    slashCounter = 0;
                    showingSlash = false; // Stop drawing
                }
            }
            
            // --- ENEMY HUD (Top Right) ---
            int hudX = gp.screenWidth - (gp.tileSize * 9);
            int hudY = gp.tileSize;
            int hudWidth = gp.tileSize * 8;
            int hudHeight = gp.tileSize * 2;
            
            // Background
            drawSubWindow(hudX, hudY, hudWidth, hudHeight);
            
            // Name & Level
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));
            g2.drawString(monster.name + " Lv." + monster.level, hudX + 20, hudY + 40);
            
            // HP Bar
            double oneScale = (double)gp.tileSize * 6 / monster.maxLife;
            double hpBarValue = oneScale * monster.life;
            
            // Bar Background
            g2.setColor(new Color(35, 35, 35));
            g2.fillRect(hudX + 20, hudY + 50, (gp.tileSize*6), 20);
            
            // Actual Health
            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(hudX + 20, hudY + 50, (int)hpBarValue, 20);
            
            // Bar Border
            g2.setColor(Color.white);
            g2.drawRect(hudX + 20, hudY + 50, (gp.tileSize*6), 20);
            
            // HP Text
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
            g2.drawString("HP: " + monster.life + "/" + monster.maxLife, hudX + 20, hudY + 90);
        }

        // 3. DRAW PLAYER HUD (Above the Command Window)
        int pFrameX = gp.tileSize * 2; 
        int pFrameHeight = gp.tileSize * 4; 
        int pFrameY = gp.screenHeight - pFrameHeight; 
        int pFrameWidth = gp.screenWidth - (gp.tileSize * 4); 
        
        // Calculate HUD position (Just above the menu)
        int hudX = pFrameX;
        int hudY = pFrameY - (gp.tileSize * 2);
        int hudWidth = gp.tileSize * 7;
        int hudHeight = gp.tileSize * 2;
        
        drawSubWindow(hudX, hudY, hudWidth, hudHeight);
        
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));
        g2.drawString("Player Lv." + gp.player.level, hudX + 20, hudY + 35);
        
        // Player HP Bar
        double scale = (double)(gp.tileSize * 5) / gp.player.maxLife;
        double hpBar = scale * gp.player.life;
        
        g2.setColor(new Color(35,35,35));
        g2.fillRect(hudX + 20, hudY + 45, gp.tileSize*5, 15);
        g2.setColor(new Color(255,0,30));
        g2.fillRect(hudX + 20, hudY + 45, (int)hpBar, 15);
        g2.setColor(Color.white);
        g2.drawRect(hudX + 20, hudY + 45, gp.tileSize*5, 15);
        
        // Player EXP Bar (Smaller, Blue)
        double xpScale = (double)(gp.tileSize * 5) / gp.player.nextLevelExp;
        double xpBar = xpScale * gp.player.exp;
        
        g2.setColor(new Color(35,35,35));
        g2.fillRect(hudX + 20, hudY + 65, gp.tileSize*5, 10);
        g2.setColor(new Color(0,150,255)); // Blue for XP
        g2.fillRect(hudX + 20, hudY + 65, (int)xpBar, 10);
        g2.setColor(Color.white);
        g2.drawRect(hudX + 20, hudY + 65, gp.tileSize*5, 10);
        g2.setFont(g2.getFont().deriveFont(12F));
        g2.drawString("EXP", hudX + 20 + (gp.tileSize*5) + 5, hudY + 75);

        // 4. DRAW BATTLE MENU, INVENTORY, OR DIALOGUE
        if (battleSubState == 1) {
            // --- STATE: INVENTORY ---
            drawInventoryWindow();
            
            // Hint text
            g2.setFont(g2.getFont().deriveFont(18F));
            g2.setColor(Color.white);
            g2.drawString("[ESC] Back", gp.screenWidth - 150, gp.screenHeight - 20);
        } 
        else {
            // --- STATE: MAIN BATTLE VIEW (SubState 0) ---
            
            // We are NOT in the inventory, so we check the Battle Phase
            
            if (gp.battlePhase == gp.PHASE_SELECT) {
                // --- PHASE 0: SELECT ACTION (Attack, Bag, Run) ---
                drawSubWindow(pFrameX, pFrameY, pFrameWidth, pFrameHeight);

                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));

                int textX = pFrameX + 40;
                int textY = pFrameY + gp.tileSize; 
                int lineHeight = 40; 

                g2.drawString("Attack", textX, textY);
                if(commandNum == 0) g2.drawString(">", textX - 25, textY);

                textY += lineHeight;
                g2.drawString("Bag", textX, textY);
                if(commandNum == 1) g2.drawString(">", textX - 25, textY);

                textY += lineHeight;
                g2.drawString("Run", textX, textY);
                if(commandNum == 2) g2.drawString(">", textX - 25, textY);
            } 
            else if (gp.battlePhase == gp.PHASE_PLAYER_ATTACK || gp.battlePhase == gp.PHASE_ENEMY_ATTACK) {
                // --- PHASE 1 & 2: SHOW TEXT DIALOGUE ---
                
                drawSubWindow(pFrameX, pFrameY, pFrameWidth, pFrameHeight);
                
                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
                
                int textX = pFrameX + 40;
                int textY = pFrameY + gp.tileSize;
                
                // Handle multiline text
                if(currentDialogue != null) {
                    for(String line : currentDialogue.split("\n")) {
                        g2.drawString(line, textX, textY);
                        textY += 40;
                    }
                }
                
                // Draw prompt arrow
                g2.drawString("▼", pFrameX + pFrameWidth - 40, pFrameY + pFrameHeight - 20);
            }
        }

        if(gp.player.damageFlash == true) {
            
            // 1. Set Color to Red with Transparency (Alpha = 100)
            g2.setColor(new Color(255, 0, 0, 100)); 
            
            // 2. Fill the whole screen
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            
            // 3. Handle the Timer (Same logic as Player.java)
            gp.player.damageFlashCounter++;
            if(gp.player.damageFlashCounter > 20) {
                gp.player.damageFlashCounter = 0;
                gp.player.damageFlash = false;
            }
        }
    }

    public void drawTransition() {
        
        g2.setColor(new Color(0,0,0));
        
        // Effect 1: Rapid Flashing (0 to 30 frames)
        if(gp.transitionCounter < 30) {
            // Flash every 5 frames
            if(gp.transitionCounter % 5 == 0) {
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            }
        }
        // Effect 2: Screen Wipe / Curtain Close (30 to 60 frames)
        else {
            // Calculate how much the "curtains" have closed
            // Total animation time = 30 frames (60-30)
            // We need to cover half the screen height (top down) and half (bottom up)
            
            int animationProgress = gp.transitionCounter - 30;
            int maxFrames = 30;
            
            // Calculate height of the black bar
            int barHeight = (int) (gp.screenHeight / 2 * ((double)animationProgress / maxFrames));
            
            // Draw Top Bar going Down
            g2.fillRect(0, 0, gp.screenWidth, barHeight);
            
            // Draw Bottom Bar going Up
            g2.fillRect(0, gp.screenHeight - barHeight, gp.screenWidth, barHeight);
        }
    }

    public void drawDialogueWindow() {
    
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        //g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        g2.setFont(dialogueFont); 
        g2.setColor(Color.white);

        x += gp.tileSize;
        y += gp.tileSize;

        // Split text by \n so it doesn't run off screen
        if(currentDialogue != null) {
            for(String line : currentDialogue.split("\n")) {
                g2.drawString(line, x, y);
                y += 40;
            }
        }
    }

    // In UI.java

    public void drawTradeScreen() {

        // 1. PREVENT CRASH: Wrap everything in try-catch
        try {
            
            // 2. CHECK FOR NULL NPC
            // If the player hasn't interacted with a chest yet, npc might be null.
            if (npc == null) {
                System.out.println("DEBUG: npc is null in UI! Closing window to prevent crash.");
                gp.gameState = gp.playState; // Force close the window
                return;
            }

            // --- EXISTING DRAWING CODE ---

            // DRAW CHEST INVENTORY (Top Half - 9x1 Grid)
            int frameX = gp.tileSize * 2;
            int frameY = gp.tileSize;
            int frameWidth = gp.tileSize * 11;
            int frameHeight = gp.tileSize * 2 + 20;
            
            drawSubWindow(frameX, frameY, frameWidth, frameHeight);
            
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
            g2.setColor(Color.white);
            g2.drawString("CHEST", frameX + 40, frameY + 40);

            int slotXstart = frameX + 20;
            int slotYstart = frameY + 60;
            int slotX = slotXstart;
            int slotY = slotYstart;
            
            for(int i = 0; i < 9; i++) {
                g2.setColor(new Color(255, 255, 255, 120)); 
                g2.setStroke(new BasicStroke(1)); 
                g2.drawRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);

                if(gp.ui.slotRow == 0 && i == gp.ui.slotCol) {
                    g2.setColor(new Color(240,190,90));
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
                }
                
                // CRITICAL CHECK: Ensure inventory isn't null
                if (npc.inventory != null && i < npc.inventory.size()) {
                    if(npc.inventory.get(i) != null) {
                        g2.drawImage(npc.inventory.get(i).down1, slotX, slotY, gp.tileSize, gp.tileSize, null);
                    }
                }
                
                slotX += gp.tileSize + 3;
            }

            // DRAW PLAYER INVENTORY (Bottom Half)
            int pFrameY = frameY + frameHeight + 20;
            int pFrameHeight = gp.tileSize * 4;
            
            drawSubWindow(frameX, pFrameY, frameWidth, pFrameHeight);
            
            g2.setColor(Color.white);
            g2.drawString("YOUR BAG", frameX + 40, pFrameY + 40);
            
            int pSlotXstart = frameX + 20;
            int pSlotYstart = pFrameY + 60;
            slotX = pSlotXstart;
            slotY = pSlotYstart;
            
            for(int i = 0; i < 18; i++) {
                g2.setColor(new Color(255, 255, 255, 120)); 
                g2.setStroke(new BasicStroke(1)); 
                g2.drawRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);

                if(gp.ui.slotRow > 0) {
                    int playerIndex = (gp.ui.slotRow - 1) * 9 + gp.ui.slotCol;
                    if(i == playerIndex) {
                        g2.setColor(new Color(240,190,90));
                        g2.setStroke(new BasicStroke(3));
                        g2.drawRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
                    }
                }

                if(i < gp.player.inventory.size()) {
                    if(gp.player.inventory.get(i) != null) {
                        g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, gp.tileSize, gp.tileSize, null);
                    }
                }
                
                slotX += gp.tileSize + 3;
                
                if(i == 8) { 
                    slotX = pSlotXstart;
                    slotY += gp.tileSize + 3;
                }
            }
            
            // DRAW DESCRIPTION WINDOW
            Entity selectedItem = null;
            
            if(gp.ui.slotRow == 0) { 
                if(npc.inventory != null && gp.ui.slotCol < npc.inventory.size()) {
                    selectedItem = npc.inventory.get(gp.ui.slotCol);
                }
            } else { 
                int playerIndex = (gp.ui.slotRow - 1) * 9 + gp.ui.slotCol;
                if(playerIndex < gp.player.inventory.size()) {
                    selectedItem = gp.player.inventory.get(playerIndex);
                }
            }
            
            if(selectedItem != null) {
                int dFrameY = pFrameY + pFrameHeight + 20;
                int dFrameHeight = gp.tileSize * 3;
                
                drawSubWindow(frameX, dFrameY, frameWidth, dFrameHeight);
                
                int textX = frameX + 20;
                int textY = dFrameY + 40;
                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(20F));
                
                g2.drawString(selectedItem.name, textX, textY);
                textY += 32;
                g2.drawString(selectedItem.description, textX, textY);
            }

        } catch (Exception e) {
            // THIS WILL PRINT THE ERROR TO YOUR CONSOLE INSTEAD OF FREEZING
            System.out.println("CRASH IN DRAW TRADE SCREEN:");
            e.printStackTrace();
            gp.gameState = gp.playState; // Reset to play state to keep game running
        }
    }

    public void drawGameOverScreen() {
        
        g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "Game Over";
        
        // Shadow
        g2.setColor(Color.black);
        x = getXforCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);

        // Main Text
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        // Retry Option
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x - 40, y);
        }

        // Quit Option
        text = "Quit";
        x = getXforCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x - 40, y);
        }
    }

    // Helper method to center text (If you don't have it already)
    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
}