package lab14;
import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int state, period, startOfPeriod, endOfPeriod;
    private double acceleration;

    public AcceleratingSawToothGenerator(int period, double acceleration) {
        state = 0;
        this.period = period;
        this.acceleration = acceleration;

        startOfPeriod = 0;
        endOfPeriod = period;
    }

    public double next() {

        double y = (state - startOfPeriod) * 2.0 / period - 1;

        if (state == endOfPeriod) {
            startOfPeriod = endOfPeriod + 1;
            period = (int) (period * acceleration);
            endOfPeriod = startOfPeriod + period;
        }

        state = state + 1;

        return y;
    }

}
