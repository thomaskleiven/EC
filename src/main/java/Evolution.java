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
	/**
	 * The method used to generate a mutant of a chromosome
	 * @param original The chromosome to mutate.
	 * @param cityList list of cities, needed to instantiate the new Chromosome.
	 * @return Mutated chromosome.
	 */
	private Chromosome Mutate(Chromosome original, City [] cityList){
 		  return new Chromosome(Mutate.mutateSwap(original.getCities(), 0.02));
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

				 Chromosome child = Crossover.nPointCrossover(1, newPopulation[i].getCities(), population[partner].getCities(), distanceMatrix);
				 newPopulation[i] = SimulatedAnnealing.localSearch(child, cityList, distanceMatrix);
			}

			Arrays.sort(newPopulation, (a,b) ->
				Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

      return TSP.TOURNAMENT ? Selection.tournamentSelection(newPopulation, 5, 2) : Selection.rankedBasedSelection(newPopulation);
   }
}
