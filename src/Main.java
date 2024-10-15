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

    int amtValuesToAdd = 10000000;
    int[] valuesToAdd = generateUniqueArray(amtValuesToAdd, 0, 2 * amtValuesToAdd);

    for (int i = 0; i < valuesToAdd.length; ++i) {
      tree.addValue(valuesToAdd[i]);
    }

    tree.printInOrder();

    // Perform some random checks

    Random random = new Random();

    int testsAmt = 24;
    int testsMaxBound = valuesToAdd.length + 32;

    for (int i = 0; i < testsAmt; ++i) {
      int randomCheck = random.nextInt(0, testsMaxBound);
      System.out.println("Is " + randomCheck +
                         " in? : " + tree.hasValue(randomCheck));
    }
  }

  // Shit works but is slow AS FUCK
  // nvm shit is fast as FUCK the slow part is printing :) 
}
