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
  }

  TwoFourTreeItem root = null;

  public boolean split(TwoFourTreeItem target) {
    if (!target.isFourNode()) {
      return false;
    }

    // Push child node value to parent according to its type (can't be a 4-node)

    TwoFourTreeItem parent = target.parent;

    // Necessary information to see what case we perform split by

    boolean targetIsLeftChild = target == parent.leftChild;
    boolean targetIsRightChild = target == parent.rightChild;
    boolean targetIsCenterChild = target == parent.centerRightChild;

    boolean parentWasTwoNode = false;
    boolean parentWasThreeNode = false;

    int middleValue = target.value2;

    if (parent.isTwoNode()) {

      if (targetIsLeftChild) {
        parent.value2 = parent.value1;
        parent.value1 = middleValue;
      }

      else if (targetIsRightChild) {
        parent.value2 = middleValue;
      }

      parentWasTwoNode = true;

    }

    else if (parent.isThreeNode()) {

      if (targetIsLeftChild) {
        parent.value3 = parent.value2;
        parent.value2 = parent.value1;
        parent.value1 = middleValue;
      }

      else if (targetIsCenterChild) {
        parent.value3 = parent.value2;
        parent.value2 = middleValue;
      }

      else if (targetIsRightChild) {
        parent.value3 = middleValue;
      }

      parentWasThreeNode = true;

    }

    parent.values++;

    // These are all the cases we need to cover
    // Also need to ensure we're ok when parent is null
    // RESUME HERE, draw by hand first!

    if (parentWasTwoNode) {

      if (targetIsLeftChild) {
        
        // Keep record of data destroyed during relink

        TwoFourTreeItem centerRightChild = target.centerRightChild;
        TwoFourTreeItem rightChild = target.rightChild;

        int value3 = target.value3;

        // Turn target into 2-node

        target.value2 = -1;
        target.value3 = -1;

        target.values -= 2;

        // Move target center left child to target right child now that it is a 2-node

        target.rightChild = target.centerLeftChild;

        // Unbind inner children

        target.centerChild = null;
        target.centerLeftChild = null;
        target.centerRightChild = null;

        // Make new 2-node for value3

        TwoFourTreeItem newNode = new TwoFourTreeItem(value3);

        // Child it to center of parent, which is now a 3-node

        parent.centerChild = newNode;

        // Old 4-node's center left and right child are now this node's left and right

        parent.centerChild.leftChild = centerRightChild;
        parent.centerChild.rightChild = rightChild;

        // Done!

      }

      else if (targetIsRightChild) {

        // Keep ref. to left and center left child of target, as well as value1

        TwoFourTreeItem leftChild = target.leftChild;
        TwoFourTreeItem centerLeftChild = target.centerLeftChild;

        int value1 = target.value1;

        // Make target into 2-node containing only old value3 as value1

        target.value1 = target.value3;

        target.value2 = -1;
        target.value3 = -1;

        target.values -= 2;

        // Rebind center right child to left child; actual right child stays same

        target.leftChild = target.centerRightChild;

        // Unbind inner children

        target.centerChild = null;
        target.centerLeftChild = null;
        target.centerRightChild = null;

        // Make a new node for the kept value1

        TwoFourTreeItem newNode = new TwoFourTreeItem(value1);

        // Make it center child of parent, that's now a 3-node

        parent.centerChild = newNode;

        // Bind kept children as its left and right

        parent.centerChild.leftChild = leftChild;
        parent.centerChild.rightChild = centerLeftChild;

        // Done!

      }

    }

    else if (parentWasThreeNode) {

      if (targetIsLeftChild) {
        
        // Cache required nodes and value
        
        TwoFourTreeItem centerRightChild = target.centerRightChild;
        TwoFourTreeItem rightChild = target.rightChild;

        int value3 = target.value3;

        // Move parent center child to center right

        parent.centerRightChild = parent.centerChild; 

        // Parent (now 4-node) doesn't need a center child definition
        
        parent.centerChild = null;

        // Make target a 2-node with value1

        target.value2 = -1;
        target.value3 = -1;
        
        target.values -= 2;

        // Make new 2-node with target's value3

        TwoFourTreeItem newNode = new TwoFourTreeItem(value3);

        // Make it center left of parent

        parent.centerLeftChild = newNode;
      
        // Bind old children to it

        parent.centerLeftChild.leftChild = centerRightChild;
        parent.centerLeftChild.rightChild = rightChild;
        
        // Done!

      }

      else if (targetIsCenterChild) {
        // Keep track of destroyed items
      
        TwoFourTreeItem centerRightChild = target.centerRightChild;
        TwoFourTreeItem rightChild = target.rightChild;

        int value3 = target.value3;
        
        // Make target the parent's center left

        parent.centerLeftChild = target;

        // No need for center ref anymore
        
        parent.centerChild = null;
        
        // Make target a 2-node w/value1 only

        target.value2 = -1;
        target.value3 = -1;
        
        target.values -= 2;

        // Make target's right child its center left

        target.rightChild = target.centerLeftChild;

        // Remove excess children

        target.centerChild = null;
        target.centerLeftChild = null;
        target.centerRightChild = null;

        // Make node for value3    
        
        TwoFourTreeItem newNode = new TwoFourTreeItem(value3);

        // It is parent's center right child

        parent.centerRightChild = newNode;

        // Child appropriate nodes to it

        parent.centerRightChild.leftChild = centerRightChild;
        parent.centerRightChild.rightChild = rightChild;
      
        // Done!
      
      }

      else if (targetIsRightChild) {

        // Keep necessary refs
        
        TwoFourTreeItem leftChild = target.leftChild;
        TwoFourTreeItem centerLeftChild = target.centerLeftChild;

        int value1 = target.value1;

        // Move parent center to center left

        parent.centerLeftChild = parent.centerChild;
        
        // No need for center ref anymore

        parent.centerChild = null;

        // Make target into 2-node containing value3
        
        target.value1 = target.value3;

        target.value2 = -1;
        target.value3 = -1;

        target.values -= 2;
       
        // Make target's left child its previous center right child 

        target.leftChild = target.centerRightChild;

        // Unlink excess
        
        target.centerChild = null;
        target.centerLeftChild = null;
        target.centerRightChild = null;

        // Make new node containing value1
            
        TwoFourTreeItem newNode = new TwoFourTreeItem(value1);

        // It's the center right child

        parent.centerRightChild = newNode;

        // Link its corresponding children
        
        newNode.leftChild = leftChild;
        newNode.rightChild = centerLeftChild;
        
        // Done!
    
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

  private void printTreeItem(String identifier, TwoFourTreeItem item) {
    if (item == null) {
      System.out.printf("%s : [ NO NODE ]\n", identifier);
      return;
    }

    System.out.printf("%s : [ %d | %d | %d ]\n", identifier, item.value1, item.value2, item.value3);

    printTreeItem(identifier + " -> l", item.leftChild);
    printTreeItem(identifier + " -> cl", item.centerLeftChild);
    printTreeItem(identifier + " -> c", item.centerChild);
    printTreeItem(identifier + " -> cr", item.centerRightChild);
    printTreeItem(identifier + " -> r", item.rightChild);
  }

  private void splitTest_2P_4L() {
    System.out.print("TESTING SPLIT WITH 2-NODE PARENT, 4-NODE LEFT CHILD\n\n");
    
    TwoFourTreeItem p = new TwoFourTreeItem(0);

    p.leftChild = new TwoFourTreeItem(1, 2, 3);

    p.leftChild.parent = p;

    p.leftChild.leftChild = new TwoFourTreeItem(4);
    p.leftChild.centerLeftChild = new TwoFourTreeItem(5);
    p.leftChild.centerRightChild = new TwoFourTreeItem(6);
    p.leftChild.rightChild = new TwoFourTreeItem(7);

    p.leftChild.leftChild.parent = p.leftChild;
    p.leftChild.centerLeftChild.parent = p.leftChild;
    p.leftChild.centerRightChild.parent = p.leftChild;
    p.leftChild.rightChild.parent = p.leftChild;

    p.rightChild = new TwoFourTreeItem(8);

    p.rightChild.parent = p;

    TwoFourTreeItem target = p.leftChild;

    System.out.print("Before:\n\n");
    printTreeItem("p", p);
    System.out.print("\n");

    split(target);

    System.out.print("After:\n\n");
    printTreeItem("p", p);
    System.out.print("\n");
  
  }

  private void splitTest_2P_4R() {
    System.out.print("TESTING SPLIT WITH 2-NODE PARENT, 4-NODE RIGHT CHILD\n\n");
    
    TwoFourTreeItem p = new TwoFourTreeItem(0);

    p.leftChild = new TwoFourTreeItem(1);

    p.leftChild.parent = p;

    p.rightChild = new TwoFourTreeItem(2, 3, 4);

    p.rightChild.parent = p;

    p.rightChild.leftChild = new TwoFourTreeItem(5);
    p.rightChild.centerLeftChild = new TwoFourTreeItem(6);
    p.rightChild.centerRightChild = new TwoFourTreeItem(7);
    p.rightChild.rightChild = new TwoFourTreeItem(8);

    p.rightChild.leftChild.parent = p.rightChild;
    p.rightChild.centerLeftChild.parent = p.rightChild;
    p.rightChild.centerRightChild.parent = p.rightChild;
    p.rightChild.rightChild.parent = p.rightChild;

    TwoFourTreeItem target = p.rightChild;

    System.out.print("Before:\n\n");
    printTreeItem("p", p);
    System.out.print("\n");

    split(target);

    System.out.print("After:\n\n");
    printTreeItem("p", p);
    System.out.print("\n");
  
  }

  public TwoFourTree() {
    splitTest_2P_4L();
    splitTest_2P_4R();
  }
}
