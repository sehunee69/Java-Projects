package obj;

import java.io.IOException;
import javax.imageio.ImageIO;
import entity.Entity;
import main.GamePanel;

public class OBJ_Leaf extends Entity {
    
    public OBJ_Leaf(GamePanel gp) {
        super(gp); // <--- THIS FIXES THE ERROR
        
        name = "Leaf";
        type = type_crafting; // Set to crafting type
        description = "[Crafting Component]\nA simple green leaf.\nCan be used for medicine.";
        
        // If you don't use the setup helper yet, use this:
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/leaf.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}