// package obj;

// import java.io.IOException;
// import javax.imageio.ImageIO;
// import entity.Entity;
// import main.GamePanel;

// public class OBJ_Water extends Entity {
    
//     public OBJ_Water(GamePanel gp) {
//         super(gp); // <--- THIS FIXES THE ERROR
        
//         name = "Water";
//         type = type_crafting;
//         description = "[Crafting Component]\nPure spring water.";
        
//         try {
//             down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/water_vial.png"));
//         } catch(IOException e) {
//             e.printStackTrace();
//         }
//     }
// }

package obj;

import entity.Entity;
import main.GamePanel;

public class OBJ_Water extends Entity {
    
    public OBJ_Water(GamePanel gp) {
        super(gp);
        
        name = "Water";
        // Ensure this variable name matches your Entity class (type_crafting_material or type_crafting)
        type = type_crafting; 
        description = "[Crafting Component]\nPure spring water.";
        
        // NO IMAGE YET: The item will be invisible.
        // down1 = setup("/obj/object/leaf"); // Use this if you want a temporary placeholder
    }
}