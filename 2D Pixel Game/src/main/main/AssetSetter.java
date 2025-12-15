package main;

import entity.monster.MON_GreenSlime;
import entity.monster.MON_Soldier;
import entity.npc.NPC_OldMan;
import entity.npc.NPC_Standing;
import obj.OBJ_Chest;
import obj.OBJ_HealingPotion;
import obj.OBJ_Shield_Metal;
import obj.OBJ_Shield_Wood;
import obj.OBJ_Water;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setMonster() {
    
        int mapNum[][][] = gp.tileM.mapTileNum;
        int map = 0;
        int i = 0; // Master index for Map 0 monsters

        // --- 1. SPAWN SLIMES (Map 0) ---
        int slimeCount = 0;
        int maxSlimes = 8;
        int attempts = 0;

        // Keep trying until we have 8 slimes or we tried 1000 times (safety break)
        while(slimeCount < maxSlimes && attempts < 1000) {
            
            // Pick a random spot anywhere on the map
            int col = new java.util.Random().nextInt(gp.maxWorldCol);
            int row = new java.util.Random().nextInt(gp.maxWorldRow);

            // Check if this random spot is GRASS (Tile 1)
            // AND ensure we haven't filled the array
            if(mapNum[map][col][row] == 1 && i < gp.monsters[map].length) {
                
                gp.monsters[map][i] = new MON_GreenSlime(gp);
                gp.monsters[map][i].worldX = col * gp.tileSize;
                gp.monsters[map][i].worldY = row * gp.tileSize;
                
                i++;
                slimeCount++;
            }
            attempts++;
        }

        // --- 2. SPAWN SOLDIERS (Map 0) ---
        int soldierCount = 0;
        int maxSoldiers = 1;
        attempts = 0;

        while(soldierCount < maxSoldiers && attempts < 1000) {
            
            int col = new java.util.Random().nextInt(gp.maxWorldCol);
            int row = new java.util.Random().nextInt(gp.maxWorldRow);

            // Check if this random spot is SAND/DIRT (Tile 19)
            if(mapNum[map][col][row] == 19 && i < gp.monsters[map].length) {
                
                gp.monsters[map][i] = new MON_Soldier(gp);
                gp.monsters[map][i].worldX = col * gp.tileSize;
                gp.monsters[map][i].worldY = row * gp.tileSize;
                
                i++;
                soldierCount++;
            }
            attempts++;
        }

        // --- 3. SPAWN SKELETONS (Map 1 - Dungeon) ---
        map = 1;
        i = 0; // Reset index for Map 1
        
        int skeletonCount = 0;
        int maxSkeletons = 10;
        attempts = 0;
        
        while(skeletonCount < maxSkeletons && attempts < 2000) {
            
            int col = new java.util.Random().nextInt(gp.maxWorldCol);
            int row = new java.util.Random().nextInt(gp.maxWorldRow);

            // Check if this random spot is DUNGEON FLOOR (e.g., Tile 1 or whatever you use inside)
            if(mapNum[map][col][row] == 19 && i < gp.monsters[map].length) {
                
                gp.monsters[map][i] = new entity.monster.MON_GreatSoldier(gp);
                gp.monsters[map][i].worldX = col * gp.tileSize;
                gp.monsters[map][i].worldY = row * gp.tileSize;
                
                i++;
                skeletonCount++;
            }
            attempts++;
        }
    }


    public void setObject() {

        int i = 0;
        int map = 0;

        gp.obj[map][i] = new obj.OBJ_Leaf(gp);
        gp.obj[map][i].worldX = gp.tileSize * 25;
        gp.obj[gp.currentMap][i].worldY = gp.tileSize * 25;
        i++;
        gp.obj[map][i] = new obj.OBJ_Leaf(gp);
        gp.obj[map][i].worldX = gp.tileSize * 28;
        gp.obj[map][i].worldY = gp.tileSize * 28;
        i++;
        gp.obj[map][i] = new OBJ_Water(gp);
        gp.obj[map][i].worldX = gp.tileSize * 26; 
        gp.obj[map][i].worldY = gp.tileSize * 26;
        i++;
        gp.obj[map][i] = new obj.OBJ_Chest(gp);
        gp.obj[map][i].worldX = gp.tileSize * 46;
        gp.obj[map][i].worldY = gp.tileSize * 23;
        // ADD THE SHIELD TO THIS SPECIFIC CHEST
        ((OBJ_Chest)gp.obj[map][i]).setLoot(new OBJ_Shield_Wood(gp));
        ((OBJ_Chest)gp.obj[map][i]).setLoot(new OBJ_HealingPotion(gp));
        ((OBJ_Chest)gp.obj[map][i]).setLoot(new OBJ_HealingPotion(gp));
        i++;

        // WORLD 1
        i = 0;
        map = 1;
        gp.obj[map][i] = new obj.OBJ_Chest(gp);
        gp.obj[map][i].worldX = gp.tileSize * 22;
        gp.obj[map][i].worldY = gp.tileSize * 23;

        ((OBJ_Chest)gp.obj[map][i]).setLoot(new OBJ_Shield_Metal(gp));
        ((OBJ_Chest)gp.obj[map][i]).setLoot(new OBJ_HealingPotion(gp));
        ((OBJ_Chest)gp.obj[map][i]).setLoot(new OBJ_HealingPotion(gp));
        i++;
    }

    public void setNPC() {

        int npcX = gp.tileSize * 33;
        int npcY = gp.tileSize * 8;
        // Spawn at col 21, row 21 (Change these to where you want him)
        gp.npc[0][0] = new NPC_OldMan(gp, npcX, npcY);
        gp.npc[0][0].worldX = npcX;
        gp.npc[0][0].worldY = npcY;

        gp.npc[0][1] = new NPC_Standing(gp);
        gp.npc[0][1].worldX = gp.tileSize * 15; // Set location here
        gp.npc[0][1].worldY = gp.tileSize * 15;

        gp.npc[1][0] = new entity.npc.NPC_Princess(gp);
        gp.npc[1][0].worldX = gp.tileSize * 14; 
        gp.npc[1][0].worldY = gp.tileSize * 25;

    }
}