/*  Gary Williams
    cssc0010
*/
package data_structures;

import java.util.Iterator;
import java.lang.Math;
import java.util.NoSuchElementException;

@SuppressWarnings({"unchecked"})

public class ArrayLinearList<E> implements LinearListADT<E>{
	
    private int front, rear, max, currentSize, rmIndex;
    private long modificationCounter;
    private E [] storage;
	
	public ArrayLinearList(){
		this(DEFAULT_MAX_CAPACITY);
	}
	
	public ArrayLinearList(int setMaxCapacity){
		max = setMaxCapacity;
		rear = front = currentSize = 0;
		modificationCounter = 0;
		storage = (E[]) new Object [max];
	}
	
	//Returns an Iterator of the values in the list, presented in
	//the same order as the underlying order of the list. (front first, rear last)
	public Iterator<E> iterator(){
		return new IteratorHelper();
	}
	
	private class IteratorHelper implements Iterator<E>{
		private int iterIndex;
		private long stateCheck;

		public IteratorHelper(){
			iterIndex = 0;
			stateCheck = modificationCounter;
		}
		
		@Override
		public boolean hasNext() {
			return iterIndex < currentSize;
		}

		@Override
		public E next() {
			if (currentSize == 0){throw new NoSuchElementException();}
			E temp = storage[(iterIndex + front) % max];
			iterIndex++;
			return temp;
			
		}
		
		public void remove(){
			if (currentSize == 0){throw new NoSuchElementException();}
			storage[(iterIndex + front) % max] = null;
			iterIndex++;
			
		}
		
	}
	
	//Adds the Object obj to the beginning of list and returns true if the list is not full.
	//returns false and aborts the insertion if the list is full.
	public boolean addFirst(E obj){
		if (isFull()) {return false;}
		if (isEmpty()){storage[front] = obj;}
		else {
			if(--front<0) front = max-1;
			storage[front] = obj;		
		}
		currentSize++;
		modificationCounter++;
		return true;
	}

	//Adds the Object obj to the end of list and returns true if the list is not full.
	//returns false and aborts the insertion if the list is full.  
	public boolean addLast(E obj){
		if (isFull()) {return false;}
		if (isEmpty()) {storage[rear] = obj;}
		else {
			rear = (rear + 1) % max;
			storage[rear] =  obj;
		}
		currentSize++;
		modificationCounter++;
		return true;
	}

	//Removes and returns the parameter object obj in first position in list if the list is not empty,  
	//null if the list is empty. 
	public E removeFirst(){
		if (isEmpty()) {return null;}
		E temp = storage[front];
		if (currentSize > 1){front = (front + 1) % max;}
		currentSize--;
		modificationCounter++;
		return temp;
	}

	//Removes and returns the parameter object obj in last position in list if the list is not empty, 
	//null if the list is empty. 
	public E removeLast(){
		if (isEmpty()) {return null;}
		E temp = storage[rear];
		if (currentSize > 1){
			if(--rear < 0) rear = max-1; //Unsure if pre-decrement will persist
        }
		currentSize--;
		modificationCounter++;
		
		return temp;
	}

	//Removes and returns the parameter object obj from the list if the list contains it, null otherwise.
	//The ordering of the list is preserved.  The list may contain duplicate elements.  This method
	//removes and returns the first matching element found when traversing the list from first position.
	//Note that you may have to shift elements to fill in the slot where the deleted element was located.
	public E remove(E obj){
		rmIndex = 0;
		E temp = find(obj);
		
		if(Math.abs(rmIndex-front) < Math.abs(rmIndex-rear)){
			//shift from front
			for(int i = 0; i < Math.abs(rmIndex-front); i++){
				storage[((rmIndex - i) + max) % max] = storage[((rmIndex - i - 1) + max) % max];
			}
			front = front + 1 % max;
		}
		else {
			//shift from rear
			for(int i = 0; i < Math.abs(rmIndex-rear); i++){
				storage[rmIndex + i] = storage[(rmIndex + i + 1) % max];
			}
			rear--;
			if(rear < 0) rear = max-1;			
		}
		
		return temp;
	}

	//Returns the first element in the list, null if the list is empty.
	//The list is not modified.
	public E peekFirst(){
		if (isEmpty()) {return null;}
		return storage[front];
	}

	//Returns the last element in the list, null if the list is empty.
	//The list is not modified.
	public E peekLast(){
		if (isEmpty()) {return null;}	
		return storage[rear];
	}                       

	//Returns true if the parameter object obj is in the list, false otherwise.
	//The list is not modified.
	public boolean contains(E obj){
		if(find(obj)!=null){return false;}
		return true;
	} 

	//Returns the element matching obj if it is in the list, null otherwise.
	//In the case of duplicates, this method returns the element closest to front.
	//The list is not modified.
	public E find(E obj){
		for(int i =0; i < currentSize; i++){
			if(((Comparable<E>)obj).compareTo(storage[(i + front) % max]) == 0){
				rmIndex = (i + front) % max;
				return storage[(i + front) % max];
		    }
		}
		return null;
	}  

	//The list is returned to an empty state.
	public void clear(){
		for(int i =0; i < currentSize; i++){
			storage[(i + front) % max] = null;
		}
		rear = front;
		currentSize = 0;		
	}

	//Returns true if the list is empty, otherwise false
	public boolean isEmpty(){
		if(currentSize==0){return true;}
		return false;
	}

	//Returns true if the list is full, otherwise false
	public boolean isFull(){
		if(currentSize==max){return true;}
		return false;
	}   

	//Returns the number of Objects currently in the list.
	public int size(){return currentSize;}

}