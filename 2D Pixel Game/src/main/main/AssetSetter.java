package main;

import entity.monster.MON_GreenSlime;

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
}