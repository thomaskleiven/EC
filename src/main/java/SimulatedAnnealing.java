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

  public static Chromosome localSearch(Chromosome original, City [] cityList){
    double temp = 100000;

    if (TSP.DEBUG) {
      System.out.printf("Original cost: %s \n", original.getCost());
    }

    double coolingRate = 0.0001;
    int[] originalCityIndexes = original.getCities();
    int[] bestTour = new int[originalCityIndexes.length];

    Chromosome bestChromosome = new Chromosome(original.getCities());
    bestChromosome.calculateCost(cityList);

    while (temp > 1){
      int[] newTour = Arrays.copyOfRange(originalCityIndexes, 0, originalCityIndexes.length);

      int start = TSP.randomGenerator.nextInt(50);
			int end = TSP.randomGenerator.nextInt(50);

			start = Math.min(start, end);
			end = Math.max(start, end);

			int half = start + ((end + 1) - start) / 2;
			int endCount = end;

			for (int startCount = start; startCount < half; startCount++){
				int store = newTour[startCount];
				newTour[startCount] = newTour[endCount];
				newTour[endCount] = store;
				endCount--;
 			}

      Chromosome solution = new Chromosome(original.getCities());
      solution.setCities(newTour);
      solution.calculateCost(cityList);

      double currentDistance = bestChromosome.getCost();
      double neighborDistance = solution.getCost();

      double rand = randomDouble();
      if(acceptanceProbability(currentDistance, neighborDistance, temp) > rand){
        originalCityIndexes = Arrays.copyOfRange(newTour, 0, newTour.length);

        bestChromosome.setCities(newTour);
        bestChromosome.calculateCost(cityList);
      }


      temp *= 1 - coolingRate;
    }

    return bestChromosome.getCost() < original.getCost() ? bestChromosome : original;
  }
}
