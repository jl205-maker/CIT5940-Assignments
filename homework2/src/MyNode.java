/** BST Node class */
public class MyNode<T> {
    /** Instance variables */
    public T item;
    public MyNode<T> left, right, parent;

    /** Constructor */
    public MyNode (T item) {
        this.item = item;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    /** Getters */
    public T getItem() {
        return this.item;
    }

    public MyNode<T> getLeft() {
        return this.left;
    }

    public MyNode<T> getRight() {
        return this.right;
    }

    public MyNode<T> getParent() {
        return this.parent;
    }

    /** Setters */
    public void setParent(MyNode<T> parent) {
        this.parent = parent;
    }

    public void setLeft(MyNode<T> left) {
        this.left = left;
    }

    public void setRight(MyNode<T> right) {
        this.right = right;
    }
}