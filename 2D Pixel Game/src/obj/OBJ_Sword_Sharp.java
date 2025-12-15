package obj;

import entity.Entity;
import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Sword_Sharp extends Entity {

    public OBJ_Sword_Sharp(GamePanel gp) {
        super(gp);

        type = type_sword;
        name = "Sharp Sword";
        attackValue = 10; // High Damage!
        description = "[" + name + "]\nA blade so sharp it\ncuts through iron.\nAtk +10.";
        
        try {
            // Ensure you have an image for this, or reuse the normal sword for now
            down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/tile241.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}