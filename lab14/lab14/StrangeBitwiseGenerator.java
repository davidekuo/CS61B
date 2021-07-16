package lab14;
import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period, state;

    public StrangeBitwiseGenerator(int period) {
        state = 0;
        this.period = period;
    }

    public double next() {
        state = state + 1;
        int weirdState = state & (state >> 7) % period;
        double normalized = (weirdState * 2.0 / period) - 1;
        return normalized;
    }

}
