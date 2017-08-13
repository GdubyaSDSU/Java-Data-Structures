/*  Gary Williams
    cssc0010
*/
package data_structures;

import java.util.Iterator;
import java.util.TreeMap;

public class BalancedTree<K, V> implements DictionaryADT<K, V>{
	TreeMap<K, V> tm;
	private int currentSize;
	private long modCounter;
	
	public BalancedTree(){
		tm = new TreeMap<K, V>();
		currentSize = 0;
		modCounter = 0;
	}

	public boolean contains(K key) {
		return tm.containsKey(key);
	}

	public boolean add(K key, V value) {
		if(!contains(key)){
			V v = tm.put(key, value);
			if(v == null){
				currentSize++;
			}
			modCounter++;
			return true;
		}
		else{
			modCounter++;
			return false;
		}
	}

	public boolean delete(K key) {
		V tmp = tm.remove(key);
		if(tmp == null){
			return false;
		}
		currentSize--;
		modCounter++;
		return true;
	}

	public V getValue(K key) {
		if(tm.containsKey(key)){
			return (V) tm.get(key);
		}
		return null;
	}

	public K getKey(V value) {
		Iterator keyItr = tm.keySet().iterator();
		Iterator valueItr = tm.values().iterator();
		while(valueItr.hasNext() && keyItr.hasNext()){
			K nextKey = (K) keyItr.next();
			V nextValue = (V) valueItr.next();
			if(((Comparable<V>)value).compareTo(nextValue) == 0){
				return nextKey;
			}
		}
		return null;
	}

	public int size() {
		return currentSize;
	}

	public boolean isFull() {
		return false;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public void clear() {
		tm.clear();
		currentSize = 0;
		modCounter++;
	}

	public Iterator<K> keys() {
		if(tm != null){
			return tm.keySet().iterator();
		}
		return null;
	}
	
	public Iterator<V> values() {
		if(tm != null){
			return tm.values().iterator();
		}
		return null;
	}
	

}
