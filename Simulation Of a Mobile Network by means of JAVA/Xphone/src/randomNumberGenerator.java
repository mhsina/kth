
import java.rmi.server.ExportException;
import java.text.DecimalFormat;
import java.util.*;

public class randomNumberGenerator {

    private int seed;
    private Random r;
    private DecimalFormat df = new DecimalFormat(".##");

    public randomNumberGenerator(int seed) {
        this.seed = seed;
        r = new Random(seed);
    }

    public float getRndInterArrival() {     //Exponential distribution

        Double d = -Math.log(r.nextFloat()) / 0.2;   //decrease landa will increase interarrival and decrease total number
        return Float.valueOf(df.format(d));
    }

    public float getRndPosition() {        //Uniform Distribution
        float f = r.nextFloat() * 40;
        return Float.valueOf(df.format(f));
    }

    public float getRndSpeed() {     //Normal distribution
        double mean = 100.0;
        double standardDeviation = 10.0;
        double d = r.nextGaussian() * standardDeviation + mean;
        return Float.valueOf(df.format(d));
    }

    public float getRndDuration() {     //Tringular distribution
        float mode = 200;
        float max = 800;
        float min = 0;
        float U = r.nextFloat();
        float F = (mode - min) / (max - min);
        if (U <= F) {
            return Float.valueOf(df.format(min + Math.sqrt(U * (max - min) * (mode - min))));
        } else {
            return Float.valueOf(df.format(max - Math.sqrt((1 - U) * (max - min) * (max - mode))));
        }
    }
}
