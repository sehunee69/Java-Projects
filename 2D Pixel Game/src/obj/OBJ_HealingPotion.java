package obj;

import java.io.IOException;
import javax.imageio.ImageIO;
import entity.Entity;
import main.GamePanel;

public class OBJ_HealingPotion extends Entity {
    
    public OBJ_HealingPotion(GamePanel gp) {
        super(gp); // <--- THIS FIXES THE ERROR

        name = "Red Potion";
        type = type_consumable; // Set to consumable
        description = "[Restores 1 Heart]\nA delicious red liquid.\nSmells like strawberries.";
        
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/redpotion.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

