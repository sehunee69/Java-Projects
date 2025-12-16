package obj;

import entity.Entity;
import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Shield_Metal extends Entity {

    public OBJ_Shield_Metal(GamePanel gp) {
        super(gp);

        type = type_shield;
        name = "Metal Shield";
        defenseValue = 4; // Stronger Defense (Wood is 2)
        description = "[" + name + "]\nA solid shield.\nDef +4.";
        
        try {
            // Ensure you have an image for this, or reuse the normal sword for now
            down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/tile191.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}