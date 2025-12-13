package obj;

import java.io.IOException;
import javax.imageio.ImageIO;
import entity.Entity;
import main.GamePanel;

public class OBJ_Water extends Entity {
    
    public OBJ_Water(GamePanel gp) {
        super(gp); // <--- THIS FIXES THE ERROR
        
        name = "Water";
        type = type_crafting;
        description = "[Crafting Component]\nPure spring water.";
        
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream("/obj/object/bluepotion.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

