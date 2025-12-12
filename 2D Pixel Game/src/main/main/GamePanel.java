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


    int FPS = 60;

    TileManager tileM = new TileManager(this); 
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public Entity monsters[] = new Entity[20];
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

        player.update();

        for(int i = 0; i < monsters.length; i++) {
            if(monsters[i] != null) {
                monsters[i].update();
            }
        }
    }
    

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);
        for(int i = 0; i < monsters.length; i++) {
            if(monsters[i] != null) {
                // We need to implement a generic draw method in Entity or cast it
                // Ideally, move the draw logic from Player to Entity class
                // For now, we assume Entity has the draw method (See Step 5 below)
                monsters[i].draw(g2, this); 
            }
        }
        player.draw(g2);
        g2.dispose();
    }
}
