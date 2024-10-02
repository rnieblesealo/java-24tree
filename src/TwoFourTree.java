public class TwoFourTree {
  private class TwoFourTreeItem {
    int values = 0;

    int value1 = -1;
    int value2 = -1;
    int value3 = -1;

    TwoFourTreeItem parent = null;
    TwoFourTreeItem leftChild = null;
    TwoFourTreeItem rightChild = null;
    TwoFourTreeItem centerChild = null;
    TwoFourTreeItem centerLeftChild = null;
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

    public boolean isLeaf() {
      return leftChild == null && rightChild == null && centerChild == null && centerLeftChild == null
          && centerRightChild == null;
    }

    public TwoFourTreeItem(int value1) {
      // Creates a 2-node
      this.values = 1;

      this.value1 = value1;
    }

    public TwoFourTreeItem(int value1, int value2) {
      // Creates a 3-node

      values = 2;

      this.value1 = value1;
      this.value2 = value2;
    }

    public TwoFourTreeItem(int value1, int value2, int value3) {
      // Creates a 4-node

      values = 3;

      this.value1 = value1;
      this.value2 = value2;
      this.value3 = value3;
    }

    // Helper

    private void printIndents(int indent) {
      for (int i = 0; i < indent; i++)
        System.out.printf("  ");
    }

    public void printInOrder(int indent) {
      if (!isLeaf())
        leftChild.printInOrder(indent + 1);
      printIndents(indent);
      System.out.printf("%d\n", value1);
      if (isThreeNode()) {
        if (!isLeaf())
          centerChild.printInOrder(indent + 1);
        printIndents(indent);
        System.out.printf("%d\n", value2);
      } else if (isFourNode()) {
        if (!isLeaf())
          centerLeftChild.printInOrder(indent + 1);
        printIndents(indent);
        System.out.printf("%d\n", value2);
        if (!isLeaf())
          centerRightChild.printInOrder(indent + 1);
        printIndents(indent);
        System.out.printf("%d\n", value3);
      }
      if (!isLeaf())
        rightChild.printInOrder(indent + 1);
    }
  }

  TwoFourTreeItem root = null;

  public boolean split(TwoFourTreeItem target) {
    if (!target.isFourNode()) {
      return false;
    }

    // Make new children

    TwoFourTreeItem newLeftChild = new TwoFourTreeItem(target.value1);
    TwoFourTreeItem newRightChild = new TwoFourTreeItem(target.value3);

    // Eliminate values from target to make it 2-node

    target.value1 = target.value2;
    target.value2 = -1;
    target.value3 = -1;

    target.values = 1;

    // Relink children of 4-node to newly created nodes

    newLeftChild.leftChild = target.leftChild;
    newLeftChild.rightChild = target.centerLeftChild;

    newRightChild.leftChild = target.centerRightChild;
    newRightChild.rightChild = target.rightChild;

    // Finalize making the target into a 2-node, childed with the newly created
    // nodes

    target.leftChild = newLeftChild;
    target.rightChild = newRightChild;

    target.centerLeftChild = null;
    target.centerRightChild = null;
    target.centerChild = null;

    return true;
  }

  public boolean addValue(int value) {
    // Case: We have no root! Create one

    if (root == null) {
      root = new TwoFourTreeItem(value);

      System.out.printf("No root; creating new: [ %d | %d | %d ]\n", root.value1, root.value2, root.value3);

      System.out.printf("Finished inserting %d\n", value);
      
      return false;
    }

    // Otherwise, begin walking down the tree

    TwoFourTreeItem current = root;

    while (current != null) {
      // If we stumble upon a 4-node, split it!

      if (current.isFourNode()) {
        System.out.printf("At 4-node [ %d | %d | %d ]; splitting...\n", current.value1, current.value2, current.value3);

        split(current);
      }

      // Case: We hit a leaf node, so we may try inserting at it

      if (current.isLeaf()) {
        System.out.printf("Hit leaf node [ %d | %d | %d ]; ", current.value1, current.value2, current.value3);

        if (current.isTwoNode()) {

          if (value < current.value1) {
            current.value2 = current.value1;
            current.value1 = value;
          }

          else {
            current.value2 = value;
          }

          System.out.printf("after insert: [ %d | %d | %d ]\n", current.value1, current.value2, current.value3);

        }

        else if (current.isThreeNode()) {

          if (value < current.value1) {
            current.value3 = current.value2;
            current.value2 = current.value1;
            current.value1 = value;
          }

          else if (value >= current.value1 && value <= current.value2) {
            current.value3 = current.value2;
            current.value2 = value;
          }

          else {
            current.value3 = value;
          }

          System.out.printf("after insert: [ %d | %d | %d ]\n", current.value1, current.value2, current.value3);
        }

        // Adding a value increases node value count!

        current.values++;
      }

      // Move further down the tree

      if (current.isTwoNode()) {

        System.out.printf("Moving from 2-node [ %d | %d | %d ] to ", current.value1, current.value2, current.value3);

        if (value < current.value1) {
          current = current.leftChild;

          if (current != null) {
            System.out.printf("left child [ %d | %d | %d]\n", current.value1, current.value2, current.value3);
          }

          else {
            System.out.print("left child (NULL)\n");
          }

        }

        else {
          current = current.rightChild;

          if (current != null) {
            System.out.printf("right child [ %d | %d | %d]\n", current.value1, current.value2, current.value3);
          }

          else {
            System.out.print("right child (NULL)\n");
          }

        }

      }

      else if (current.isThreeNode()) {

        System.out.printf("Moving from 3-node [ %d | %d | %d ] to ", current.value1, current.value2, current.value3);

        if (value < current.value1) {
          current = current.leftChild;

          if (current != null) {
            System.out.printf("left child [ %d | %d | %d]\n", current.value1, current.value2, current.value3);
          }

          else {
            System.out.print("left child (NULL)\n");
          }

        }

        else if (value >= current.value1 && value <= current.value2) {
          current = current.centerChild;

          if (current != null) {
            System.out.printf("center child [ %d | %d | %d]\n", current.value1, current.value2, current.value3);
          }

          else {
            System.out.print("center child (NULL)\n");
          }

        }

        else {
          current = current.rightChild;

          if (current != null) {
            System.out.printf("right child [ %d | %d | %d]\n", current.value1, current.value2, current.value3);
          }

          else {
            System.out.print("right child (NULL)\n");
          }

        }

      }

      else if (current.isFourNode()) {

        System.out.printf("Moving from 4-node [ %d | %d | %d ] to ", current.value1, current.value2, current.value3);

        if (value < current.value1) {
          current = current.leftChild;


          if (current != null) {
            System.out.printf("left child [ %d | %d | %d]\n", current.value1, current.value2, current.value3);
          }

          else {
            System.out.print("left child (NULL)\n");
          }
        
        }

        else if (value >= current.value1 && value < current.value2) {
          current = current.centerLeftChild;

          if (current != null) {
            System.out.printf("center left child [ %d | %d | %d]\n", current.value1, current.value2, current.value3);
          }

          else {
            System.out.print("center left child (NULL)\n");
          }

        }

        else if (value >= current.value2 && value <= current.value3) {
          current = current.centerRightChild;

          if (current != null) {
            System.out.printf("center right child [ %d | %d | %d]\n", current.value1, current.value2, current.value3);
          }

          else {
            System.out.print("center right child (NULL)\n");
          }

        }

        else {
          current = current.rightChild;
          
          if (current != null) {
            System.out.printf("right child [ %d | %d | %d]\n", current.value1, current.value2, current.value3);
          }

          else {
            System.out.print("right child (NULL)\n");
          }
        
        }

      }
    
    }

    System.out.printf("Finished inserting %d\n", value);

    return true;
  }

  public boolean hasValue(int value) {
    return false;
  }

  public boolean deleteValue(int value) {
    return false;
  }

  // Helper

  public void printInOrder() {
    if (root != null)
      root.printInOrder(0);
  }

  private void printTreeItem(TwoFourTreeItem item) {
    if (item == null) {
      System.out.print("[ NO NODE ]");
      return;
    }

    System.out.printf("[ %d | %d | %d ]", item.value1, item.value2, item.value3);
  }

  private void splitTest() {
    // Create sample node and give it children

    TwoFourTreeItem test = new TwoFourTreeItem(1, 2);

    test.leftChild = new TwoFourTreeItem(1, 2, 3);
    test.centerLeftChild = new TwoFourTreeItem(3);
    test.centerRightChild = new TwoFourTreeItem(7, 8);
    test.rightChild = new TwoFourTreeItem(5);

    // Show before state

    System.out.println("--- BEFORE ---\n");

    System.out.print("TARGET: ");
    printTreeItem(test);
    System.out.println("\n");

    System.out.print("L: ");
    printTreeItem(test.leftChild);
    System.out.print("\n");
    System.out.print("CL: ");
    printTreeItem(test.centerLeftChild);
    System.out.print("\n");
    System.out.print("CR: ");
    printTreeItem(test.centerRightChild);
    System.out.print("\n");
    System.out.print("R: ");
    printTreeItem(test.rightChild);
    System.out.print("\n");

    System.out.print("\n");

    System.out.print("L of L: ");
    printTreeItem(test.leftChild.leftChild);
    System.out.print("\n");
    System.out.print("R of L: ");
    printTreeItem(test.leftChild.rightChild);
    System.out.print("\n");
    System.out.print("L of R: ");
    printTreeItem(test.rightChild.leftChild);
    System.out.print("\n");
    System.out.print("R of R: ");
    printTreeItem(test.rightChild.rightChild);
    System.out.print("\n");

    // Perform test split

    split(test);

    // Show after state

    System.out.println("\n--- AFTER ---\n");

    System.out.print("TARGET: ");
    printTreeItem(test);
    System.out.println("\n");

    System.out.print("L: ");
    printTreeItem(test.leftChild);
    System.out.print("\n");
    System.out.print("CL: ");
    printTreeItem(test.centerLeftChild);
    System.out.print("\n");
    System.out.print("CR: ");
    printTreeItem(test.centerRightChild);
    System.out.print("\n");
    System.out.print("R: ");
    printTreeItem(test.rightChild);
    System.out.print("\n");

    System.out.print("\n");

    System.out.print("L of L: ");
    printTreeItem(test.leftChild.leftChild);
    System.out.print("\n");
    System.out.print("R of L: ");
    printTreeItem(test.leftChild.rightChild);
    System.out.print("\n");
    System.out.print("L of R: ");
    printTreeItem(test.rightChild.leftChild);
    System.out.print("\n");
    System.out.print("R of R: ");
    printTreeItem(test.rightChild.rightChild);
    System.out.print("\n");
  }

  public TwoFourTree() {

  }
}
