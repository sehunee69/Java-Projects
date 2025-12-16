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
    public final int maxMap = 10;
    public int currentMap = 0;

    //GAME STATE
    public int gameState;
    public final int playState = 1;
    public final int battleState = 2;
    public final int characterState = 3;
    public final int transitionState = 4;
    public final int dialogueState = 5;
    public final int tradeState = 6;
    public final int gameOverState = 7;
    public final int gameWinState = 8;

    // REMEMBER! CHEST IS AN ENTITY!
    public Entity currentChest;

    // BATTLE VARS
    public int currentBattleMonsterIndex;
    public int battlePhase; // 0 = Player Turn, 1 = Enemy Turn
    int battleTurnCounter = 0; // Timer for enemy attack delay
    public final int PHASE_SELECT = 0;        // Player selects Attack/Run
    public final int PHASE_PLAYER_ATTACK = 1; // Reading Player's attack text
    public final int PHASE_ENEMY_ATTACK = 2;  // Reading Enemy's attack text
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
    public Entity monsters[][] = new Entity[maxMap][20];
    public Entity obj[][] = new Entity[maxMap][10]; // Objects
    public Entity npc[][] = new Entity[maxMap][10];
    AssetSetter aSetter = new AssetSetter(this);
    public EventHandler eHandler = new EventHandler(this);
    public Lighting light = new Lighting(this);

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

            eHandler.checkEvent();

            for(int i = 0; i < npc[1].length; i++) {
                if(npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }

            for(int i = 0; i < monsters[1].length; i++) {
                if(monsters[currentMap][i] != null) {
                    monsters[currentMap][i].update();
                }
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
            player.update();
        }
    }
    

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);

        for(int i = 0; i < npc[1].length; i++) {
            if(npc[currentMap][i] != null) {
                npc[currentMap][i].draw(g2, this);
            }
        }

        for(int i = 0; i < monsters[1].length; i++) {
            if(monsters[currentMap][i] != null) {
                monsters[currentMap][i].draw(g2, this);
            }
        }

        for(int i = 0; i < obj[1].length; i++) {
            if(obj[currentMap][i] != null) {
                obj[currentMap][i].draw(g2, this);
            }
        }
        
        player.draw(g2);
        light.draw(g2);
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

    public void retry() {
        //player.setDefaultValues(); // Restores HP, speed, direction
        player.invincible = false;
        
        // Restore Player Position (Optional: Set to a specific checkpoint)
        player.worldX = tileSize * 23; 
        player.worldY = tileSize * 21;
        
        // Reset Music
        stopMusic();
        playMusic(0); // Play background music
    }

    public void restart() {
        // Use this if you want to completely reset the game (reload maps, etc)
        player.setDefaultValues();
        player.inventory.clear(); // Clear bag
        aSetter.setObject(); // Reset items on map
        aSetter.setMonster(); // Reset monsters
        playMusic(0);
    }
}
