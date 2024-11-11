package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> ,Iterable<T> {
    private T[] Ts;
    private int size;
    private int nextFirst;
    private int nextLast;
    private static final int INITIAL_CAPACITY = 8;
    private static final double MIN_CAPACITY_RATIO = 0.25;

    /* Creates an empty ArrayDeque. */
    public ArrayDeque() {
        Ts = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
        nextFirst = INITIAL_CAPACITY - 1;
        nextLast = 0;
    }

    @Override
    public Iterator<T> iterator() {
    	return new ArrayDequeIterator();
    }

    /* Resize the AList. */
    private void resize(int capacity) {
    	T[] a = (T[]) new Object[capacity];
    	
    	int firstIndex = (nextFirst + 1) % Ts.length;

    	if (firstIndex < nextLast) { // 元素在List里是连续的
    		System.arraycopy(Ts, firstIndex, a, 0, size);
    	} else { // 元素在List里不是连续的
    		System.arraycopy(Ts, firstIndex, a, 0, Ts.length - firstIndex);
    		System.arraycopy(Ts, 0, a, Ts.length - firstIndex, nextLast);
    	}

    	Ts = a;
    	nextFirst = capacity - 1;
    	nextLast = size;
    }

    /* Helper function to calculate next index based on direction. */
    private int adjustIndex(boolean isNextLast, int index) {
        if (isNextLast) {
            return (index + 1) % Ts.length;  // 向后
        } else {
            return (index - 1 + Ts.length) % Ts.length;  // 向前
        }
    }

    /* Adds an T to the end of the deque. */
    public void addLast(T value) {
    	if (size == Ts.length) {
    		resize(size * 2);
    	}

        Ts[nextLast] = value;
        nextLast = adjustIndex(true, nextLast);
        size += 1;
    }

    /* Adds an T to the beginning of the deque. */
    public void addFirst(T value) {
    	if (size == Ts.length) {
    		resize(size * 2);
    	}

        Ts[nextFirst] = value;
        nextFirst = adjustIndex(false, nextFirst);
        size += 1;
    }

    /* Removes and returns the first T of the deque. */
    public T removeFirst() {
        nextFirst = adjustIndex(true, nextFirst);
        T target = Ts[nextFirst];
        Ts[nextFirst] = null;
        if (size > 0) {
            size -= 1;
        }

        if (size > 0 && size < Ts.length * MIN_CAPACITY_RATIO && size > INITIAL_CAPACITY) {
        	resize(Ts.length / 4);
        }
        return target;
    }

    /* Removes and returns the last T of the deque. */
    public T removeLast() {
        nextLast = adjustIndex(false, nextLast);
        T target = Ts[nextLast];
        Ts[nextLast] = null;
        if (size > 0) {
            size -= 1;
        }

        if (size > 0 && size < Ts.length * MIN_CAPACITY_RATIO && size > INITIAL_CAPACITY) {
        	resize(Ts.length / 4);
        }
        return target;
    }

    /* Gets the size of the deque. */
    public int size() {
        return size;
    }

    /* Gets the T at the given index, adjusting for wraparound. */
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;                  // Return null if index is out of bounds
        }
        int actualIndex = (nextFirst + 1 + index) % Ts.length;
        return Ts[actualIndex];
    }

    /* Prints the ArrayDeque. */
    public void printDeque() {
    	int Index = (nextFirst + 1) % Ts.length;
    	for (int i = 0; i < size; i++) {
    		System.out.print(Ts[Index] + " ");
    		Index = (Index + 1) % Ts.length;
    	}
    	System.out.println();
    }

    /* Check whether Object o is equal to this. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }

        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            T item1 = get(i);
            T item2 = other.get(i);
            if (!item1.equals(item2)) {
                return false;
            }
        }
        return true;
    }

    private class ArrayDequeIterator implements Iterator<T> {
    	private int index;

    	ArrayDequeIterator() {
    		index = 0;
    	}

    	public boolean hasNext() {
            return index < size;
        }

        public T next() {
            T T = get(index);
            index += 1;
            return T;
        }
    }
}
