import java.util.Stack;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;

public class ParkingLot {

    private class Spot {
        char type; // 'H' =  handicap, 'C' = compact, 'R' = regular
        int dist;

        public Spot() {
            type = 'N';
            dist = 0;
        }

        public Spot(char t, int d) {
            type = t;
            dist = d;
        }

        public char getType() {
            return type;
        }

        public int getDist() {
            return dist;
        }
    }

    Stack<Integer> handicapSpots;
    Stack<Integer> compactSpots;
    Stack<Integer> regularSpots;
    HashMap<String, Spot> parkedCars;

    public ParkingLot(Integer[] distToH, Integer[] distToC, Integer[] distToR) {
        handicapSpots = new Stack<>();
        compactSpots = new Stack<>();
        regularSpots = new Stack<>();
        parkedCars = new HashMap<>();

        // for each array of distances to (H, C, R) spots
        // sort the array in descending order
        // push distances (in descending order) to stack
        // such that stack has smallest distances at top, and largest distances at bottom
        Integer[] sorted = distToH;
        Arrays.sort(sorted, Collections.reverseOrder());
        for (Integer i : sorted) {
            handicapSpots.push(i);
        }
        sorted = distToC;
        Arrays.sort(sorted, Collections.reverseOrder());
        for (Integer i : sorted) {
            compactSpots.push(i);
        }
        sorted = distToR;
        Arrays.sort(sorted, Collections.reverseOrder());
        for (Integer i : sorted) {
            regularSpots.push(i);
        }
    }

    public boolean hasSpot(Car c) {
        switch(c.type()) {
            case 'H' : {return !handicapSpots.isEmpty();}
            case 'R' : {return !regularSpots.isEmpty();}
            case 'C' : {return !regularSpots.isEmpty() || !compactSpots.isEmpty();}
            default : { System.out.println("type is not H, R, or C: " + c.type()); return false;}
        }

        /*if (c.type() == 'H') {
            return !handicapSpots.isEmpty();
        } else if (c.type() == 'R') {
            return !regularSpots.isEmpty();
        } else if (c.type() == 'C') {
            return !regularSpots.isEmpty() || !compactSpots.isEmpty();
        } */
    }

    public boolean park(Car c) {
        if (!hasSpot(c)) {
            System.out.println("No spots available for " + c.ID());
            return false;
        } else {
            Spot s = new Spot();
            switch(c.type()) {
                case 'H' : {s = new Spot('H', handicapSpots.pop()); break;}
                case 'R' : {s = new Spot('R', regularSpots.pop()); break;}
                case 'C' : {
                    if (regularSpots.empty()) {
                        s = new Spot('C', compactSpots.pop());
                    } else if (compactSpots.empty()) {
                        s = new Spot('R', regularSpots.pop());
                    } else if (regularSpots.peek() < compactSpots.peek()) {
                        s = new Spot('R', regularSpots.pop());
                    } else {
                        s = new Spot('C', compactSpots.pop());
                    } break;
                }
            }
            parkedCars.put(c.ID(), s);
            System.out.println(c.ID() + " successfully parked at " + s.getType() + s.getDist());
            return true;

            /*if (c.type() == 'H') {
                parkedCars.put(c.ID(), new Spot('H', handicapSpots.pop()));
            } else if (c.type() == 'R') {
                parkedCars.put(c.ID(), new Spot('R', regularSpots.pop()));
            } else if (c.type() == 'C') {
                if (regularSpots.peek() < compactSpots.peek()) {
                    parkedCars.put(c.ID(), new Spot('R', regularSpots.pop()));
                } else {
                    parkedCars.put(c.ID(), new Spot('C', compactSpots.pop()));
                }
            }
            return true;*/
        }
    }

    public boolean leave(Car c) {
        if (!parkedCars.containsKey(c.ID())) {
            System.out.println(c.ID() + " is not in this parking lot");
            return false;
        } else {
            Spot s = parkedCars.remove(c.ID());
            switch(s.getType()) {
                case 'H' : {handicapSpots.push(s.getDist()); break;}
                case 'R' : {regularSpots.push(s.getDist()); break;}
                case 'C' : {compactSpots.push(s.getDist()); break;}
            }
            System.out.println(c.ID() + " has left " + s.getType() + s.getDist());
            return true;

            /*if (s.getType() == 'H') {
                handicapSpots.push(s.getDist());
            } else if (s.getType() == 'R') {
                regularSpots.push(s.getDist());
            } else if (s.getType() == 'C') {
                compactSpots.push(s.getDist());
            }
            return true;*/
        }
    }

    public static void main(String[] args) {
        Integer[] dtH = new Integer[]{1};
        Integer[] dtR = new Integer[]{2, 3};
        Integer[] dtC = new Integer[]{3};

        ParkingLot p = new ParkingLot(dtH, dtC, dtR);

        Car h1 = new Car("h1", 'H');
        Car h2 = new Car("h2", 'H');

        Car r1 = new Car("r1", 'R');
        Car r2 = new Car("r2", 'R');

        Car c1 = new Car("c1", 'C');
        Car c2 = new Car("c2", 'C');
        Car c3 = new Car("c3", 'C');

        p.park(h1);     // h1 successfully parked at H1
        p.park(h2);     // No spots available for h2

        p.park(r1);     // r1 successfully parked at R2
        p.park(c1);     // c1 successfully parked at C3
        p.park(c2);     // c2 successfully parked at R3
        p.park(r2);     // No spots available for r2

        p.leave(r2);    // r2 is not in this parking lot

        p.leave(h1);    // h1 has left H1
        p.park(r2);     // No spots available for r2

        p.leave(c1);    // c1 has left C3
        p.park(r2);     // No spots available for r2

        p.leave(r1);    // r1 has left R2
        p.park(r2);     // r2 successfully parked at R2
        p.leave(c2);    // c2 has left R3
        p.park(r1);     // r1 successfully parked at R3

        p.park(c2);     // c2 successfully parked at C3
        p.park(c1);     // No spots available for c1

    }
}
