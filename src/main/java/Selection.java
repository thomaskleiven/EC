import java.util.Arrays;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.Math;
import java.util.stream.IntStream;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;


public class Selection{

  private static int NUM_ELITE = 2;

  /**
	 * Ranked based selection
	 * @param chromosome The population to evolve.
	 * @return The new generation of individuals.
	 */
  public static Chromosome[] rankedBasedSelection(final Chromosome[] chromosomes){
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

    return TSP.ELITIST ? eliteSelection(newPopulation, chromosomes) : newPopulation;
  }

  /**
	 * Collect the champions
	 * @param Chromosome the contestors
   * @param int numChampions
	 * @return The chromosomes with best fitness
	 */
  private static Chromosome[] getChampions(Chromosome[] contestors, int numChampions){
     Arrays.sort(contestors, (a,b) ->
     Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

     return Arrays.copyOfRange(contestors, 0, numChampions+1);
  }

  /**
	 * Collect the competitors
	 * @param population The population to evolve.
	 * @param int tournamentSize
	 * @return The competitos.
	 */
  private static Chromosome[] getCompetitors(Chromosome[] population, int tournamentSize){
    Chromosome[] competitors = new Chromosome[tournamentSize];
    for (int i = 0; i < tournamentSize; i++){
      competitors[i] = population[TSP.randomGenerator.nextInt(50)];
    }

    return competitors;
  }

  /**
	 * Tournament selection.
	 * @param population The population to evolve.
	 * @param int tournamentSize
   * @param int numChampions
	 * @return The new generation of individuals.
	 */
  public static Chromosome[] tournamentSelection(Chromosome[] population, int tournamentSize, int numChampions){

    Chromosome[] newPopulation = new Chromosome[population.length];
    int counter = 0;

    while(true){
      final Chromosome[] competitors = getCompetitors(population, tournamentSize);
      final Chromosome[] champions = getChampions(competitors, numChampions);

      for(int champ = 0; champ < champions.length; champ++){
        newPopulation[counter++] = champions[champ];
        if(counter == population.length) break;
      }
      if(counter == population.length) break;
    }

    return TSP.ELITIST ? eliteSelection(newPopulation, population) : newPopulation;
  }

  /**
   * The elite selection.
   * @param newPopulation The new population.
   * @param population The old sorted population
   * @return The new generation of individuals.
   */
  private static Chromosome[] eliteSelection(Chromosome[] newPopulation, Chromosome[] population){
    for (int i = 0; i < NUM_ELITE; i++){
      newPopulation[i] = new Chromosome(population[i].cityList, population[i].getHistoricalDistances());
      newPopulation[i].setCost(population[i].getCost());
    }

    return newPopulation;
  }
}
