/*  Gary Williams
    cssc0010
*/
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinarySearchTree<K, V> implements DictionaryADT<K, V> {
	private class Node<K, V>{
		private K key;
		private V value;
		private Node<K, V> leftChild;
		private Node<K, V> rightChild;
		//private Node<K, V> parent;
		boolean isLeft;
		
		public 	Node(K key, V value){
			this.key = key;
			this.value = value;
			leftChild = rightChild  = null;
			isLeft = false;
		}
	}
	
	private Node<K, V> root;
	private int currentSize;
	private long modCounter;
	
	public BinarySearchTree(){
		root = null;
		currentSize = 0;
		modCounter = 0;
	}

	public boolean contains(K key) {
		return getValue(key) != null;
	}

	public boolean add(K key, V value) {
		if(contains(key)){
			return false;
		}
		if(root == null){
			root = new Node<K, V>(key, value);
		}
		else {
			insert(key, value, root, null, false);
		}
		currentSize++;
		modCounter++;
		return true;		
	}
	
	private boolean insert(K key, V value, Node<K, V> n, Node<K, V> parent, boolean isLeft){
		if(n == null){
			if(isLeft){
				parent.leftChild = new Node<K, V>(key, value);
			}
			else{
				parent.rightChild = new Node<K, V>(key, value);
			}
		}
		else if(((Comparable<K>)key).compareTo((K)n.key) < 0){
			insert(key, value, n.leftChild, n, true);
		}
		else{
			insert(key, value, n.rightChild, n, false);
		}
		return false;
	}

	public boolean delete(K key) {
		if(root == null){
			return false;
		}
		Node<K, V> tmpRoot = deleteIntern(key, root);
		if(tmpRoot == null && currentSize > 1){
			return false;
		}
		currentSize--;
		modCounter++;
		root = tmpRoot;
		return true;
	}
	
	private Node<K, V> deleteIntern(K key, Node<K, V> node){
		if(node == null){
			return null;
		}
		if(((Comparable<K>)key).compareTo(node.key) < 0){
			node.leftChild = deleteIntern(key, node.leftChild);
		}
		else if(((Comparable<K>)key).compareTo(node.key) > 0){
			node.rightChild = deleteIntern(key, node.rightChild);
		}
		else if(node.leftChild != null && node.rightChild != null){
			node.key = getMinVal(node.rightChild);
			node.rightChild = removeMinNode(node.rightChild);
		}
		else{
			node = (node.leftChild != null) ? node.leftChild : node.rightChild;
		}
		return node;
	}
	
	private Node<K, V> removeMinNode(Node<K, V> node){
		if(node == null){
			return null;
		}
		if(node.leftChild != null){
			node.leftChild = removeMinNode(node.leftChild);
			return node;
		}
		else{
			return node.rightChild;
		}
	}
	
	private K getMinVal(Node<K, V> node){
		if(node != null){
			while(node.leftChild != null){
				node = node.leftChild;
			}
		}
		return node.key;
	}

	public V getValue(K key) {
		return find(key, root);
	}
	
	private V find(K key, Node<K, V> n){
		if(n == null){
			return null;
		}
		if(((Comparable<K>)key).compareTo(n.key) < 0){
			return find(key, n.leftChild);
		}
		if(((Comparable<K>)key).compareTo(n.key) > 0){
			return find(key, n.rightChild);
		}
		return (V) n.value;
	}

	public K getKey(V value) {
		return preOrderKey(value, root);
	}
	
	private K preOrderKey(V value, Node<K, V> n){
		if(n != null){
			if(((Comparable<V>)value).compareTo(n.value) == 0){
				return n.key;
			}
			preOrderKey(value, n.leftChild);
			preOrderKey(value, n.rightChild);
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
		return root == null;
	}
	
	public void clear() {
		root = null;
		currentSize = 0;
		modCounter++;
	}

	public Iterator<K> keys() {
		return new KeyIteratorHelper<K>();
	}
	
	private class KeyIteratorHelper<K> extends IteratorHelper<K>{

		
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
		return new ValueIteratorHelper<V>();
	}
	
	private class ValueIteratorHelper<V> extends IteratorHelper<V>{

		public V next() {
			if(modCheck != modCounter){
				throw new ConcurrentModificationException();
			}
			if (!hasNext()){
				throw new NoSuchElementException();
			}
			return (V) nodes[idx++].value;
		}
		
	}
	
	abstract class IteratorHelper<E> implements Iterator<E>{
		protected Node<K, V> [] nodes;
		protected int idx, sortIdx;
		protected long modCheck;
		
		public IteratorHelper(){
			nodes = new Node[currentSize];
			idx = sortIdx = 0;
			modCheck = modCounter;
			inOrder(root);
		}
		
		private void inOrder(Node<K, V> n){
			if(n == null){
				return;
			}
			inOrder(n.leftChild);
			nodes[sortIdx++] = n;
			inOrder(n.rightChild);
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

}
