/*  Gary Williams
    cssc0010
*/
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class HashTable<K, V> implements DictionaryADT<K, V>{
	private int currentSize, tableSize;
	private long modCounter;
	private LinearListADT<DictionaryNode<K, V>> [] list;
	
	private class DictionaryNode<K, V> implements Comparable<DictionaryNode<K, V>>{
		K key;
		V value;
		
		public DictionaryNode(K key, V value){
			this.key = key;
			this.value = value;
		}
		
		public int compareTo(DictionaryNode<K, V> node){
			return ((Comparable<K>)key).compareTo((K)node.key);
		}
	}
	
	public HashTable(){
		currentSize = 0;
		modCounter = 0;
		tableSize = 16;
		list = new LinearList[tableSize];
		for(int i=0; i < tableSize; i++){
			list[i] = new LinearList<DictionaryNode<K, V>>();
		}
	}
	

	public boolean contains(K key) {
		return list[getHashCode(key)].contains(new DictionaryNode<K, V>(key, null));
	}
	
	public int getHashCode(K key){
		return (key.hashCode() & 0x7FFFFFFF) % tableSize;
	}

	public boolean add(K key, V value) {
		if(isFull()){
			return false;
		}
		if(list[getHashCode(key)].contains(new DictionaryNode<K, V>(key, null))){
			return false;
		}
		list[getHashCode(key)].addLast(new DictionaryNode<K, V>(key, value));
		currentSize++;
		//If our Load Factor hits 75%, double table size
		if((currentSize / tableSize) > 0.75){
			tableResize();
		}
		modCounter++;
		return true;
	}
	
	private void tableResize(){
		int tmpTableSize = tableSize << 1;
		LinearListADT<DictionaryNode<K, V>> [] tmpLLList = new LinearList[tmpTableSize];
		for(int i=0; i < tmpTableSize; i++){
			tmpLLList[i] = new LinearList<DictionaryNode<K, V>>();
		}
		for(int i=0; i < tableSize; i++){
			for(DictionaryNode<K, V> n : list[i]){
				tmpLLList[(n.key.hashCode() & 0x7FFFFFFF) % tmpTableSize].addLast(new DictionaryNode<K, V>(n.key, n.value));
			}
		}
		tableSize = tmpTableSize;
		list = tmpLLList;
	}

	public boolean delete(K key) {
		if(!list[getHashCode(key)].contains(new DictionaryNode<K, V>(key, null))){
			return false;
		}
		list[getHashCode(key)].remove(new DictionaryNode<K, V>(key, getValue(key)));
		currentSize--;
		modCounter++;
		return true;
	}

	public V getValue(K key) {
		DictionaryNode<K, V> tmp = list[getHashCode(key)].find(new DictionaryNode<K, V>(key, null));
		if (tmp == null){
			return null;
		}
		return tmp.value;
	}

	public K getKey(V value) {
		for(int i=0; i < tableSize; i++){
			for(DictionaryNode<K, V> n : list[i]){
				if(((Comparable<V>)value).compareTo((V)n.value) == 0){
					return (K)n.key;
				}
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
		currentSize = 0;
		modCounter++;
		tableSize = 16;
		list = new LinearList[tableSize];
		for(int i=0; i < tableSize; i++){
			list[i] = new LinearList<DictionaryNode<K, V>>();
		}		
	}
	
	abstract class IteratorHelper<E> implements Iterator<E>{
		protected DictionaryNode<K, V> [] nodes;
		protected int idx;
		protected long modCheck;
		
		public IteratorHelper(){
			nodes = new DictionaryNode[currentSize];
			idx = 0;
			int j = 0;
			modCheck = modCounter;
			for(int i=0; i < tableSize; i++){
				for(DictionaryNode n : list[i]){
					nodes[j++] = n;
				}
			}
			nodes = (DictionaryNode<K, V>[]) quickSort(nodes);
		}
		
		public boolean hasNext(){
			if(modCheck != modCounter){
				throw new ConcurrentModificationException();
			}
			return idx < currentSize;
		}
		
		public abstract E next();
		
		public void remove(){
			throw new UnsupportedOperationException();
		}
	}
	
	private static <E> E[] quickSort(E[] array){
		E[] on = array;
		E tmp = on[on.length - 1];
		on[on.length - 1] = on[on.length / 2];
		on[on.length / 2] = tmp;
		quickSort(0,on.length - 1,on);
		return on;
	}
	
	private static <E> void quickSort(int left, int right, E[] array){
		if(right - left <= 0){
			return;
		}
		E pivot = array[right];
		int partition = getPartition(left, right, pivot, array);
		quickSort(left, partition - 1, array);
		quickSort(partition + 1, right, array);
	}
	
	private static <E> int getPartition(int left, int right, E pivot, E[] array){
		int lPtr = left - 1;
		int rPtr = right;
		for(;;){
			while(((Comparable<E>)array[++lPtr]).compareTo((E)pivot) < 0 ){}
			while(rPtr > 0 && ((Comparable<E>)array[--rPtr]).compareTo((E)pivot) > 0){}
			if (lPtr >= rPtr){
				break;
			}
			else{
				swap(lPtr, rPtr, array);
			}
			
		}
		swap(lPtr, right, array);
		return lPtr;
	}
	
	private static <E> void swap(int one, int two, E[] array){
		E tmp = array[one];
		array[one] = array[two];
		array[two] = tmp;
	}

	public Iterator<K> keys() {
		return new KeyIteratorHelper();
	}
	
	private class KeyIteratorHelper<K> extends IteratorHelper<K>{
		public KeyIteratorHelper(){
			super();
		}
		
		public K next() {
			if(modCheck != modCounter){
				throw new ConcurrentModificationException();
			}
			if (!hasNext()){
				throw new NoSuchElementException();
			}
			return (K) nodes[idx++].key;
		}
	}

	public Iterator<V> values() {
		return new ValueIteratorHelper();
	}
	
	private class ValueIteratorHelper<V> extends IteratorHelper<V>{
		public ValueIteratorHelper(){
			super();
		}
		
		public V next(){
			if(modCheck != modCounter){
				throw new ConcurrentModificationException();
			}
			if (currentSize == 0){
				throw new NoSuchElementException();
			}
			return (V) nodes[idx++].value;
		}
	}

}
