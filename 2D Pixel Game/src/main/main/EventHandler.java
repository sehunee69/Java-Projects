package main;

import java.awt.Rectangle;

public class EventHandler {

    GamePanel gp;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        // The "trigger" area on a tile (usually small so you have to walk INTO the tile)
        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent() {
        // Check if player is on Map 0 at coordinates (25, 25) (Example spot)
        // If true, move to Map 1 at coordinates (10, 10)
        if(hit(0, 17, 22, "any") == true) {
            teleport(1, 21, 25);
        }

        // Return trip: Map 1 (10, 10) back to Map 0 (25, 25)
        else if(hit(1, 20, 25, "any") == true) {
            teleport(0, 16, 22);
        }
    }

    public boolean hit(int map, int col, int row, String reqDirection) {
        boolean hit = false;

        // Only check if we are on the correct map
        if(map == gp.currentMap) {
            
            // Get player's solid area position
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

            // Get event rect position
            eventRect.x = col * gp.tileSize + eventRect.x;
            eventRect.y = row * gp.tileSize + eventRect.y;

            // Check if they overlap
            if(gp.player.solidArea.intersects(eventRect)) {
                if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true;
                }
            }

            // Reset inputs
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect.x = eventRectDefaultX;
            eventRect.y = eventRectDefaultY;
        }

        return hit;
    }

    public void teleport(int map, int col, int row) {
        if(map != gp.currentMap) {
            
            gp.stopMusic(); // Silence the old track
            
            if(map == 0) {
                gp.playMusic(0); // Play Overworld Theme
            }
            else if(map == 1) {
                gp.playMusic(7); // Play New Dungeon Theme
            }
        }
        gp.currentMap = map;
        gp.player.worldX = gp.tileSize * col;
        gp.player.worldY = gp.tileSize * row;
        // Optional: gp.playSE(soundIndex) for teleport sound
    }
}