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
		 Chromosome max = Arrays.stream(chromosomes).max(Comparator.comparingDouble(Chromosome::getCost)).get();
		 List<Pair<Integer, Double>> probabilities = new ArrayList<Pair<Integer, Double>>();

		 for (int i = 0; i < chromosomes.length; i++) {
			 double probability = 1.01 - Math.pow(chromosomes[i].getCost() / max.getCost(), 2);
			 if (i < 15){
				 probability = probability + 5;
			 }
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
		List<Integer> tour1 = Arrays.stream(parent1.getCities()).boxed().collect(Collectors.toList());
		List<Integer> tour2 = Arrays.stream(parent2.getCities()).boxed().collect(Collectors.toList());

		if (TSP.DEBUG){
			System.out.printf("Parent1: %s \n", Arrays.toString(parent1.getCities()));

			System.out.printf("Parent2: %s \n", Arrays.toString(parent2.getCities()));
		}

		      final int size = tour1.size();

		      // choose two random numbers for the start and end indices of the slice
		      // (one can be at index "size")
		      final int number1 = TSP.randomGenerator.nextInt(size - 1);
		      final int number2 = TSP.randomGenerator.nextInt(size);

		      // make the smaller the start and the larger the end
		     	final int start = Math.min(number1, number2);
		      final int end = Math.max(number1, number2);

		      // instantiate two child tours
		      final List<Integer> child1 = new Vector<Integer>();
		      final List<Integer> child2 = new Vector<Integer>();

		      // add the sublist in between the start and end points to the children
		      child1.addAll(tour1.subList(start, end));
		      child2.addAll(tour2.subList(start, end));

		      // iterate over each city in the parent tours
		      int currentCityIndex = 0;
		      int currentCityInTour1 = 0;
		      int currentCityInTour2 = 0;
		      for (int i = 0; i < size; i++ ) {

		        // get the index of the current city
		        currentCityIndex = (end + i) % size;

		        // get the city at the current index in each of the two parent tours
		        currentCityInTour1 = tour1.get(currentCityIndex);
		        currentCityInTour2 = tour2.get(currentCityIndex);

		        // if child 1 does not already contain the current city in tour 2, add it
		        if (!child1.contains(currentCityInTour2)) {
		          child1.add(currentCityInTour2);
		        }

		        // if child 2 does not already contain the current city in tour 1, add it
		        if (!child2.contains(currentCityInTour1)) {
		          child2.add(currentCityInTour1);
		       }
		     }

		     // rotate the lists so the original slice is in the same place as in the
		     // parent tours
		     Collections.rotate(child1, start);
		     Collections.rotate(child2, start);

				 City [] newCities = new City[child1.size()];
				 for (int i = 0; i<child1.size(); ++i){
					 newCities[i] = cityList[child1.get(i)];
				 }

				 // parent1.setCities(child1.stream().mapToInt(i->i).toArray());
				 // parent1.calculateCost(newCities);

				 if (TSP.DEBUG) {
					 System.out.printf("Start index: %s \n", start);
					 System.out.printf("End index: %s \n", end);

					 System.out.println("---------------");


					 System.out.printf("Child1 cities: %s \n", Arrays.toString(child1.toArray()));
					 System.out.printf("Child cities: %s \n", Arrays.toString(parent1.getCities()));
					 System.out.printf("Number of unique elements in Child: %s\n", Arrays.stream(parent1.getCities()).distinct().count());
					 System.out.printf("Number of unique elements in Parent1: %s\n", Arrays.stream(parent1.getCities()).distinct().count());
					 System.out.printf("Number of unique elements in Parent2: %s\n", Arrays.stream(parent2.getCities()).distinct().count());
			 	 }

				 Chromosome a = new Chromosome(newCities);
				 a.setCities(child1.stream().mapToInt(i->i).toArray());
				 a.calculateCost(newCities);

				 return a;
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
				 newPopulation[i] = ((double) TSP.randomGenerator.nextInt(100) / 100) > mutationRate ? Mutate(population[i], cityList) : population[i];
				 int partner = TSP.randomGenerator.nextInt(population.length);
				 if (TSP.DEBUG) {
					 System.out.printf("Parent1: %s\n", i);
					 System.out.printf("Parent2: %s\n", partner);
				 }

				 Chromosome child = Breed(population[i], population[partner], cityList);


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
