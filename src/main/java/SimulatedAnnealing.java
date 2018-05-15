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
    double temp = 1;
    double annealTemp = temp;
    double coolingRate = 0.9985;
    int[] originalCityIndexes = original.getCities();

    Chromosome bestChromosome = new Chromosome(original.getCities(), original.getHistoricalDistances());
    bestChromosome.setCost(Utils.getDistanceOfTour(originalCityIndexes, distanceMatrix));
    Chromosome candidate = new Chromosome(original.getCityIndexes(), cityList, original.getHistoricalDistances());
    candidate.setCost(Utils.getDistanceOfTour(originalCityIndexes, distanceMatrix));

    int run = 0;
    while (run < 2000){
			int[] mutatedIndexes = Mutate.mutateInversion(candidate.cityList);
			Chromosome mutatedCandidate = new Chromosome(mutatedIndexes, cityList, original.getHistoricalDistances());
			annealTemp = temp * Math.pow(coolingRate, run);

      double currentDistance = candidate.getCost();
      double neighborDistance = Utils.getDistanceOfTour(mutatedCandidate.getCityIndexes(), distanceMatrix);

      double rand = randomDouble();
			if (acceptanceProbability(currentDistance, neighborDistance, temp) > rand) {
				candidate = new Chromosome(mutatedCandidate.getCityIndexes(), cityList, original.getHistoricalDistances());

				if (candidate.getCost() < bestChromosome.getCost()) {
					bestChromosome = new Chromosome(candidate.getCityIndexes(), cityList, original.getHistoricalDistances());
          bestChromosome.setCost(candidate.getCost());
				}
			}
			run++;
		}

		return bestChromosome;
  }
}
