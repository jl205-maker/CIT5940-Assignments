/** BST Node class */
private class MyNode<T> {
    /** Instance variables */
    T item;
    MyNode<T> left, right, parent;

    /** Constructor */
    private MyNode (T item) {
        this.item = item;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    /** Getters */
    private T getItem() {
        return this.item;
    }

    private MyNode<T> getLeft() {
        return this.left;
    }

    private MyNode<T> getRight() {
        return this.right;
    }

    private MyNode<T> getParent() {
        return this.parent;
    }

    /** Setters */
    private void setParent(MyNode<T> parent) {
        this.parent = parent;
    }

    private void setLeft(MyNode<T> left) {
        this.left = left;
    }

    private void setRight(MyNode<T> right) {
        this.right = right;
    }
}