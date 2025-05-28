package bstmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
	// I finally fucking get it, the reason why I need to define a root is that 
	// BST always do methods from the root. It is the only fucking access.
	private Node root;

	private class Node {
		private K key;
		private V value;
		private Node left;
		private Node right;
		private int size;

		public Node(K key, V value, int size) {
			this.key = key;
			this.value = value;
			this.size = size;
		}
	}

	// Dont know why yet.
	public BSTMap() {

	}

	// Clear all of the mappings from the whole map.
	@Override
	public void clear() {
		root.size = 0;
		root = null;
	}

	// Return true if the map contains a certain mapping for the certain key.
	@Override
	public boolean containsKey(K key) {
		return containsKey(root, key);
	}

	private boolean containsKey(Node x, K key) {
		if (x == null) return false;
		int cmp = key.compareTo(x.key);
		if (cmp < 0) return containsKey(x.left, key);
		else if (cmp > 0) return containsKey(x.right, key);
		else return key.equals(x.key);
		
	}

	// Return the value of the specified key, or null if there is no key.
	@Override
	public V get(K key) {
		return get(root, key);	
	}

	private V get(Node x, K key) {
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if (cmp < 0) return get(x.left, key);
		else if (cmp > 0) return get(x.right, key);
		return x.value;
	}

	// Return the number of the key-value pairs in the whole map.
	@Override
	public int size() {
		return size(root);
	}

	private int size(Node x) {
		if (x == null) {
			return 0;
		}
		return x.size;
	}

	// put a key-value pair into the BST.
	@Override
	public void put(K key, V value) {
		if (key == null) {throw new IllegalArgumentException("bruh, you cant input null.");}
		if (value == null) {}
		root = put(root, key, value); // because of this, private put need to return.
	}

	/* if x == null,  return new one
	   int cmp, key.compareTo(x.key)
	   1
	   2
	   3

	*/
	private Node put(Node x, K key, V value) {
		if (x == null) {return new Node(key, value, 1);}
		int cmp = key.compareTo(x.key);
		if (cmp < 0) {
			x.left = put(x.left, key, value);
		} else if (cmp > 0) {
			x.right = put(x.right, key, value);
		} else {
			x.value = value;
		}
		x.size = 1 + size(x.left) + size(x.right);
		return x;
	}

	@Override
	public Set<K> keySet() {
		throw new UnsupportedOperationException("not finished yet.");
	}

	@Override
	public V remove(K key) {
		throw new UnsupportedOperationException("not finished yet.");
	}

	@Override
	public V remove(K key, V value) {
		throw new UnsupportedOperationException("not finished yet.");
	}

	// Iterators.
	@Override
	public Iterator<K> iterator() {
		return new BSTMapIterator();
	}

	private class BSTMapIterator implements Iterator<K> {
		private List<K> keys;
		private int index;

		public BSTMapIterator() {
			keys = new ArrayList<>();
			index = 0;
			InOrder(root);
		}

		// Put every key in the array in order.
		private void InOrder(Node x) {
			if (x == null) return;
			InOrder(x.left);
			keys.add(x.key);
			InOrder(x.right);
		}

		@Override
		public boolean hasNext() {
			return index < keys.size();
		}

		@Override
		public K next() {
			K nextKey = keys.get(index);
			index += 1;
			return nextKey;
		}
	}

	public void printInOrder() {
		
	}
}