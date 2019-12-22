import java.util.*;

/**
 * Implementation of a BST based String Set.
 *
 * @author Mingjie Chen
 */
public class BSTStringSet implements StringSet, Iterable<String>, SortedStringSet {
    /**
     * Creates a new empty set.
     */
    public BSTStringSet() {
        _root = null;
    }

    public void put(String s) {
        _root = put(s, _root);
    }

    /**
     * Helper method for put. Returns a BST rooted in P,
     * but with S added to this BST.
     */
    private Node put(String s, Node p) {
        if (p == null) {
            return new Node(s);
        }

        int cmp = s.compareTo(p.s);

        if (cmp < 0) {
            p.left = put(s, p.left);
        }
        if (cmp > 0) {
            p.right = put(s, p.right);
        }

        return p;
    }

    @Override
    public boolean contains(String s) {
        // FIXME: PART A
        Node node = getParentNode(s, _root);
        return node != null && node.s.equals(s);
    }


    private Node getParentNode(String s, Node subRoot) {
        if (subRoot == null) {
            return null;
        }
        int comp = s.compareTo(subRoot.s);
        if (comp < 0) {
            if (subRoot.left == null) {
                return subRoot;
            } else {
                return getParentNode(s, subRoot.left);
            }
        } else if (comp > 0) {
            if (subRoot.right == null) {
                return subRoot;
            } else {
                return getParentNode(s, subRoot.right);
            }
        } else {
            return subRoot;
        }
    }

    @Override
    public List<String> asList() {
        // FIXME: PART A
        ArrayList<String> res = new ArrayList<>();
        Iterator<String> ite = iterator();
        while (ite.hasNext()) {
            res.add(ite.next());
        }
        return res;
    }

    /**
     * Represents a single Node of the tree.
     */
    public static class Node {
        /**
         * String stored in this Node.
         */
        public String s;
        /**
         * Left child of this Node.
         */
        public Node left;
        /**
         * Right child of this Node.
         */
        public Node right;

        /**
         * Creates a Node containing SP.
         */
        Node(String sp) {
            s = sp;
        }
    }

    /**
     * An iterator over BSTs.
     */
    private static class BSTIterator implements Iterator<String> {
        /**
         * Stack of nodes to be delivered.  The values to be delivered
         * are (a) the label of the top of the stack, then (b)
         * the labels of the right child of the top of the stack inorder,
         * then (c) the nodes in the rest of the stack (i.e., the result
         * of recursively applying this rule to the result of popping
         * the stack.
         */
        private Stack<Node> _toDo = new Stack<>();

        /**
         * A new iterator over the labels in NODE.
         */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Add the relevant subtrees of the tree rooted at NODE.
         */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    @Override
    public Iterator<String> iterator(String low, String high) {
        // FIXME: PART B
        Iterator ite = new BSTIterator(_root);
        return new BSTBoundedIterator(ite, low, high);
    }

    /**
     * An iterator over BSTs with bounding.
     */
    private static class BSTBoundedIterator implements Iterator<String> {
        public BSTBoundedIterator(Iterator ite, String low, String high) {
            _ite = ite;
            _low = low;
            _high = high;
        }

        public void setNext() {
            while (_ite.hasNext()) {
                String next = (String) _ite.next();
                if (next.compareTo(_low) >= 0 && next.compareTo(_high) <= 0) {
                    _next = next;
                    break;
                }
                _next = " ";
            }
        }

        @Override
        public boolean hasNext() {
            setNext();
            return !_next.equals(" ");
        }

        @Override
        public String next() {
            return _next;
        }

        private String _low;
        private String _high;
        private Iterator _ite;
        private String _next;
    }

    /**
     * Root node of the tree.
     */
    public Node _root;
}
