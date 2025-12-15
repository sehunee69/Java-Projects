package obj;

import entity.Entity;
import main.GamePanel;
import javax.imageio.ImageIO;

public class OBJ_Chest extends Entity {

    GamePanel gp;

    public OBJ_Chest(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickupOnly; 
        name = "Chest";
        
        // VISUALS
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/tile012.png")); 
            // Use your chest image here
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        collisionOn = true;

        // --- CHANGE: REMOVED HARDCODED POTIONS ---
        // We leave the inventory empty here so we can customize it in AssetSetter.
    }
    
    // --- NEW HELPER METHOD ---
    // Call this from AssetSetter to put items in specific chests
    public void setLoot(Entity loot) {
        inventory.add(loot);
    }
    
    public void interact() {
        gp.gameState = gp.tradeState; 
        gp.ui.npc = this; 
    }
}