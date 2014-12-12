package homework3;

import java.util.*;
import org.joda.time.DateTime;

public interface StockPath {
	public double[] getPrices();
	public List<Pair<DateTime,Double>> getPath();
	public double getLast(); // This was for me to test antithetic pattern, which I didn't understand at the beginning;
}
