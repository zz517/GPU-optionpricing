package homework3;
import org.joda.time.DateTime;
import java.util.*;
/*
 * I used both methods to calculate prices. In these cases, the prices should be very close
 * These methods are similar in algorithms but used different data structure.
 * At the same time, it shows algorithm is correct. 
 *
 * I worte 3 methods for different gussian transformation.
 * However, they work differently. Sometime, they will cause out of resources Exception.
 * Where I didn't figure out why.
 * For example, in the main function, if I use batches = 1024*1024*2
 * The Exception will show. On the other hand, the Exception won't show and time to converge is extremely short.
 *
 */
public class Main {

	public static void main(String[] args) {
		double K=165;
		double rate=0.0001;
		int N=252;
		double sigma=0.01;
		double S0=152.35;
		double tolerance=0.01;
		double K1=164;
		DateTime startDate=new DateTime(1999,10,10,0,0);
		DateTime endDate=startDate.plusDays(252);
		int batches = 2000000;

		Simulator sm=new Simulator (N, rate ,S0, sigma, K, tolerance, "European",batches);
		Simulator sm1=new Simulator (N, rate ,S0, sigma, K1, tolerance, "Asian",batches);
		System.out.println("European Call Option Price by array: "+ sm.Expectation());
		System.out.println("Asian Call Option Price by array: "+ sm1.Expectation());
		
		SimulatorList sm2=new SimulatorList (N, rate ,S0, sigma, K, tolerance, startDate, endDate, "European",batches);
		SimulatorList sm3=new SimulatorList (N, rate ,S0, sigma, K1, tolerance, startDate, endDate,"Asian",batches);
		System.out.println("European Call Option Price by List: "+ sm2.Expectation());
		System.out.println("Asian Call Option Price by List: "+ sm3.Expectation());
	}	
}
