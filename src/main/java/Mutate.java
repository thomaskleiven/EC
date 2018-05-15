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

  /**
	 * Mutation by sampling two indexes and reversing the order of the
	 * elements in between them.
	 * @param cityIndexes the vector of numbers to mutate
	 * @return mutated vector
	 */
	public static int[] mutateInversion(int[] cityIndexes) {
		int[] indexes = Arrays.copyOf(cityIndexes, cityIndexes.length);

		int index1=0; int index2=0;
		while(index1==index2) {
			index1 = TSP.randomGenerator.nextInt(indexes.length);
			index2 = TSP.randomGenerator.nextInt(indexes.length);
		}

		int start = Math.min(index1, index2);
		int end = Math.max(index1,index2);

		while (start <= end) {
			int temp = indexes[start];
			indexes[start] = indexes[end];
			indexes[end] = temp;
			start++; end--;
		}

		return indexes;
	}
}
