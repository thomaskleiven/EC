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

    return TSP.ELITIST ? eliteSelection(newPopulation) : newPopulation;
  }

  private static Chromosome[] getChampions(Chromosome[] contestors, int numChampions){
     Arrays.sort(contestors, (a,b) ->
     Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

     return Arrays.copyOfRange(contestors, 0, numChampions+1);
  }

  private static Chromosome[] getCompetitors(Chromosome[] population, int[] contestors){
    Chromosome[] competitors = new Chromosome[contestors.length];
    for (int i = 0; i < contestors.length; i++){
      competitors[i] = population[contestors[i]];
    }

    return competitors;
  }

  public static Chromosome[] tournamentSelection(Chromosome[] population, int tournamentSize, int numChampions){
    double[] old_distance = IntStream.range(0,100).mapToDouble(i->population[i].getCost()).toArray();
    Chromosome[] newPopulation = new Chromosome[population.length];

    for(int i = 0; i < population.length; i+=numChampions){
      final int[] contestors = TSP.randomGenerator.ints(0, 50).distinct().limit(tournamentSize).toArray();
      final Chromosome[] competitors = getCompetitors(population, contestors);
      final Chromosome[] champions = getChampions(competitors, 1);

      for(int champ = 0; champ < champions.length; champ++){
        newPopulation[numChampions == 1 ? i : i+champ] = champions[champ];
      }
    }

    return TSP.ELITIST ? eliteSelection(newPopulation) : newPopulation;
  }

  private static Chromosome[] eliteSelection(Chromosome[] population){
    Arrays.sort(population, (a,b) ->
    Double.valueOf(a.getCost()).compareTo(Double.valueOf(b.getCost())));

    for (int i = 0; i < NUM_ELITE; i++){
      population[population.length-(i+1)] = new Chromosome(population[i].getCities());
      population[population.length-(i+1)].setCost(population[i].getCost());
    }

    return population;
  }
}
