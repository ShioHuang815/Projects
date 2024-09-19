package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;

public class Room {
    private static final int MIN_SIZE = 5;
    private static final int MAX_SIZE = 10;

    private int width;
    private int height;
    private Random random;
    private Hallway incidentHallway;
    private int x;
    private int y;

    public Room(Random random) {
        this.random = random;
        this.x = RandomUtils.uniform(this.random, 1, Engine.WIDTH - Room.MIN_SIZE);
        this.y = RandomUtils.uniform(this.random, 1, Engine.HEIGHT - Room.MIN_SIZE);
        this.width = RandomUtils.uniform(this.random, MIN_SIZE, Math.min(Engine.WIDTH - this.x, MAX_SIZE + 1));
        this.height = RandomUtils.uniform(this.random, MIN_SIZE, Math.min(Engine.HEIGHT - this.y, MAX_SIZE + 1));
    }
    public Room(Random random, Hallway incidentHallway) {
        this.random = random;
        this.x = incidentHallway.getXPos();
        this.y = incidentHallway.getYPos();
        this.width = RandomUtils.uniform(this.random, Room.MIN_SIZE, Room.MAX_SIZE);
        this.height = RandomUtils.uniform(this.random, Room.MIN_SIZE, Room.MAX_SIZE);
    }

    public void drawRoom(TETile[][] world) {
        for (int n = this.x; n < this.x + this.width; n += 1) {
            for (int m = this.y; m < this.y + this.height; m += 1) {
                world[n][m] = Tileset.GRASS;
            }
        }
    }
    public void clear() {
        this.width = 0;
        this.height = 0;
        this.x = 0;
        this.y = 0;
    }
    public int getXPos() {
        return this.x;
    }

    public int getYPos() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
    public int getDis(Room room) {
        return Math.abs(this.x - room.x) + Math.abs(this.y - room.y);
    }

    public boolean checkCollision(Room room) {
        int inter = 0;
        if ((room.getXPos() <= this.getXPos() + 2
                && (room.getXPos() + room.getWidth()) >= this.getXPos() - 1)
                || (this.getXPos() <= room.getXPos() + 2 && (this.getXPos() + this.getWidth()) >= room.getXPos() - 1)) {
            inter += 1;
        }
        if ((room.getYPos() <= this.getYPos() + 2
                && (room.getYPos() + room.getHeight()) >= this.getYPos() - 1)
                || (this.getYPos() <= room.getYPos() + 2 && (this.getYPos() + this.getHeight()) >= room.getYPos() - 1)) {
            inter += 1;
        }
        if (inter == 2) {
            return true;
        }
        return false;
    }
}
