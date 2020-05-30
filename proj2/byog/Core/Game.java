package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game implements Serializable {

    private static final long SERIALVERSIONUID = 4L;

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    private Font titleFont = new Font("Monaco", Font.BOLD, 30);
    private Font smallFont = new Font("Monaco", Font.BOLD, 15);
    private Font hudFont = new Font("Monaco", Font.PLAIN, 12);

    private TERenderer ter = new TERenderer();
    private RoomsHallwaysMap map;
    private TETile[][] world;
    private boolean gameIsActive;

    public Game() {
        ter.initialize(WIDTH, HEIGHT);
    }

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
            int s = readRandomSeed();
            playNewGame(s);
        } else if (input == 'l' || input == 'L') {
            System.out.println("Attempting to load previous game");
            loadGame();
            playSavedGame();
        } else if (input == 'q' || input == 'Q') {
            System.out.println("Quit");
            endGame();
        } else {
            System.out.println("Input must be N, L, or Q.");
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

    private int readRandomSeed() {
        String input = "";

        while (!StdDraw.isKeyPressed(83) || input.equals("")) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (Character.isDigit(c)) {
                    input += c;
                } else {
                    System.out.println("Invalid entry. Starting over.");
                    input = "";
                }
                drawSeedMenu();
                drawInput(input);
            }
        }

        int seed = Integer.parseInt(input);
        return seed;
    }

    private void saveGame() {
        try {
            FileOutputStream fOut = new FileOutputStream("savedGame.ser");
            ObjectOutputStream out = new ObjectOutputStream(fOut);

            out.writeObject(map);
            fOut.close();
            out.close();
            System.out.println("Game saved.");
        } catch (java.io.IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        }
    }

    private void endGame() {
        saveGame();

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
            System.out.println("Previous game loaded.");

        } catch (java.io.IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
            playWithKeyboard();
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound Exception");
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

                String hud = "";
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

    private void playNewGame(int seed) {
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
            System.out.println(c);
            processMove(c);
        }

        endGame();
    }

    private void processMove(char c) {
        if (c == ':') {
            char next = getNextChar();
            if (next == 'q' || next == 'Q') {
                System.out.println("Quit");
                gameIsActive = false;
            }
        } else if (c == 'w' || c == 'W') {
            if (world[map.player.x][map.player.y + 1].description().equals("floor")) {
                world[map.player.x][map.player.y] = Tileset.FLOOR;
                world[map.player.x][map.player.y + 1] = Tileset.PLAYER;
                map.player.y += 1;
            } else {
                System.out.println("W: invalid move");
            }
        } else if (c == 'a' || c == 'A') {
            if (world[map.player.x - 1][map.player.y].description().equals("floor")) {
                world[map.player.x][map.player.y] = Tileset.FLOOR;
                world[map.player.x - 1][map.player.y] = Tileset.PLAYER;
                map.player.x -= 1;
            } else {
                System.out.println("A: invalid move");
            }
        } else if (c == 's' || c == 'S') {
            if (world[map.player.x][map.player.y - 1].description().equals("floor")) {
                world[map.player.x][map.player.y] = Tileset.FLOOR;
                world[map.player.x][map.player.y - 1] = Tileset.PLAYER;
                map.player.y -= 1;
            } else {
                System.out.println("S: invalid move");
            }
        } else if (c == 'd' || c == 'D') {
            if (world[map.player.x + 1][map.player.y].description().equals("floor")) {
                world[map.player.x][map.player.y] = Tileset.FLOOR;
                world[map.player.x + 1][map.player.y] = Tileset.PLAYER;
                map.player.x += 1;
            } else {
                System.out.println("D: invalid move");
            }
        } else {
            System.out.println(c + ": Must enter W, A, S, D, or :Q");
        }
    }


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        drawMainMenu();
        readMainInput();
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
        int seed;
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
            seed = Integer.parseInt(mN.group(1));
            moves = mN.group(2).toCharArray();
            quit = mN.group(3);

            map = new RoomsHallwaysMap(WIDTH, HEIGHT, seed);
            world = map.generateWorld(50);
            ter.renderFrame(world);
            // StdDraw.pause(1000);

            for (char m : moves) {
                processMove(m);
                // ter.renderFrame(world);
                // StdDraw.pause(500);
            }

            if (quit != null) {
                saveGame();
            }
        } else if (mL.matches()) {
            moves = mL.group(1).toCharArray();
            quit = mL.group(2);

            System.out.println(moves);
            System.out.println(quit);

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

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        Game g = new Game();

        // Test playWithInputString()
        //g.playWithInputString("N999SDDDWWWDDD");
        //g.playWithInputString("N999SDDD:Q");
        //g.playWithInputString("LWWWDDD");
        //g.playWithInputString("L:Q");

        // Test playWithKeyboard()
        g.playWithKeyboard();

        System.exit(0);

    }
}
