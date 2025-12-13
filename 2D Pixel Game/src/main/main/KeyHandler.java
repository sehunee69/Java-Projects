package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entity.Entity;

public class KeyHandler implements KeyListener{

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // 1. PLAY STATE
        if(gp.gameState == gp.playState) {
            if(code == KeyEvent.VK_W) upPressed = true;
            if(code == KeyEvent.VK_S) downPressed = true;
            if(code == KeyEvent.VK_A) leftPressed = true;
            if(code == KeyEvent.VK_D) rightPressed = true;
            if(code == KeyEvent.VK_C) gp.gameState = gp.characterState;
            if(code == KeyEvent.VK_ENTER) { /* Interact logic */ }
        }
        
        // 2. CHARACTER STATE (Menu Hub)
        else if(gp.gameState == gp.characterState) {
            
            // A. NAVIGATING TABS (Left Side)
            if(gp.ui.subState == 0) {
                if(code == KeyEvent.VK_W) {
                    gp.ui.commandNum--;
                    if(gp.ui.commandNum < 0) gp.ui.commandNum = 3;
                }
                if(code == KeyEvent.VK_S) {
                    gp.ui.commandNum++;
                    if(gp.ui.commandNum > 3) gp.ui.commandNum = 0;
                }
                if(code == KeyEvent.VK_ENTER) {
                    if(gp.ui.commandNum == 0) gp.ui.subState = 1; // Status
                    if(gp.ui.commandNum == 1) gp.ui.subState = 2; // Bag
                    if(gp.ui.commandNum == 2) gp.ui.subState = 0; // Options
                    if(gp.ui.commandNum == 3) gp.ui.subState = 0; // Save
                }
            }
            // B. INSIDE STATUS (Right Side)
            else if (gp.ui.subState == 1) {
                if(code == KeyEvent.VK_ESCAPE) gp.ui.subState = 0;
            }
            // C. INSIDE BAG (Right Side)
            else if(gp.ui.subState == 2) {
                if(code == KeyEvent.VK_W) { if(gp.ui.slotRow != 0) gp.ui.slotRow--; }
                if(code == KeyEvent.VK_A) { if(gp.ui.slotCol != 0) gp.ui.slotCol--; }
                if(code == KeyEvent.VK_S) { if(gp.ui.slotRow != 3) gp.ui.slotRow++; }
                if(code == KeyEvent.VK_D) { if(gp.ui.slotCol != 4) gp.ui.slotCol++; }
                
                if(code == KeyEvent.VK_ENTER) {
                    gp.player.selectItem(); 
                }
                if(code == KeyEvent.VK_ESCAPE) {
                    gp.ui.subState = 0; // Go back to tabs
                }
            }
            
            // Close Character Screen entirely
            if(code == KeyEvent.VK_C) {
                gp.gameState = gp.playState;
                gp.ui.subState = 0;
            }
        }
        
        // 3. BATTLE STATE
        else if(gp.gameState == gp.battleState) {
            // ONLY ALLOW INPUT IF IT IS PLAYER'S TURN (Phase 0)
            if(gp.battlePhase == 0) {

                if(code == KeyEvent.VK_W) { 
                    gp.ui.commandNum--;
                    if(gp.ui.commandNum < 0) gp.ui.commandNum = 2;
                }
                if(code == KeyEvent.VK_S) { 
                    gp.ui.commandNum++;
                    if(gp.ui.commandNum > 2) gp.ui.commandNum = 0;
                }
                
                if(code == KeyEvent.VK_ENTER) {
                    
                    // ATTACK
                    if(gp.ui.commandNum == 0) {
                        
                        int monsterIndex = gp.currentBattleMonsterIndex;
                        Entity monster = gp.monsters[monsterIndex];
                        
                        // PLAYER ATTACKS
                        int damage = gp.player.atk - monster.def;
                        if(damage < 0) damage = 0;
                        
                        monster.life -= damage;
                        gp.ui.showMessage("You hit " + monster.name + " for " + damage + " dmg!");

                        // CHECK DEATH
                        if(monster.life <= 0) {
                            // Drop Item
                            for(int j = 0; j < gp.obj.length; j++) {
                                if(gp.obj[j] == null) {
                                    gp.obj[j] = new obj.OBJ_SlimeGoop(gp);
                                    gp.obj[j].worldX = monster.worldX;
                                    gp.obj[j].worldY = monster.worldY;
                                    break; 
                                }
                            }
                            // End Battle
                            gp.monsters[monsterIndex] = null; 
                            gp.gameState = gp.playState; 
                            gp.ui.showMessage("You won!");
                        } else {
                            // IF ALIVE, PASS TURN TO ENEMY
                            gp.battlePhase = 1;
                        }
                    }
                    // BAG
                    if(gp.ui.commandNum == 1) { 
                        // You can eventually open bag here, for now it does nothing
                        gp.ui.showMessage("Bag not ready!");
                    }
                    // RUN
                    if(gp.ui.commandNum == 2) {
                        gp.gameState = gp.playState;
                    }
                }
            }
        }
    }

    public void characterStateInput(int code) {
        // 1. Close Inventory
        if (code == KeyEvent.VK_C || code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }

        // 2. Move Cursor (updates UI slot variables)
        if (code == KeyEvent.VK_W) { if(gp.ui.slotRow != 0) gp.ui.slotRow--; }
        if (code == KeyEvent.VK_A) { if(gp.ui.slotCol != 0) gp.ui.slotCol--; }
        if (code == KeyEvent.VK_S) { if(gp.ui.slotRow != 3) gp.ui.slotRow++; }
        if (code == KeyEvent.VK_D) { if(gp.ui.slotCol != 4) gp.ui.slotCol++; }

        // 3. Select Item (Craft or Consume)
        if (code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if(code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        
    }

}
