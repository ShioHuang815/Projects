package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Avatar {
    private int xPos;
    private int yPos;
    private TETile player = Tileset.AVATAR;
    private enum Direction {
        North, East, South, West,
    }
    private Direction direction;

    public Avatar(Room room) {
        xPos = room.getXPos();
        yPos = room.getYPos();
        direction = null;
    }
    public void changePos(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }
    public int getXpos() {
        return this.xPos;
    }
    public int getYpos() {
        return this.yPos;
    }
    public TETile getPlayer() {
        return this.player;
    }
    public Direction getDirection() {
        return this.direction;
    }
    public void playerDirection(char d) {
        switch (d) {
            default:
                break;
            case 'n':
                this.direction = Direction.North;
                break;
            case 's':
                this.direction = Direction.South;
                break;
            case 'w':
                this.direction = Direction.West;
                break;
            case 'e':
                this.direction = Direction.East;
                break;
        }
    }
}
