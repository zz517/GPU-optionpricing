package homework3;

import java.util.*;
import java.util.List;
import org.joda.time.DateTime;

public class stockpricepath implements StockPath {
	
	private int N;
	private double r;
	private DateTime startDate;
	private DateTime endDate;
	private double S0;
	private double sigma;
	private RandomVectorGenerator rvg;
	
	public stockpricepath (int N, double r, double S0, double sigma, RandomVectorGenerator rvg){
		this.N=N;
		this.r=r;
		this.S0=S0;
		this.sigma=sigma;
		this.rvg=rvg;	
	}
	
	@Override
	public double[] getPrices(){
		double[] n = rvg.getVector();
		double[] sp = new double[N];
		sp[0]=S0;
		for (int i=1;i<N;i++){
			sp[i]=sp[i-1]*Math.exp((r-sigma*sigma/2)+sigma * n[i-1]);
		}
		return sp;
	}
	
    @Override
	public double getLast(){
		double x=rvg.getVector()[1];
		return x;
	}
    
    @Override //this is useless
    public List<Pair<DateTime,Double>> getPath(){
		double[] n = rvg.getVector();
		DateTime current = new DateTime(startDate.getMillis());
		long delta = (endDate.getMillis() - startDate.getMillis())/N;
		List<Pair<DateTime, Double>> path = new LinkedList<Pair<DateTime,Double>>();
		path.add(new Pair<DateTime, Double>(current, S0));
		for ( int i=1; i < N; ++i){
			current.plusMillis((int) delta);
			path.add(new Pair<DateTime, Double>(current, 
					path.get(path.size()-1).getValue()*Math.exp((r-sigma*sigma/2)+sigma * n[i-1])));
		}
		return path;
    }

}
