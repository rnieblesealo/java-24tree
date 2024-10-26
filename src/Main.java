import java.lang.Integer;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Main {
  public static int[] generateUniqueArray(int size, int min, int max) {
    if (size > (max - min + 1)) {
      throw new IllegalArgumentException(
          "Range is too small for the requested array size.");
    }

    Set<Integer> uniqueNumbers = new HashSet<>();
    Random random = new Random();

    // Keep adding unique random numbers to the set
    while (uniqueNumbers.size() < size) {
      int num = random.nextInt((max - min) + 1) + min;
      uniqueNumbers.add(num);
    }

    // Convert set to array
    int[] uniqueArray = new int[size];
    int index = 0;
    for (int num : uniqueNumbers) {
      uniqueArray[index++] = num;
    }

    return uniqueArray;
  }

  public static void main(String[] args) {
    TwoFourTree tree = new TwoFourTree();
    
    // Make a random tree
    /*
    int amtValuesToAdd = 10;
    int[] valuesToAdd =
        generateUniqueArray(amtValuesToAdd, 0, 2 * amtValuesToAdd);
    */

    int[] valuesToAdd = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    for (int i = 0; i < valuesToAdd.length; ++i) {
      tree.addValue(valuesToAdd[i]);
    }

    tree.printFromRoot();

    System.out.println("---");

    tree.deleteValue(4);
    tree.deleteValue(6);
    tree.deleteValue(5);
    tree.deleteValue(3);
    tree.deleteValue(2);
    tree.deleteValue(8);
    tree.deleteValue(7);
    tree.deleteValue(9);
    tree.deleteValue(1);
    tree.deleteValue(10);

    tree.printFromRoot();
  }
}
