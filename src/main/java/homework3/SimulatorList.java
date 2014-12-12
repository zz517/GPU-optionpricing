package homework3;

import org.joda.time.DateTime;

import java.util.*;

public class SimulatorList {
	private int N;
	private double r;
	private DateTime startDate;
	private DateTime endDate;
	private double tolerance;
	RandomVectorGenerator rvg;
	StatsCollector stats=new StatsCollector();
	StockPath sp;
	private PayOut Payout;
	int count=0;
	
	public SimulatorList (int N, double r,double S0, double sigma, double K, double tolerance, DateTime startDate, 
			DateTime endDate, String Optiontype){
		this.r=r;
		this.tolerance=tolerance;
		this.N=N;
		
		RandomVectorGenerator rvg=new GussianRandomNumberGenerator(N);
		RandomVectorGenerator antirvg=new AntiTheticVectorGenerator(rvg);
		sp=new GBMPathGenerator(r, N, sigma, S0, startDate, 
				endDate, antirvg);
		if(Optiontype.equals("European"))
		Payout =new EuropeanCallOption(K);
		else if (Optiontype.equals("Asian"))
		Payout =new AsianCallOption(K);
		else
		System.out.println("Wrong Option type");
	}

	public SimulatorList (int N, double r,double S0, double sigma, double K, double tolerance, DateTime startDate,
						  DateTime endDate, String Optiontype, int numberOfBatches){
		this.r=r;
		this.tolerance=tolerance;
		this.N=N;

		RandomVectorGenerator rvg=new GPURandomNumberGenerator(N,numberOfBatches);
		RandomVectorGenerator antirvg=new AntiTheticVectorGenerator(rvg);
		sp=new GBMPathGenerator(r, N, sigma, S0, startDate,
				endDate, antirvg);
		if(Optiontype.equals("European"))
			Payout =new EuropeanCallOption(K);
		else if (Optiontype.equals("Asian"))
			Payout =new AsianCallOption(K);
		else
			System.out.println("Wrong Option type");
	}
	
	public double Expectation(){
		do{
			count++;
			stats.add(Payout.getPayoutList(sp));
			//System.out.println(stats.getError());
		}while(stats.getError()>tolerance||count<100);
			
		return (stats.getMean())*Math.exp(-r*N);
		
	}
	
}
