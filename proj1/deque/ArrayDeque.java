package deque;

import java.util.Iterator;

public class ArrayDeque<Item> implements Deque<Item>, Iterable<Item>{
    private Item[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private static final int INITIAL_CAPACITY = 8;
    private static final double MIN_CAPACITY_RATIO = 0.25;

    /* Creates an empty ArrayDeque. */
    public ArrayDeque() {
        items = (Item[]) new Object[INITIAL_CAPACITY];
        size = 0;
        nextFirst = INITIAL_CAPACITY - 1;
        nextLast = 0;
    }

    @Override
    public Iterator<Item> iterator() {
    	return new ArrayDequeIterator();
    }

    /* Resize the AList. */
    private void resize(int capacity) {
    	Item[] a = (Item[]) new Object[capacity];
    	
    	int firstIndex = (nextFirst + 1) % items.length;

    	if (firstIndex < nextLast) { // 元素在List里是连续的
    		System.arraycopy(items, firstIndex, a, 0, size);
    	} else { // 元素在List里不是连续的
    		System.arraycopy(items, firstIndex, a, 0, items.length - firstIndex);
    		System.arraycopy(items, 0, a, items.length - firstIndex, nextLast);
    	}

    	items = a;
    	nextFirst = capacity - 1;
    	nextLast = size;
    }

    /* Helper function to calculate next index based on direction. */
    private int adjustIndex(boolean isNextLast, int index) {
        if (isNextLast) {
            return (index + 1) % items.length;  // 向后
        } else {
            return (index - 1 + items.length) % items.length;  // 向前
        }
    }

    /* Adds an item to the end of the deque. */
    public void addLast(Item value) {
    	if (size == items.length) {
    		resize(size * 2);
    	}

        items[nextLast] = value;
        nextLast = adjustIndex(true, nextLast);
        size += 1;
    }

    /* Adds an item to the beginning of the deque. */
    public void addFirst(Item value) {
    	if (size == items.length) {
    		resize(size * 2);
    	}

        items[nextFirst] = value;
        nextFirst = adjustIndex(false, nextFirst);
        size += 1;
    }

    /* Removes and returns the first item of the deque. */
    public Item removeFirst() {
        nextFirst = adjustIndex(true, nextFirst);
        Item target = items[nextFirst];
        items[nextFirst] = null;
        if (size > 0) {
            size -= 1;
        }

        if (size > 0 && size < items.length * MIN_CAPACITY_RATIO && size > INITIAL_CAPACITY) {
        	resize(items.length / 4);
        }
        return target;
    }

    /* Removes and returns the last item of the deque. */
    public Item removeLast() {
        nextLast = adjustIndex(false, nextLast);
        Item target = items[nextLast];
        items[nextLast] = null;
        if (size > 0) {
            size -= 1;
        }

        if (size > 0 && size < items.length * MIN_CAPACITY_RATIO && size > INITIAL_CAPACITY) {
        	resize(items.length / 4);
        }
        return target;
    }

    /* Gets the size of the deque. */
    public int size() {
        return size;
    }

    /* Gets the item at the given index, adjusting for wraparound. */
    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;                  // Return null if index is out of bounds
        }
        int actualIndex = (nextFirst + 1 + index) % items.length;
        return items[actualIndex];
    }

    /* Prints the ArrayDeque. */
    public void printDeque() {
    	int Index = (nextFirst + 1) % items.length;
    	for (int i = 0; i < size; i ++) {
    		System.out.print(items[Index] + " ");
    		Index = (Index + 1) % items.length;
    	}
    	System.out.println();
    }

    /* Check whether Object o is equal to this. */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArrayDeque)) {
            return false;
        }
        ArrayDeque<?> ad = (ArrayDeque<?>) o;
        if (ad.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (ad.get(i) != get(i)) {
                return false;
            }
        }
        return true;
    }

    private class ArrayDequeIterator implements Iterator<Item> {
    	private int index;

    	ArrayDequeIterator() {
    		index = 0;
    	}

    	public boolean hasNext() {
            return index < size;
        }

        public Item next() {
            Item item = get(index);
            index += 1;
            return item;
        }
    }
}