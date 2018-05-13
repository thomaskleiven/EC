import java.util.Arrays;
import java.lang.Math;

public class SimulatedAnnealing {

  public static double acceptanceProbability(double currentDistance, double newDistance, double temperature) {
    // If the new solution is better, accept it
    if (newDistance < currentDistance) {
      return 1.0;
    }
    // If the new solution is worse, calculate an acceptance probability
    return Math.exp((currentDistance - newDistance) / temperature);
  }

  static double randomDouble(){
    return TSP.randomGenerator.nextInt(1000) / 1000.0;
  }

  public static Chromosome localSearch(Chromosome original, City [] cityList, double[][] distanceMatrix){
    double temp = 4;
    double coolingRate = 0.001;
    int[] originalCityIndexes = original.getCities();

    Chromosome bestChromosome = new Chromosome(original.getCities(), original.getHistoricalDistances());
    bestChromosome.setCost(Utils.getDistanceOfTour(originalCityIndexes, distanceMatrix));

    while (temp > 1){
      int[] newTour = Arrays.copyOfRange(originalCityIndexes, 0, originalCityIndexes.length);
      newTour = Utils.RSM(newTour);

      double currentDistance = bestChromosome.getCost();
      double neighborDistance = Utils.getDistanceOfTour(newTour, distanceMatrix);

      double rand = randomDouble();
      if(acceptanceProbability(currentDistance, neighborDistance, temp) > rand){
        originalCityIndexes = Arrays.copyOfRange(newTour, 0, newTour.length);

        bestChromosome.setCities(newTour);
        bestChromosome.setCost(Utils.getDistanceOfTour(newTour, distanceMatrix));
      }

      temp *= 1 - coolingRate;
    }

    return bestChromosome.getCost() < original.getCost() ? bestChromosome : original;
  }
}
