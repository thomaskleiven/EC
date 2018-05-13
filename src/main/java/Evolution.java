import java.util.Arrays;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.Math;
import java.util.stream.IntStream;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import java.util.Scanner;
import java.util.Collections;
import java.util.stream.Stream;
import java.util.Comparator;

class Evolution{

	public static double mutationRate = 0.72;
	private static int NUM_ELITE = 2;

	/**
	 * The method used to generate a mutant of a chromosome
	 * @param original The chromosome to mutate.
	 * @param cityList list of cities, needed to instantiate the new Chromosome.
	 * @return Mutated chromosome.
	 */
	private Chromosome Mutate(Chromosome original, City [] cityList){
 		  return new Chromosome(Utils.RSM(original.getCities()));
   }


	 private Chromosome[] rankedBasedSelection(final Chromosome[] chromosomes){
		 List<Pair<Integer, Double>> probabilities = new ArrayList<Pair<Integer, Double>>();

		 for (int i = 0; i < chromosomes.length; i++) {
			 double probability = Math.pow(0.95, i);
			 probabilities.add(new Pair<Integer,Double>(i, probability));
		 }

		 final EnumeratedDistribution<Integer> probabilityDistribution =
		 	new EnumeratedDistribution<Integer> (probabilities);

		 probabilityDistribution.reseedRandomGenerator(0);
		 Object[] samples = probabilityDistribution.sample(100);

		 Chromosome[] newPopulation = new Chromosome[samples.length];
		 for (int i = 0; i < samples.length; i++){
			 newPopulation[i] = chromosomes[(int) samples[i]];
		 }

		 return TSP.ELITIST ? eliteSelection(newPopulation) : newPopulation;
   }

	 private static Chromosome[] getChampions(Chromosome[] contestors, int numChampions){
			Arrays.sort(contestors, (a,b) ->
			Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

			return Arrays.copyOfRange(contestors, 0, numChampions+1);
	 }

	 private Chromosome[] getCompetitors(Chromosome[] population, int[] contestors){
		 Chromosome[] competitors = new Chromosome[contestors.length];
		 for (int i = 0; i < contestors.length; i++){
			 competitors[i] = population[contestors[i]];
		 }

		 return competitors;
	 }

	 private Chromosome[] tournamentSelection(Chromosome[] population, int tournamentSize, int numChampions){
		 double[] old_distance = IntStream.range(0,100).mapToDouble(i->population[i].getCost()).toArray();
		 Chromosome[] newPopulation = new Chromosome[population.length];

		 for(int i = 0; i < population.length; i+=numChampions){
			 final int[] contestors = TSP.randomGenerator.ints(0, 50).distinct().limit(tournamentSize).toArray();
			 final Chromosome[] competitors = getCompetitors(population, contestors);
			 final Chromosome[] champions = getChampions(competitors, 1);

			 for(int champ = 0; champ < champions.length; champ++){
				 newPopulation[numChampions == 1 ? i : i+champ] = champions[champ];
			 }
		 }

		 return TSP.ELITIST ? eliteSelection(newPopulation) : newPopulation;
	 }

	 private Chromosome[] eliteSelection(Chromosome[] population){
		 Arrays.sort(population, (a,b) ->
		 Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

		 for (int i = 0; i < NUM_ELITE; i++){
			 population[population.length-(i+1)] = new Chromosome(population[i].getCities());
			 population[population.length-(i+1)].setCost(population[i].getCost());
		 }

		 return population;
	 }



	 /**
 	 * Breed two chromosomes to create a offspring
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

		Chromosome child = new Chromosome(cityIndexesParent1);
		child.calculateCost(cityList);

		if(Arrays.stream(parent2.getCities()).distinct().count() != 50){
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
	 	public static Chromosome nPointCrossover(int n, int[] p1, int[] p2, double[][] distanceMatrix) {
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

			Chromosome child = new Chromosome(newIndexes);
			child.setCost(Utils.getDistanceOfTour(newIndexes, distanceMatrix));

	 		return child;
	 	}


	/**
	 * Evolve given population to produce the next generation.
	 * @param population The population to evolve.
	 * @param cityList List of ciies, needed for the Chromosome constructor calls you will be doing when mutating and breeding Chromosome instances
	 * @return The new generation of individuals.
	 */
   public Chromosome [] evolve(Chromosome [] population, City [] cityList, int generation, double[][] distanceMatrix){
      Chromosome [] newPopulation = new Chromosome [population.length];

      for (int i = 0; i<population.length; i++){
				 boolean shouldMutate = TSP.randomGenerator.nextDouble() < mutationRate;
				 newPopulation[i] = shouldMutate ?
				 										Mutate(population[i], cityList) :
														new Chromosome(population[i].getCities());

				 int partner = TSP.randomGenerator.nextInt(100);

				 Chromosome child = nPointCrossover(1, newPopulation[i].getCities(), population[partner].getCities(), distanceMatrix);
				 newPopulation[i] = SimulatedAnnealing.localSearch(child, cityList, distanceMatrix);
			}

			Arrays.sort(newPopulation, (a,b) ->
				Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

      return TSP.TOURNAMENT ? tournamentSelection(newPopulation, 5, 2) : rankedBasedSelection(newPopulation);
   }
}
