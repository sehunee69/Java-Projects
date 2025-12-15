package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Lighting {

    GamePanel gp;
    BufferedImage darknessFilter;
    public int dayCounter;
    public float filterAlpha = 0f;

    // Day/Night Cycle constants (Optional)
    public final int day = 0;
    public final int dusk = 1;
    public final int night = 2;
    public final int dawn = 3;
    public int dayState = day;

    public Lighting(GamePanel gp) {
        this.gp = gp;
        setLightSource();
    }

    public void setLightSource() {

        // Create a buffered image the size of the screen
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();

        if(gp.player.currentLight == null) {
            // If player has no lantern, default circle size is huge (or zero if you want total pitch black)
            // But let's create a circle around player for visibility
            
            // Get the center of the player's screen position
            int centerX = gp.player.screenX + (gp.tileSize)/2;
            int centerY = gp.player.screenY + (gp.tileSize)/2;

            // Create a gradient effect (fades from transparent at center to black at edges)
            Color color[] = new Color[5];
            float fraction[] = new float[5];

            color[0] = new Color(0,0,0,0f);       // Center is transparent (0%)
            color[1] = new Color(0,0,0,0.25f);    // 25% Dark
            color[2] = new Color(0,0,0,0.5f);     // 50% Dark
            color[3] = new Color(0,0,0,0.75f);    // 75% Dark
            color[4] = new Color(0,0,0,0.98f);    // Edge is almost Pitch Black

            fraction[0] = 0f;
            fraction[1] = 0.25f;
            fraction[2] = 0.5f;
            fraction[3] = 0.75f;
            fraction[4] = 1f;

            // Create a "Paint" using these settings
            // 350 is the radius (change to make light bigger/smaller)
            RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, 350, fraction, color);

            // Apply the paint to the full screen rectangle
            g2.setPaint(gPaint);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            
            g2.dispose();
        }
    }
    
    public void draw(Graphics2D g2) {
        
        // ONLY DRAW IF WE ARE ON THE DUNGEON MAP (Map 1)
        if(gp.currentMap == 1) { 
            g2.drawImage(darknessFilter, 0, 0, null);
        }
    }
}