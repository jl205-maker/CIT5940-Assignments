import java.util.ArrayList;
import java.util.List;

public class MyHashTable<T extends Comparable<T>> {
    /** instance variables */
    // an array of buckets, where each bucket contains MyTree or null
    private List<MyTree<T>> myTree;
    // total number of buckets in the hash table
    private int capacity;
    // number of items currently stored in the hash table
    private int size;
    /** Constructor */
    public MyHashTable() {
        capacity = 701;
        myTree = new ArrayList<>(capacity);
        // prefill the buckets with null trees
        for (int i = 0; i < capacity; i++) {
            myTree.add(null);
        }
        size = 0;
    }
    /** 1-arg Constructor */
    public MyHashTable(int capacity) {
        if  (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be greater than 0");
        }
        // initialize the instance variables
        this.capacity = capacity;
        this.size = 0;
        this.myTree = new ArrayList<>(capacity);
        // fill the table with empty trees
        for (int i = 0; i < capacity; i++) {
            myTree.add(null);
        }
    }
    /** Insert an item if it is not yet present in the table
     * Returns a pointer to the node where the item is now stored */
    public MyNode<T> add(T item){
        // invalid input
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
        // get target bucket index
        int index = Math.floorMod(item.hashCode(), capacity);
        // get target bucket
        MyTree<T> targetBucket = myTree.get(index);
        if (targetBucket == null) {
            targetBucket = new MyTree<>();
            myTree.set(index, targetBucket);
        }
        // record original tree size
        int before = targetBucket.size();
        // try inserting the item
        MyNode<T> res = targetBucket.insert(item);
        // record new tree size
        int after = targetBucket.size();
        // check whether to increment hash table size
        if (after > before) size++;
        return res;
    }
    /** Returns a pointer to the node where the given item is stored */
    public MyNode<T> contains(T item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
        int index = Math.floorMod(item.hashCode(), capacity);
        MyTree<T> targetBucket = myTree.get(index);
        return (targetBucket == null) ? null : targetBucket.contains(item);
    }
    /** Remove the item if present */
    public boolean remove(T item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
        int index = Math.floorMod(item.hashCode(), capacity);
        MyTree<T> targetBucket = myTree.get(index);
        if (targetBucket == null) return false;
        boolean removed = targetBucket.remove(item);
        if (removed) size--;
        return removed;
    }
    /** Returns true if the hash table is empty */
    public boolean isEmpty() {
        return size == 0;
    }
    /** Returns the number of stored items in the hash table */
    public int size() {
        return size;
    }
    /** Empties the hash table */
    public void clear(){
        for (int i = 0; i < capacity; i++) {
            myTree.set(i, null);
        }
        size = 0;
    }
    /** Test */
    public static void main (String[] args) {
        MyHashTable<String> myHashTable = new MyHashTable<>();

        // insert
        assert myHashTable.size == 0;

        System.out.println(myHashTable.add("Apple"));
        System.out.println(myHashTable.add("Orange"));
        System.out.println(myHashTable.add("Watermelon"));
        System.out.println(myHashTable.add("Pineapple"));
        System.out.println(myHashTable.add("Banana"));
        System.out.println(myHashTable.size());
        assert myHashTable.size == 5;

        // contains
        System.out.println(myHashTable.contains("Apple"));
        // System.out.println(myHashTable.contains("Apple").getItem());
        assert myHashTable.contains("Apple") != null;

        System.out.println(myHashTable.contains("Dragon"));
        assert myHashTable.contains("Dragon") == null;

        // duplicate insert
        System.out.println(myHashTable.add("Banana"));
        System.out.println(myHashTable.size());
        assert myHashTable.size == 5;

        // remove
        System.out.println(myHashTable.remove("Apple")); // should print true
        assert myHashTable.size == 4;

        // remove again should fail
        System.out.println(myHashTable.remove("Apple")); // should print false
        assert myHashTable.size == 4;

        System.out.println("All tests passed.");
    }
}

