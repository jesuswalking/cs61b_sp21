package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
	
	/* Naked recursive data structure. */
	private class LinkedList<T> {
		private LinkedList prev;
		private T middle;
		private LinkedList rest;

		/* Creates a LinkedList. */
		private LinkedList(LinkedList prev, T middle, LinkedList rest) {
			this.prev = prev;
			this.middle = middle;
			this.rest = rest;
		}

		/* Creates an empty LinkedList. */
		private LinkedList() {
			this.prev = this;
			this.middle = null;
			this.rest = this;
		}
	}

	/* Begins building data structure. */
	private LinkedList sentinel;
	private int size;

	@Override
	public Iterator<T> iterator() {
	    return new LinkedListDequeIterator();
	}

	/* Returns an empty LinkedList. */
	public LinkedListDeque() {
		sentinel = new LinkedList();
		size = 0;
	}

	/* Returns the size of the LinkedListDeque. */
	public int size() {
		return size;
	}

	/* Prints the deque, from the first to last. */
	public void printDeque() {
		LinkedList current = sentinel.rest;

		while (current != sentinel) {
			System.out.print(current.middle + " ");
			current = current.rest;
		}
		System.out.println();
	}

	/* Adds an T at the front of the deque. */
	public void addFirst(T value) {
		LinkedList newNode = new LinkedList(sentinel, value, sentinel.rest);
		sentinel.rest.prev = newNode;
		sentinel.rest = newNode;
		size += 1;
	}

	/* Adds an T at the end of the deque. */
	public void addLast(T value) {
		LinkedList newNode = new LinkedList(sentinel.prev, value, sentinel);
		sentinel.prev.rest = newNode;
		sentinel.prev = newNode;
		size += 1;
	}

	/* Returns and removes the first T from the deque. */
	public T removeFirst() {
		if (size == 0) {
			return null;
		}

		T target = (T) sentinel.rest.middle;
		if (size == 1) {
			sentinel.rest = sentinel;
			sentinel.prev = sentinel;
		} else {
			sentinel.rest = sentinel.rest.rest;
			sentinel.rest.prev = sentinel;
		}
		size -= 1;
		return target;
	}

	/* Returns and removes the last T from the deque. */
	public T removeLast() {
		if (size == 0) {
			return null;
		}

		T target = (T) sentinel.prev.middle;
		if (size == 1) {
			sentinel.rest = sentinel;
			sentinel.prev = sentinel;
		} else {
			sentinel.prev = sentinel.prev.prev;
			sentinel.prev.rest = sentinel;
		}
		size -= 1;
		return target;
	}

	/* Returns the T at the given index. */
	public T get(int index) {
		if (index >= size) {
			return null;
		}

		LinkedList current = sentinel.rest;
		int count = 0;
		while (count < index) {
			current = current.rest;
			count += 1;
		}
		T target = (T) current.middle;
		return target;
	}

	/* GETRECURSIVEHELPER. */
	private T getRecursiveHelper(int index, int count, LinkedList current) {
		if (index >= size) {
			return null;
		} else if (count < index) {
			current = current.rest;
			return getRecursiveHelper(index, count + 1, current);
		} else if (count == index) {
			T target = (T) current.middle;
			return target;
 		}
 		return null;
	}

	/* Returns the T at the given index recursively. */
	public T getRecursive(int index) {
		LinkedList current = sentinel.rest;
		return getRecursiveHelper(index, 0, current);
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

    private class LinkedListDequeIterator implements Iterator<T> {
    	private LinkedList<T> p;

    	LinkedListDequeIterator() {
    		p = sentinel.rest;
    	}

    	public boolean hasNext() {
    		return p != sentinel;
    	}

    	public T next() {
    		T items = p.middle;
    		p = p.rest;
    		return items;
    	}
    }
}
