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
            if(code == KeyEvent.VK_ENTER) { /* Interact logic */ }

            if(code == KeyEvent.VK_E) {
            // Check if NPC is in front of player
                int npcIndex = gp.cChecker.checkEntity(gp.player, gp.npc[gp.currentMap]);
                if(npcIndex != 999) {
                    gp.currentNPCIndex = npcIndex;
                    gp.npc[gp.currentMap][npcIndex].speak();
                }
            }
        } 
        else if(gp.gameState == gp.dialogueState) {
            if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E) {
                if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E) {
                    gp.npc[gp.currentMap][gp.currentNPCIndex].speak();
                }
            }
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
                    if(gp.ui.commandNum == 0) {
                        gp.ui.subState = 1; // Status
                        gp.playSE(3);
                    }
                    if(gp.ui.commandNum == 1) {
                        gp.ui.subState = 2; // Bag
                        gp.playSE(2);
                    }
                    if(gp.ui.commandNum == 2) {
                        gp.ui.subState = 0; // Options
                        gp.playSE(3);
                    }
                    if(gp.ui.commandNum == 3) {
                        gp.ui.subState = 0; // Save
                        gp.playSE(3);
                    }
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
                if(code == KeyEvent.VK_S) { if(gp.ui.slotRow != 1) gp.ui.slotRow++; }
                if(code == KeyEvent.VK_D) { if(gp.ui.slotCol != 8) gp.ui.slotCol++; }
                
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
        // 3. BATTLE STATE
        else if(gp.gameState == gp.battleState) {
            
            // PLAYER TURN ONLY
            if(gp.battlePhase == 0) {

                // --- SUBSTATE 0: MAIN BATTLE MENU ---
                if(gp.ui.battleSubState == 0) {
                    
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
                        // ATTACK
                        if(gp.ui.commandNum == 0) {
                            int monsterIndex = gp.currentBattleMonsterIndex;
                            Entity monster = gp.monsters[gp.currentMap][monsterIndex];

                            monster.shakeCounter = 30; // Shake for 30 frames
                            gp.ui.showingSlash = true; 
                            gp.ui.slashCounter = 0;
                            
                            gp.playSE(6);
                            int damage = gp.player.atk - monster.def;
                            if(damage < 0) damage = 0;
                            monster.life -= damage;
                            gp.ui.showMessage("You hit " + monster.name + " for " + damage + " dmg!");

                            // CHECK DEATH (Copy your existing death logic here)
                            if(monster.life <= 0) {
                                // 1. DROP ITEMS (Existing code)
                                for(int j = 0; j < gp.obj[1].length; j++) {
                                    if(gp.obj[gp.currentMap][j] == null) {
                                        gp.obj[gp.currentMap][j] = new obj.OBJ_SlimeGoop(gp);
                                        gp.obj[gp.currentMap][j].worldX = monster.worldX;
                                        gp.obj[gp.currentMap][j].worldY = monster.worldY;
                                        break; 
                                    }
                                }
                                // 2. GAIN EXP (NEW!)
                                gp.player.exp += monster.exp;
                                gp.ui.showMessage("You killed " + monster.name + "! +" + monster.exp + " EXP");
                                
                                // 3. CHECK LEVEL UP (NEW!)
                                gp.player.checkLevelUp();

                                // 4. DESPAWN & END BATTLE
                                gp.monsters[gp.currentMap][monsterIndex] = null; // Basic despawn
                                gp.stopMusic();
                                gp.gameState = gp.playState;
                                gp.playMusic(0);
                            } else {
                                gp.battlePhase = 1; // Enemy Turn
                            }
                        }
                        // BAG
                        else if(gp.ui.commandNum == 1) { 
                            gp.ui.battleSubState = 1; // Switch to Bag Mode
                            gp.playSE(2);
                        }
                        // RUN
                        else if(gp.ui.commandNum == 2) {
                            gp.ui.commandNum = 0;
                            gp.stopMusic();
                            gp.gameState = gp.playState;
                            gp.playMusic(0);
                        }
                    }
                }
                
                // --- SUBSTATE 1: INVENTORY SELECTION ---
                else if (gp.ui.battleSubState == 1) {
                    
                    // NAVIGATE BAG (Reuse UI slots)
                    if(code == KeyEvent.VK_W) { if(gp.ui.slotRow != 0) gp.ui.slotRow--; gp.playSE(3);}
                    if(code == KeyEvent.VK_A) { if(gp.ui.slotCol != 0) gp.ui.slotCol--; gp.playSE(3);}
                    if(code == KeyEvent.VK_S) { if(gp.ui.slotRow != 3) gp.ui.slotRow++; gp.playSE(3);}
                    if(code == KeyEvent.VK_D) { if(gp.ui.slotCol != 4) gp.ui.slotCol++; gp.playSE(3);}
                    
                    if(code == KeyEvent.VK_ENTER) {
                        gp.player.selectItem(); // Use the item
                    }
                    
                    // GO BACK TO MENU
                    if(code == KeyEvent.VK_ESCAPE) {
                        gp.ui.battleSubState = 0;
                    }
                }
            }
        }
        else if(gp.gameState == gp.tradeState) {
            if(code == KeyEvent.VK_E) ePressed = true;
    
            if(code == KeyEvent.VK_ENTER) {
                tradeItem();
            }
            
            // CLOSE CHEST
            if(code == KeyEvent.VK_E || code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.playState;
            }
            
            // NAVIGATION
            if(code == KeyEvent.VK_W) {
                if(gp.ui.slotRow != 0) {
                    gp.ui.slotRow--;
                    gp.playSE(2); // Cursor sound
                }
            }
            if(code == KeyEvent.VK_A) {
                if(gp.ui.slotCol != 0) {
                    gp.ui.slotCol--;
                    gp.playSE(2);
                }
            }
            if(code == KeyEvent.VK_S) {
                // Allow going down to row 3 (Player's last row)
                if(gp.ui.slotRow != 2) {
                    gp.ui.slotRow++;
                    gp.playSE(2);
                }
            }
            if(code == KeyEvent.VK_D) {
                if(gp.ui.slotCol != 8) {
                    gp.ui.slotCol++;
                    gp.playSE(2);
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
        
        if(code == KeyEvent.VK_ENTER) enterPressed = false;
        if(code == KeyEvent.VK_E) ePressed = false;
    }

    public void tradeItem() {
    
        // 1. CHEST (Row 0)
        if(gp.ui.slotRow == 0) {
            
            // Ensure valid index for chest inventory
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
            // Calculate player index based on rows 1 and 2
            // If row is 1, index is 0-8. If row is 2, index is 9-17.
            int playerIndex = (gp.ui.slotRow - 1) * 9 + gp.ui.slotCol;
            
            if(playerIndex < gp.player.inventory.size()) {
                
                Entity item = gp.player.inventory.get(playerIndex);
                
                gp.ui.npc.inventory.add(item);
                gp.player.inventory.remove(playerIndex);
                gp.playSE(1);
            }
        }
    }
}
