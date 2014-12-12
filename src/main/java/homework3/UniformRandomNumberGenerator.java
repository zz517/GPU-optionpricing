package homework3;
import java.util.*;
/**
 * Created by Lunaic on 12/12/2014.
 */
public class UniformRandomNumberGenerator implements RandomVectorGenerator {
    private int n;

    public UniformRandomNumberGenerator(int n){
        this.n=n;
    }
    @Override
    public double[] getVector(){
        Random r = new Random();
        double[] vector = new double[n];
        for (int i=0;i<n;i++){
            vector[i]=r.nextDouble();
        }
        return vector;
    }
}
