public class QueueFromArray {
    private int[] array;
    private int front;
    private int rear;
    private int size;
    private int capacity;
    public QueueFromArray() {
        this.array = new int[10];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
        this.capacity = 10;
    }
    public QueueFromArray(int capacity) {
        this.capacity = capacity;
        this.array = new int[capacity];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }
    public boolean isEmpty() {
        return (this.size == 0);
    }
    public boolean isFull() {
        return (this.size == this.capacity);
    }
    public int size() {
        return this.size;
    }
    public void enqueue(int value) {
        if (this.isFull()) {
            throw new IllegalStateException("Queue is full");
        }
        this.array[this.rear] = value;
        this.rear = (this.rear + 1) % this.capacity;
        this.size++;
    }
    public int dequeue() {
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        int removed = this.array[this.front];
        this.front = (front + 1) % this.capacity;
        this.size--;
        return removed;
    }
}
