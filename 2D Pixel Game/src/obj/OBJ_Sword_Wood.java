package obj;

import entity.Entity;
import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Sword_Wood extends Entity {

    public OBJ_Sword_Wood(GamePanel gp) {
        super(gp);

        type = type_sword;
        name = "Wooden Sword";
        attackValue = 2; // Adds +2 to your Strength
        description = "[" + name + "]\nAn old wooden sword.\nAttack +2.";
        
        try {
            // Make sure you have this image in res/objects/
            down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/tile240.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}