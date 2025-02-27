public class TwoFourTree {
  private class TwoFourTreeItem {
    int values = 0;

    int value1 = -1;
    int value2 = -1;
    int value3 = -1;

    public boolean isLeaf = true;

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

    // Gerber Helper

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

      // the newly set children may be nulls; account for this

      if (newRight.leftChild != null) {
        newRight.leftChild.parent = newRight;
      }

      if (newRight.rightChild != null) {
        newRight.rightChild.parent = newRight;
      }

      // Reassign the root

      this.root = newRoot;

      // New root is not a leaf

      this.root.isLeaf = false;

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

        // Do parent links
        // Account for null accesses by using if-guards

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

        // Do parent links
        // Account for nullity

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

        // Give correct right child, unbind excess definitions

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

        // Do parent links
        // Account for nullity

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

        // Do parent links
        // Account for nullity

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

        // Do parent links
        // Account for nullity

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

    // If successfully performed split, then the parent can no longer be a leaf
    target.parent.isLeaf = false;

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
      if (current.isLeaf) {

        // Split leaf 4-node if necessary, moving current to its parent
        if (current.isFourNode()) {
          current = split(current);
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

          // Insertion is done, so stop!
          break;
        }
      }

      // If no leaf hit, traverse downward
      else {
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
            current = current.centerLeftChild;
          }

          else {
            current = current.rightChild;
          }
        }
      }
    }

    return true;
  }

  public boolean hasValue(int value) {
    // Check value against the ones written to node
    // If not present, walk down following ordering until either we find it or
    // we hit a leaf

    TwoFourTreeItem current = root;

    while (current != null) {

      // Walk down

      if (current.isTwoNode()) {

        // Compare value against ones in node
        // We could just put this outside these ifs but I think it's a little
        // more readable this way!

        if (value == current.value1) {
          return true;
        }

        // If not there, move on

        else {
          if (value < current.value1) {
            current = current.leftChild;
          }

          else {
            current = current.rightChild;
          }
        }
      }

      else if (current.isThreeNode()) {
        if (value == current.value1 || value == current.value2) {
          return true;
        }

        else {
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

      else if (current.isFourNode()) {
        if (value == current.value1 || value == current.value2 ||
            value == current.value3) {
          return true;
        }

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

    // If we go past a leaf the value can't exist

    return false;
  }

  void merge(TwoFourTreeItem current) {
    if (!current.isTwoNode()) {
      return;
    }

    // Kill the root if it's a leaf 2-node
    if (current.isRoot() && current.isLeaf) {
      this.root = null;
      return;
    }

    // First, try to borrow

    TwoFourTreeItem parent = current.parent;

    TwoFourTreeItem leftSibling = null;
    TwoFourTreeItem rightSibling = null;

    boolean isLeftChild = current == parent.leftChild;
    boolean isCenterChild = current == parent.centerChild;
    boolean isCenterLeftChild = current == parent.centerLeftChild;
    boolean isCenterRightChild = current == parent.centerRightChild;
    boolean isRightChild = current == parent.rightChild;

    int keyRotatedUp = -1;
    int keyRotatedDown = -1;

    // Grab adequate sibling

    if (parent.isTwoNode()) {
      if (isLeftChild) {
        rightSibling = parent.rightChild;
      }

      else if (isRightChild) {
        leftSibling = parent.leftChild;
      }
    }

    else if (parent.isThreeNode()) {
      if (isLeftChild) {
        rightSibling = parent.centerChild;
      }

      else if (isCenterChild) {
        leftSibling = parent.leftChild;
        rightSibling = parent.rightChild;

      }

      else if (isRightChild) {
        leftSibling = parent.centerChild;
      }
    }

    else if (parent.isFourNode()) {
      if (isLeftChild) {
        rightSibling = parent.centerLeftChild;
      }

      else if (isCenterLeftChild) {
        leftSibling = parent.leftChild;
        rightSibling = parent.centerRightChild;
      }

      else if (isCenterRightChild) {
        leftSibling = parent.centerLeftChild;
        rightSibling = parent.rightChild;
      }

      else if (isRightChild) {
        leftSibling = parent.centerRightChild;
      }
    }

    // Grab adequate key to push up
    // At this stage, we can pop that key from the child; it's okay since we're
    // storing it
    // Once we have the key, we officialize direction of borrow

    boolean borrowingFromLeft = false;
    boolean borrowingFromRight = false;

    if (leftSibling != null && !leftSibling.isTwoNode()) {
      borrowingFromLeft = true;

      // Since we popped from the left's right, remove the rightmost value only
      // Rearrange children as necessary

      if (leftSibling.isThreeNode()) {
        keyRotatedUp = leftSibling.value2;
        leftSibling.value2 = -1;
      }

      else if (leftSibling.isFourNode()) {
        keyRotatedUp = leftSibling.value3;
        leftSibling.value3 = -1;
      }

      leftSibling.values--;
    }

    else if (rightSibling != null && !rightSibling.isTwoNode()) {
      borrowingFromRight = true;

      keyRotatedUp = rightSibling.value1;

      // Since we popped from the right's left, move every value 1 over to the left to
      // keep order

      if (rightSibling.isThreeNode()) {
        rightSibling.value1 = rightSibling.value2;
      }

      else if (rightSibling.isFourNode()) {
        rightSibling.value1 = rightSibling.value2;
        rightSibling.value2 = rightSibling.value3;
      }

      rightSibling.values--;
    }

    // Grab adequate key to push down

    if (parent.isTwoNode()) {
      keyRotatedDown = parent.value1;
    }

    else if (parent.isThreeNode()) {
      if (borrowingFromLeft) {
        if (isCenterChild) {
          keyRotatedDown = parent.value1;
        }

        else if (isRightChild) {
          keyRotatedDown = parent.value2;
        }
      }

      else if (borrowingFromRight) {
        if (isCenterChild) {
          if (isLeftChild) {
            keyRotatedDown = parent.value1;
          }

          else if (isCenterChild) {
            keyRotatedDown = parent.value2;
          }
        }
      }
    }

    else if (parent.isFourNode()) {
      if (borrowingFromLeft) {
        if (isCenterLeftChild) {
          keyRotatedDown = parent.value1;
        }

        else if (isCenterRightChild) {
          keyRotatedDown = parent.value2;
        }

        else if (isRightChild) {
          keyRotatedDown = parent.value3;
        }
      }

      else if (borrowingFromRight) {
        if (isLeftChild) {
          keyRotatedDown = parent.value1;
        }

        else if (isCenterLeftChild) {
          keyRotatedDown = parent.value2;
        }

        else if (isCenterRightChild) {
          keyRotatedDown = parent.value3;
        }
      }
    }

    // Now we should have everything we need to rotate! So atually try to borrow

    if (borrowingFromLeft) {

      // Pull parent value down, growing the borrower
      current.value2 = current.value1;
      current.value1 = keyRotatedDown;

      current.values++;

      // This makes the borrower's children "move one over" to fit the left sibling's
      // right child
      current.centerChild = current.leftChild;

      if (leftSibling.rightChild != null) {
        current.leftChild = leftSibling.rightChild;
        current.leftChild.parent = current;
      }

      // Now push sibling's value up, replacing the parent's key
      if (keyRotatedDown == parent.value1) {
        parent.value1 = keyRotatedUp;
      }

      else if (keyRotatedDown == parent.value2) {
        parent.value2 = keyRotatedUp;
      }

      else if (keyRotatedDown == parent.value3) {
        parent.value3 = keyRotatedUp;
      }

      // This shrinks the lender sibling
      // We already trimmed the value and used necessary child references
      // Rearrange them as necessary depending on whether after shrinking it's a
      // 3-node or 2-node

      if (leftSibling.isTwoNode()) {
        leftSibling.rightChild = leftSibling.centerChild;
      }

      else if (leftSibling.isThreeNode()) {
        leftSibling.centerChild = leftSibling.centerLeftChild;
        leftSibling.rightChild = leftSibling.centerRightChild;
      }

      // Left borrow is done!
      return;
    }

    else if (borrowingFromRight) {

      // Pull parent value down
      current.value2 = keyRotatedDown;

      current.values++;

      // Move over borrower's children and grab lender's left child
      current.centerChild = current.rightChild;

      if (rightSibling.leftChild != null) {
        current.rightChild = rightSibling.leftChild;
        current.rightChild.parent = current;
      }

      // Push sibling value up
      if (keyRotatedDown == parent.value1) {
        parent.value1 = keyRotatedUp;
      }

      else if (keyRotatedDown == parent.value2) {
        parent.value2 = keyRotatedUp;
      }

      else if (keyRotatedDown == parent.value3) {
        parent.value3 = keyRotatedUp;
      }

      // Sibling already shrank; reaccomodate its children
      if (rightSibling.isTwoNode()) {
        rightSibling.leftChild = rightSibling.centerChild;
        rightSibling.centerChild = null;
      }

      else if (rightSibling.isThreeNode()) {
        rightSibling.leftChild = rightSibling.centerLeftChild;
        rightSibling.centerChild = rightSibling.centerRightChild;

        rightSibling.centerLeftChild = null;

        rightSibling.centerRightChild = null;
      }

      // Right borrow is done!
      return;
    }

    // Get here if couldn't borrow

    // Try special merge with 2-node root first...

    if (current.isRoot() || (current.parent.isRoot() && current.parent.isTwoNode())) {

      if (isLeftChild) {
        // Push values to parent
        current.value2 = current.parent.value1;
        current.value3 = current.parent.rightChild.value1;

        // This results in 2 new values
        current.values += 2;

        // Rebind children
        current.centerLeftChild = current.rightChild;
        current.centerChild = null;

        current.centerRightChild = current.parent.rightChild.leftChild;
        current.rightChild = current.parent.rightChild.rightChild;

        // Do links!
        if (current.leftChild != null) {
          current.leftChild.parent = current;
        }

        if (current.centerLeftChild != null) {
          current.centerLeftChild.parent = current;
        }

        if (current.centerRightChild != null) {
          current.centerRightChild.parent = current;
        }

        if (current.rightChild != null) {
          current.rightChild.parent = current;
        }

        // Officialize
        current.parent = null;
        root = current;

        System.out.println();

        return;
      }

      else if (isRightChild) {
        // Push values to parent
        current.value3 = current.value1;
        current.value2 = current.parent.value1;
        current.value1 = current.parent.leftChild.value1;

        // This results in 2 new values
        current.values += 2;

        // Rebind children
        current.centerRightChild = current.leftChild;
        current.centerLeftChild = current.parent.leftChild.rightChild;
        current.leftChild = current.parent.leftChild.leftChild;

        current.centerChild = null;

        // Do links!
        if (current.leftChild != null) {
          current.leftChild.parent = current;
        }

        if (current.centerLeftChild != null) {
          current.centerLeftChild.parent = current;
        }

        if (current.centerRightChild != null) {
          current.centerRightChild.parent = current;
        }

        if (current.rightChild != null) {
          current.rightChild.parent = current;
        }

        // Officialize
        current.parent = null;
        root = current;

        System.out.println();

        return;
      }
    }

    // If merging with non-root parent isn't possible either, move up
    if (!parent.isThreeNode() && !parent.isFourNode()) {
      merge(parent);
    }

    // Need to update these! They might've been moved around
    isLeftChild = current == parent.leftChild;
    isCenterChild = current == parent.centerChild;
    isCenterLeftChild = current == parent.centerLeftChild;
    isCenterRightChild = current == parent.centerRightChild;
    isRightChild = current == parent.rightChild;

    // Siblings too?
    if (parent.isTwoNode()) {
      if (isLeftChild) {
        rightSibling = parent.rightChild;
      }

      else if (isRightChild) {
        leftSibling = parent.leftChild;
      }
    }

    else if (parent.isThreeNode()) {
      if (isLeftChild) {
        rightSibling = parent.centerChild;
      }

      else if (isCenterChild) {
        leftSibling = parent.leftChild;
        rightSibling = parent.rightChild;

      }

      else if (isRightChild) {
        leftSibling = parent.centerChild;
      }
    }

    else if (parent.isFourNode()) {
      if (isLeftChild) {
        rightSibling = parent.centerLeftChild;
      }

      else if (isCenterLeftChild) {
        leftSibling = parent.leftChild;
        rightSibling = parent.centerRightChild;
      }

      else if (isCenterRightChild) {
        leftSibling = parent.centerLeftChild;
        rightSibling = parent.rightChild;
      }

      else if (isRightChild) {
        leftSibling = parent.centerRightChild;
      }
    }

    // Two-node leaves have special merge cases; we're overwriting the value,
    // effectively deleting it
    if (current.isLeaf && parent.isThreeNode()) {
      if (isLeftChild) {
        // Overwrite value with parent's leftmost
        current.value1 = parent.value1;

        // This removes value1 from parent, shrinking it
        parent.value1 = parent.value2;
        parent.value2 = -1;

        parent.values--;

        // Merge with right sibling (middle child of parent)
        // That grows this node and eliminates parent's middle child
        if (rightSibling != null) {
          current.value2 = rightSibling.value1;

          current.values++;

          parent.centerChild = null;
        }

        // Merge done!

        return;
      }

      else if (isCenterChild) {
        // Overwrite value with parent-facing
        current.value1 = parent.value1;

        // Apply change to parent
        parent.value1 = parent.value2;
        parent.value2 = -1;

        parent.values--;

        // Merge with left sibling
        if (leftSibling != null) {
          current.value2 = current.value1;
          current.value1 = leftSibling.value1;

          current.values++;

          parent.centerChild = null;

          parent.leftChild = current;
        }

        // Merge done!
        return;
      }

      else if (isRightChild) {
        // Overwrite with parent-facing
        current.value1 = parent.value2;

        // Apply change to parent
        parent.value2 = -1;

        parent.values--;

        // Merge with left sibling
        if (leftSibling != null) {
          current.value2 = current.value1;
          current.value1 = leftSibling.value1;

          current.values++;

          parent.centerChild = null;
        }

        // Merge done!
        return;
      }

    }

    else if (current.isLeaf && parent.isFourNode()) {
      if (isLeftChild) {
        // This follows the same logic as before, but accounting for the extra node in
        // the parent

        current.value1 = parent.value1;

        parent.value1 = parent.value2;
        parent.value2 = parent.value3;
        parent.value3 = -1;

        parent.values--;

        if (rightSibling != null) {
          current.value2 = rightSibling.value1;

          current.values++;

          parent.centerChild = parent.centerRightChild;

          parent.centerLeftChild = null;
          parent.centerRightChild = null;
        }

        return;
      }

      else if (isCenterLeftChild) {
        current.value1 = parent.value1;

        parent.value1 = parent.value2;
        parent.value2 = parent.value3;
        parent.value3 = -1;

        parent.values--;

        if (leftSibling != null) {
          current.value2 = current.value1;
          current.value1 = leftSibling.value1;

          current.values++;

          parent.leftChild = current;

          parent.centerChild = parent.centerRightChild;

          parent.centerLeftChild = null;
          parent.centerRightChild = null;
        }

        return;
      }

      else if (isCenterRightChild) {
        current.value1 = parent.value2;

        parent.value2 = parent.value3;
        parent.value3 = -1;

        parent.values--;

        if (leftSibling != null) {
          current.value2 = current.value1;
          current.value1 = leftSibling.value1;

          current.values++;

          parent.centerChild = current;

          parent.centerLeftChild = null;
          parent.centerRightChild = null;
        }

        return;
      }

      else if (isRightChild) {
        current.value1 = parent.value3;

        parent.value3 = -1;

        parent.values--;

        if (leftSibling != null) {
          current.value2 = current.value1;
          current.value1 = leftSibling.value1;

          current.values++;

          parent.centerChild = parent.centerLeftChild;

          parent.centerLeftChild = null;
          parent.centerRightChild = null;
        }

        return;
      }
    }

    // If we get here, we're merging an inner node

    if (parent.isThreeNode()) {
      if (isLeftChild) {
        // Borrow parent-facing value
        current.value2 = parent.value1;

        parent.value1 = parent.value2;
        parent.value2 = -1;

        // This shrinks the parent and grows the child
        current.values++;
        parent.values--;

        // Scoot borrower's children to the left
        current.centerChild = current.rightChild;

        // And then attach what used to be the parent's center child to the right
        current.rightChild = parent.centerChild;
        parent.centerChild = null;

        if (current.rightChild != null) {
          current.rightChild.parent = current;
        }

        // Merge done!
        return;
      }

      else if (isCenterChild) {
        // Borrow parent-facing value
        current.value2 = current.value1;
        current.value1 = parent.value1;

        parent.value1 = parent.value2;
        parent.value2 = -1;

        // Shrink parent, grow child
        current.values++;
        parent.values--;

        // Scoot borrower's children to the right
        current.centerChild = current.leftChild;

        // Attach what used to be the parent's left child to the left
        current.leftChild = parent.leftChild;

        if (current.leftChild != null) {
          current.leftChild.parent = current;

          // And move this from the center to the left
          parent.centerChild = null;
          parent.leftChild = current;
        }

        // Move borrower from center to left, since parent is now a 2-node

        // Merge done!
        return;
      }

      else if (isRightChild) {
        // Borrow parent-facing value
        current.value2 = current.value1;
        current.value1 = parent.value2;

        parent.value2 = -1;

        // Shrink parent, grow child
        current.values++;
        parent.values--;

        // Scoot borrower children to the right
        current.centerChild = current.leftChild;

        // Attach what used to be the parent's center child to the left
        current.leftChild = parent.centerChild;
        parent.centerChild = null;

        if (current.leftChild != null) {
          current.leftChild.parent = current;
        }

        // Merge done!
        return;
      }
    }

    else if (parent.isFourNode()) {
      if (isLeftChild) {
        // Borrow parent-facing value
        current.value2 = parent.value1;

        parent.value1 = parent.value2;
        parent.value2 = parent.value3;
        parent.value3 = -1;

        // Shrink parent, grow merger
        current.values++;
        parent.values--;

        // Scoot merger's children to the left
        current.centerChild = current.rightChild;

        // Yank parent's center left child to the right
        current.rightChild = parent.centerLeftChild;
        parent.centerLeftChild = null;

        if (current.rightChild != null) {
          current.rightChild.parent = current;
        }

        // Merge done!
        return;
      }

      else if (isCenterLeftChild) {
        // Borrow parent-facing value
        current.value2 = current.value1;
        current.value1 = parent.value1;

        parent.value1 = parent.value2;
        parent.value2 = parent.value3;
        parent.value3 = -1;

        // Shrink parent, grow merger
        current.values++;
        parent.values--;

        // Scoot merger children to the right
        current.centerChild = current.leftChild;

        // Yank parent left child
        current.leftChild = parent.leftChild;
        parent.leftChild = null;

        if (current.leftChild != null) {
          current.leftChild.parent = current;
        }

        // Merge done!
        return;
      }

      else if (isCenterRightChild) {
        // Borrow parent-facing value
        current.value2 = current.value1;
        current.value1 = parent.value2;

        parent.value2 = parent.value3;
        parent.value3 = -1;

        // Shrink parent, grow merger
        current.values++;
        parent.values--;

        // Scoot merger children to the right
        current.centerChild = current.leftChild;

        // Yank parent center left child
        current.leftChild = parent.centerLeftChild;
        parent.centerLeftChild = null;

        if (current.leftChild != null) {
          current.leftChild.parent = current;
        }

        // Merge done!
        return;
      }

      else if (isRightChild) {
        // Borrow parent-facing value
        current.value2 = current.value1;
        current.value1 = parent.value3;

        parent.value3 = -1;

        // Shrink parent, grow merger
        current.values++;
        parent.values--;

        // Scoot merger children
        current.centerChild = current.leftChild;

        // Yank parent center right child
        current.leftChild = parent.centerRightChild;
        parent.centerLeftChild = null;

        if (current.leftChild != null) {
          current.leftChild.parent = current;
        }

        // Merge done!
        return;
      }
    }
  }

  private int getKeyPosition(TwoFourTreeItem node, int key) {
    // Get the position of the given key in the node; if node doesn't exist or key
    // isn't in the node return -1

    if (node == null) {
      return -1;
    }

    if (node.value1 == key) {
      return 1;
    }

    if (node.value2 == key) {
      return 2;
    }

    if (node.value3 == key) {
      return 3;
    }

    return -1;

  }

  private int searchKeyInLeaf(TwoFourTreeItem node, int key) {
    // Return rightmost or leftmost key in a leaf

    if (node == null) {
      return -1;
    }

    if (node.isTwoNode()) {
      return 1;
    }

    if (key < node.value1) {
      return 1;
    }

    if (key > node.value2 && node.isThreeNode()) {
      return 2;
    }

    if (key > node.value3 && node.isFourNode()) {
      return 3;
    }

    return -1;
  }

  private boolean directDelete(TwoFourTreeItem current, int keyIndex) {
    if (current.isThreeNode()) {
      switch (keyIndex) {
        case 1:
          current.value1 = current.value2;
          current.value2 = -1;
          break;
        case 2:
          current.value2 = -1;
          break;
      }

      // Shrink the leaf
      current.values--;

      // Deletion done!
      return true;
    }

    if (current.isFourNode()) {
      switch (keyIndex) {
        case 1:
          current.value1 = current.value2;
          current.value2 = current.value3;
          current.value3 = -1;
          break;
        case 2:
          current.value2 = current.value3;
          current.value3 = -1;
          break;
        case 3:
          current.value3 = -1;
          break;
      }

      // Shrink the leaf
      current.values--;

      // Deletion done!
      return true;
    }

    return false;

  }

  public TwoFourTreeItem getSuccessor(TwoFourTreeItem start, int key) {
    // Get either smallest or largest inorder successor

    TwoFourTreeItem current;

    if (start.leftChild == null) {
      current = start.rightChild;

      while (current.leftChild != null) {
        current = current.leftChild;
      }

      return current;
    }

    else {
      current = start.leftChild;

      while (current.rightChild != null) {
        current = current.rightChild;
      }

      return current;
    }

  }

  public boolean deleteValue(int value) {
    // Look for the key
    TwoFourTreeItem current = this.root;

    while (getKeyPosition(current, value) == -1) {

      if (current == null) {
        return false;
      }

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
          current = current.centerLeftChild;
        }

        else {
          current = current.rightChild;
        }
      }
    }

    // Will reach here if deletion key has been found, print it!

    // Get position of the key we're trying to delete in the current node
    int keyIndex = getKeyPosition(current, value);

    // If not leaf, do successor
    if (!current.isLeaf) {
      TwoFourTreeItem successor = getSuccessor(current, value);

      // Get the successor key, value, and tag the place it was at with a new delete
      // key
      // -2 is special value to mark an underflowed node
      int successorKey = -1;
      int successorKeyIndex = searchKeyInLeaf(successor, value);

      switch (successorKeyIndex) {
        case 1:
          successorKey = successor.value1;
          successor.value1 = -2;
          break;
        case 2:
          successorKey = successor.value2;
          successor.value2 = -2;
          break;
        case 3:
          successorKey = successor.value3;
          successor.value3 = -2;
          break;
      }

      // Replace current for the successor key's value
      switch (keyIndex) {
        case 1:
          current.value1 = successorKey;
          break;
        case 2:
          current.value2 = successorKey;
          break;
        case 3:
          current.value3 = successorKey;
          break;
      }

      // Change deletion target to symbolic value and move to successor
      value = successor.value1;
      keyIndex = successorKeyIndex;
      current = successor;

    }

    // Try direct delete once...
    boolean didDirectDelete = directDelete(current, keyIndex);
    if (didDirectDelete) {
      return true;
    }

    // If it didn't work, try to merge.
    // If the key still exists after the merge, try direct deletion again

    if (current.isLeaf) {
      if (current.isTwoNode()) {
        merge(current);

        keyIndex = getKeyPosition(current, value);

        // If value not found it was deleted!
        if (keyIndex == -1) {
          return true;
        }

        // Otherwise we need to try direct delete again
        return directDelete(current, keyIndex);
      }
    }

    // Get here if deletion failed very badly
    return false;
  }

  // Helper

  public void printSingleNode(String identifier, TwoFourTreeItem item) {
    if (item == null) {
      System.out.printf("%s : ", identifier);
      return;
    }

    switch (item.values) {
      case 1:
        System.out.printf("%s : [ %d ] (Leaf: %s)", identifier, item.value1, item.isLeaf);
        break;
      case 2:
        System.out.printf("%s : [ %d | %d ] (Leaf: %s)", identifier, item.value1,
            item.value2, item.isLeaf);
        break;
      case 3:
        System.out.printf("%s : [ %d | %d | %d ] (Leaf: %s)", identifier, item.value1,
            item.value2, item.value3, item.isLeaf);
        break;
      default:
        System.out.printf("%s : INVALID VALUE COUNT");
        break;

    }

    if (item.parent != null) {
      printSingleNode(" Parent", item.parent);
    } else {
      System.out.print(" NO PARENT");
    }
  }

  private void printTree(String identifier, TwoFourTreeItem item) {
    if (item == null) {
      System.out.printf("%s : \n", identifier);
      return;
    }

    printSingleNode(identifier, item);
    System.out.println();

    printTree(identifier + " -> l", item.leftChild);
    printTree(identifier + " -> cl", item.centerLeftChild);
    printTree(identifier + " -> c", item.centerChild);
    printTree(identifier + " -> cr", item.centerRightChild);
    printTree(identifier + " -> r", item.rightChild);
  }

  public void printFromRoot() {
    printTree("root", this.root);
  }

  // Gerber Helper

  public void printInOrder() {
    if (root != null)
      root.printInOrder(0);
  }

  public TwoFourTree() {

  }
}
