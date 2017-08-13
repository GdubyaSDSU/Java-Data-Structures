/*  Gary Williams
    cssc0010
*/

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

@SuppressWarnings({"unchecked"})

public class LinearList<E> implements LinearListADT<E> {
	
	private int currentSize;
	private long modificationCounter;
	private Node<E> head, tail;
	
	public LinearList(){
		modificationCounter = currentSize = 0;
	}
	
	private static class Node<T>{
		private T data;
		private Node<T> prev;
		private Node<T> next;
		public Node(T e){
			data = e;
			prev = next = null;
		}
	}
    
//  Adds the Object obj to the beginning of list and returns true if the list is not full.
//  returns false and aborts the insertion if the list is full.
    public boolean addFirst(E obj){
    	Node<E> newNode = new Node<E>(obj);
    	if ( head == null)
    		head = tail = newNode;
    	else {
    		head.prev = newNode;
    		newNode.next = head;
    		head = newNode;
    	}
    	currentSize++;
    	modificationCounter++;
    	return true;
    }
    
//  Adds the Object obj to the end of list and returns true if the list is not full.
//  returns false and aborts the insertion if the list is full..  
    public boolean addLast(E obj){
    	Node<E> newNode = new Node<E>(obj);
    	if (head == null)
    		head = tail = newNode;
    	else {
    		tail.next = newNode;
    		newNode.prev = tail;
    		tail = newNode;
    	}
    	currentSize++;
    	modificationCounter++;
    	return true;
    }
    
//  Removes and returns the parameter object obj in first position in list if the list is not empty,  
//  null if the list is empty. 
    public E removeFirst(){
    	Node<E> tmp;
    	if(currentSize == 0)
    		return null;
    	else if (currentSize == 1){
    		tmp = head;
    		head = tail = null;
    	}
    	else {
    		tmp = head;
    		head.next.prev = null;
    		head = head.next;
    	}
    	removeCount();
    	return tmp.data;
    }
    
//  Removes and returns the parameter object obj in last position in list if the list is not empty, 
//  null if the list is empty. 
    public E removeLast(){
    	Node<E> tmp;
    	if(currentSize == 0)
    		return null;
    	else if (currentSize == 1){
    		tmp = tail;
    		tail = head = null;
    	}
    	else {
    		tmp = tail;
    		tail.prev.next = null;
    		tail = tail.prev;    		
    	}
    	removeCount();
    	return tmp.data;
    }
    
//  Removes and returns the parameter object obj from the list if the list contains it, null otherwise.
//  The ordering of the list is preserved.  The list may contain duplicate elements.  This method
//  removes and returns the first matching element found when traversing the list from first position.
//  Note that you may have to shift elements to fill in the slot where the deleted element was located.
    public E remove(E obj){
    	if(currentSize==0) return null;
    	Node<E> tmpHead = head;
    	while(tmpHead!=null){
    		if(((Comparable<E>)obj).compareTo(tmpHead.data)==0){
    			if(currentSize == 1){
    				head = tail = null;
    				removeCount();
    				return tmpHead.data;
    			}
    			else if(tmpHead == head){
    				head.next.prev = null;
    				head = head.next;
    				removeCount();
    				return tmpHead.data;
    			}
    			else if(tmpHead == tail){
    				tail.prev.next = null;
    				tail = tail.prev;
    				removeCount();
    				return tmpHead.data;
    			}
    			else {
    				tmpHead.prev.next = tmpHead.next;
    				tmpHead.next.prev = tmpHead.prev;
    				removeCount();
    				return tmpHead.data;
    			}
    		}
    		tmpHead = tmpHead.next;
    	}
    	
    	return null;
    }
    
//  Returns the first element in the list, null if the list is empty.
//  The list is not modified.
    public E peekFirst(){
    	if(currentSize == 0) return null;
    	return head.data;
    }
    
//  Returns the last element in the list, null if the list is empty.
//  The list is not modified.
    public E peekLast(){
    	if(currentSize == 0) return null;
    	return tail.data;
    }                     

//  Returns true if the parameter object obj is in the list, false otherwise.
//  The list is not modified.
    public boolean contains(E obj){
    	if(find(obj) == null)return false;
    	return true;
    }
    
//  Returns the element matching obj if it is in the list, null otherwise.
//  In the case of duplicates, this method returns the element closest to front.
//  The list is not modified.
    public E find(E obj){
    	Node<E> tmp = head;
    	while(tmp!=null){
    		if(((Comparable<E>)obj).compareTo(tmp.data)==0){
    			return tmp.data;
    		}
    		tmp = tmp.next;
    	}
    	return null;
    }  

//  The list is returned to an empty state.
    public void clear(){
    	head = tail = null;
    	modificationCounter = currentSize = 0;    	
    }

//  Returns true if the list is empty, otherwise false
    public boolean isEmpty(){
    	return (currentSize == 0);
    }
    
//  Returns true if the list is full, otherwise false
    public boolean isFull(){
    	return false;
    }  

//  Returns the number of Objects currently in the list.
    public int size(){
    	return currentSize;
    }
    
//  Returns an Iterator of the values in the list, presented in
//  the same order as the underlying order of the list. (front first, rear last)
    public Iterator<E> iterator(){
    	return new IteratorHelper();
    }
	private class IteratorHelper implements Iterator<E>{
		private Node<E> tmpHead;
		private int iterIndex;
		private long stateCheck;

		public IteratorHelper(){
			tmpHead = head;
			iterIndex = 0;
			stateCheck = modificationCounter;
		}
		
		public boolean hasNext() {
			if (stateCheck!=modificationCounter){
				throw new ConcurrentModificationException("Something changed before hasNext");}
			return tmpHead!=null;
		}

		public E next() {
			if (stateCheck!=modificationCounter){
				throw new ConcurrentModificationException("Something changed before next");}
			if (currentSize == 0){throw new NoSuchElementException();}
			E tmp = tmpHead.data;
			iterIndex++;
			tmpHead = tmpHead.next;
			return tmp;
		}
		
		public void remove(){
			if (stateCheck!=modificationCounter){
				throw new ConcurrentModificationException("Something changed before remove");}
			if (currentSize == 0)throw new NoSuchElementException();
			throw new UnsupportedOperationException("Not supported at this time. Use other remove/clear methods");
		}
		
	}
	
	//Private inner method to decrement the size and increase mod count
	private void removeCount(){
		currentSize--;
		modificationCounter++;
	}
	@Override
	public String toString() {
		return "LinearList [currentSize=" + currentSize + ", modificationCounter=" + modificationCounter + ", head="
				+ head + ", tail=" + tail + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentSize;
		result = prime * result + ((head == null) ? 0 : head.hashCode());
		result = prime * result + (int) (modificationCounter ^ (modificationCounter >>> 32));
		result = prime * result + ((tail == null) ? 0 : tail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinearList<E> other = (LinearList<E>) obj;
		if (currentSize != other.currentSize)
			return false;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		if (modificationCounter != other.modificationCounter)
			return false;
		if (tail == null) {
			if (other.tail != null)
				return false;
		} else if (!tail.equals(other.tail))
			return false;
		return true;
	}
    
}