package homework3;

import java.util.*;

import org.joda.time.DateTime;

public class GBMPathGenerator implements StockPath {
	private double rate;
	private double sigma;
	private double S0;
	private int N;
	private DateTime startDate;
	private DateTime endDate;
	private RandomVectorGenerator rvg;
	
	public GBMPathGenerator(double rate, int N, double sigma, double S0, DateTime startDate, 
			DateTime endDate, RandomVectorGenerator rvg){
		this.startDate = startDate;
		this.endDate = endDate;
		this.rate = rate;
		this.S0 = S0;
		this.sigma = sigma;
		this.N = N;
		this.rvg = rvg;
	}

	@Override
	public List<Pair<DateTime, Double>> getPath()  {
		if (startDate.getYear() == endDate.getYear()){
			double[] n = rvg.getVector();
			DateTime current = new DateTime(startDate.getMillis());
			long delta = (endDate.getDayOfYear() - startDate.getDayOfYear())/N;
			List<Pair<DateTime, Double>> path = new LinkedList<Pair<DateTime,Double>>();
			path.add(new Pair<DateTime, Double>(current, S0));
			for ( int i=1; i < N; ++i){
				current.plusDays((int) delta);
				path.add(new Pair<DateTime, Double>(current, 
						path.get(path.size()-1).getValue()*Math.exp((rate-sigma*sigma/2)+sigma * n[i-1])));
			}
			return path;
		}
		else if (startDate.getYear() < endDate.getYear()){
			double[] n = rvg.getVector();
			DateTime current = new DateTime(startDate.getMillis());
			long delta = (endDate.getDayOfYear() - startDate.getDayOfYear()+365*(endDate.getYear()-startDate.getYear()))/N;
			List<Pair<DateTime, Double>> path = new LinkedList<Pair<DateTime,Double>>();
			path.add(new Pair<DateTime, Double>(current, S0));
			for ( int i=1; i < N; ++i){
				current.plusDays((int) delta);
				path.add(new Pair<DateTime, Double>(current, 
						path.get(path.size()-1).getValue()*Math.exp((rate-sigma*sigma/2)+sigma * n[i-1])));
			}
			return path;
		}
			else {
				double[] n = rvg.getVector();
				DateTime current = new DateTime(startDate.getMillis());
				long delta = (endDate.getMillis() - startDate.getMillis())/N;
				List<Pair<DateTime, Double>> path = new LinkedList<Pair<DateTime,Double>>();
				path.add(new Pair<DateTime, Double>(current, S0));
				for ( int i=1; i < N; ++i){
					current.plusMillis((int) delta);
					path.add(new Pair<DateTime, Double>(current, 
							path.get(path.size()-1).getValue()*Math.exp((rate-sigma*sigma/2)+sigma * n[i-1])));
				}
				return path;
		}
	}

	@Override  // this is useless
	public double[] getPrices(){
		double[] n = rvg.getVector();
		double[] sp = new double[N];
		sp[0]=S0;
		for (int i=1;i<N;i++){
			sp[i]=sp[i-1]*Math.exp((rate-sigma*sigma/2)+sigma * n[i-1]);
		}
		return sp;
	}
	
    @Override
	public double getLast(){
		double x=rvg.getVector()[1];
		return x;
	}
}
