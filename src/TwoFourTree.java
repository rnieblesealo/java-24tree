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
    // Case 1: We have no root

    if (this.root == null) {
      this.root = new TwoFourTreeItem(value);
      return true;
    }

    // If we have a root, start traversing!

    TwoFourTreeItem current = this.root;

    // Walk down the tree

    while (true) {

      // Case 2: The root is a 4-node

      if (current.isRoot() && current.isFourNode()) {

        // Cache the l, r, and middle values

        int leftValue = current.value1;
        int middleValue = current.value2;
        int rightValue = current.value3;

        // Cache the children

        TwoFourTreeItem leftChild = current.leftChild;
        TwoFourTreeItem centerLeftChild = current.centerLeftChild;
        TwoFourTreeItem centerRightChild = current.centerRightChild;
        TwoFourTreeItem rightChild = current.rightChild;

        // Make a new 2-node the root
        // Assign it the middle value

        current = new TwoFourTreeItem(middleValue);
        current.parent = null;

        // Make l, r 2-nodes
        // Give them the left and right values

        current.leftChild = new TwoFourTreeItem(leftValue);
        current.leftChild.parent = current;

        current.rightChild = new TwoFourTreeItem(rightValue);
        current.rightChild.parent = current;

        // Make:
        // l child of l -> left child of old 4-node
        // r child of l -> center left child of old 4-node
        // l child of r -> center right child of old 4-node
        // r child of r -> right child of old 4-node

        // Also set their parents correctly

        current.leftChild.leftChild = leftChild;
        leftChild.parent = current.leftChild;

        current.leftChild.rightChild = centerLeftChild;
        centerLeftChild.parent = current.leftChild;

        current.rightChild.leftChild = centerRightChild;
        centerRightChild.parent = current.rightChild;

        current.rightChild.rightChild = rightChild;
        rightChild.parent = current.rightChild;

        // Done!
      }

      // Case 3: Inner 4-node

      else if (current.isFourNode()) {
        // Keep track of parent and its old state
        TwoFourTreeItem parent = this.parent;

        boolean parentWasTwoNode = false;
        boolean parentWasThreeNode = false;

        // Push the middle value to the parent

        int middleValue = current.value2;

        if (parent.isTwoNode()) {

          // Update prior state before changing it

          parentWasTwoNode = true;

          // Push middle val to left if containing node was left child

          if (current == parent.leftChild) {
            parent.value2 = parent.value1;
            parent.value1 = middleValue;
          }

          // Push middle val to right if containing node was a right child

          else if (current == parent.rightChild) {
            parent.value1 = parent.value2;
            parent.value2 = middleValue;
          }
        }

        else if (parent.isThreeNode()) {

          // Update prior state before changing it

          parentWasThreeNode = true;

          // Push middle val to left if containing node was a left child

          if (current == parent.leftChild) {
            parent.value3 = parent.value2;
            parent.value2 = parent.value1;
            parent.value1 = middleValue;
          }

          // Push middle val to middle if containing node was inner left or inner right
          // child

          else if (current == parent.centerLeftChild || current == parent.centerRightChild) {
            parent.value3 = parent.value2;
            parent.value2 = middleValue;
          }

          // Push middle val to right if containing node was a right child

          else if (current == parent.rightChild) {
            parent.value1 = parent.value2;
            parent.value2 = parent.value3;
            parent.value3 = middleValue;
          }
        }

        // Pushing middle value up increases amt. of values in parent

        parent.values++;

        // Cache children of inner node

        TwoFourTreeItem leftChild = current.leftChild;
        TwoFourTreeItem centerLeftChild = current.centerLeftChild;
        TwoFourTreeItem centerRightChild = current.centerRightChild;
        TwoFourTreeItem rightChild = current.rightChild;

        // Split the node into two new 2-nodes

        int leftValue = current.value1;
        int rightValue = current.value3;

        TwoFourTreeItem newLeft = new TwoFourTreeItem(leftValue);
        TwoFourTreeItem newRight = new TwoFourTreeItem(rightValue);

        // Parent the new nodes properly

        newLeft.parent = parent;
        newRight.parent = parent;

        if (parentWasTwoNode) {
          boolean isLChild = parent.leftChild == current;
          boolean isRChild = parent.rightChild == current;

          if (isLChild) {
            parent.leftChild = newLeft;
            parent.centerLeftChild = newRight;
          }

          else if (isRChild) {
            parent.centerLeftChild = newLeft;
            parent.rightChild = newRight;
          }
        }

        else if (parentWasThreeNode) {
          boolean isLChild = parent.leftChild == current;
          boolean isMChild = parent.centerLeftChild == current || parent.centerRightChild == current;
          boolean isRChild = parent.rightChild == current;

          if (isLChild) {
            parent.leftChild = newLeft;

            parent.centerRightChild = parent.centerLeftChild;

            parent.centerLeftChild = newRight;
          }

          else if (isMChild) {
            parent.centerLeftChild = newLeft;

            parent.rightChild = parent.centerRightChild;

            parent.centerRightChild = newRight;
          }

          else if (isRChild) {
            parent.centerRightChild = newLeft;

            parent.rightChild = newRight;
          }
        }

        // Reattach children to the newly created 2-nodes properly

        newLeft.leftChild = leftChild;
        newLeft.leftChild.parent = newLeft;

        newLeft.rightChild = centerLeftChild;
        newLeft.rightChild.parent = newLeft;

        newRight.leftChild = centerRightChild;
        newRight.leftChild.parent = newRight;

        newRight.rightChild = rightChild;
        newRight.rightChild.parent = newRight;

        // Go back to parent to continue traversal

        current = parent;
      }

      // Case: We hit a leaf node

      else if (current.isLeaf) {

        if (current.isTwoNode()) {
          // Make a 3-node from a 2-node, ordering the values appropriately

          if (value < current.value1) {
          }

          else {
          }

          return true;
        }

        else if (current.isThreeNode()) {
          // Make a 4-node from a 3-node, ordering values appropriately

          // [ RESUME HERE ]

          // Also, anytime we're assigning something to current or parent, we're fucking
          // up since these are just references

          if (value < current.value1) {
          }

          else if (value >= current.value1 && value <= current.value2) {

          }

          else if (value > current.value2) {

          }

          return true;
        }
      }

      // Case: We reach an inner 2-node or 3-node, so we must keep walking

      else {
        if (current.isTwoNode()) {

          // 2-nodes are traversed like ordinary BST nodes

          if (value < current.value1) {
            current = current.leftChild;
          }

          else if (value >= current.value1) {
            current = current.rightChild;
          }
        }

        else if (current.isThreeNode()) {
          // 3-nodes also follow similar logic, but if our value lies between value1 and
          // value2, we go to the middle instead

          // Note that in this implementation a 3-node's middle child is always stored at
          // centerLeftChild

          if (value < current.value1) {
            current = current.leftChild;
          }

          else if (value >= current.value1 && value <= current.value2) {
            current = current.centerLeftChild;
          }

          else if (value >= current.value2) {
            current = current.rightChild;
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
