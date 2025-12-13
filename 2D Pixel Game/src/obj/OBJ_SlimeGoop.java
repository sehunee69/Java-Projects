package obj;

import java.io.IOException;
import javax.imageio.ImageIO;
import entity.Entity;
import main.GamePanel;

public class OBJ_SlimeGoop extends Entity {
    
    public OBJ_SlimeGoop(GamePanel gp) {
        super(gp); // <--- THIS FIXES THE ERROR
        
        name = "Slime Goop";
        type = type_crafting;
        description = "[Crafting Component]\nSticky and gross.\nMight be useful later.";
        
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/slime_goop.png")); // Check your file name
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}