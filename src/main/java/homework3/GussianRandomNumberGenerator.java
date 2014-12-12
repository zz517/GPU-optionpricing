package homework3;

import java.util.*;

public class GussianRandomNumberGenerator implements RandomVectorGenerator{
	private int N;
	
	public GussianRandomNumberGenerator(int N){
		this.N = N;
	}
	
	@Override
	public double[] getVector() {
		double[] vector = new double[N];
		Random rnd= new Random();
		for ( int i = 0; i < vector.length; ++i){
			vector[i] = rnd.nextGaussian();
		}
		return vector;
	}
	
}
