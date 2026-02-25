public class MyTree<T extends Comparable<T>> {
    /** Root of the tree */
    private MyNode<T> root;
    /** Size of the tree */
    private int size;

    /** Constructor */
    public MyTree() {
        this.root = null;
        this.size = 0;
    }

    /** Inserts the item into the BST if it is not already present */
    public MyNode<T> insert(T item) {
        // invalid input
        if (item == null) {
            throw new IllegalArgumentException("insert(): item can't be null");
        }
        // empty tree
        if (root == null) {
            root = new MyNode<>(item);
            size++;
            return root;
        }

        // iteratively find the location to insert
        MyNode<T> curr = root;
        MyNode<T> parent = null;

        while (curr != null) {
            parent = curr;
            int cmp = item.compareTo(curr.item);
            if (cmp == 0) {
                return curr;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }

        MyNode<T> newNode = new MyNode<>(item);
        newNode.parent = parent;

        if (item.compareTo(parent.item) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        size++;
        return newNode;
    }

    /** Searches the BST for the given itemusing the compareTo() method */
    public  MyNode<T> contains(T item) {
        if (item == null) {
            throw new IllegalArgumentException("contains(): item can't be null");
        }

        MyNode<T> curr = root;
        while (curr != null) {
            int cmp = item.compareTo(curr.item);
            if (cmp == 0) {
                return curr;
            } else if (cmp < 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return null;
    }

    /** If the item is in the tree, removes the node */
    public boolean remove(T item) {
        // invalid input
        if (item == null) {
            throw new IllegalArgumentException("remove(): item can't be null");
        }
        // iteratively find the node to remove
        MyNode<T> curr = root;
        while (curr != null) {
            int cmp = item.compareTo(curr.item);
            if (cmp == 0) {
                break;
            }
            curr = (cmp < 0) ? curr.left : curr.right;
        }
        // if the tree does not contain target node, return false
        if (curr == null) {
            return false;
        }
        // case: two children -> swap with successor, then delete successor node
        if (curr.left != null && curr.right != null) {
            // find the successor that is the first node greater than current node
            MyNode<T> succ = curr.right;
            while(succ.left != null) {
                succ = succ.left;
            }
            // copy successor's item into curr, then remove successor
            curr.item = succ.item;
            curr = succ;
        }
        // now curr has at most one child
        MyNode<T> child = (curr.left != null)? curr.left : curr.right;
        // reconnect parent pointer to splice out curr
        if (child != null) {
            child.parent = curr.parent;
        }
        if (curr.parent == null){
            // deleting root
            root = child;
        } else if (curr == curr.parent.left) {
            curr.parent.left = child; // curr is the left child
        } else {
            curr.parent.right = child; // curr is the right child
        }
        size--;
        return true;
    }

    /** Returns a string containing the in-order items in your tree */
    @Override
    public String toString(){
        if (root == null) {return "";}
        StringBuilder sb = new StringBuilder();
        inOrder(root, sb);
        // remove trailing ,
        if (sb.length() >= 2){
            sb.setLength(sb.length()-2);
        }
        return sb.toString();
    }

    private void inOrder(MyNode<T> n, StringBuilder sb){
        if (n == null) {
            return;
        }
        inOrder(n.left, sb);
        sb.append(n.item).append(", ");
        inOrder(n.right, sb);
    }

    /** Returns the root node of the tree */
    public MyNode<T> getRoot() {
        return root;
    }

    public int size() {return size;}
}