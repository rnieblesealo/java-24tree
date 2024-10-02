import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.lang.Integer;

public class Main {
  public static int[] generateUniqueArray(int size, int min, int max) {
    if (size > (max - min + 1)) {
      throw new IllegalArgumentException("Range is too small for the requested array size.");
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

    System.out.print("-- CREATING TEST ARRAY --\n\n");

    int[] nums = generateUniqueArray(8, 0, 32);
   
    for (int i = 0; i < 8; ++i) {
      System.out.printf("%d ", nums[i]);
    }

    System.out.print("\n\n-- ADDING TO TREE --\n\n");
   
    for (int i = 0; i < 8; ++i){
      tree.addValue(nums[i]);
    }  

    System.out.print("\n-- PRINTING TREE --\n\n");

    tree.printInOrder();
  }
}
