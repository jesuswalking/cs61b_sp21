package hashmap;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int size;

    private static final int initialSize = 16;
    private static double loadFactor = 0.75;

    //private double maxLoad;

    /** Constructors */
    public MyHashMap() {
        this.buckets = createTable(initialSize);
        setupBuckets();
        this.size = 0;
    }

    public MyHashMap(int initialSize) {
        this.buckets = createTable(initialSize);
        setupBuckets();
        this.size = 0;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.buckets = createTable(initialSize);
        this.loadFactor = maxLoad;
        setupBuckets();
        this.size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return (Collection<Node>[]) new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    public void setupBuckets() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = createBucket();
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        setupBuckets();
        this.size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int bucketIndex = Math.floorMod(key.hashCode(), buckets.length);
        Collection<Node> chosenBucket = buckets[bucketIndex];
        for (Node node : chosenBucket) {
            if (node.key.equals(key)) return true;
        }
        return false;
    }

    @Override
    public V get(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = Math.floorMod(key.hashCode(), buckets.length);
        Collection<Node> chosenBucket = buckets[bucketIndex];
        for (Node node : chosenBucket) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        chosenBucket.add(new Node (key, value));
        this.size += 1;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    // Iterators.
    @Override
    public Iterator<K> iterator() {
        return new myHashMapIterator();
    }

    private class myHashMapIterator implements Iterator<K> {
        
        public myHashMapIterator() {

        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public K next() {
            throw new UnsupportedOperationException();
        }
    }
}
