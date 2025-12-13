// package obj;

// import java.io.IOException;
// import javax.imageio.ImageIO;
// import entity.Entity;
// import main.GamePanel;

// public class OBJ_HealingPotion extends Entity {
    
//     public OBJ_HealingPotion(GamePanel gp) {
//         super(gp); // <--- THIS FIXES THE ERROR

//         name = "Red Potion";
//         type = type_consumable; // Set to consumable
//         description = "[Restores 1 Heart]\nA delicious red liquid.\nSmells like strawberries.";
        
//         try {
//             down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/potion_red.png"));
//         } catch(IOException e) {
//             e.printStackTrace();
//         }
//     }
// }

package obj;

import entity.Entity;
import main.GamePanel;

public class OBJ_HealingPotion extends Entity {
    
    public OBJ_HealingPotion(GamePanel gp) {
        super(gp);

        name = "Red Potion";
        type = type_consumable; 
        description = "[Restores 1 Heart]\nA delicious red liquid.\nSmells like strawberries.";
        
        // NO IMAGE YET: The item will be invisible.
        // down1 = setup("/obj/object/potion_red"); // Use this if you have the file later
    }
}