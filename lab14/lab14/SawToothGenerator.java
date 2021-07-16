package lab14;
import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int period, state;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }

    public double next() {
        state = state + 1;
        double y = state % period; // range (0,period-1)
        double normalized = y / period; // range (0,1)
        return normalized * 2 - 1; // range (0,2) -> (-1,1)
    }

}
