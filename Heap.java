package data_structures;

public class Heap<E> {
	private int currentSize;
	private long entryNumber = 0;
	private Wrapper<E>[] storage = new Wrapper[currentSize];
	
	private Heap(){
		
	}
	
	private void trickleUp(){
		int newIndex = currentSize - 1;
		int parentIndex = (newIndex - 1) >> 1;
		Wrapper<E> newValue = storage[newIndex];
		while(parentIndex >= 0 && newValue.compareTo(storage[parentIndex]) < 0){
			storage[newIndex] = storage[parentIndex];
			newIndex = parentIndex;
			parentIndex = (parentIndex - 1) >> 1;
		}
		storage[newIndex] = newValue;
	}
	
	private void trickleDown(){
		int current = 0;
		int child = getNextChild(current);
		while(child != -1 && storage[current].compareTo(storage[child]) < 0 && storage[child].compareTo(storage[currentSize - 1]) < 0){
			storage[current] = storage[child];
			current = child;
			child = getNextChild(current);
		}
		storage[current] = storage[currentSize - 1];
	}
	
	private int getNextChild(int current){
		int left = (current << 1) + 1;
		int right = left + 1;
		if(right < currentSize){
			if(storage[left].compareTo(storage[right]) < 0){
				return left;
			}
			return right;
		}
		if(left < currentSize){
			return left;
		}
		return -1;
	}
	
	protected class Wrapper<E> implements Comparable<Wrapper<E>>{
		long number;
		E data;
		
		public Wrapper(E d){
			number = entryNumber++;
			data = d;
		}
		
		public int compareTo(Wrapper<E> o){
			if(((Comparable<E>)data).compareTo(o.data) == 0){
				return (int) (number - o.number);
			}
			return((Comparable<E>)data).compareTo(o.data);
		}
	}
	
	public E[] heapSort(E[] array){
		E[] n = array;
		E top, newInsert;
		int heapSize = 1;
		int i, index, parent;
		int larger, left, right;
		int length = n.length;
		
		//first build the heap
		for(i=0; i < length; i++){
			index = heapSize - 1;
			parent = (index - 1) >> 1;
			newInsert = n[index];
			while(index > 0 && n[parent] < newInsert){
				n[index] = n[parent];
				index = parent;
				parent = (parent - 1) >> 1;
			}
			n[index] = newInsert;
			heapSize++;
		}
		
		heapSize--;
		for(i=length-1; i >= 0; i--){
			E tmp = (E) n[0];
			larger = 0;
			index = 0;
			n[0] = n[heapSize-1];
			top = n[0];
			while(index < heapSize >> 1){
				left = (index << 1) + 1;
				right = left + 1;
				if(right < heapSize && n[left] < n[right]){
					larger = right;
				}
				else{
					larger = left;
				}
				if(top >= n[larger]){
					break;
				}
				n[index] = n[larger];
				index = larger;
			}
			n[index] = top;
			heapSize--;
			n[i] = tmp;
		}
		
		return n;
	}

}
