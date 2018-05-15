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
	private Chromosome Mutate(Chromosome original, City [] cityList){
 		  return new Chromosome(Mutate.RSM(original.getCities()), original.getHistoricalDistances());
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

			double bestCostInitialPopulation = population[0].getCost();

      for (int i = 0; i<population.length; i++){
				 boolean shouldMutate = TSP.randomGenerator.nextDouble() < population[i].getMutationRate();
				 newPopulation[i] = shouldMutate ?
				 										Mutate(population[i], cityList) :
														new Chromosome(population[i].getCities(), population[i].getHistoricalDistances());

				 int partner = TSP.randomGenerator.nextInt(100);
				 Chromosome child = Crossover.nPointCrossover(1, newPopulation[i], population[partner], distanceMatrix, generation);

				 if (child.getCost() == bestCostInitialPopulation) {
					 child = SimulatedAnnealing.localSearch(child, cityList, distanceMatrix);
				 }

				 newPopulation[i] = child;
			}

			Arrays.sort(newPopulation, (a,b) ->
				Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

      return TSP.TOURNAMENT ? Selection.tournamentSelection(newPopulation, 5, 2) : Selection.rankedBasedSelection(newPopulation);
   }
}
