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
	private static int NUM_ELITE = 2;

	/**
	 * The method used to generate a mutant of a chromosome
	 * @param original The chromosome to mutate.
	 * @param cityList list of cities, needed to instantiate the new Chromosome.
	 * @return Mutated chromosome.
	 */
	private Chromosome Mutate(Chromosome original, City [] cityList, double[][] distanceMatrix){
			int[] mutatedIndexes = Mutate.RSM(original.getCities());
			Chromosome child = new Chromosome(mutatedIndexes, original.getHistoricalDistances());
			// child.setCost(Utils.getDistanceOfTour(mutatedIndexes, distanceMatrix));
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
			Arrays.sort(population, (a,b) ->
				Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

      for (int i = 0; i<population.length; i++){
				 boolean shouldMutate = TSP.randomGenerator.nextDouble() < population[i].getMutationRate();
				 newPopulation[i] = shouldMutate ?
				 										Mutate(population[i], cityList, distanceMatrix) :
														new Chromosome(population[i].getCities(), population[i].getHistoricalDistances());

				 int partner = TSP.randomGenerator.nextInt(100);
				 boolean shouldCrossover = TSP.randomGenerator.nextDouble() > 0.2 ? true : false;
				 Chromosome child = shouldCrossover ?
				 										Crossover.nPointCrossover(1, newPopulation[i], population[partner], distanceMatrix, generation)
														: new Chromosome(newPopulation[i].getCities(), newPopulation[i].getHistoricalDistances());

				 child.setCost(Utils.getDistanceOfTour(child.cityList, distanceMatrix));
				 newPopulation[i] = child;
			}

			// Sort in order for easier selection procedure
			Arrays.sort(newPopulation, (a,b) ->
				Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

			// Do a local search if best child is also best in parent generation
			if(population[0].getCost() > newPopulation[0].getCost()){
				newPopulation[0] = SimulatedAnnealing.localSearch(newPopulation[0], cityList, distanceMatrix);
				TSP.simCount++;
			}


			// Return tournament selection by default
      return TSP.TOURNAMENT ? Selection.tournamentSelection(newPopulation, 5, 2) : Selection.rankedBasedSelection(newPopulation);
   }
}
