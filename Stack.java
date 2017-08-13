/*  Gary Williams
    cssc0010
*/
package data_structures;

import java.util.Iterator;

public class Stack<E> {
	private LinearListADT<E> list;
	
	public Stack(){
		list = new LinearList<E>();
	}
	
	// inserts the object obj into the stack
    public void push(E obj){
    	list.addFirst(obj);
    }
    // pops and returns the element on the top of the stack    
    public E pop(){
    	return list.removeFirst();
    }
    // returns the number of elements currently in the stack
    public int size(){
    	return list.size();
    }
    // return true if the stack is empty, otherwise false
    public boolean isEmpty(){
    	return list.isEmpty();
    }        
    // returns but does not remove the element on the top of the stack    
    public E peek(){
    	return list.peekFirst();
    }
    // returns true if the object obj is in the stack,
    // otherwise false   
    public boolean contains(E obj){
    	return list.contains(obj);
    }
    // returns the stack to an empty state    
    public void makeEmpty(){
    	list.clear();
    }
    // removes the Object obj if it is in the stack and
    // returns true, otherwise returns false.
    public boolean remove(E obj){
    	if(list.contains(obj)){
    		list.remove(obj);
    		return true;
    	}
    	return false;
    }
    // returns a iterator of the elements in the stack.  The elements
    // must be in the same sequence as pop() would return them.    
    public Iterator<E> iterator(){
    	return list.iterator();
    }
    
	@Override
	public String toString() {
		return "Stack [list=" + list + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stack<E> other = (Stack<E>) obj;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		return true;
	}
}
