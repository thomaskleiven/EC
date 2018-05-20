import java.util.Arrays;
import java.util.*;
import java.util.stream.Collectors;


public class Crossover{


  	 /**
   	 * Breed two chromosomes to create a offspring, this is the
     * cycle crossover
   	 * @param parent1 First parent.
   	 * @param parent2 Second parent.
   	 * @param cityList list of cities, needed to instantiate the new Chromosome.
   	 * @return Chromosome resuling from breeding parent.
   	 */
   	private Chromosome Breed(Chromosome parent1, Chromosome parent2, City [] cityList){
   		int [] cityIndexesParent1 = parent1.getCities();
   		int [] cityIndexesParent2 = parent2.getCities();

   		// In orrder to find index
   		List<Integer> parent1Array = Arrays.stream(cityIndexesParent1).boxed().collect(Collectors.toList());

   		int index = TSP.randomGenerator.nextInt(cityIndexesParent1.length);
   		int start_value = cityIndexesParent1[index];
   		int end_value = 0;
   		ArrayList<Integer> swapPositions = new ArrayList<Integer>();

   		while(true) {
   			if( start_value == end_value ){
   				break;
   			}
   			end_value = cityIndexesParent2[index];

   			index = parent1Array.indexOf(end_value);
   			swapPositions.add(index);
   		}

   		int[] temp = parent1.getCities();

   		for( Integer position : swapPositions ){
   			cityIndexesParent1[position] = cityIndexesParent2[position];
   			cityIndexesParent2[position] = temp[position];
   		}

  		Chromosome child = new Chromosome(cityIndexesParent1, parent1.getHistoricalDistances());
  		child.calculateCost(cityList);

      // This one is handy
  		if(Arrays.stream(child.getCities()).distinct().count() != 50){
  			throw new IllegalStateException("Child has not 50 distinct cities");
  		}

  		return child;
  	 }

  	 /**
  	 	 * Performs N-Point Crossover on two parents. Returning only one child.
  	 	 * @param n number of crossover points
  	 	 * @param p1 city indexes of parent 1
  	 	 * @param p2 city indexes of parent 2
  	 	 * @return new city indexes
  	 	 */
  	 	public static Chromosome nPointCrossover(int n, Chromosome parent1, Chromosome parent2, double[][] distanceMatrix, int iter) {
        int[] p1 = parent1.getCities();
        int[] p2 = parent2.getCities();

        int[] newIndexes = new int[p1.length];
  	 		int[] crossoverPoints = new int[n];

  	 		for (int i=0; i<n; i++) {
  	 			int sampledIndex = 0;
  	 			while(Utils.find(crossoverPoints, sampledIndex) >= 0 || sampledIndex == 0) {
  	 				sampledIndex = TSP.randomGenerator.nextInt(p1.length);
  	 			}
  	 			crossoverPoints[i] = sampledIndex;
  	 		}
  	 		Arrays.sort(crossoverPoints);

  	 		boolean swap = false;
  	 		int added = 0; int currIndex = 0;
  	 		int[] arr1 = p1; int[] arr2 = p2;

  	 		int counter = 0;
  	 		while (added+1 < newIndexes.length) {

  	 			if (swap) {
  	 				arr1 = p2;
  	 				arr2 = p1;
  	 				swap = false;
  	 				currIndex = 0;
  	 			}

  	 			for (int i=0; i<p1.length;i++) {
  	 				if (currIndex < crossoverPoints.length) {
  	 					if (i > crossoverPoints[currIndex]) {
  	 						currIndex++;
  	 					}
  	 				}

  	 				if (currIndex % 2 == 0 && Utils.find(newIndexes, arr1[i]) < 0) {
  	 					newIndexes[added] = arr1[i];
  	 					added++;
  	 				} else if (currIndex % 2 != 0 && Utils.find(newIndexes, arr2[i]) < 0) {
  	 					newIndexes[added] = arr2[i];
  	 					added++;
  	 				}
  	 			}

  	 			swap = true;
  	 		}

  			Chromosome child = new Chromosome(newIndexes, parent1.getHistoricalDistances());
  			child.setCost(Utils.getDistanceOfTour(newIndexes, distanceMatrix));

        if(Arrays.stream(child.getCities()).distinct().count() != 50){
          throw new IllegalStateException("Child has not 50 distinct cities");
        }

  	 		return child;
  	 	}
}
