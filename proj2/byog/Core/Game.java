package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {

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
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        String s = input.toLowerCase();

        /* input string must:
            1) start with 'N' for New Game,
            2) be followed by a positive integer of any length,
            3) end with 'S' to denote the end of the seed integer
        */
        if (s.charAt(0) != 'n' ||
                s.charAt(s.length() - 1) != 's' ||
                !s.substring(1, input.length() - 1).matches("[0-9]+")) {
            throw new IllegalArgumentException("Input must be composed of 'N' + " +
                    "a positive integer + 'S'. Your input: " + input + " is invalid.");
        }

        int seed = Integer.parseInt(s.substring(1, s.length() - 1));

        RoomsHallwaysMap map = new RoomsHallwaysMap(WIDTH, HEIGHT, seed);
        TETile[][] finalWorldFrame = map.generateWorld(30);
        return finalWorldFrame;
    }

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        Game g = new Game();
        TETile[][] world = g.playWithInputString("N1234S");

        ter.renderFrame(world);

    }
}
