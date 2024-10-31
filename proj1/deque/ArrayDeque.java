package deque;

public class ArrayDeque<Item> {
	private Item[] items;
	private int size;
	private int nextFirst;
	private int nextLast;

	/* Creates an empty AList. */
	public ArrayDeque() {
		items = (Item[]) new Object[8];
		size = 0;
		nextFirst = 4;
		nextLast = 5;
	}

	/* nextFirst and nextLast value helper. True equals nextLast.*/
	private int helper (boolean direction, int index) {
		if (direction == true) {
			index += 1;
			if (index == items.length) {
				index = 0;
			}
			return index;
		} else {
			index -= 1;
			if (index == -1) {
				index = items.length - 1;
			}
			return index;
		}
	}

	/* Add an item at the end of the AList. */
	public void addLast(Item value) {
		items[nextLast] = value;
		helper(true, nextLast);
		size += 1;
	}

	/* Add an item at the beginning of the AList. */
	public void addFirst(Item value) {
		items[nextFirst] = value;
		helper(false, nextFirst);
		size += 1;
	}

	/* Removes and returns the first item of the AList. */
	public Item removeFirst() {
		helper(true, nextFirst);
		Item target = items[nextFirst];
		items[nextFirst] = null;
		size -= 1;
		return target;
	}

	/* Removes and returns the last item of the AList. */
	public Item removeLast() {
		helper(false, nextLast);
		Item target = items[nextLast];
		items[nextLast] = null;
		size -= 1;
		return target;
	}

	/* Gets the size of the AList. */
	public int size() {
		return size;
	}

	/* Gets the items at the given certain index. */
	public Item get(int index) {
		int times = nextFirst + 1;
		int count = 0;
		while (count < times) {
			helper(true, index);
			count += 1;
		}
		return items[index];
	}

	/* Returns if the AList is empty. */
	public boolean isEmpty() {
		return size == 0;
	}
}