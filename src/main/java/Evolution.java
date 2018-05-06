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

	/**
	 * The method used to generate a mutant of a chromosome
	 * @param original The chromosome to mutate.
	 * @param cityList list of cities, needed to instantiate the new Chromosome.
	 * @return Mutated chromosome.
	 */
	public static Chromosome Mutate(Chromosome original, City [] cityList){
      int [] cityIndexes = original.getCities();
      City [] newCities = new City[cityIndexes.length];

			if(TSP.DEBUG){
				System.out.printf("Original chromosome: %s\n", Arrays.toString(cityIndexes));
				System.out.printf("Cost original chromosome: %s\n", original.getCost());
			}

			int start = TSP.randomGenerator.nextInt(50);
			int end = TSP.randomGenerator.nextInt(50);

			int temp = cityIndexes[start];

			cityIndexes[start] = cityIndexes[end];
			cityIndexes[end] = temp;

      for (int i = 0; i<cityIndexes.length; ++i){
         newCities[i] = cityList[cityIndexes[i]];
      }

			original.setCities(cityIndexes);

			Chromosome child = new Chromosome(original.getCities());

			child.setCities(cityIndexes);

			if(TSP.DEBUG) {
				System.out.printf("Swapped indexes: %s, %s\n", start, end);
				System.out.printf("Mutated chromosome: %s\n", Arrays.toString(child.getCities()));
				System.out.printf("Cost mutated chromosome: %s\n", child.getCost());
				System.out.printf("Number of unique elements in mutated chromosome: %s\n", Arrays.stream(child.getCities()).distinct().count());
			}


 		return child;
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

		 if (TSP.DEBUG){
			 System.out.printf("Probability distribution: %s\n\n", probabilityDistribution.getPmf());
			 double[] distance_old_population = IntStream.range(0,100).mapToDouble(i->chromosomes[i].getCost()).toArray();
			 double[] distance = IntStream.range(0,100).mapToDouble(i->newChromosomes[i].getCost()).toArray();
			 System.out.printf("Distance old selected population: %s\n\n", Arrays.toString(distance_old_population));
			 System.out.printf("Average distance old population: %s\n\n", Arrays.stream(distance_old_population).average());
			 System.out.printf("Distance selected population: %s\n\n", Arrays.toString(distance));
			 System.out.printf("Average distance new population: %s\n\n", Arrays.stream(distance).average());
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

 		if(TSP.DEBUG){
 			 System.out.printf("Parent1: %s \n\n", Arrays.toString(parent1.getCities()));
 			 System.out.printf("Parent2: %s \n\n", Arrays.toString(parent2.getCities()));
 		}

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

 		City [] newCities = new City[cityIndexesParent1.length];

 		for (int i = 0; i<cityIndexesParent1.length; ++i){
 			 newCities[i] = cityList[cityIndexesParent1[i]];
 		}

		Chromosome child = new Chromosome(parent1.getCities());

		child.setCities(cityIndexesParent1);
		child.calculateCost(newCities);

		 if (TSP.DEBUG) {

			 System.out.println("---------------");

			 System.out.printf("Child cities: %s \n", Arrays.toString(child.getCities()));
			 System.out.printf("Number of unique elements in Child: %s\n", Arrays.stream(child.getCities()).distinct().count());
			 System.out.printf("Number of unique elements in Parent1: %s\n", Arrays.stream(parent1.getCities()).distinct().count());
			 System.out.printf("Number of unique elements in Parent2: %s\n", Arrays.stream(parent2.getCities()).distinct().count());
	 	 }

		 return child;
	 }


	/**
	 * Evolve given population to produce the next generation.
	 * @param population The population to evolve.
	 * @param cityList List of ciies, needed for the Chromosome constructor calls you will be doing when mutating and breeding Chromosome instances
	 * @return The new generation of individuals.
	 */
   public static Chromosome [] Evolve(Chromosome [] population, City [] cityList){
      Chromosome [] newPopulation = new Chromosome [population.length];
      for (int i = 0; i<population.length; i++){
				 if (TSP.DEBUG) {
				 	System.out.printf("Individual number: %s\n", i);
			 	 }

				 newPopulation[i] = ((double) TSP.randomGenerator.nextInt(100) / 100) > TSP.mutationRate ? Mutate(population[i], cityList) : population[i];

				 int partner = TSP.randomGenerator.nextInt(population.length);
				 if (TSP.DEBUG) {
					 System.out.printf("Parent1: %s\n", i);
					 System.out.printf("Parent2: %s\n\n", partner);
				 }

				 Chromosome simulatedChild = SimulatedAnnealing.localSearch(population[i], cityList);
				 Chromosome child = Breed(simulatedChild, population[partner], cityList);


				 if (TSP.DEBUG){
					 System.out.printf("Current Individual: %s\n", Arrays.toString(newPopulation[i].getCities()));
					 System.out.printf("Current Individual after breed: %s\n", Arrays.toString(child.getCities()));
					 TSP.scanner.nextLine();
				 }

				 newPopulation[i] = child;
			}

			if (TSP.DEBUG){
				System.out.printf("New population: %s \n\n", Arrays.toString(IntStream.range(0,100).mapToDouble(i->newPopulation[i].getCost()).toArray()));
			}

			Arrays.sort(newPopulation, (a,b) ->
				Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

			if (TSP.DEBUG){
				System.out.printf("Sorted new population: %s \n\n", Arrays.toString(IntStream.range(0,100).mapToDouble(i->newPopulation[i].getCost()).toArray()));
			}

      return selectNewPopulation(newPopulation);
   }
}
