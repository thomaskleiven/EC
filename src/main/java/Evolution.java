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

	public static double mutationRate = 0.7;

	/**
	 * The method used to generate a mutant of a chromosome
	 * @param original The chromosome to mutate.
	 * @param cityList list of cities, needed to instantiate the new Chromosome.
	 * @return Mutated chromosome.
	 */
	public static Chromosome Mutate(Chromosome original, City [] cityList){
      int [] cityIndexes = original.getCities();

			int start = TSP.randomGenerator.nextInt(50);
			int end = TSP.randomGenerator.nextInt(50);

			start = Math.min(start, end);
			end = Math.max(start, end);

			int half = start + ((end + 1) - start) / 2;
			int endCount = end;

			for (int startCount = start; startCount < half; startCount++){
				int store = cityIndexes[startCount];
				cityIndexes[startCount] = cityIndexes[endCount];
				cityIndexes[endCount] = store;
				endCount--;
 			}

 		  return new Chromosome(cityIndexes);
   }


	 private static Chromosome[] selectNewPopulation(final Chromosome[] chromosomes){
		 Chromosome max = Arrays.stream(chromosomes).max(Comparator.comparingDouble(Chromosome::getCost)).get();
		 List<Pair<Integer, Double>> probabilities = new ArrayList<Pair<Integer, Double>>();

		 for (int i = 0; i < chromosomes.length; i++) {
			 double probability = Math.pow(0.95, i);
			 probabilities.add(new Pair<Integer,Double>(i, probability));
		 }

		 final EnumeratedDistribution<Integer> probabilityDistribution =
		 	new EnumeratedDistribution<Integer> (probabilities);

		 probabilityDistribution.reseedRandomGenerator(0);
		 Object[] samples = probabilityDistribution.sample(100);

		 Chromosome[] newChromosomes = new Chromosome[samples.length];
		 for (int i = 0; i < samples.length; i++){
			 newChromosomes[i] = chromosomes[(int) samples[i]];
		 }

		 return newChromosomes;
   }



	 /**
 	 * Breed two chromosomes to create a offspring
 	 * @param parent1 First parent.
 	 * @param parent2 Second parent.
 	 * @param cityList list of cities, needed to instantiate the new Chromosome.
 	 * @return Chromosome resuling from breeding parent.
 	 */
 	public static Chromosome Breed(Chromosome parent1, Chromosome parent2, City [] cityList){
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

		Chromosome child = new Chromosome(parent1.getCities());

		child.setCities(cityIndexesParent1);
		child.calculateCost(cityList);
		return child;
	 }

	public static void increaseOrDecreaseMutationRate(double[] formerIndividuals, double newCost){
		int numberOfBetterIndividuals = 0;
		for(int i = 0; i < formerIndividuals.length; i++){
			if (formerIndividuals[i] < newCost) numberOfBetterIndividuals++;
		}

		mutationRate = 	((double) numberOfBetterIndividuals / formerIndividuals.length > 0.25) ?
										(mutationRate *= 0.85) : (mutationRate /= 0.85);


		System.out.println("Number of better individuals: " + numberOfBetterIndividuals);
		System.out.printf("Former individuals: %s\n", Arrays.toString(formerIndividuals));
		System.out.printf("New cost: %s\n", newCost);
		System.out.printf("Mutation rate: %s\n", mutationRate);
		System.out.printf("Success rate: %s\n", (double) numberOfBetterIndividuals / formerIndividuals.length);



		TSP.scanner.nextLine();
	}


	/**
	 * Evolve given population to produce the next generation.
	 * @param population The population to evolve.
	 * @param cityList List of ciies, needed for the Chromosome constructor calls you will be doing when mutating and breeding Chromosome instances
	 * @return The new generation of individuals.
	 */
   public static Chromosome [] Evolve(Chromosome [] population, City [] cityList, int generation){
      Chromosome [] newPopulation = new Chromosome [population.length];

			double[] formerIndividuals = new double[20];
			int counter = 0;

      for (int i = 0; i<population.length; i++){
				 newPopulation[i] = ((double) TSP.randomGenerator.nextInt(1000) / 1000) > mutationRate ?
				 										Mutate(population[i], cityList) :
														new Chromosome(population[i].getCities());

				 int partner = TSP.randomGenerator.nextInt(100);
				 Chromosome child = Breed(newPopulation[i], population[partner], cityList);
				 newPopulation[i] = SimulatedAnnealing.localSearch(child, cityList);

				 formerIndividuals[counter++] = child.getCost();
				 if (i % 19 == 0 && i != 0) counter = 0;
				 // if(i > 19) increaseOrDecreaseMutationRate(formerIndividuals, child.getCost());
			}

			Arrays.sort(newPopulation, (a,b) ->
				Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

		  if (generation == 99) mutationRate = 0.7;

      return selectNewPopulation(newPopulation);
   }
}
