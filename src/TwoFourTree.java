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
      return leftChild == null && rightChild == null && centerChild == null &&
          centerLeftChild == null && centerRightChild == null;
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

    // Special case if target is root

    if (target.isRoot()) {
      // Keep cache of target's remaining values and empty it; it will now be
      // the left child of the new root

      int value2 = target.value2;
      int value3 = target.value3;

      target.value2 = -1;
      target.value3 = -1;

      target.values -= 2;

      // Make ctr. left child the right

      target.rightChild = target.centerLeftChild;

      // Cache right and ctr. right

      TwoFourTreeItem centerRightChild = target.centerRightChild;
      TwoFourTreeItem rightChild = target.rightChild;

      // Remove excess children on target

      target.centerChild = null;
      target.centerLeftChild = null;
      target.centerRightChild = null;

      // Make 2 new nodes for value2 and 3 (value2 will be pushed up)

      TwoFourTreeItem newRoot = new TwoFourTreeItem(value2);
      TwoFourTreeItem newRight = new TwoFourTreeItem(value3);

      // Do parent links

      newRoot.leftChild = target;
      newRoot.rightChild = newRight;

      target.parent = newRoot;
      newRoot.rightChild.parent = newRoot;

      // Put cached children where they belong, doing parent links

      newRight.leftChild = centerRightChild;
      newRight.rightChild = rightChild;

      // FIX: the newly set children may be nulls; account for this

      if (newRight.leftChild != null) {
        newRight.leftChild.parent = newRight;
      }

      if (newRight.rightChild != null) {
        newRight.rightChild.parent = newRight;
      }

      // Reassign the root

      this.root = newRoot;

      return true;
    }

    // If our parent isn't a root we go to regular inner node cases

    // Collect necessary information to perform the different cases

    TwoFourTreeItem parent = target.parent;

    boolean targetIsLeftChild = target == parent.leftChild;
    boolean targetIsRightChild = target == parent.rightChild;
    boolean targetIsCenterChild = target == parent.centerChild;

    boolean parentWasTwoNode = false;
    boolean parentWasThreeNode = false;

    int middleValue = target.value2;

    // Phase 1: Push middle value up

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

    // Phase 2: Perform actual node split

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

        // Move target center left child to target right child now that it is a
        // 2-node

        target.rightChild = target.centerLeftChild;

        // Unbind inner children

        target.centerChild = null;
        target.centerLeftChild = null;
        target.centerRightChild = null;

        // Make new 2-node for value3

        TwoFourTreeItem newNode = new TwoFourTreeItem(value3);

        // Child it to center of parent, which is now a 3-node

        parent.centerChild = newNode;

        // FIX: Do parent links
        // FIX: Account for null accesses by using if-guards

        // Old 4-node's center left and right child are now this node's left and
        // right

        newNode.parent = parent;

        parent.centerChild.leftChild = centerRightChild;
        parent.centerChild.rightChild = rightChild;

        if (parent.centerChild.leftChild != null) {
          parent.centerChild.leftChild.parent = parent.centerChild;
        }

        if (parent.centerChild.rightChild != null) {
          parent.centerChild.rightChild.parent = parent.centerChild;
        }

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

        // Rebind center right child to left child; actual right child stays
        // same

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

        // FIX: Do parent links
        // FIX: Account for nullity

        newNode.parent = parent;

        parent.centerChild.leftChild = leftChild;
        parent.centerChild.rightChild = centerLeftChild;

        if (parent.centerChild.leftChild != null) {
          parent.centerChild.leftChild.parent = parent.centerChild;
        }

        if (parent.centerChild.rightChild != null) {
          parent.centerChild.rightChild.parent = parent.centerChild;
        }

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

        // FIX: Give correct right child, unbind excess definitions

        target.rightChild = target.centerLeftChild;

        target.centerLeftChild = null;
        target.centerRightChild = null;

        // Make new 2-node with target's value3

        TwoFourTreeItem newNode = new TwoFourTreeItem(value3);

        // Make it center left of parent

        parent.centerLeftChild = newNode;

        // Bind old children to it

        parent.centerLeftChild.leftChild = centerRightChild;
        parent.centerLeftChild.rightChild = rightChild;

        // FIX: Do parent links
        // FIX: Account for nullity

        newNode.parent = parent;

        if (parent.centerLeftChild.leftChild != null) {
          parent.centerLeftChild.leftChild.parent = parent.centerLeftChild;
        }

        if (parent.centerLeftChild.rightChild != null) {
          parent.centerLeftChild.rightChild.parent = parent.centerLeftChild;
        }

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

        // FIX: Do parent links
        // FIX: Account for nullity

        newNode.parent = parent;

        if (parent.centerRightChild.leftChild != null) {
          parent.centerRightChild.leftChild.parent = parent.centerRightChild;
        }

        if (parent.centerRightChild.rightChild != null) {
          parent.centerRightChild.rightChild.parent = parent.centerRightChild;
        }

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

        parent.centerRightChild.leftChild = leftChild;
        parent.centerRightChild.rightChild = centerLeftChild;

        // FIX: Do parent links
        // FIX: Account for nullity

        newNode.parent = parent;

        if (parent.centerRightChild.leftChild != null) {
          parent.centerRightChild.leftChild.parent = parent.centerRightChild;
        }

        if (parent.centerRightChild.rightChild != null) {
          parent.centerRightChild.rightChild.parent = parent.centerRightChild;
        }

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
      // WARNING: The logic here is not clear
      // Do we split from root-down, or from leaf-up?
      // In the first case, we just split everything we run into
      // In the second case, we'd need an initial root split to trigger upwards splits
      // This logic follows case 1

      // If we stumble upon a 4-node, perform split and then move to correct
      // node from parent

      if (current.isFourNode()) {
        split(current);

        current = current.parent;

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

      // If we hit a leaf node, we try performing insertion here
      // Can only be 2-node or 3-node

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

        // Stop walking

        break;
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
    }

    return false;
  }

  public boolean hasValue(int value) { return false; }

  public boolean deleteValue(int value) { return false; }

  // Helper

  private void printTreeItem(String identifier, TwoFourTreeItem item) {
    if (item == null) {
      System.out.printf("%s : [ NO NODE ]\n", identifier);
      return;
    }

    System.out.printf("%s : [ %d | %d | %d ]\n", identifier, item.value1,
                      item.value2, item.value3);

    printTreeItem(identifier + " -> l", item.leftChild);
    printTreeItem(identifier + " -> cl", item.centerLeftChild);
    printTreeItem(identifier + " -> c", item.centerChild);
    printTreeItem(identifier + " -> cr", item.centerRightChild);
    printTreeItem(identifier + " -> r", item.rightChild);
  }

  public void printTreeWhole() { printTreeItem("root", this.root); }

  private void splitTestCase1() {
    System.out.print("TESTING SPLIT WITH 2-NODE PARENT, 4-NODE LEFT CHILD\n\n");

    TwoFourTreeItem p = new TwoFourTreeItem(0);

    p.leftChild = new TwoFourTreeItem(1, 2, 3);

    p.leftChild.parent = p;

    p.leftChild.leftChild = new TwoFourTreeItem(4);
    p.leftChild.centerLeftChild = new TwoFourTreeItem(5);
    p.leftChild.centerRightChild = new TwoFourTreeItem(6);
    p.leftChild.rightChild = new TwoFourTreeItem(7);

    // important! create parent connection

    p.leftChild.leftChild.parent = p.leftChild;
    p.leftChild.centerLeftChild.parent = p.leftChild;
    p.leftChild.centerRightChild.parent = p.leftChild;
    p.leftChild.rightChild.parent = p.leftChild;

    p.rightChild = new TwoFourTreeItem(8);

    // bind parent

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

  private void splitTestCase2() {
    System.out.print(
        "TESTING SPLIT WITH 2-NODE PARENT, 4-NODE RIGHT CHILD\n\n");

    TwoFourTreeItem p = new TwoFourTreeItem(0);

    p.leftChild = new TwoFourTreeItem(8);

    p.leftChild.parent = p;

    p.rightChild = new TwoFourTreeItem(1, 2, 3);

    p.rightChild.parent = p;

    p.rightChild.leftChild = new TwoFourTreeItem(4);
    p.rightChild.centerLeftChild = new TwoFourTreeItem(5);
    p.rightChild.centerRightChild = new TwoFourTreeItem(6);
    p.rightChild.rightChild = new TwoFourTreeItem(7);

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

  private void splitTestCase3() {
    System.out.print("TESTING SPLIT WITH 3-NODE PARENT, 4-NODE LEFT CHILD\n\n");

    TwoFourTreeItem p = new TwoFourTreeItem(0, 9);

    p.leftChild = new TwoFourTreeItem(1, 2, 3);
    p.leftChild.parent = p;

    p.centerChild = new TwoFourTreeItem(8);
    p.centerChild.parent = p;

    p.rightChild = new TwoFourTreeItem(9);
    p.rightChild.parent = p;

    p.leftChild.leftChild = new TwoFourTreeItem(4);
    p.leftChild.centerLeftChild = new TwoFourTreeItem(5);
    p.leftChild.centerRightChild = new TwoFourTreeItem(6);
    p.leftChild.rightChild = new TwoFourTreeItem(7);

    p.leftChild.leftChild.parent = p.rightChild;
    p.leftChild.centerLeftChild.parent = p.rightChild;
    p.leftChild.centerRightChild.parent = p.rightChild;
    p.leftChild.rightChild.parent = p.rightChild;

    TwoFourTreeItem target = p.leftChild;

    System.out.print("Before:\n\n");
    printTreeItem("p", p);
    System.out.print("\n");

    split(target);

    System.out.print("After:\n\n");
    printTreeItem("p", p);
    System.out.print("\n");
  }

  private void splitTestCase4() {
    System.out.print(
        "TESTING SPLIT WITH 3-NODE PARENT, 4-NODE CENTER CHILD\n\n");

    TwoFourTreeItem p = new TwoFourTreeItem(0, 9);

    p.leftChild = new TwoFourTreeItem(8);
    p.leftChild.parent = p;

    p.centerChild = new TwoFourTreeItem(1, 2, 3);
    p.centerChild.parent = p;

    p.rightChild = new TwoFourTreeItem(10);
    p.rightChild.parent = p;

    p.centerChild.leftChild = new TwoFourTreeItem(4);
    p.centerChild.centerLeftChild = new TwoFourTreeItem(5);
    p.centerChild.centerRightChild = new TwoFourTreeItem(6);
    p.centerChild.rightChild = new TwoFourTreeItem(7);

    p.centerChild.leftChild.parent = p.rightChild;
    p.centerChild.centerLeftChild.parent = p.rightChild;
    p.centerChild.centerRightChild.parent = p.rightChild;
    p.centerChild.rightChild.parent = p.rightChild;

    TwoFourTreeItem target = p.centerChild;

    System.out.print("Before:\n\n");
    printTreeItem("p", p);
    System.out.print("\n");

    split(target);

    System.out.print("After:\n\n");
    printTreeItem("p", p);
    System.out.print("\n");
  }

  private void splitTestCase5() {
    System.out.print(
        "TESTING SPLIT WITH 3-NODE PARENT, 4-NODE RIGHT CHILD\n\n");

    TwoFourTreeItem p = new TwoFourTreeItem(0, 9);

    p.leftChild = new TwoFourTreeItem(8);
    p.leftChild.parent = p;

    p.centerChild = new TwoFourTreeItem(10);
    p.centerChild.parent = p;

    p.rightChild = new TwoFourTreeItem(1, 2, 3);
    p.rightChild.parent = p;

    p.rightChild.leftChild = new TwoFourTreeItem(4);
    p.rightChild.centerLeftChild = new TwoFourTreeItem(5);
    p.rightChild.centerRightChild = new TwoFourTreeItem(6);
    p.rightChild.rightChild = new TwoFourTreeItem(7);

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

  private void splitTestCase6() {
    System.out.print("TESTING SPLIT WITH 4-NODE ROOT\n\n");

    TwoFourTreeItem p = new TwoFourTreeItem(1, 2, 3);

    p.leftChild = new TwoFourTreeItem(4);
    p.leftChild.parent = p;

    p.centerLeftChild = new TwoFourTreeItem(5);
    p.centerLeftChild.parent = p;

    p.centerRightChild = new TwoFourTreeItem(6);
    p.centerRightChild.parent = p;

    p.rightChild = new TwoFourTreeItem(7);
    p.rightChild.parent = p;

    TwoFourTreeItem target = p;

    System.out.print("Before:\n\n");
    printTreeItem("p", p);
    System.out.print("\n");

    split(target);

    System.out.print("After:\n\n");
    printTreeItem("p", root);
    System.out.print("\n");
  }

  public TwoFourTree() {}
}
