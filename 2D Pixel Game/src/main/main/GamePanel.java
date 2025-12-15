package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{

    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 pixel tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile for player sprites 
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxWorldWidth = tileSize * maxWorldCol;
    public final int maxWorldHeight = tileSize * maxWorldRow;
    public int worldWidth = tileSize * maxWorldCol;
    public int worldHeight = tileSize * maxWorldRow;

    //GAME STATE
    public int gameState;
    public final int playState = 1;
    public final int battleState = 2;
    public final int characterState = 3;
    public final int transitionState = 4;
    public final int dialogueState = 5;
    public final int tradeState = 6;

    // REMEMBER! CHEST IS AN ENTITY!
    public Entity currentChest;

    // BATTLE VARS
    public int currentBattleMonsterIndex;
    public int battlePhase; // 0 = Player Turn, 1 = Enemy Turn
    int battleTurnCounter = 0; // Timer for enemy attack delay
    public int transitionCounter = 0;

    // SOUNDS
    Sound music = new Sound();
    Sound se = new Sound(); // For Sound Effects later

    //UI
    public UI ui = new UI(this);
    int FPS = 60;
    public int currentNPCIndex;

    TileManager tileM = new TileManager(this); 
    public KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public Entity monsters[] = new Entity[20];
    public Entity obj[] = new Entity[10]; // Objects
    public Entity npc[] = new Entity[10];
    AssetSetter aSetter = new AssetSetter(this);

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {

        aSetter.setMonster();
        aSetter.setObject();
        aSetter.setNPC();
        playMusic(0);

        gameState = playState;
        
    }


    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {
        
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null) {
            
            update();

            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    

    public void update() {

        if(gameState == playState) {
            player.update();

            for(int i = 0; i < npc.length; i++) {
                if(npc[i] != null) {
                    npc[i].update();
                }
            }

            for(int i = 0; i < monsters.length; i++) {
                if(monsters[i] != null) monsters[i].update();
            }
        }
        
        // --- TRANSITION STATE UPDATE ---
        if(gameState == transitionState) {
            transitionCounter++;
            if(transitionCounter > 60) { // Animation plays for 1 second (60 frames)
                transitionCounter = 0;
                playMusic(5);
                gameState = battleState; // Switch to actual battle
            }
        }

        if(gameState == battleState) {
            // ENEMY TURN LOGIC
            if(battlePhase == 1) {
                battleTurnCounter++;
                
                // Wait 60 frames (1 second) then monster attacks
                if(battleTurnCounter > 60) {
                    
                    Entity monster = monsters[currentBattleMonsterIndex];
                    
                    // Simple Damage Calculation
                    int damage = monster.atk - player.def;
                    if(damage < 0) damage = 0;
                    
                    player.life -= damage;
                    ui.showMessage(monster.name + " attacks! You took " + damage + " dmg!");
                    
                    // Reset to Player Turn
                    battlePhase = 0; 
                    battleTurnCounter = 0;
                    
                    // Check Game Over (Optional)
                    if(player.life <= 0) {
                        // gameState = gameOverState; (Future implementation)
                        ui.showMessage("You died...");
                    }
                }
            }
        }
    }
    

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);

        for(int i = 0; i < npc.length; i++) {
            if(npc[i] != null) {
                npc[i].draw(g2, this);
            }
        }

        for(int i = 0; i < monsters.length; i++) {
            if(monsters[i] != null) {
                // We need to implement a generic draw method in Entity or cast it
                // Ideally, move the draw logic from Player to Entity class
                // For now, we assume Entity has the draw method (See Step 5 below)
                monsters[i].draw(g2, this); 
            }
        }

        for(int i = 0; i < obj.length; i++) {
            if(obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }
        
        player.draw(g2);
        ui.draw(g2);
        g2.dispose();
    }

    public void playMusic(int i) {
        music.play(i); // Simplified: Just play index i
        music.loop(i);
    }

    public void stopMusic() {
        music.stopAll(); // New helper method
    }

    public void playSE(int i) {
        se.play(i); // Simplified: Just play index i
    }
}
