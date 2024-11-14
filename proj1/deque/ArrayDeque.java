package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] ts;
    private int size;
    private int nextFirst;
    private int nextLast;
    private static final int INITIAL_CAPACITY = 8;
    private static final double MIN_CAPACITY_RATIO = 0.25;

    /* Creates an empty ArrayDeque. */
    public ArrayDeque() {
        ts = (T[]) new Object[INITIAL_CAPACITY];
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

        int firstIndex = (nextFirst + 1) % ts.length;

    	if (firstIndex < nextLast) { // 元素在List里是连续的
    		System.arraycopy(ts, firstIndex, a, 0, size);
    	} else { // 元素在List里不是连续的
    		System.arraycopy(ts, firstIndex, a, 0, ts.length - firstIndex);
    		System.arraycopy(ts, 0, a, ts.length - firstIndex, nextLast);
    	}

    	ts = a;
    	nextFirst = capacity - 1;
    	nextLast = size;
    }

    /* Helper function to calculate next index based on direction. */
    private int adjustIndex(boolean isNextLast, int index) {
        if (isNextLast) {
            return (index + 1) % ts.length;  // 向后
        } else {
            return (index - 1 + ts.length) % ts.length;  // 向前
        }
    }

    /* Adds an T to the end of the deque. */
    public void addLast(T value) {
    	if (size == ts.length) {
    		resize(size * 2);
    	}

        ts[nextLast] = value;
        nextLast = adjustIndex(true, nextLast);
        size += 1;
    }

    /* Adds an T to the beginning of the deque. */
    public void addFirst(T value) {
    	if (size == ts.length) {
    		resize(size * 2);
    	}

        ts[nextFirst] = value;
        nextFirst = adjustIndex(false, nextFirst);
        size += 1;
    }

    /* Removes and returns the first T of the deque. */
    public T removeFirst() {
        nextFirst = adjustIndex(true, nextFirst);
        T target = ts[nextFirst];
        ts[nextFirst] = null;
        if (size > 0) {
            size -= 1;
        }

        if (size > 0 && size < ts.length * MIN_CAPACITY_RATIO && size > INITIAL_CAPACITY) {
        	resize(ts.length / 4);
        }
        return target;
    }

    /* Removes and returns the last T of the deque. */
    public T removeLast() {
        nextLast = adjustIndex(false, nextLast);
        T target = ts[nextLast];
        ts[nextLast] = null;
        if (size > 0) {
            size -= 1;
        }

        if (size > 0 && size < ts.length * MIN_CAPACITY_RATIO && size > INITIAL_CAPACITY) {
        	resize(ts.length / 4);
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
        int actualIndex = (nextFirst + 1 + index) % ts.length;
        return ts[actualIndex];
    }

    /* Prints the ArrayDeque. */
    public void printDeque() {
    	int index = (nextFirst + 1) % ts.length;
    	for (int i = 0; i < size; i++) {
    		System.out.print(ts[index] + " ");
    		index = (index + 1) % ts.length;
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
