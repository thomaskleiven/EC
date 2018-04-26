import java.util.Arrays;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Evolution{

	private static double mutationRate = 0.9;

	/**
	 * The method used to generate a mutant of a chromosome
	 * @param original The chromosome to mutate.
	 * @param cityList list of cities, needed to instantiate the new Chromosome.
	 * @return Mutated chromosome.
	 */
	public static Chromosome Mutate(Chromosome original, City [] cityList){
      int [] cityIndexes = original.getCities();
      City [] newCities = new City[cityIndexes.length];

      for (int i = 0; i<cityIndexes.length; ++i){
         newCities[i] = cityList[cityIndexes[i]];
      }

      return new Chromosome(newCities);
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

	      City [] newCities = new City[cityIndexesParent1.length];

	      for (int i = 0; i<cityIndexesParent1.length; ++i){
	         newCities[i] = cityList[cityIndexesParent1[i]];
	      }

	      return new Chromosome(newCities);
	   }

	/**
	 * Evolve given population to produce the next generation.
	 * @param population The population to evolve.
	 * @param cityList List of ciies, needed for the Chromosome constructor calls you will be doing when mutating and breeding Chromosome instances
	 * @return The new generation of individuals.
	 */
   public static Chromosome [] Evolve(Chromosome [] population, City [] cityList){
      Chromosome [] newPopulation = new Chromosome [population.length];
      for (int i = 0; i<population.length; ++i){
         newPopulation[i] = ((double) TSP.randomGenerator.nextInt(100) / 10000) > mutationRate ? Mutate(population[i], cityList) : population[i];
				 int partner = TSP.randomGenerator.nextInt(population.length);
				 newPopulation[i] = Breed(population[i], population[partner], cityList);
			}

      return newPopulation;
   }
}
