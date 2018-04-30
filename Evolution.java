import java.util.Arrays;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.Math;
import java.util.stream.IntStream;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import java.util.Scanner;
import java.util.Collections;
import java.util.stream.Stream;
import java.util.Comparator;

class Evolution{

	private static double mutationRate = 0.85;

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
			original.calculateCost(newCities);

			if(TSP.DEBUG) {
				System.out.printf("Swapped indexes: %s, %s\n", start, end);
				System.out.printf("Mutated chromosome: %s\n", Arrays.toString(original.getCities()));
				System.out.printf("Cost mutated chromosome: %s\n", original.getCost());
				System.out.printf("Number of unique elements in mutated chromosome: %s\n", Arrays.stream(original.getCities()).distinct().count());
			}

      return original;
   }


	 private static Chromosome[] selectNewPopulation(final Chromosome[] chromosomes){

		 Chromosome[] newChromosomes = Stream.of(
		 	Arrays.copyOfRange(chromosomes, 0, 25),
		 	Arrays.copyOfRange(chromosomes, 0, 25),
		 	Arrays.copyOfRange(chromosomes, 0, 25),
		 	Arrays.copyOfRange(chromosomes, 0, 25)
		 )
		 .flatMap(Stream::of).toArray(Chromosome[]::new);

		 if (TSP.DEBUG){
			 double[] distance = IntStream.range(0,100).mapToDouble(i->newChromosomes[i].getCost()).toArray();
			 System.out.printf("Distance: %s\n", Arrays.toString(distance));
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
			System.out.printf("Parent1: %s \n", Arrays.toString(parent1.getCities()));
	 }

	 // In orrder to find index
	 List<Integer> parent1Array = Arrays.stream(cityIndexesParent1).boxed().collect(Collectors.toList());

	 int index = TSP.randomGenerator.nextInt(cityIndexesParent1.length);
	 int start_value = cityIndexesParent1[index];
	 int end_value = 0;
	 ArrayList<Integer> swapPositions = new ArrayList<Integer>();

	 if(TSP.DEBUG){
			System.out.printf("Index: %s \n", index);
			System.out.printf("Start value: %s \n", start_value);
	 }

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

		parent1.setCities(cityIndexesParent1);
		parent1.calculateCost(newCities);

		if (TSP.DEBUG) {
			System.out.printf("Parent2: %s \n", Arrays.toString(parent2.getCities()));

			System.out.println("---------------");


			System.out.printf("Child1 cities: %s \n", Arrays.toString(cityIndexesParent1));
			System.out.printf("Child cities: %s \n", Arrays.toString(parent1.getCities()));
			System.out.printf("Number of unique elements in Child: %s\n", Arrays.stream(parent1.getCities()).distinct().count());
			System.out.printf("Number of unique elements in Parent1: %s\n", Arrays.stream(parent1.getCities()).distinct().count());
			System.out.printf("Number of unique elements in Parent2: %s\n", Arrays.stream(parent2.getCities()).distinct().count());
		}

		return parent1;
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
         	newPopulation[i] = ((double) TSP.randomGenerator.nextInt(100) / 100) > mutationRate ? Mutate(population[i], cityList) : population[i];
			 	 }
				 int partner = TSP.randomGenerator.nextInt(population.length);

				 Chromosome child = Breed(population[i], population[partner], cityList);

				 if (TSP.DEBUG) {
					 System.out.printf("Second city: %s", cityList[1]);
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
