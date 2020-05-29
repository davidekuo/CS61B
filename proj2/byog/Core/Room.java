package byog.Core;

import java.awt.Point;
import java.io.Serializable;

public class Room implements Serializable {
    private static final long SERIALVERSIONUID = 5L;

    // Lower left corner of room
    Point ll;

    // Upper right corner of room
    Point ur;

    // Center
    Point c;

    public Room(int llx, int lly, int urx, int ury) {
        ll = new Point(llx, lly);
        ur = new Point(urx, ury);
        c = new Point((llx + urx) / 2, (lly + ury) / 2);
    }

    public boolean intersect(Room otherRoom) {
        return !(ll.x - 1  > otherRoom.ur.x
                || ll.y - 1 > otherRoom.ur.y
                || ur.x + 1 < otherRoom.ll.x
                || ur.y + 1 < otherRoom.ll.y);
        // easier to define no intersection than intersection
        // +/- 1 to ensure 1 element buffer around each room
        // to leave space for wall
    }
}
