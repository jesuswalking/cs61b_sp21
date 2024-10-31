package deque;

public class LinkedListDeque<Item> {
	
	/* Naked recursive data structure. */
	private class LinkedList<Item> {
		public LinkedList prev;
		public Item middle;
		public LinkedList rest;

		/* Creates a LinkedList. */
		public LinkedList(LinkedList prev, Item middle, LinkedList rest) {
			this.prev = prev;
			this.middle = middle;
			this.rest = rest;
		}

		/* Creates an empty LinkedList. */
		public LinkedList() {
			this.prev = this;
			this.middle = null;
			this.rest = this;
		}
	}

	/* Begins building data structure. */
	private LinkedList sentinel;
	private int size;

	/* Returns an empty LinkedList. */
	public LinkedListDeque() {
		sentinel = new LinkedList();
		size = 0;
	}

	/* Returns true if LinkedListDeque is null, otherwise, return false */
	public boolean isEmpty() {
		return size == 0;
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

	/* Adds an item at the front of the deque. */
	public void addFirst(Item value) {
		LinkedList newNode = new LinkedList(sentinel, value, sentinel.rest);
		sentinel.rest.prev = newNode;
		sentinel.rest = newNode;
		size += 1;
	}

	/* Adds an item at the end of the deque. */
	public void addLast(Item value) {
		LinkedList newNode = new LinkedList(sentinel.prev, value, sentinel);
		sentinel.prev.rest = newNode;
		sentinel.prev = newNode;
		size += 1;
	}

	/* Returns and removes the first item from the deque. */
	public Item removeFirst() {
		if (size == 0) {
			return null;
		}

		Item target = (Item) sentinel.rest.middle;
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

	/* Returns and removes the last item from the deque. */
	public Item removeLast() {
		if (size == 0) {
			return null;
		}

		Item target = (Item) sentinel.prev.middle;
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

	/* Returns the item at the given index. */
	public Item get(int index) {
		if (index >= size) {
			return null;
		}

		LinkedList current = sentinel.rest;
		int count = 0;
		while (count < index) {
			current = current.rest;
			count += 1;
		}
		Item target = (Item) current.middle;
		return target;
	}

	/* GETRECURSIVEHELPER. */
	private Item getRecursiveHelper(int index, int count, LinkedList current) {
		if (index >= size) {
			return null;
		} else if (count < index) {
			current = current.rest;
			return getRecursiveHelper(index, count + 1, current);
		} else if (count == index) {
			Item target = (Item) current.middle;
			return target;
 		}
 		return null;
	}

	/* Returns the item at the given index recursively. */
	public Item getRecursive(int index) {
		LinkedList current = sentinel.rest;
		return getRecursiveHelper(index, 0, current);
	}
}