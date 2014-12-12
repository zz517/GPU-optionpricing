package homework3;

public class Simulator {
	
	private double r;
	private double tolerance;
	StatsCollector stats=new StatsCollector();
	private PayOut Payout;
	int N;
	StockPath sp;
	int count=0;



	public Simulator (int N, double r,double S0, double sigma, double K, double tolerance, String Optiontype){
		this.r=r;
		this.tolerance=tolerance;
		this.N=N;

		RandomVectorGenerator rvg=new GussianRandomNumberGenerator(N);
		RandomVectorGenerator antirvg=new AntiTheticVectorGenerator(rvg);
		sp=new stockpricepath(N,r,S0,sigma,antirvg);
		if(Optiontype.equals("European"))
		Payout =new EuropeanCallOption(K);
		else if (Optiontype.equals("Asian"))
		Payout =new AsianCallOption(K);
		else
		System.out.println("Wrong Option type");
	}

	public Simulator (int N, double r,double S0, double sigma, double K, double tolerance, String Optiontype, int numberOfBatches){
		this.r=r;
		this.tolerance=tolerance;
		this.N=N;
		RandomVectorGenerator rvg=new GPURandomNumberGenerator(N,numberOfBatches);
		RandomVectorGenerator antirvg=new AntiTheticVectorGenerator(rvg);
		sp=new stockpricepath(N,r,S0,sigma,antirvg);
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
			stats.add(Payout.getPayout(sp));
			//System.out.println(stats.getError());
		}while(stats.getError()>tolerance||count<100);
			
		return (stats.getMean())*Math.exp(-r*N);
		
	}

}
