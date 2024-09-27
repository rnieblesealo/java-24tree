public class TwoFourTree {
  private class TwoFourTreeItem {
    int values = 1;
    int value1 = 0; // always exists.
    int value2 = 0; // exists iff the node is a 3-node or 4-node.
    int value3 = 0; // exists iff the node is a 4-node.
    boolean isLeaf = true;

    TwoFourTreeItem parent = null; // parent exists iff the node is not root.
    TwoFourTreeItem leftChild = null; // left and right child exist iff the note is a non-leaf.
    TwoFourTreeItem rightChild = null;
    TwoFourTreeItem centerChild = null; // center child exists iff the node is a non-leaf 3-node.
    TwoFourTreeItem centerLeftChild = null; // center-left and center-right children exist iff the node is a non-leaf
                                            // 4-node.
    TwoFourTreeItem centerRightChild = null;

    public boolean isTwoNode() {
      // 2-nodes have 1 value
      return this.values == 1;
    }

    public boolean isThreeNode() {
      // 3-nodes have 2 values
      return this.values == 2;
    }

    public boolean isFourNode() {
      // 4-nodes have 3 values
      return this.values == 3;
    }

    public boolean isRoot() {
      // A root has no parent
      return this.parent == null;
    }

    public TwoFourTreeItem(int value1) {
      // Creates a 2-node

      this.value1 = value1;
      this.value2 = -1;
      this.value3 = -1;
    }

    public TwoFourTreeItem(int value1, int value2) {
      // Creates a 3-node

      values = 2;

      this.value1 = value1;
      this.value2 = value2;
      this.value3 = -1;
    }

    public TwoFourTreeItem(int value1, int value2, int value3) {
      // Creates a 4-node

      values = 3;

      this.value1 = value1;
      this.value2 = value2;
      this.value3 = value3;
    }

    private void printIndents(int indent) {
      for (int i = 0; i < indent; i++)
        System.out.printf("  ");
    }

    public void printInOrder(int indent) {
      if (!isLeaf)
        leftChild.printInOrder(indent + 1);
      printIndents(indent);
      System.out.printf("%d\n", value1);
      if (isThreeNode()) {
        if (!isLeaf)
          centerChild.printInOrder(indent + 1);
        printIndents(indent);
        System.out.printf("%d\n", value2);
      } else if (isFourNode()) {
        if (!isLeaf)
          centerLeftChild.printInOrder(indent + 1);
        printIndents(indent);
        System.out.printf("%d\n", value2);
        if (!isLeaf)
          centerRightChild.printInOrder(indent + 1);
        printIndents(indent);
        System.out.printf("%d\n", value3);
      }
      if (!isLeaf)
        rightChild.printInOrder(indent + 1);
    }
  }

  TwoFourTreeItem root = null;

  public boolean addValue(int value) {
    // Walk down the tree

    // Case: We have no root
    if (this.root == null) {
      this.root = new TwoFourTreeItem(value);
      return true;
    }

    // If we have a root, start traversing!

    TwoFourTreeItem current = this.root;

    // Walk down the tree
    while (true) {

      // Case: The root is a 4-node

      if (current.isRoot() && current.isFourNode()) {
        int leftValue = current.value1;
        int middleValue = current.value2;
        int rightValue = current.value3;

        // Cache the children
        TwoFourTreeItem leftChild = current.leftChild;
        TwoFourTreeItem centerLeftChild = current.centerLeftChild;
        TwoFourTreeItem centerRightChild = current.centerRightChild;
        TwoFourTreeItem rightChild = current.rightChild;

        // Make a new 2-node the root and give it the middle value
        current = new TwoFourTreeItem(middleValue);

        // Make this node's L and R children be 2-node with the left and right values
        current.leftChild = new TwoFourTreeItem(leftValue);
        current.rightChild = new TwoFourTreeItem(rightValue);

        // Make the left child bear the old 4-node's 2 leftmost children
        current.leftChild.leftChild = leftChild;
        current.leftChild.rightChild = centerLeftChild;

        // The right child therefore bears the old 2 rightmost children
        current.rightChild.leftChild = centerRightChild;
        current.rightChild.rightChild = rightChild;
      }

      // Case: We have a 4-node, but it isn't the root

      else if (current.isFourNode()) {

        // Push the child's middle value to the parent, which could either be a 2-node
        // or a 3-node

        TwoFourTreeItem parent = current.parent;

        int middleValue = current.value2;

        if (parent.isTwoNode()) {

          // If the parent is a 2-node, move the middle value up to it, making it a 3-node
          // If the middle value comes from a left child, it should be the parent's
          // leftmost val
          // Vice-versa for rightmost

          if (current == parent.leftChild) {
            parent.value2 = parent.value1;
            parent.value1 = middleValue;
          }

          else if (current == parent.RightChild) {
            parent.value1 = parent.value2;
            parent.value2 = middleValue;
          }
        }

        else if (parent.isThreeNode()) {

          // Turn 3-node into 4-node by moving value up
          // If middle val. from right child, make it rightmost of 4-node
          // If from left, make it leftmost of 4-node
          // If comes from either of the two middles, make it the middle value; this is an
          // else case

          if (current == parent.leftChild) {
            parent.value3 = parent.value2;
            parent.value2 = parent.value1;
            parent.value1 = middleValue;
          }

          else if (current == parent.rightChild) {
            parent.value1 = parent.value2;
            parent.value2 = parent.value3;
            parent.value3 = middleValue;
          }
        }

        // [ RESUME HERE ]

        // Then split the current node
        // To make this easier, do it with reference to the parent

        // Cache children
        TwoFourTreeItem leftChild = current.leftChild;
        TwoFourTreeItem centerLeftChild = current.centerLeftChild;
        TwoFourTreeItem centerRightChild = current.centerRightChild;
        TwoFourTreeItem rightChild = current.rightChild;
      }

      // Case: We hit a leaf node

      if (current.isLeaf()) {

        // Case: We hit a leaf 2-node or 3-node

        if (current.isTwoNode()) {
          // Make a 3-node from a 2-node, ordering the values appropriately

          if (value < current.value1) {
            current = new TwoFourTreeItem(value, current.value1);
          }

          else {
            current = new TwoFourTreeItem(current.value1, value);
          }
        }

        else if (current.isThreeNode()) {
          // Make a 4-node from a 3-node, ordering values appropriately

          if (value < current.value1) {
            current = new TwoFourTreeItem(value, current.value1, current.value2);
          }

          else if (value > current.value2) {
            current = new TwoFourTreeItem(current.value1, current.value2, value);
          }

          else {
            current = new TwoFourTreeItem(current.value1, value, current.value2);
          }
        }
      }
    }

    return false;

  }

  public boolean hasValue(int value) {
    return false;
  }

  public boolean deleteValue(int value) {
    return false;
  }

  public void printInOrder() {
    if (root != null)
      root.printInOrder(0);
  }

  public TwoFourTree() {

  }
}
