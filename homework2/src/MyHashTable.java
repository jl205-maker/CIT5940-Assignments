public class MyHashTable<T extends Comparable<T>> {
    /** instance variables */
    // an array of buckets, where each bucket contains MyTree or null
    private List<Integer> myTree;
    // total number of buckets in the hash table
    private int capacity;
    // number of items currently stored in the hash table
    private int size;
    /** Constructor */
    public MyHashTable() {
        myTree = new ArrayList<>();
        capacity = 701;
        size = 0;
    }

}