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

  public TwoFourTreeItem split(TwoFourTreeItem target) {
    // Split the target node and then return its parent
    // Returns itself if the target was the root or needs no split

    if (!target.isFourNode()) {
      return target;
    }

    // If the parent of target is also a 4-node, we're gonna have some trouble
    // promoting

    // Recursively call this on the parent until it's not a 4-node or it's the
    // root This should take care of backprop splitting on addValue too

    if (!target.isRoot() && target.parent.isFourNode()) {
      split(target.parent);
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

      // Cache right and ctr. right

      TwoFourTreeItem centerRightChild = target.centerRightChild;
      TwoFourTreeItem rightChild = target.rightChild;

      // Make ctr. left child the right

      target.rightChild = target.centerLeftChild;

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

      return newRoot;
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

    return target.parent;
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
      // Stop when a leaf is hit
      if (current.isLeaf()) {
        
        /*
        System.out.println("Stopping...");
        printSingleNode("CURR AT STOP", current);
        */

        // Split leaf 4-node if necessary, moving current to its parent
        if (current.isFourNode()) {

          // System.out.println("Doing split...");

          current = split(current);

          // printSingleNode("CURR AFTER SPLIT", current);
        }

        // If not, perform insertion
        else {
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

          /*
          System.out.println("Insertion done!");
          printSingleNode("CURR AFTER INSERTION", current);
          */

          // Insertion is done, so stop!
          break;
        }
      }

      // If no leaf hit, traverse downward
      else {

        // System.out.println("Walking...");

        if (current.isTwoNode()) {

          // System.out.println("Moving from 2node...");

          if (value < current.value1) {
            current = current.leftChild;
          }

          else {
            current = current.rightChild;
          }
        }

        else if (current.isThreeNode()) {

          // System.out.println("Moving from 3node...");

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

          // System.out.println("Moving from 4node...");
          
          if (value < current.value1) {
            current = current.leftChild;
          }

          else if (value >= current.value1 && value < current.value2) {
            current = current.centerLeftChild;
          }

          else if (value >= current.value2 && value <= current.value3) {
            current = current.centerLeftChild;
          }

          else {
            current = current.rightChild;
          }
        }
      }
    }

    return false;
  }

  public boolean hasValue(int value) { return false; }

  public boolean deleteValue(int value) { return false; }

  // Helper

  private void printSingleNode(String identifier, TwoFourTreeItem item) {
    switch (item.values) {
    case 1:
      System.out.printf("%s : [ %d ]\n", identifier, item.value1);
      break;
    case 2:
      System.out.printf("%s : [ %d | %d ]\n", identifier, item.value1,
                        item.value2);
      break;
    case 3:
      System.out.printf("%s : [ %d | %d | %d ]\n", identifier, item.value1,
                        item.value2, item.value3);
      break;
    default:
      System.out.printf("%s : INVALID VALUE COUNT\n");
      break;
    }
  }

  private void printTree(String identifier, TwoFourTreeItem item) {
    if (item == null) {
      System.out.printf("%s : \n", identifier);
      return;
    }

    printSingleNode(identifier, item);

    printTree(identifier + " -> l", item.leftChild);
    printTree(identifier + " -> cl", item.centerLeftChild);
    printTree(identifier + " -> c", item.centerChild);
    printTree(identifier + " -> cr", item.centerRightChild);
    printTree(identifier + " -> r", item.rightChild);
  }

  public void printFromRoot() { printTree("root", this.root); }

  public TwoFourTree() {}
}
