package obj; // or package entity;

import entity.Entity;
import main.GamePanel;
import javax.imageio.ImageIO;


public class OBJ_Chest extends Entity {

    GamePanel gp;

    public OBJ_Chest(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickupOnly; // Or create a new type if you want
        name = "Chest";
        
        // VISUALS
        try {
            // Replace with your chest image
            down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/tile012.png"));
            // If you don't have a chest image yet, use a potion or box as placeholder
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        collisionOn = true;

        // --- ADD LOOT TO THE CHEST ---
        // The chest has its own inventory!
        inventory.add(new OBJ_HealingPotion(gp));
        inventory.add(new OBJ_HealingPotion(gp));
        // inventory.add(new OBJ_Sword_Normal(gp)); // Example
    }
    
    public void interact() {
        gp.gameState = gp.tradeState; // Switch to the new UI state
        gp.ui.npc = this; // Tell UI that THIS is the specific chest we opened
    }
}