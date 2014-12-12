package homework3;

public class Pair <K,V>{
	private K k;
	private V v;
	
	public Pair(K key, V value){
		k = key;
		v = value;
	}
	
	public K getKey(){ return k;}
	public V getValue(){ return v;}
}
