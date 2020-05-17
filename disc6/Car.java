public class Car {
    String id;
    char type; // 'H' =  handicap, 'C' = compact, 'R' = regular

    public Car(String s, char t) {
        id = s;
        type = t;
    }

    public String ID() {
        return id;
    }

    public char type() {
        return type;
    }
}
