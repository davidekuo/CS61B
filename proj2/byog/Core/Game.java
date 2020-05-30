package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    private final Font titleFont = new Font("Monaco", Font.BOLD, 30);
    private final Font smallFont = new Font("Monaco", Font.BOLD, 15);
    private final Font hudFont = new Font("Monaco", Font.PLAIN, 12);

    private final TERenderer ter = new TERenderer();
    private RoomsHallwaysMap map;
    private TETile[][] world;
    private boolean gameIsActive;

    private void drawMainMenu() {
        StdDraw.clear(Color.BLACK);

        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2.0, HEIGHT * 3 / 4.0, "CS61B: THE GAME");

        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "New Game (N)");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 - 2, "Load Game (L)");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 - 4, "Quit Game (Q)");

        StdDraw.show();
    }

    private char getNextChar() {
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(100);
        }
        return StdDraw.nextKeyTyped();
    }

    private void readMainInput() {
        char input = getNextChar();
        if (input == 'n' || input == 'N') {
            drawSeedMenu();
            long s = readRandomSeed();
            playNewGame(s);
        } else if (input == 'l' || input == 'L') {
            loadGame();
            playSavedGame();
        } else if (input == 'q' || input == 'Q') {
            endGame();
        } else {
            readMainInput();
        }
    }

    private void drawSeedMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 + 1, "Enter Random Seed, then press 'S'");

        StdDraw.show();
    }

    private void drawInput(String in) {
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 - 1, in);
        StdDraw.show();
    }

    private long readRandomSeed() {
        String input = "";

        while (!StdDraw.isKeyPressed(83) || input.equals("")) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (Character.isDigit(c)) {
                    input += c;
                } else {
                    input = "";
                }
                drawSeedMenu();
                drawInput(input);
            }
        }
        return Long.parseLong(input);
    }

    private void saveGame() {
        try {
            FileOutputStream fOut = new FileOutputStream("savedGame.ser");
            ObjectOutputStream out = new ObjectOutputStream(fOut);

            out.writeObject(map);
            fOut.close();
            out.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void endGame() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "GAME ENDED");
        StdDraw.show();
        StdDraw.pause(500);
    }

    private void loadGame() {
        try {
            FileInputStream fIn = new FileInputStream("savedGame.ser");
            ObjectInputStream in = new ObjectInputStream(fIn);

            map = (RoomsHallwaysMap) in.readObject();
            fIn.close();
            in.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            playWithKeyboard();
        }
    }

    private class DrawWorld implements Runnable {
        public void run() {
            while (gameIsActive) {
                ter.renderFrame(world);

                int mX = (int) StdDraw.mouseX();
                int mY = (int) StdDraw.mouseY();

                String hud;
                if (mX < 0 || mX >= WIDTH
                        || mY < 0 || mY >= HEIGHT) {
                    hud = "";
                } else {
                    hud = world[mX][mY].description();
                }

                StdDraw.setFont(hudFont);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textRight(WIDTH - 1, 1, hud);
                StdDraw.show();
            }
        }
    }

    private void playNewGame(long seed) {
        map = new RoomsHallwaysMap(WIDTH, HEIGHT, seed);
        world = map.generateWorld(50);

        // clear keyboard buffer
        if (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
        playGame();
    }

    private void playSavedGame() {
        world = map.map;
        // clear keyboard buffer
        if (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
        playGame();
    }

    private void playGame() {
        gameIsActive = true;

        Thread hudThread = new Thread(new DrawWorld());
        hudThread.start();

        while (gameIsActive) {
            char c = getNextChar();
            processMove(c);
        }
        saveGame();
        endGame();
    }

    private void processMove(char c) {
        if (c == ':') {
            char next = getNextChar();
            if (next == 'q' || next == 'Q') {
                gameIsActive = false;
            }
        } else if (c == 'w' || c == 'W') {
            if (world[map.player.x][map.player.y + 1].description().equals("floor")) {
                world[map.player.x][map.player.y] = Tileset.FLOOR;
                world[map.player.x][map.player.y + 1] = Tileset.PLAYER;
                map.player.y += 1;
            }
        } else if (c == 'a' || c == 'A') {
            if (world[map.player.x - 1][map.player.y].description().equals("floor")) {
                world[map.player.x][map.player.y] = Tileset.FLOOR;
                world[map.player.x - 1][map.player.y] = Tileset.PLAYER;
                map.player.x -= 1;
            }
        } else if (c == 's' || c == 'S') {
            if (world[map.player.x][map.player.y - 1].description().equals("floor")) {
                world[map.player.x][map.player.y] = Tileset.FLOOR;
                world[map.player.x][map.player.y - 1] = Tileset.PLAYER;
                map.player.y -= 1;
            }
        } else if (c == 'd' || c == 'D') {
            if (world[map.player.x + 1][map.player.y].description().equals("floor")) {
                world[map.player.x][map.player.y] = Tileset.FLOOR;
                world[map.player.x + 1][map.player.y] = Tileset.PLAYER;
                map.player.x += 1;
            }
        }
    }


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        drawMainMenu();
        readMainInput();
        System.exit(0);
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        String s = input.toLowerCase();
        long seed;
        char[] moves;
        String quit;

        // Regex 1: n (####) s (wasd) (:q)
        String regexN = "^n([0-9]+)s([wasd]*)(:q)?$";
        Pattern pN = Pattern.compile(regexN);
        Matcher mN = pN.matcher(s);

        // Regex 2: l (wasd) (:q)
        String regexL = "^l([wasd]*)(:q)?$";
        Pattern pL = Pattern.compile(regexL);
        Matcher mL = pL.matcher(s);

        if (mN.matches()) {
            seed = Long.parseLong(mN.group(1));
            moves = mN.group(2).toCharArray();
            quit = mN.group(3);

            map = new RoomsHallwaysMap(WIDTH, HEIGHT, seed);
            world = map.generateWorld(50);
            // ter.renderFrame(world);
            // StdDraw.pause(1000);

            for (char m : moves) {
                processMove(m);
                // ter.renderFrame(world);
                // StdDraw.pause(500);
            }

            if (quit != null) {
                saveGame();
            }
        }
        if (mL.matches()) {
            moves = mL.group(1).toCharArray();
            quit = mL.group(2);

            loadGame();
            world = map.map;
            // ter.renderFrame(world);
            // StdDraw.pause(1000);

            for (char m : moves) {
                processMove(m);
                // ter.renderFrame(world);
                // StdDraw.pause(500);
            }

            if (quit != null) {
                saveGame();
            }
        }
        return world;
    }

    public static void main(String[] args) {

        Game g = new Game();

        /*
        // Test playWithInputString()
        // t1 should equal t3, t2 should equal t4
        TETile[][] t1 = g.playWithInputString("N999SDDDWWWDDD");
        TETile[][] t2 = g.playWithInputString("N999SDDD:Q");
        TETile[][] t3 = g.playWithInputString("LWWWDDD");
        TETile[][] t4 = g.playWithInputString("L:Q");
        */

        // Test playWithKeyboard()
        g.playWithKeyboard();
    }
}
