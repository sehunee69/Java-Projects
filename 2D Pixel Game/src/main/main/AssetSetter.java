package main;

import entity.monster.MON_GreenSlime;
import entity.monster.MON_Soldier;
import entity.npc.NPC_OldMan;
import entity.npc.NPC_Standing;
import obj.OBJ_Water;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setMonster() {
    
    int mapNum[][] = gp.tileM.mapTileNum;
    int i = 0; // The index for the gp.monsters array
    int j = 0;
    
    // 1. SET YOUR LIMITS HERE
    int slimeCount = 0;   
    int maxSlimes = 8;
    int soldierCount = 0;
    int maxSoldiers = 1;
    
    for(int col = 0; col < gp.maxWorldCol; col++) {
        for(int row = 0; row < gp.maxWorldRow; row++) {

            // Check if we found the correct tile (19)
            if(mapNum[col][row] == 1) {

                // 2. CHECK THE LIMITS
                // Ensure we haven't hit the max slimes AND we have space in the array
                if(slimeCount < maxSlimes && i < gp.monsters.length) {

                    // 3. RANDOM CHANCE (Distribution)
                    // If we don't have this, the first 8 tiles found will get slimes.
                    // This 10% chance spreads them out over the map.
                    if(new java.util.Random().nextInt(100) < 10) {
                        
                        gp.monsters[i] = new MON_GreenSlime(gp);
                        gp.monsters[i].worldX = col * gp.tileSize;
                        gp.monsters[i].worldY = row * gp.tileSize;
                        
                        i++;            // Move to next array slot
                        slimeCount++;   // Count this slime
                    }
                }
            }

            if(mapNum[col][row] == 19) {

                // 2. CHECK THE LIMITS
                // Ensure we haven't hit the max slimes AND we have space in the array
                if(soldierCount < maxSoldiers && i < gp.monsters.length) {

                    // 3. RANDOM CHANCE (Distribution)
                    // If we don't have this, the first 8 tiles found will get slimes.
                    // This 10% chance spreads them out over the map.
                    if(new java.util.Random().nextInt(100) < 10) {
                        
                        gp.monsters[j] = new MON_Soldier(gp);
                        gp.monsters[j].worldX = col * gp.tileSize;
                        gp.monsters[j].worldY = row * gp.tileSize;
                        
                        j++;            // Move to next array slot
                        soldierCount++;   // Count this slime
                    }
                }
            }
        }
    }
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

        gp.obj[3] = new obj.OBJ_Chest(gp);
        gp.obj[3].worldX = gp.tileSize * 46;
        gp.obj[3].worldY = gp.tileSize * 23;
    }

    public void setNPC() {

        int npcX = gp.tileSize * 33;
        int npcY = gp.tileSize * 8;
        // Spawn at col 21, row 21 (Change these to where you want him)
        gp.npc[0] = new NPC_OldMan(gp, npcX, npcY);
        gp.npc[0].worldX = npcX;
        gp.npc[0].worldY = npcY;

        gp.npc[1] = new NPC_Standing(gp);
        gp.npc[1].worldX = gp.tileSize * 15; // Set location here
        gp.npc[1].worldY = gp.tileSize * 15;
    }
}