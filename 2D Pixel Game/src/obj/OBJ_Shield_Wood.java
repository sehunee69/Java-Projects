package obj;

import entity.Entity;
import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Shield_Wood extends Entity {

    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);

        type = type_shield;
        name = "Wooden Shield";
        defenseValue = 2; // +2 Defense
        description = "[" + name + "]\nShield made of wood.\nDef +2.";
        
        try {
            // Ensure you have a shield image (or reuse another for testing)
            down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/tile190.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}