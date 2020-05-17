package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.ArrayList;
import java.awt.Point;

public class RoomsHallwaysMap {

    //private static final long SEED = 2873123;
    //private static final Random RANDOM = new Random(SEED);

    int width;
    int height;
    Random RANDOM;
    TETile[][] map;
    ArrayList<Room> rooms;
    ArrayList<Point> walls;

    public RoomsHallwaysMap(int w, int h, int s) {
        width = w;
        height = h;
        RANDOM = new Random(s);
        map = new TETile[width][height];
        rooms = new ArrayList<>();
        walls = new ArrayList<>();
    }

    private boolean hasRoomOverlap(Room newRoom) {
        for (Room r : rooms) {
            if (newRoom.intersect(r)) {
                return true;
            }
        }
        return false;
    }

    private Room generateRandomRoom() {
        int minRoomSize = 3;
        int roomWidth = RANDOM.nextInt(width / 5) + minRoomSize;
        int roomHeight = RANDOM.nextInt(height / 5) + minRoomSize;
        // minRoomSize to keep rooms from getting to small
        // divide by 5 to keep rooms from getting too large

        int llX = RANDOM.nextInt(width - roomWidth -2) + 2;
        int llY = RANDOM.nextInt(height - roomHeight -2) + 2;
        // - 2 + 2 to keep rooms away from map edges to leave space for walls

        Room newRoom = new Room(llX, llY, llX + roomWidth, llY + roomHeight);

        return newRoom;
    }

    private void addRoom(Room r) {
        for (int x = r.ll.x; x < r.ur.x; x++) {
            for (int y = r.ll.y; y < r.ur.y; y++) {
                map[x][y] = Tileset.FLOOR;
            }
        }

        if (rooms.size() > 0) {
            Room prevRoom = rooms.get(rooms.size() - 1);
            connectRooms(r, prevRoom);
        }

        rooms.add(r);
    }

    private void connectRooms(Room r1, Room r2) {
        if (RANDOM.nextInt(2) == 0) {
            makeHorizontalTunnel(r1.c.x, r2.c.x, r1.c.y);
            makeVerticalTunnel(r1.c.y, r2.c.y, r2.c.x);
        } else {
            makeVerticalTunnel(r1.c.y, r2.c.y, r1.c.x);
            makeHorizontalTunnel(r1.c.x, r2.c.x, r2.c.y);
        }
    }

    private void makeHorizontalTunnel(int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            map[x][y] = Tileset.FLOOR;
        }
    }

    private void makeVerticalTunnel(int y1, int y2, int x) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            map[x][y] = Tileset.FLOOR;
        }
    }

    public void addRandomRoom(int n) {
        for (int i = 0; i < n; i++) {
            Room r = generateRandomRoom();
            if (! hasRoomOverlap(r)) {
                addRoom(r);
            }
        }
    }

    private boolean nextToFloor(int x, int y) {
        for (Point n : validNeighbors(x, y)) {
            if (map[n.x][n.y] == Tileset.FLOOR) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Point> validNeighbors(int x, int y) {
        Point[] neighbors = new Point[] {
                new Point(x, y + 1),
                new Point(x, y - 1),
                new Point(x - 1, y),
                new Point(x + 1, y),
                new Point(x + 1, y + 1),
                new Point(x + 1, y - 1),
                new Point(x - 1, y + 1),
                new Point(x - 1, y - 1)
        };

        ArrayList<Point> valid = new ArrayList<>();

        for (Point p : neighbors) {
            if (p.x < 0 || p.x >= width || p.y < 0 || p.y >= height) {
                continue;
            } else {
                valid.add(p);
            }
        }

        return valid;
    }

    public void fillRest() {
        // Fill remainder of map
        // If map[x][y] is next to a floor tile, map[x][y] -> wall
        // Otherwise, map[x][y] -> nothing
        // Keep track of walls -> randomly turn one into a door
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] == null) {
                    if (nextToFloor(x,y)) {
                        map[x][y] = Tileset.WALL;
                        walls.add(new Point(x,y));
                    } else {
                        map[x][y] = Tileset.NOTHING;
                    }
                }
            }
        }
    }

    private boolean isValidDoor(int index) {
        Point wallPoint = walls.get(index);
        ArrayList<Point> neighbors = validNeighbors(wallPoint.x, wallPoint.y);
        int floorNeighbors = 0;
        for (Point p : neighbors) {
            if (map[p.x][p.y] == Tileset.FLOOR) {
                floorNeighbors += 1;
            }
        }
        return floorNeighbors == 3;
        // having 3 floor neighbors = part of straight h. or v. wall
        // and not a corner
    }

    private void addDoor() {
        int doorIndex = RANDOM.nextInt(walls.size());
        while (!isValidDoor(doorIndex)) {
            doorIndex = RANDOM.nextInt(walls.size());
        }
        Point doorPoint = walls.get(doorIndex);
        map[doorPoint.x][doorPoint.y] = Tileset.LOCKED_DOOR;
    }

    public TETile[][] generateWorld(int numRooms) {
        addRandomRoom(numRooms);
        fillRest();
        addDoor();
        return map;
    }

    public static void main(String[] args) {
        int W = 50;
        int H = 50;
        int S = 12345;
        int nRooms = 20;

        TERenderer ter = new TERenderer();
        ter.initialize(W, H);

        RoomsHallwaysMap m = new RoomsHallwaysMap(W, H, S);

        ter.renderFrame(m.generateWorld(nRooms));

    }

}

