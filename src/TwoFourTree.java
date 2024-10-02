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

    // Make new children containing split values

    TwoFourTreeItem newLeftChild = new TwoFourTreeItem(target.value1);
    TwoFourTreeItem newRightChild = new TwoFourTreeItem(target.value3);

    // Eliminate values from target to make it 2-node

    target.value1 = target.value2;
    target.value2 = -1;
    target.value3 = -1;

    target.values = 1;

    // Push child node value to parent according to its type (can't be a 4-node)
   
    TwoFourTreeItem parent = target.parent;
    int middleValue = parent.value1;

    if (parent.isTwoNode()){
        
        if (parent.isTwoNode()) {

          if (middleValue < parent.value1) {
            parent.value2 = parent.value1;
            parent.value1 = middleValue;
          }

          else {
            parent.value2 = middleValue;
          }

        }

        else if (parent.isThreeNode()) {

          if (middleValue < parent.value1) {
            parent.value3 = parent.value2;
            parent.value2 = parent.value1;
            parent.value1 = middleValue;
          }

          else if (middleValue >= parent.value1 && middleValue <= parent.value2) {
            parent.value3 = parent.value2;
            parent.value2 = middleValue;
          }

          else {
            parent.value3 = middleValue;
          }
    
      }
    
      parent.values++;

    }
    
    boolean targetIsLeftChild = target == parent.leftChild;
    boolean targetIsRightChild = target == parent.rightChild;
    boolean targetIsCenterChild = target == parent.centerRightChild;
    boolean targetIsCenterLeftChild = target == parent.centerLeftChild;
    boolean targetIsCenterRightChild = target == parent.centerRightChild;

    // These are all the cases we need to cover
    // Also need to ensure we're ok when parent is null
    // RESUME HERE, draw by hand first!

    if (parent.isTwoNode()){
      
      if (targetIsLeftChild){

      }

      else if (targetIsRightChild){

      }
    
    }

    else if (parent.isThreeNode()){
      
      if (targetIsLeftChild){

      }
      
      else if (targetIsCenterChild){

      }

      else if (targetIsRightChild){

      }
    
    }

    else if (parent.isFourNode()){
      
      if (targetIsLeftChild){

      }

      else if (targetIsCenterLeftChild){

      }
      
      else if (targetIsCenterRightChild){

      }

      else if (targetIsRightChild){

      } 
    
    }

    return true;
  
  }

  public boolean addValue(int value) {
    // Case: We have no root! Create one

    if (root == null) {
      root = new TwoFourTreeItem(value);

      return false;
    }

    // Otherwise, begin walking down the tree

    TwoFourTreeItem current = root;

    while (current != null) {
      // If we stumble upon a 4-node, split it!

      if (current.isFourNode()) {
        split(current);
      }

      // Case: We hit a leaf node, so we may try inserting at it

      if (current.isLeaf()) {

        if (current.isTwoNode()) {

          if (value < current.value1) {
            current.value2 = current.value1;
            current.value1 = value;
          }

          else {
            current.value2 = value;
          }

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

        }

        // Adding a value increases node value count!

        current.values++;
      }

      // Move further down the tree

      if (current.isTwoNode()) {

        if (value < current.value1) {
          current = current.leftChild;
        }

        else {
          current = current.rightChild;
        }

      }

      else if (current.isThreeNode()) {

        if (value < current.value1) {
          current = current.leftChild;
        }

        else if (value >= current.value1 && value <= current.value2) {
          current = current.centerChild;
        }

        else {
          current = current.rightChild;
        }

      }

      else if (current.isFourNode()) {

        if (value < current.value1) {
          current = current.leftChild;
        }

        else if (value >= current.value1 && value < current.value2) {
          current = current.centerLeftChild;
        }

        else if (value >= current.value2 && value <= current.value3) {
          current = current.centerRightChild;
        }

        else {
          current = current.rightChild;
        }

      }

    }

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
