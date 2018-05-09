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

	public static double mutationRate = 0.85;

	/**
	 * The method used to generate a mutant of a chromosome
	 * @param original The chromosome to mutate.
	 * @param cityList list of cities, needed to instantiate the new Chromosome.
	 * @return Mutated chromosome.
	 */
	public static Chromosome Mutate(Chromosome original, City [] cityList){
 		  return new Chromosome(Utils.RSM(original.getCities()));
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

		Chromosome child = new Chromosome(cityIndexesParent1);
		child.setCost(Utils.getDistanceOfTour(cityIndexesParent1));

		return child;
	 }

	/**
	 * Evolve given population to produce the next generation.
	 * @param population The population to evolve.
	 * @param cityList List of ciies, needed for the Chromosome constructor calls you will be doing when mutating and breeding Chromosome instances
	 * @return The new generation of individuals.
	 */
   public static Chromosome [] Evolve(Chromosome [] population, City [] cityList, int generation){
      Chromosome [] newPopulation = new Chromosome [population.length];

      for (int i = 0; i<population.length; i++){
				 boolean shouldMutate = TSP.randomGenerator.nextDouble() > mutationRate;
				 newPopulation[i] = shouldMutate ?
				 										Mutate(population[i], cityList) :
														new Chromosome(population[i].getCities());

				 int partner = TSP.randomGenerator.nextInt(100);
				 Chromosome child = Breed(newPopulation[i], population[partner], cityList);
				 newPopulation[i] = SimulatedAnnealing.localSearch(child, cityList);
			}

			Arrays.sort(newPopulation, (a,b) ->
				Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

      return selectNewPopulation(newPopulation);
   }
}
