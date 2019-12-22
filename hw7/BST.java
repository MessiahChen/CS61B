import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * A binary search tree with arbitrary Objects as keys.
 *
 * @author Mingjie Chen
 */
public class BST {
    /**
     * Root of tree.
     */
    private BSTNode root;
    private ArrayList<Object> _arr;

    /**
     * A BST containing the elements in the sorted list LIST.
     */
    public BST(LinkedList list) {
        root = linkedListToTree(list.iterator(), list.size());
    }

    /**
     * Firstly convert the sorted LinkedList to ArrayList,
     * then start from the middle of the ArrayList,
     * recursively 'treefy' the left and right part.
     */
    private BSTNode linkedListToTree(Iterator iter, int n) {
        // YOUR CODE HERE
        _arr = new ArrayList<>();
        while (iter.hasNext()) {
            _arr.add(iter.next());
        }
        return toTree(0, _arr.size() - 1);
    }

    private BSTNode toTree(int left, int right) {
        if (left > right) {
            return null;
        }

        // Middle element forms the root.
        int mid = (left + right) / 2;
        BSTNode node = new BSTNode(_arr.get(mid));

        // Base case for when there is only one element left in the array
        if (left == right) {
            return node;
        }

        // Recursively form BST on the two halves
        node.left = toTree(left, mid - 1);
        node.right = toTree(mid + 1, right);
        return node;
    }

    /**
     * Prints the tree to the console.
     */
    private void print() {
        print(root, 0);
    }

    /**
     * Print NODE and its subtrees, indented D levels.
     */
    private void print(BSTNode node, int d) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < d; i++) {
            System.out.print("  ");
        }
        System.out.println(node.item);
        print(node.left, d + 1);
        print(node.right, d + 1);
    }

    /**
     * Node for BST.
     */
    static class BSTNode {

        /**
         * Item.
         */
        protected Object item;

        /**
         * Left child.
         */
        protected BSTNode left;

        /**
         * Right child.
         */
        protected BSTNode right;

        public BSTNode(Object o) {
            this.item = o;
        }
    }
}
