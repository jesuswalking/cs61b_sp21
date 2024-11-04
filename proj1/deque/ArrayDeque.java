package deque;

public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    /* Creates an empty ArrayDeque. */
    public ArrayDeque() {
        items = (Item[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
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
        items[nextLast] = value;
        nextLast = adjustIndex(true, nextLast);
        size += 1;
    }

    /* Adds an item to the beginning of the deque. */
    public void addFirst(Item value) {
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

    /* Returns true if the deque is empty. */
    public boolean isEmpty() {
        return size == 0;
    }
}