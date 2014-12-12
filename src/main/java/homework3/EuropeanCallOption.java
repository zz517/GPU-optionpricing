package homework3;

import java.util.*;
import java.util.List;
import org.joda.time.DateTime;

public class EuropeanCallOption implements PayOut{
	private double K;
	
	public EuropeanCallOption(double K){
		this.K = K;
	}

	@Override
	public double getPayout(StockPath path) {
		double[] prices = path.getPrices();
		return Math.max(0, prices[prices.length-1] - K);
	}

	@Override
	public double getPayoutList(StockPath path){
		List<Pair<DateTime,Double>> prices=path.getPath();
		double priceSt=prices.get(prices.size()-1).getValue();
		return Math.max(0, priceSt - K);
	}
}
