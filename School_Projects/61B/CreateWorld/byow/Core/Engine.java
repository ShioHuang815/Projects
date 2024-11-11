package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;

import java.io.*;

import java.util.ArrayList;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int SIGHT = 5;

    private ArrayList<Room> roomList;
    private ArrayList<Hallway> hallwayList;
    private Random random;
    private TETile[][] world;
    private Avatar player;
    private Boolean initialized = false;
    private String commandLis = "";
    private Boolean startCommand = false;
    private Boolean closeLight = false;
    private Boolean _showframe = true;
    private int coinNumber = 0;
    private boolean chinese = false;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void setupWorld() {
        roomList = new ArrayList<>();
        hallwayList = new ArrayList<>();
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.WALL;
            }
        }
    }
    public void showFrame() {
        if (_showframe) {
            if (closeLight) {
                TETile[][] newWorld = new TETile[WIDTH][HEIGHT];
                for (int x = 0; x < WIDTH; x += 1) {
                    for (int y = 0; y < HEIGHT; y += 1) {
                        if (Math.abs(x - player.getXpos()) + Math.abs(y - player.getYpos()) > SIGHT) {
                            newWorld[x][y] = Tileset.NOTHING;
                        } else {
                            newWorld[x][y] = world[x][y];
                        }
                    }
                }
                ter.renderFrame(newWorld);
            } else {
                ter.renderFrame(world);
            }
        }
    }
    public boolean controlCommand(char f) {
        if (f == 'w'
                || f == 's'
                || f == 'a'
                || f == 'd'
                || f == 'W'
                || f == 'S'
                || f == 'A'
                || f == 'D') {
            commandLis += f;
            this.move(f);
        }
        if ((f == 'q' || f == 'Q') && (startCommand)) {
            save();
            
            return true;
        }
        if (f == 'l' || f == 'L') {
            commandLis += f;
            closeLight = !closeLight;
        }
        if (f == ':') {
            startCommand = true;
        } else {
            startCommand = false;
        }
        showFrame();
        return false;
    }

    public void interactWithKeyboard() {
        if (!initialized) {
            renderInitial();
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    char f = StdDraw.nextKeyTyped();
                    if (f == 'N' || f == 'n') {
                        getSeed(f);
                        ter.initialize(WIDTH, HEIGHT + 4, 0, 0);
                        showFrame();
                        break;
                    } else if (f == 'L' || f == 'l') {
                        load();
                        ter.initialize(WIDTH, HEIGHT + 4, 0, 0);
                        showFrame();
                        break;
                    } else if (f == 'C' || f == 'c') {
                        if (chinese) {
                            renderInitial();
                            chinese = false;
                        } else {
                            renderChineseInitial();
                            chinese = true;
                        }
                    } else if (f == 'Q' || f == 'q') {
                        System.exit(0);
                    }
                }
            }
        }
        while (true) {
            if (chinese) {
                this.hudInChinese();
            } else {
                this.hud();
            }
            if (StdDraw.hasNextKeyTyped()) {
                char f = StdDraw.nextKeyTyped();
                if (controlCommand(f)) {
                    System.exit(0);
                }
            }
            if (checkWins()) {
                if (chinese) {
                    renderChineseEnd();
                } else {
                    renderEnd();
                }
                break;
            }
        }
    }

    public void getSeed(char ch) {
        commandLis = "";
        commandLis += ch;
        if (chinese) {
            StdDraw.text(15, 11, "输入种子");
        } else {
            StdDraw.text(15, 11, "Enter Seed");
        }
        StdDraw.text(15, 10, commandLis);
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char f = StdDraw.nextKeyTyped();
                if (f >= '0' && f <= '9') {
                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.text(15, 10, commandLis);
                    commandLis += f;
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.text(15, 10, commandLis);
                    StdDraw.show();
                }
                if (f == 'S' || f == 's') {
                    commandLis += f;
                    break;
                }
            }
        }
        genWorld(commandLis);
        showFrame();
    }
    public void renderInitial() {
        StdDraw.setCanvasSize(30 * 16, 30 * 16);
        StdDraw.setXscale(0, 30);
        StdDraw.setYscale(0, 30);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 22);
        StdDraw.setFont(font);
        StdDraw.text(15, 22, "CS61B: Coin Picker");
        Font font1 = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font1);
        StdDraw.text(15, 15, "New Game (N)");
        StdDraw.text(15, 14, "Load Game (L)");
        StdDraw.text(15, 13, "Language(C)");
        StdDraw.text(15, 12, "Quit (Q)");
        StdDraw.show();
    }
    public void renderChineseInitial() {
        StdDraw.setCanvasSize(30 * 16, 30 * 16);
        StdDraw.setXscale(0, 30);
        StdDraw.setYscale(0, 30);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 22);
        StdDraw.setFont(font);
        StdDraw.text(15, 22, "CS61B: 吃豆人儿");
        Font font1 = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font1);
        StdDraw.text(15, 15, "新游戏 (N)");
        StdDraw.text(15, 14, "加载游戏 (L)");
        StdDraw.text(15, 13, "语言(C)");
        StdDraw.text(15, 12, "退出 (Q)");
        StdDraw.show();
    }
    public void renderEnd() {
        StdDraw.setCanvasSize(30 * 16, 30 * 16);
        StdDraw.setXscale(0, 30);
        StdDraw.setYscale(0, 30);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 22);
        StdDraw.setFont(font);
        StdDraw.text(15, 15, "You Win!");
        Font font1 = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font1);
        StdDraw.text(15, 10, "Next Round?");
        StdDraw.show();
    }
    public void renderChineseEnd() {
        StdDraw.setCanvasSize(30 * 16, 30 * 16);
        StdDraw.setXscale(0, 30);
        StdDraw.setYscale(0, 30);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 22);
        StdDraw.setFont(font);
        StdDraw.text(15, 15, "你赢了!");
        Font font1 = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font1);
        StdDraw.text(15, 10, "再来一把吗小老弟?");
        StdDraw.show();
    }
    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        int st = 0;
        for (; input.charAt(st) != 'n' && input.charAt(st) != 'N' && input.charAt(st) != 'L' && input.charAt(st) != 'l' && st < input.length(); st++);
        int en = st;
        _showframe = false;
        if (input.charAt(st) == 'L' || input.charAt(st) == 'l') {
            load();
            _showframe = false;
        } else {
            for (; input.charAt(en) != 's' && input.charAt(en) != 'S' && en < input.length(); en++) ;
            commandLis = input.substring(st, en + 1);
            genWorld(commandLis);
        }
        for (en++; en < input.length(); en++) {
            if (controlCommand(input.charAt(en))) {
                return world;
            }
        }
        _showframe = true;
        return world;
    }

    public void genWorld(String input) {
        Long seed = Long.parseLong(input.substring(1, input.length() - 1));
        random = new Random(seed);
        setupWorld();

        int roomTotal = RandomUtils.uniform(random, 10) + 15;
        addRooms(roomTotal);
        for (Room i : roomList) {
            i.drawRoom(world);
        }
        addHallways();
        for (Hallway i : hallwayList) {
            i.drawHallway(world);
        }
        deleteFakeWall(world);
        addPlayer();
        addCoins();
        initialized = true;
        return;
    }
    public Boolean checkRoomsCollision(Room room) {
        for (Room temp : roomList) {
            if (room.checkCollision(temp)) {
                return true;
            }
        }
        return false;
    }

    public void addRooms(int roomTotal) {
        for (int i = 0; i < roomTotal; i++) {
            Room temp = new Room(random);
            int cnt;
            for (cnt = 0; checkRoomsCollision(temp) && cnt < 100; cnt++) {
                temp = new Room(random);
            }
            if (cnt < 100) {
                roomList.add(temp);
            }
        }
    }

    public void addHallway(Room room1, Room room2) {
        int x1 = RandomUtils.uniform(random, room1.getXPos(), room1.getXPos() + room1.getWidth());
        int y1 = RandomUtils.uniform(random, room1.getYPos(), room1.getYPos() + room1.getHeight());
        int x2 = RandomUtils.uniform(random, room2.getXPos(), room2.getXPos() + room2.getWidth());
        int y2 = RandomUtils.uniform(random, room2.getYPos(), room2.getYPos() + room2.getHeight());
        if (x1 == x2 || y1 == y2) {
            hallwayList.add(new Hallway(x1, y1, x2, y2));
            return;
        }
        int t = RandomUtils.uniform(random, 2);
        if (t == 0) {
            hallwayList.add(new Hallway(x1, y1, x1, y2));
            hallwayList.add(new Hallway(x1, y2, x2, y2));
        } else {
            hallwayList.add(new Hallway(x1, y1, x2, y1));
            hallwayList.add(new Hallway(x2, y1, x2, y2));
        }
    }
    public void addHallways() {
        int n = roomList.size();
        int[] dis = new int[n];
        for (int i = 1; i < n; i++) {
            dis[i] = 1000000;
        }
        int now = 0, next = 0;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (dis[j] > 0) {
                    if (dis[j] > dis[now] + roomList.get(now).getDis(roomList.get(j))) {
                        dis[j] = dis[now] + roomList.get(now).getDis(roomList.get(j));
                    }
                    if (dis[j] < dis[next] || dis[next] == 0) {
                        next = j;
                    }
                }
            }
            int minDis = 100000, argminDis = 0;

            for (int j = 0; j < n; j++) {
                if (dis[j] == 0 && roomList.get(j).getDis(roomList.get(next)) < minDis) {
                    minDis = roomList.get(j).getDis(roomList.get(next));
                    argminDis = j;
                }
            }
            addHallway(roomList.get(argminDis), roomList.get(next));
            dis[next] = 0;
            now = next;
        }
    }
    public boolean checkWall(int x, int y, TETile[][] w) {
        for (int i = -1; i <= 1; i += 1) {
            for (int j = -1; j <= 1; j += 1) {
                int X = x + i, Y = y + j;
                if (X < 0 || X >= WIDTH || Y < 0 || Y >= HEIGHT) {
                    continue;
                }
                if (w[X][Y].equals(Tileset.GRASS)) {
                    return true;
                }
            }
        }
        return false;
    }
    public void deleteFakeWall(TETile[][] w) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (!checkWall(x, y, w)) {
                    w[x][y] = Tileset.NOTHING;
                }
            }
        }
    }
    public void swap(Avatar p, int moveX, int moveY) {
        TETile temp = this.world[moveX][moveY];
        if (temp != Tileset.WALL) {
            if (temp == Tileset.SAND) {
                coinNumber--;
            }
            this.world[moveX][moveY] = p.getPlayer();
            this.world[p.getXpos()][p.getYpos()] = Tileset.GRASS;
            this.player.changePos(moveX, moveY);
        }
    }
    public void move(char direction) {
        if (direction >= 'A' && direction <= 'Z') {
            direction += 'a' - 'A';
        }
        player.playerDirection(direction);
        switch (direction) {
            default:
                break;
            case 'w':
                swap(this.player, this.player.getXpos(), this.player.getYpos() + 1);
                break;
            case 's':
                swap(this.player, this.player.getXpos(), this.player.getYpos() - 1);
                break;
            case 'a':
                swap(this.player, this.player.getXpos() - 1, this.player.getYpos());
                break;
            case 'd':
                swap(this.player, this.player.getXpos() + 1, this.player.getYpos());
                break;
        }
    }
    public void addPlayer() {
        int i = RandomUtils.uniform(random, roomList.size());
        this.player = new Avatar(roomList.get(i));
        this.world[roomList.get(i).getXPos()][roomList.get(i).getYPos()] = this.player.getPlayer();
    }
    public void addCoins() {
        for (Room i:roomList) {
            int x = RandomUtils.uniform(random, i.getXPos(), i.getXPos() + i.getWidth());
            int y = RandomUtils.uniform(random, i.getYPos(), i.getYPos() + i.getHeight());
            this.world[x][y] = Tileset.SAND;
            coinNumber++;
        }
    }
    public void load() {
        interactWithInputString(Utils.readContentsAsString(new File("savefile.txt")));
    }
    public void save() {
        Utils.writeContents(new File("savefile.txt"), commandLis);
    }

    public boolean checkWins() {
        if (coinNumber == 0) {
            return true;
        }
        return false;
    }
    public void hud() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        StdDraw.clear(Color.BLACK);
        showFrame();
        if (mouseX < WIDTH && mouseY < HEIGHT) {
            if (this.world[mouseX][mouseY] == Tileset.WALL) {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(2, Engine.HEIGHT + 2, "This is a Wall, you cannot pass it.");
            } else if (this.world[mouseX][mouseY] == Tileset.GRASS) {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(2, Engine.HEIGHT + 2, "This is a Grass-like Floor!");
            } else if (this.world[mouseX][mouseY] == Tileset.SAND) {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(2, Engine.HEIGHT + 2, "A Coin! Who doesn't wanna be rich?");
            } else {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(2, Engine.HEIGHT + 2, "Don't think about the Dark Area outside "
                        + "of the wall, focus on the game！");
            }
        }
        StdDraw.textRight(70, Engine.HEIGHT + 2, "GO PICK UP ALL THE COINS! WHERE ARE U LOOKING AT?");
        StdDraw.show();
    }
    public void hudInChinese() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        StdDraw.clear(Color.BLACK);
        showFrame();
        if (mouseX < WIDTH && mouseY < HEIGHT) {
            if (this.world[mouseX][mouseY] == Tileset.WALL) {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(2, Engine.HEIGHT + 2, "别去撞墙");
            } else if (this.world[mouseX][mouseY] == Tileset.GRASS) {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(2, Engine.HEIGHT + 2, "这是地板（草）");
            } else if (this.world[mouseX][mouseY] == Tileset.SAND) {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(2, Engine.HEIGHT + 2, "把金币捡起来，你难道没玩过吃豆人吗？");
            } else {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(2, Engine.HEIGHT + 2, "墙外之地别想了，专心游戏！");
            }
        }
        StdDraw.textRight(70, Engine.HEIGHT + 2, "别想墙外的黑暗宇宙，专注游戏！");
        StdDraw.show();
    }
}
