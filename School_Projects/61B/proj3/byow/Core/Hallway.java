package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Hallway {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 10;

    private Random random;
    private int length;
    private int x;
    private int y;
    private Hallway incidentHallway;

    private enum Direction {
        North, East, South, West,
    }

    private Direction direction;

    public Hallway(int x1, int y1, int x2, int y2) {
        this.x = x1;
        this.y = y1;
        this.length = Math.abs(y2 - y1) + Math.abs(x2 - x1);
        if (x1 == x2) {
            if (y1 < y2) {
                this.direction = Direction.North;
            } else {
                this.direction = Direction.South;
            }
        } else {
            if (x1 < x2) {
                this.direction = Direction.East;
            } else {
                this.direction = Direction.West;
            }
        }
    }
    public Hallway(Random random) {
        this.random = random;
        this.x = RandomUtils.uniform(random, 11, Engine.WIDTH - 11);
        this.y = RandomUtils.uniform(random, 11, Engine.HEIGHT - 11);
        this.direction = Direction.values()[RandomUtils.uniform(this.random, 0, 4)];
        this.length = RandomUtils.uniform(this.random, MIN_LENGTH, MAX_LENGTH);
    }

    /*
    可以写helper method 但我这儿没用
    public void OutOfBoundHelper (Direction direction)
    */

    public Hallway(Random random, Hallway incidentHallway) {
        this.random = random;
        this.length = RandomUtils.uniform(this.random, MIN_LENGTH, MAX_LENGTH);
        this.direction = Direction.values()[RandomUtils.uniform(this.random, 0, 4)];
        this.x = incidentHallway.getXPos();
        this.y = incidentHallway.getYPos();
    }

    public void drawHallway(TETile[][] world) {
        drawHelper(world, this);
    }

    private void drawHelper(TETile[][] world, Hallway hallway) {
        switch (hallway.direction) {
            default:
                break;
            case North:
                for (int index = hallway.y; index < hallway.y + hallway.length; index++) {
                    world[hallway.x][index] = Tileset.GRASS;
                }
                break;
            case West:
                for (int index = hallway.x; index > hallway.x - hallway.length; index--) {
                    world[index][hallway.y] = Tileset.GRASS;
                }
                break;
            case South:
                for (int index = hallway.y; index > hallway.y - hallway.length; index--) {
                    world[hallway.x][index] = Tileset.GRASS;
                }
                break;
            case East:
                for (int index = hallway.x; index < hallway.x + hallway.length; index++) {
                    world[index][hallway.y] = Tileset.GRASS;
                }
                break;
        }
    }

    public void clear() {
        this.length = 0;
        this.direction = null;
        this.x = 0;
        this.y = 0;
    }
    public int getXPos() {
        if (this.direction.equals(Direction.West)) {
            return this.x - this.length;
        } else if (this.direction.equals(Direction.East)) {
            return this.x + this.length; 
        }
        return this.x;
    }

    public int getYPos() {
        if (this.direction.equals(Direction.South)) { 
            return this.y - this.length;
        } else if (this.direction.equals(Direction.North)) {
            return this.y + this.length;
        }
        return this.y;
    }

    public int getIncXPos() {
        return this.x;
    }

    public int getIncYPos() {
        return this.y;
    }

    public int getLength() {
        return this.length;
    }
    public Direction getDirection() {
        return this.direction;
    }
    public Boolean hallwayChecker(Hallway hallway) {
        if (hallway.direction == Direction.North) {
            if (hallway.getIncYPos() + hallway.getLength() > 28) {
                return false;
            }
        }
        if (hallway.direction == Direction.South) {
            if (hallway.getIncYPos() - hallway.getLength() < 1) {
                return false;
            }
        }
        if (hallway.direction == Direction.West) {
            if (hallway.getIncXPos() - hallway.getLength() < 1) {
                return false;
            }
        }
        if (hallway.direction == Direction.East) {
            if (hallway.getIncXPos() + hallway.getLength() > 79) {
                return false;
            }
        }
        return true;
    }
}
