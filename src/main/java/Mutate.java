import java.util.Arrays;

public class Mutate{

  public static int[] mutateSwap(int[] indexes, double singleProb){
    int[] newCityIndexes = Arrays.copyOf(indexes, indexes.length);

    for (int city : indexes) {
      Boolean shouldMutate = TSP.randomGenerator.nextDouble() <= singleProb;
      if (shouldMutate) {
        int fromIndex = city;
        int toIndex = TSP.randomGenerator.nextInt(newCityIndexes.length);

        int temp = newCityIndexes[fromIndex];
        newCityIndexes[fromIndex] = newCityIndexes[toIndex];
        newCityIndexes[toIndex] = temp;
      }
    }
    return newCityIndexes;
  }
}
