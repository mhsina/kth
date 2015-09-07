
import java.rmi.server.ExportException;
import java.text.DecimalFormat;
import java.util.*;

public class randomNumberGeneratorG22 {

    private int seed;
    private Random r;
    private DecimalFormat df = new DecimalFormat(".##");

    public randomNumberGeneratorG22(int seed) {
        this.seed = seed;
        r = new Random(seed);
    }

    public float getRndInterArrival() {     //Exponential distribution

        Double d = -Math.log(r.nextFloat()) / 0.48;
        return Float.valueOf(df.format(d));
    }

    public float getRndDuration() {     //Exponential distribution

        Double d = -Math.log(r.nextFloat()) / 0.005;
        return Float.valueOf(df.format(d));
    }

    public float getRndPosition() {        //Uniform Distribution
        float f = r.nextFloat() * 40;
        return Float.valueOf(df.format(f));
    }

    public float getRndSpeed() {     //Normal distribution
        double mean = 100.955;
        double standardDeviation = 20.376;
        double d = r.nextGaussian() * standardDeviation + mean;
        return Float.valueOf(df.format(d));
    }
}
