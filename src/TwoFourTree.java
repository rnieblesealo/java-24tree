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

    // Helper

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

  public boolean split(TwoFourTreeItem target){
    if (!target.isFourNode()){
      return false;
    }

    // Make new children

    TwoFourTreeItem newLeftChild = new TwoFourTreeItem(target.value1);
    TwoFourTreeItem newRightChild = new TwoFourTreeItem(target.value3);

    // Eliminate values from target to make it 2-node

    target.value1 = target.value2;
    target.value2 = -1;
    target.value3 = -1;

    // Relink children of 4-node to newly created nodes

    newLeftChild.leftChild = target.leftChild;
    newLeftChild.rightChild = target.centerLeftChild;

    newRightChild.leftChild = target.centerRightChild;
    newRightChild.rightChild = target.rightChild;

    // Finalize making the target into a 2-node, childed with the newly created nodes

    target.leftChild = newLeftChild;
    target.rightChild = newRightChild;

    target.centerLeftChild = null;
    target.centerRightChild = null;
    target.centerChild = null;

    return true;
  }

  public boolean addValue(int value) {
    return false;
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

  private void printTreeItem(TwoFourTreeItem item){
    if (item == null){
      System.out.print("[ NO NODE ]");
      return;
    }

    System.out.printf("[ %d | %d | %d ]", item.value1, item.value2, item.value3);
  }

  private void splitTest(){
    // Create sample node and give it children

    TwoFourTreeItem test = new TwoFourTreeItem(1, 2, 3);

    test.leftChild = new TwoFourTreeItem(1, 2, 3);
    test.centerLeftChild = new TwoFourTreeItem(3);
    test.centerRightChild = new TwoFourTreeItem(7, 8);
    test.rightChild = new TwoFourTreeItem(5);

    // Show before state

    System.out.println("--- BEFORE ---\n");

    System.out.print("TARGET: "); printTreeItem(test); System.out.println("\n");

    System.out.print("L: "); printTreeItem(test.leftChild); System.out.print("\n");
    System.out.print("CL: "); printTreeItem(test.centerLeftChild); System.out.print("\n");
    System.out.print("CR: "); printTreeItem(test.centerRightChild); System.out.print("\n");
    System.out.print("R: "); printTreeItem(test.rightChild); System.out.print("\n");

    System.out.print("\n");

    System.out.print("L of L: "); printTreeItem(test.leftChild.leftChild); System.out.print("\n");
    System.out.print("R of L: "); printTreeItem(test.leftChild.rightChild); System.out.print("\n");
    System.out.print("L of R: "); printTreeItem(test.rightChild.leftChild); System.out.print("\n");
    System.out.print("R of R: "); printTreeItem(test.rightChild.rightChild); System.out.print("\n");

    // Perform test split

    split(test);

    // Show after state

    System.out.println("\n--- AFTER ---\n");

    System.out.print("TARGET: "); printTreeItem(test); System.out.println("\n");

    System.out.print("L: "); printTreeItem(test.leftChild); System.out.print("\n");
    System.out.print("CL: "); printTreeItem(test.centerLeftChild); System.out.print("\n");
    System.out.print("CR: "); printTreeItem(test.centerRightChild); System.out.print("\n");
    System.out.print("R: "); printTreeItem(test.rightChild); System.out.print("\n");

    System.out.print("\n");

    System.out.print("L of L: "); printTreeItem(test.leftChild.leftChild); System.out.print("\n");
    System.out.print("R of L: "); printTreeItem(test.leftChild.rightChild); System.out.print("\n");
    System.out.print("L of R: "); printTreeItem(test.rightChild.leftChild); System.out.print("\n");
    System.out.print("R of R: "); printTreeItem(test.rightChild.rightChild); System.out.print("\n");
  }

  public TwoFourTree() {
    splitTest();
  }
}