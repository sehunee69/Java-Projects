package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entity.Entity;

public class KeyHandler implements KeyListener{

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, ePressed;

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
            if(code == KeyEvent.VK_ENTER) enterPressed = true;
            if(code == KeyEvent.VK_E) ePressed = true;
            
            if(code == KeyEvent.VK_E) {
                int npcIndex = gp.cChecker.checkEntity(gp.player, gp.npc[gp.currentMap]);
                if(npcIndex != 999) {
                    gp.currentNPCIndex = npcIndex;
                    gp.npc[gp.currentMap][npcIndex].speak();
                }
            }
        } 
        else if(gp.gameState == gp.dialogueState) {
            if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E) {
                gp.npc[gp.currentMap][gp.currentNPCIndex].speak();
            }
        }
        
        // 2. CHARACTER STATE (Menu Hub)
        else if(gp.gameState == gp.characterState) {
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
                    if(gp.ui.commandNum == 0) { gp.ui.subState = 1; gp.playSE(3); }
                    if(gp.ui.commandNum == 1) { gp.ui.subState = 2; gp.playSE(2); }
                    if(gp.ui.commandNum == 2) { gp.ui.subState = 0; gp.playSE(3); }
                    if(gp.ui.commandNum == 3) { gp.ui.subState = 0; gp.playSE(3); }
                }
            }
            else if (gp.ui.subState == 1) {
                if(code == KeyEvent.VK_ESCAPE) gp.ui.subState = 0;
            }
            else if(gp.ui.subState == 2) {
                if(code == KeyEvent.VK_W) { if(gp.ui.slotRow != 0) gp.ui.slotRow--; }
                if(code == KeyEvent.VK_A) { if(gp.ui.slotCol != 0) gp.ui.slotCol--; }
                if(code == KeyEvent.VK_S) { if(gp.ui.slotRow != 1) gp.ui.slotRow++; }
                if(code == KeyEvent.VK_D) { if(gp.ui.slotCol != 8) gp.ui.slotCol++; }
                
                if(code == KeyEvent.VK_ENTER) gp.player.selectItem(); 
                if(code == KeyEvent.VK_ESCAPE) gp.ui.subState = 0; 
            }
            if(code == KeyEvent.VK_C) {
                gp.gameState = gp.playState;
                gp.ui.subState = 0;
            }
        }
        
        // 3. BATTLE STATE
        else if(gp.gameState == gp.battleState) {

            // --- SUBSTATE 1: INVENTORY ---
            if (gp.ui.battleSubState == 1) {
                if(code == KeyEvent.VK_W) { if(gp.ui.slotRow != 0) gp.ui.slotRow--; gp.playSE(3);}
                if(code == KeyEvent.VK_A) { if(gp.ui.slotCol != 0) gp.ui.slotCol--; gp.playSE(3);}
                if(code == KeyEvent.VK_S) { if(gp.ui.slotRow != 3) gp.ui.slotRow++; gp.playSE(3);}
                if(code == KeyEvent.VK_D) { if(gp.ui.slotCol != 4) gp.ui.slotCol++; gp.playSE(3);}
                if(code == KeyEvent.VK_ENTER) gp.player.selectItem();
                if(code == KeyEvent.VK_ESCAPE) gp.ui.battleSubState = 0;
                return; 
            }

            // --- PHASE 0: SELECT ACTION ---
            if(gp.battlePhase == gp.PHASE_SELECT) {
                if(code == KeyEvent.VK_W) { 
                    gp.ui.commandNum--;
                    if(gp.ui.commandNum < 0) gp.ui.commandNum = 2;
                    gp.playSE(3);
                }
                if(code == KeyEvent.VK_S) { 
                    gp.ui.commandNum++;
                    if(gp.ui.commandNum > 2) gp.ui.commandNum = 0;
                    gp.playSE(3);
                }
                
                if(code == KeyEvent.VK_ENTER) {
                    if(gp.ui.commandNum == 0) { // ATTACK
                        int monsterIndex = gp.currentBattleMonsterIndex;
                        Entity monster = gp.monsters[gp.currentMap][monsterIndex];
                        monster.shakeCounter = 30;
                        gp.ui.showingSlash = true; 
                        gp.ui.slashCounter = 0;
                        gp.playSE(6);
                        
                        int damage = gp.player.atk - monster.def;
                        if(damage < 0) damage = 0;
                        monster.life -= damage;
                        
                        gp.ui.currentDialogue = "You attacked " + monster.name + "!\n" + 
                                                "It took " + damage + " damage.";
                        gp.battlePhase = gp.PHASE_PLAYER_ATTACK; 
                    }
                    else if(gp.ui.commandNum == 1) { // BAG
                        gp.ui.battleSubState = 1; 
                        gp.playSE(2);
                    }
                    else if(gp.ui.commandNum == 2) { // RUN
                        gp.ui.commandNum = 0;
                        gp.stopMusic();
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                    }
                }
            }

            // --- PHASE 1: READING PLAYER ATTACK RESULT ---
            else if(gp.battlePhase == gp.PHASE_PLAYER_ATTACK) {
                
                if(code == KeyEvent.VK_ENTER) {
                    
                    int monsterIndex = gp.currentBattleMonsterIndex;
                    Entity monster = gp.monsters[gp.currentMap][monsterIndex];

                    // PREPARE DROP LOGIC
                    Entity droppedItem = null;
                    if(monster.name.equals("Green Slime")) {
                            droppedItem = new obj.OBJ_SlimeGoop(gp);
                    }
                    else if(monster.name.equals("Soldier")) {
                            droppedItem = new obj.OBJ_Sword_Sharp(gp); 
                    }
                    else if(monster.name.equals("Great Soldier")) {
                            // Example: Great Soldiers drop Shields or Keys
                    }

                    // A. CHECK IF MONSTER DIED
                    if(monster.life <= 0) {
                        
                        // 1. DROP ITEMS
                        if(droppedItem != null) {
                            for(int j = 0; j < gp.obj[1].length; j++) {
                                if(gp.obj[gp.currentMap][j] == null) {
                                    gp.obj[gp.currentMap][j] = droppedItem;
                                    gp.obj[gp.currentMap][j].worldX = monster.worldX;
                                    gp.obj[gp.currentMap][j].worldY = monster.worldY;
                                    break; 
                                }
                            }
                        }

                        // 2. REWARDS
                        gp.player.exp += monster.exp;
                        gp.player.checkLevelUp();
                        gp.ui.showMessage("You killed " + monster.name + "! +" + monster.exp + " EXP"); 

                        gp.monsters[gp.currentMap][monsterIndex] = null;
                        gp.stopMusic();
                        gp.gameState = gp.playState; // END BATTLE
                        gp.playMusic(0);
                        
                        // Reset phase for next battle
                        gp.battlePhase = gp.PHASE_SELECT;
                    }
                    // B. MONSTER ALIVE -> ENEMY ATTACKS
                    else {
                        int damage = monster.atk - gp.player.def;
                        if(damage < 0) damage = 0;
                        gp.player.life -= damage;
                        gp.player.damageFlash = true;
                        
                        gp.ui.currentDialogue = monster.name + " attacks back!\n" + 
                                                "You took " + damage + " damage.";
                        
                        gp.battlePhase = gp.PHASE_ENEMY_ATTACK; 
                    }
                }
            }

            // --- PHASE 2: READING ENEMY ATTACK RESULT ---
            else if(gp.battlePhase == gp.PHASE_ENEMY_ATTACK) {
                
                if(code == KeyEvent.VK_ENTER) {
                    // A. CHECK IF PLAYER DIED
                    if(gp.player.life <= 0) {
                        gp.gameState = gp.gameOverState;
                        gp.stopMusic();
                    } 
                    // B. PLAYER ALIVE -> RETURN TO MENU
                    else {
                        gp.battlePhase = gp.PHASE_SELECT; 
                        gp.ui.commandNum = 0; 
                    }
                }
            }
        } // <--- THIS BRACKET CLOSES BATTLE STATE

        // 4. TRADE STATE
        else if(gp.gameState == gp.tradeState) {
            if(code == KeyEvent.VK_E) ePressed = true;
    
            if(code == KeyEvent.VK_ENTER) {
                tradeItem();
            }
            if(code == KeyEvent.VK_E || code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.playState;
            }
            // NAVIGATION
            if(code == KeyEvent.VK_W) {
                if(gp.ui.slotRow != 0) { gp.ui.slotRow--; gp.playSE(2); }
            }
            if(code == KeyEvent.VK_A) {
                if(gp.ui.slotCol != 0) { gp.ui.slotCol--; gp.playSE(2); }
            }
            if(code == KeyEvent.VK_S) {
                if(gp.ui.slotRow != 2) { gp.ui.slotRow++; gp.playSE(2); }
            }
            if(code == KeyEvent.VK_D) {
                if(gp.ui.slotCol != 8) { gp.ui.slotCol++; gp.playSE(2); }
            }
        }

        // 5. GAME OVER STATE
        else if(gp.gameState == gp.gameOverState) {
            gameOverStateInput(code);
        }
    }

    public void characterStateInput(int code) {
        if (code == KeyEvent.VK_C || code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_W) { if(gp.ui.slotRow != 0) gp.ui.slotRow--; }
        if (code == KeyEvent.VK_A) { if(gp.ui.slotCol != 0) gp.ui.slotCol--; }
        if (code == KeyEvent.VK_S) { if(gp.ui.slotRow != 3) gp.ui.slotRow++; }
        if (code == KeyEvent.VK_D) { if(gp.ui.slotCol != 4) gp.ui.slotCol++; }

        if (code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W) upPressed = false;
        if(code == KeyEvent.VK_S) downPressed = false;
        if(code == KeyEvent.VK_A) leftPressed = false;
        if(code == KeyEvent.VK_D) rightPressed = false;
        if(code == KeyEvent.VK_ENTER) enterPressed = false;
        if(code == KeyEvent.VK_E) ePressed = false;
    }

    public void tradeItem() {
        // 1. CHEST (Row 0)
        if(gp.ui.slotRow == 0) {
            if(gp.ui.slotCol < gp.ui.npc.inventory.size()) {
                Entity item = gp.ui.npc.inventory.get(gp.ui.slotCol);
                if(gp.player.inventory.size() < gp.player.maxInventorySize) {
                    gp.player.inventory.add(item);
                    gp.ui.npc.inventory.remove(gp.ui.slotCol);
                    gp.playSE(1); 
                } else {
                    gp.ui.showMessage("Bag is full!");
                }
            }
        }
        // 2. PLAYER (Row 1 or 2)
        else {
            int playerIndex = (gp.ui.slotRow - 1) * 9 + gp.ui.slotCol;
            if(playerIndex < gp.player.inventory.size()) {
                Entity item = gp.player.inventory.get(playerIndex);
                gp.ui.npc.inventory.add(item);
                gp.player.inventory.remove(playerIndex);
                gp.playSE(1);
            }
        }
    }

    public void gameOverStateInput(int code) {
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) gp.ui.commandNum = 1;
            gp.playSE(3); 
        }
        if(code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) gp.ui.commandNum = 0;
            gp.playSE(3);
        }
        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) { // RETRY
                gp.gameState = gp.playState;
                gp.retry();
                gp.playMusic(0);
            }
            else if(gp.ui.commandNum == 1) { // QUIT
                System.exit(0); 
            }
        }
    }
}