package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.HashSet;
import java.awt.Point;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /*
        Lab 5 Part 1 Notes: add 1 hexagon to world

        As a preliminary step towards addHexagon(..), I first wrote
        drawHexagon(TETile t, int s), which rendered a hexagon with
        side length s and made of TETile t.

        After testing and validating drawHexagon(..), I refactored it
        to generateHexagonArray(..) which returned a TETile[][] array
        containing the hexagon to be drawn.

        After calling generateHexagonArray(..), all addHexagon(..) had
        to do was copy the non-null elements of the generated hexagon
        array to the appropriate location in TETile[][] world as specified
        by x0, y0 which designate the bottom left corner of where the hexagon
        array would align with the world array.
    */
    /*
        Hexagon calculations:

        side length     height      width
        2               4           4
        3               6           7
        4               8           10
        5               10          13
        sl              sl x 2     sl + 2(sl-1)
    */

    public static int hexWidth(int s) {
        return s + 2 * (s - 1);
    }

    public static int hexHeight(int s) {
        return s * 2;
    }

    /*
        Calculations for drawing hexagon:

        Middle 2 rows are full
        1st middle row = height / 2 - 1 (subtract 1 for 0-based indexing)

        For each row you move away from middle, lose 1 tile from each side (offset += 1)
        # rows from middle to top/bottom is height / 2, aka s
    */
    /*
        s = 2
        0xx0
        xxxx
        xxxx
        0xx0

        s = 3
        00xxx00
        0xxxxx0
        xxxxxxx
        xxxxxxx
        0xxxxx0
        00xxx00

        s = 4
        000xxxx000
        00xxxxxx00
        0xxxxxxxx0
        xxxxxxxxxx
        xxxxxxxxxx
        0xxxxxxxx0
        00xxxxxx00
        000xxxx000
    */

    public static TETile[][] generateHexagonArray(TETile t, int s) {
        int height = hexHeight(s);
        int width = hexWidth(s);
        int offset  = 0;

        TETile[][] hexagon = new TETile[width][height];

        for (int i = height / 2 - 1; i >= 0; i--) {
            for (int j = offset; j < width - offset; j++) {
                hexagon[j][i] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
                hexagon[j][(height - 1) - i] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
            }
            offset++;
        }

        return hexagon;
    }

    public static void addHexagon(TETile[][] world, TETile t, int s, Point p) {
        int x0 = p.x;
        int y0 = p.y;

        //Generate hexagon array
        TETile[][] hexagon = generateHexagonArray(t, s);

        // Copy !null elements of hexagon array to world array
        int width = hexagon.length;
        int height = hexagon[0].length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (hexagon[x][y] != null) {
                    world[x0 + x][y0 + y] = hexagon[x][y];
                }
            }
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WATER;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.TREE;
            case 4: return Tileset.MOUNTAIN;
            default: return Tileset.MOUNTAIN;
        }
    }

    public static void addRandomHexagons(TETile[][] world, int s, HashSet<Point> pts) {
        for (Point p : pts) {
            addHexagon(world, randomTile(), s, p);
        }
    }

    public static HashSet<Point> getTesselationPoints(TETile[][] world, int s, Point p) {
        HashSet<Point> pts = new HashSet<>();
        return getTPRecursive(world, pts, s, p);
    }

    public static HashSet<Point> getTPRecursive(TETile[][] w, HashSet<Point> pts, int s, Point p) {
        if (!isNewValidPoint(w, pts, s, p)) {
            return pts;
        }

        pts.add(p);

        Point sw = neighborSW(p, s);
        Point se = neighborSE(p, s);
        Point nw = neighborNW(p, s);
        Point ne = neighborNE(p, s);

        pts = getTPRecursive(w, pts, s, sw);
        pts = getTPRecursive(w, pts, s, se);
        pts = getTPRecursive(w, pts, s, nw);
        pts = getTPRecursive(w, pts, s, ne);

        return pts;
    }

    public static boolean isNewValidPoint(TETile[][] world, HashSet<Point> pts, int s, Point p) {
        int worldW = world.length;
        int worldH = world[0].length;

        int hexW = hexWidth(s);
        int hexH = hexHeight(s);

        return !(p.x < 0 || (p.x + hexW) > worldW ||   // make sure hexagon fits in world (x)
                p.y < 0 || (p.y + hexH) > worldH ||    // make sure hexagon fits in world (y)
                pts.contains(p));                       // make sure p is not already in pts
    }

    /*
        Calculations for neighboring points for tesselation:

        Each point has 4 theoretic diagonal neighbors: SW, SE, NW, NE
            Theoretic b/c hexagon associated with neighbor may not fit in world

        Horizontal (x) distance to neighbor = (s + hexWidth(s))/2
        Vertical (y) distance to neighbor = s
     */
    public static Point neighborSW(Point p, int s) {
        int x = p.x - (s + hexWidth(s)) / 2;
        int y = p.y - s;

        Point n = new Point(x, y);
        return n;
    }

    public static Point neighborSE(Point p, int s) {
        int x = p.x + (s + hexWidth(s)) / 2;
        int y = p.y - s;

        Point n = new Point(x, y);
        return n;
    }

    public static Point neighborNW(Point p, int s) {
        int x = p.x - (s + hexWidth(s)) / 2;
        int y = p.y + s;

        Point n = new Point(x, y);
        return n;
    }

    public static Point neighborNE(Point p, int s) {
        int x = p.x + (s + hexWidth(s)) / 2;
        int y = p.y + s;

        Point n = new Point(x, y);
        return n;
    }

    public static void main(String[] args) {

        // Create world
        int w = 27;
        int h = 30;

        TERenderer ter = new TERenderer();
        ter.initialize(w, h);

        TETile[][] world = new TETile[w][h];
        for (int x = 0; x < w; x += 1) {
            for (int y = 0; y < h; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        int s = 3; // side length
        Point seed = new Point(10, 0);

        HashSet<Point> pts = getTesselationPoints(world, s, seed);

        Point sw = new Point(0, 0);
        Point se = new Point(20, 0);
        Point nw = new Point(0, 24);
        Point ne = new Point(20, 24);

        pts.remove(sw);
        pts.remove(se);
        pts.remove(nw);
        pts.remove(ne);

        System.out.println(pts.size());
        System.out.println(pts);

        addRandomHexagons(world, s, pts);

        ter.renderFrame(world);
    }
}
