package homework3;

public class StatsCollector {
	
	private double meansquare;
	private double mean;
	private double variance;
	private double sumOfSquares;
	private double sum;
	private int count=0;
	
	public void add(double price) {
		count++;
		sum += price; 
		mean = sum/count;
		sumOfSquares += price*price;
		variance= sumOfSquares/count - mean*mean;
	}


	public double getVariance() {
		return variance;
	}

	public double getMean() {
		return mean;
	}
	
	public double getSD(){
		return Math.sqrt(variance);
	}
	
	public double getError(){
		return 2*Math.sqrt(variance)/Math.sqrt(count); // 96% CI
	}
}
