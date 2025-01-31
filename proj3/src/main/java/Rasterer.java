import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    /**
     * The root upper left/lower right longitudes and latitudes represent the bounding box of
     * the root tile.
     *
     * Longitude == x-axis and increases as you move East towards the Prime Meridian
     *      => X increases as longitude INCREASES
     * Latitude == y-axis and increases as you move North away from Equator
     *      => Y increases as latitude DECREASES
     *
     * (ullat = 37.892)  |-------------------------------------------|
     *                   |                                           |
     *                   |                                           |
     *                   |                                           |
     *                   |                                           |
     * (lrlat = 37.822)  |-------------------------------------------|
     *                   (ullon = -122.299)                          (lrlon = -122.211)
     */
    public static final double
            ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;

    /** Each tile is 256x256 pixels. */
    public static final int TILE_SIZE = 256;

    /** Reference array of lonDPP for each depth */
    private final double[] lonDPPatDepth = new double[8];

    public Rasterer() {

        /* Initialize lonDPP reference array */
        for (int i = 0; i < lonDPPatDepth.length; i++) {
            lonDPPatDepth[i] = longDistPerPixelAtDepth(i);
        }

    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println("\nDebug getMapRaster():");
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        int depth = correctDepthForQuery(params);
        System.out.println("depth: " + depth);

        int ulTileX = tileX(depth, params.get("ullon"), false);
        int ulTileY = tileY(depth, params.get("ullat"), false);
        System.out.println("UL: X = " + ulTileX + ", Y = " + ulTileY);

        int lrTileX = tileX(depth, params.get("lrlon"), true);
        int lrTileY = tileY(depth, params.get("lrlat"), true);
        System.out.println("LR: X = " + lrTileX + ", Y = " + lrTileY);

        String[][] rendergrid = renderGrid(depth, ulTileX, ulTileY, lrTileX, lrTileY);

        double rasterULlon = tileULlon(depth, ulTileX);
        double rasterULlat = tileULlat(depth, ulTileY);
        double rasterLRlon = tileLRlon(depth, lrTileX);
        double rasterLRlat = tileLRlat(depth, lrTileY);
        // Edge cases where query longitude or latitude is off map
        // addressed in tile_X and tile_Y functions

        boolean querySuccess = querySuccessful(params);

        results.put("render_grid", rendergrid);
        results.put("raster_ul_lon", rasterULlon);
        results.put("raster_ul_lat", rasterULlat);
        results.put("raster_lr_lon", rasterLRlon);
        results.put("raster_lr_lat", rasterLRlat);
        results.put("depth", depth);
        results.put("query_success", querySuccess);

        System.out.println("End Debug getMapRaster()\n");

        return results;
    }

    public String[][] renderGrid(int depth, int ulTileX, int ulTileY,
                                 int lrTileX, int lrTileY) {
        /*
        Example from project specification

        Inputs:
                            UL                      LR
        longitude   -122.30410170759153     -122.2104604264636
        latitude    37.870213571328854      37.8318576119893

        width: 1085 px, height: 566 px

        Expected output:

        [[d2_x0_y1.png, d2_x1_y1.png, d2_x2_y1.png, d2_x3_y1.png],
        [d2_x0_y2.png, d2_x1_y2.png, d2_x2_y2.png, d2_x3_y2.png],
        [d2_x0_y3.png, d2_x1_y3.png, d2_x2_y3.png, d2_x3_y3.png]]

        ... aka ...

        [Y = 1 ... X = 0, 1, 2, 3]
        [Y = 2 ... X = 0, 1, 2, 3]
        [Y = 3 ... X = 0, 1, 2, 3]

        Thus, Y should be first index and X should be the second index
         */
        System.out.println("\nDebug renderGrid:");

        int xSize = Math.abs(lrTileX - ulTileX + 1);
        System.out.println("X_size: " + xSize + " ul_X: " + ulTileX + " lr_X: " + lrTileX);

        int ySize = Math.abs(lrTileY - ulTileY + 1);
        System.out.println("Y_size: " + ySize + " ul_Y: " + ulTileY + " lr_Y: " + lrTileY);

        String[][] grid = new String[ySize][xSize];

        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                int Y = ulTileY + i;
                int X = ulTileX + j;
                grid[i][j] = "d" + depth + "_x" + X + "_y" + Y + ".png";
                System.out.print(grid[i][j] + " ");
            }
            System.out.println(" ");
        }
        System.out.println("End Debug renderGrid\n");

        return grid;
    }

    public boolean querySuccessful(Map<String, Double> params) {

        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");

        /*
            if query box is completely outside of the root long/lat
            OR query box doesn't make sense ex. UL is to the right
            of or below LR
            - raster_ul_lon, raster_ul_lat can be arbitrary
            - set query_success to false
         */
        if (lrlat >= ROOT_ULLAT || ullat <= ROOT_LRLAT
            || lrlon <= ROOT_ULLON || ullon >= ROOT_LRLON
            || lrlat >= ullat || lrlon <= ullon) {
            return false;
        }

        return true;
    }

    /**
     * Returns the latitudinal distance per tile at a given depth
     *
     * @param depth level of zoom/magnification
     *              depth ranges from 0 (lowest magnification) to 7 (highest magnification)
     * @return  latitudinal distance per tile at the [depth] level of zoom/magnification
     *          Conceptually, the fraction of the total map (bounded by ROOT_UL and ROOT_LR)
     *          at each depth is:
     *              d0 1
     *              d1 1/2
     *              d2 1/4
     *              d3 1/8
     *              d4 1/16
     *              d5 1/32
     *              d6 1/64
     *              d7 1/128
     *              ...
     *              dN 1/(2^N)
     */
    public static double latDistPerTileAtDepth(int depth) {
        return Math.abs(ROOT_ULLAT - ROOT_LRLAT) / Math.pow(2, depth);
    }

    /**
     * Returns the longitudinal distance per tile at a given depth
     *
     * @param depth level of zoom/magnification
     *              depth ranges from 0 (lowest magnification) to 7 (highest magnification)
     * @return  longitudinal distance per tile at the [depth] level of zoom/magnification
     *          Conceptually, the fraction of the total map (bounded by ROOT_UL and ROOT_LR)
     *          at each depth is:
     *              d0 1
     *              d1 1/2
     *              d2 1/4
     *              d3 1/8
     *              d4 1/16
     *              d5 1/32
     *              d6 1/64
     *              d7 1/128
     *              ...
     *              dN 1/(2^N)
     */
    public static double longDistPerTileAtDepth(int depth) {
        return Math.abs(ROOT_LRLON - ROOT_ULLON) / Math.pow(2, depth);
    }

    /**
     * Returns the longitudinal distance per pixel at a given depth
     *
     * @param depth level of zoom/magnification
     *              depth ranges from 0 (lowest magnification) to 7 (highest magnification)
     * @return  longitudinal distance per pixel at the [depth] level of zoom/magnification
     *          Each tile is TILE_SIZE pixels wide
     */
    public static double longDistPerPixelAtDepth(int depth) {
        return longDistPerTileAtDepth(depth) / TILE_SIZE;
    }

    /**
     * Returns the longitudinal distance per pixel of the query box
     *
     * @param params contains (ullon, ullat; lrlon, lrlat) which define the geographic area
     *               being queried
     *               as well as (w, h) which define the width and height of the window
     *               for displaying the geographic area
     * @return  longitudinal distance per pixel of the query box
     */
    public double queryLonDPP(Map<String, Double> params) {
        // params: double lrlon, double ullon, double lrlat, double ullat, double w
        double queryLongDist = params.get("lrlon") - params.get("ullon");
        double queryPixelWidth = params.get("w");
        return queryLongDist / queryPixelWidth;
    }

    /**
     * Returns the correct depth of zoom/magnification given the query parameters
     *
     * @param params contains (ullon, ullat; lrlon, lrlat) which define the geographic area
     *               being queried
     *               as well as (w, h) which define the width and height of the window
     *               for displaying the geographic area
     * @return  correct depth of zoom / magnification
     *          Must be <= the longitudinal distance per pixel of the query box
     */
    public int correctDepthForQuery(Map<String, Double> params) {
        double queryLonDPP = queryLonDPP(params);

        for (int i = 0; i < lonDPPatDepth.length; i++) {
            if (lonDPPatDepth[i] <= queryLonDPP) {
                return i;
            }
        }

        // if the queryLonDPP is smaller (aka higher resolution)
        // than lonDPP at greatest depth (highest map resolution),
        // return the highest map resolution available
        return lonDPPatDepth.length - 1;
    }

    /**
     * Returns the X-index of the tiles containing the given longitude at the given depth
     * @param depth depth
     * @param longitude longitude
     * @param isLowerRight isLowerRight
     * @return the appropriate X index. X index for "d0_x1_y2.png" is 1.
     */
    public int tileX(int depth, double longitude, boolean isLowerRight) {
        if (longitude <= ROOT_ULLON) {
            return 0;
        }
        if (longitude >= ROOT_LRLON) {
            return (int) (Math.pow(2, depth) - 1);
        }

        double ldpt = longDistPerTileAtDepth(depth);

        if (isLowerRight) {
            return (int) (Math.ceil(Math.abs(longitude - ROOT_ULLON) / ldpt) - 1);
        } else {
            return (int) (Math.floor(Math.abs(longitude - ROOT_ULLON) / ldpt));
        }
    }

    /**
     * Returns the Y-index of the tiles containing the given latitude at the given depth
     * @param depth depth
     * @param latitude latitude
     * @param isLowerRight isLowerRight
     * @return the appropriate Y index. Y index for "d0_x1_y2.png" is 2.
     */
    public int tileY(int depth, double latitude, boolean isLowerRight) {
        if (latitude <= ROOT_LRLAT) {
            return (int) (Math.pow(2, depth) - 1);
        }
        if (latitude >= ROOT_ULLAT) {
            return 0;
        }

        double ldpt = latDistPerTileAtDepth(depth);

        if (isLowerRight) {
            return (int) (Math.ceil(Math.abs(ROOT_ULLAT - latitude) / ldpt) - 1);
        } else {
            return (int) (Math.floor(Math.abs(ROOT_ULLAT - latitude) / ldpt));
        }
    }

    public static double tileULlon(int depth, int X) {
        return ROOT_ULLON + longDistPerTileAtDepth(depth) * X;
    }

    public static double tileULlat(int depth, int Y) {
        return ROOT_ULLAT - latDistPerTileAtDepth(depth) * Y;
    }

    public static double tileLRlon(int depth, int X) {
        return ROOT_ULLON + longDistPerTileAtDepth(depth) * (X + 1);
    }

    public static double tileLRlat(int depth, int Y) {
        return ROOT_ULLAT - latDistPerTileAtDepth(depth) * (Y + 1);
    }

    public static void testTileCalcs(int depth, int X, int Y) {
        System.out.println("tile_ul_lon: " + tileULlon(depth, X));
        System.out.println("tile_ul_lat: " + tileULlat(depth, Y));
        System.out.println("tile_lr_lon: " + tileLRlon(depth, X));
        System.out.println("tile_lr_lat: " + tileLRlat(depth, Y));
    }


    public static void main(String[] args) {

        // Given tile d?_x?_y?, test for correct ul_lon, ul_lat, lr_lon, lr_lat
        // by comparing against provided FileDisplayDemo.html
        System.out.println("Test ul_lon, ul_lat, lr_lon, lr_lat:");
        for (int i = 0; i < 8; i++) {
            System.out.println("d" + i + "_x" + i + "_y" + i + ":");
            testTileCalcs(i, i, i);
        }
        System.out.println("End Test \n");
        // passed! -> tile_ul_lon, tile_ul_lat, tile_lr_lon, tile_lr_lat work


        System.out.println("Test latDistPerTileAtDepth(depth) vs. FileDisplayDemo.html:");
        for (int i = 0; i < 8; i++) {
            System.out.println(latDistPerTileAtDepth(i));
        }
        System.out.println("\nEnd Test \n");
        // passed! -> latDistPerTileAtDepth(depth) works!

        Rasterer r = new Rasterer();

        Map<String, Double> testTwelveImagesParams = new HashMap<>();
        testTwelveImagesParams.put("lrlon", -122.2104604264636);
        testTwelveImagesParams.put("lrlat", 37.8318576119893);
        testTwelveImagesParams.put("ullon", -122.30410170759153);
        testTwelveImagesParams.put("ullat", 37.870213571328854);
        testTwelveImagesParams.put("w", 1091.0);
        testTwelveImagesParams.put("h", 566.0);

        Map<String, Object> testTwelveImagesResults = r.getMapRaster(testTwelveImagesParams);
        System.out.println("Expect: {"
                + "raster_ul_lon=-122.2998046875, depth=2, raster_lr_lon=-122.2119140625, "
                + "raster_lr_lat=37.82280243352756, raster_ul_lat=37.87484726881516, "
                + "query_success=true,  \n"
                + "render_grid=[\n"
                + "[d2_x0_y1.png, d2_x1_y1.png, d2_x2_y1.png, d2_x3_y1.png], \n"
                + "[d2_x0_y2.png, d2_x1_y2.png, d2_x2_y2.png, d2_x3_y2.png], \n"
                + "[d2_x0_y3.png, d2_x1_y3.png, d2_x2_y3.png, d2_x3_y3.png] \n"
                + "]}");
        System.out.println("Actual: " + testTwelveImagesResults);
        // passed!

        System.out.println("Testing test.html:");
        Map<String, Double> testParams = new HashMap<>();
        testParams.put("lrlon", -122.24053369025242);
        testParams.put("lrlat", 37.87548268822065);
        testParams.put("ullon", -122.24163047377972);
        testParams.put("ullat", 37.87655856892288);
        testParams.put("w", 892.0);
        testParams.put("h", 875.0);
        Map<String, Object> testResults = r.getMapRaster(testParams);
        System.out.println("Expect: {raster_ul_lon=-122.24212646484375, depth=7, "
                + "raster_lr_lon=-122.24006652832031, raster_lr_lat=37.87538940251607, "
                + "raster_ul_lat=37.87701580361881, query_success=true, "
                + "render_grid=[[d7_x84_y28.png, d7_x85_y28.png, d7_x86_y28.png], "
                + "[d7_x84_y29.png, d7_x85_y29.png, d7_x86_y29.png], "
                + "[d7_x84_y30.png, d7_x85_y30.png, d7_x86_y30.png]]}");
        System.out.println("Actual: " + testResults);
        // passed!
    }
}
