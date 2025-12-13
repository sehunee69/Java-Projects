package main;

import entity.monster.MON_GreenSlime;
import obj.OBJ_Water;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setMonster() {
        
        gp.monsters[0] = new MON_GreenSlime(gp);
        gp.monsters[0].worldX = gp.tileSize * 23; // Set X coordinate
        gp.monsters[0].worldY = gp.tileSize * 36; // Set Y coordinate

        gp.monsters[1] = new MON_GreenSlime(gp);
        gp.monsters[1].worldX = gp.tileSize * 23;
        gp.monsters[1].worldY = gp.tileSize * 37;

    }

    public void setObject() {
        gp.obj[0] = new obj.OBJ_Leaf(gp);
        gp.obj[0].worldX = gp.tileSize * 25;
        gp.obj[0].worldY = gp.tileSize * 25;
        
        gp.obj[1] = new obj.OBJ_Leaf(gp);
        gp.obj[1].worldX = gp.tileSize * 28;
        gp.obj[1].worldY = gp.tileSize * 28;

        gp.obj[2] = new OBJ_Water(gp);
        gp.obj[2].worldX = gp.tileSize * 26; 
        gp.obj[2].worldY = gp.tileSize * 26;
    }
}