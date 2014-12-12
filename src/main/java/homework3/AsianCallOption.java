package homework3;

import java.util.List;

import org.joda.time.DateTime;

public class AsianCallOption implements PayOut {

	private double K;
	
	public AsianCallOption(double K){
		this.K = K;
	}

	@Override
	public double getPayout(StockPath path) {
		double[] prices = path.getPrices();
		double mean=0;
		for (int i=0;i<prices.length;i++)
			mean += prices[i];
		mean = mean/prices.length;
		return Math.max(0, mean - K);
	}
	
	@Override
	public double getPayoutList(StockPath path){
		List<Pair<DateTime,Double>> prices=path.getPath();
		double mean=0;
		for (int i=0;i<prices.size();i++)
			mean += prices.get(i).getValue();
		mean = mean/prices.size();
		return Math.max(0, mean - K);
	}
}
