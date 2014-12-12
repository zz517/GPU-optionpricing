/**
 * Created by Lunaic on 12/12/2014.
 */
import homework3.*;
import junit.framework.TestCase;

public class TestGPU extends TestCase {
    public void testGPU() {

        int n = 100000;

        int batch = 64 * 64;
        GPURandomNumberGenerator rvg = new GPURandomNumberGenerator(n, batch);

        double[] gussian = rvg.getVector();


        double tolernce = 0.01;

        StatsCollector stats = new StatsCollector();

        for (int i = 0; i < n; i++) {
            stats.add(gussian[i]);
        }


        System.out.println(stats.getMean());
        System.out.println(stats.getVariance());

        //Test if is following normal distribution
        assertTrue(gussian.length == n);
        assertEquals(0., stats.getMean(), tolernce);
        assertEquals(1.0, stats.getVariance(), tolernce);

    }
}
