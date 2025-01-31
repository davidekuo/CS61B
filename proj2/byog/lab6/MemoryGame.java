package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }
        int s = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, s);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //Generate random string of letters of length n
        String randomString = "";
        for (int i = 0; i < n; i++) {
            randomString += CHARACTERS[rand.nextInt(CHARACTERS.length)];
        }
        return randomString;
    }

    public void drawFrame(String s) {
        //Take the string and display it in the center of the screen
        StdDraw.clear(Color.BLACK);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.text(this.width / 2.0, this.height / 2.0, s);

        //If game is not over, display relevant game information at the top of the screen
        drawHeader();

        StdDraw.show();
    }

    public void drawHeader() {
        Font font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.textLeft(1, height - 1, "Round: " + round);
        StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
        StdDraw.line(0, height - 2, width, height - 2);

        if (playerTurn) {
            StdDraw.text(width / 2, height - 1, "Type!");
        } else {
            StdDraw.text(width / 2, height - 1, "Watch!");
        }
    }

    public void flashSequence(String letters) {
        //Display each character in letters, making sure to blank the screen between letters
        for (char c : letters.toCharArray()) {
            StdDraw.pause(500);
            drawFrame("" + c);
            StdDraw.pause(1000);
            drawFrame(" ");
        }
    }

    public String solicitNCharsInput(int n) {
        //Read n letters of player input
        String input = "";
        for (int i = 0; i < n; i++) {
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(100);
            }
            input += StdDraw.nextKeyTyped();
            drawFrame(input);
        }
        return input;
    }

    public void startGame() {
        //Set any relevant variables before the game starts
        round = 1;
        gameOver = false;
        playerTurn = false;

        //Establish Game loop
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round " + round + "!");
            StdDraw.pause(1000);

            String key = generateRandomString(round);
            flashSequence(key);
            playerTurn = true;
            drawFrame(" ");
            String input = solicitNCharsInput(round);

            if (key.compareTo(input) == 0) {
                round++;
                drawFrame("Correct! " + ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
                StdDraw.pause(1000);
            } else {
                gameOver = true;
                StdDraw.pause(1000);
                drawFrame("Game Over! You made it to Round " + round + "!");
            }
        }

    }
}
