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
    double t0 = 1;
    double temp = t0;
    double coolingRate = 0.99;
    int[] originalCityIndexes = original.getCities();

    Chromosome bestChromosome = new Chromosome(original.getCities(), original.getHistoricalDistances());
    bestChromosome.setCost(Utils.getDistanceOfTour(originalCityIndexes, distanceMatrix));
    Chromosome candidate = new Chromosome(original.getCityIndexes(), cityList, original.getHistoricalDistances());
    candidate.setCost(Utils.getDistanceOfTour(originalCityIndexes, distanceMatrix));

    int run = 0;
    while (run < 5000){
			int[] mutatedIndexes = Mutate.RSM(candidate.cityList);
			temp = t0 * Math.pow(coolingRate, run++);

      double currentDistance = candidate.getCost();
      double neighborDistance = Utils.getDistanceOfTour(mutatedIndexes, distanceMatrix);

      double rand = randomDouble();
			if (acceptanceProbability(currentDistance, neighborDistance, temp) > rand) {
				candidate = new Chromosome(mutatedIndexes, cityList, original.getHistoricalDistances());

				if (candidate.getCost() < bestChromosome.getCost()) {
					bestChromosome = new Chromosome(candidate.getCityIndexes(), cityList, original.getHistoricalDistances());
          bestChromosome.setCost(candidate.getCost());
				}
			}
		}

		return bestChromosome;
  }
}
