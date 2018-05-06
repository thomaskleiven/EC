
import java.util.Arrays;
import java.lang.Math;
import org.apache.commons.lang3.SerializationUtils;

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

    double coolingRate = 0.003;
    int[] originalCityIndexes = original.getCities();

    City[] bestCities = new City[originalCityIndexes.length];
    int[] bestTour = new int[originalCityIndexes.length];

    Chromosome bestChromosome = new Chromosome(original.getCities());
    bestChromosome.calculateCost(cityList);

    while (temp > 1){
      int[] newTour = Arrays.copyOfRange(originalCityIndexes, 0, originalCityIndexes.length);
      City[] newCities = new City[originalCityIndexes.length];

      int tour1 = TSP.randomGenerator.nextInt(50);
      int tour2 = TSP.randomGenerator.nextInt(50);

      while(tour1 == tour2){ tour2 = TSP.randomGenerator.nextInt(50);}

      int citySwap1 = originalCityIndexes[tour1];
      int citySwap2 = originalCityIndexes[tour2];

      newTour[tour1] = citySwap2;
      newTour[tour2] = citySwap1;


      for (int i = 0; i<originalCityIndexes.length; ++i){
         newCities[i] = cityList[originalCityIndexes[i]];
      }

      Chromosome solution = new Chromosome(original.getCities());
      solution.setCities(newTour);
      solution.calculateCost(newCities);

      double currentDistance = bestChromosome.getCost();
      double neighborDistance = solution.getCost();

      double rand = randomDouble();
      if(acceptanceProbability(currentDistance, neighborDistance, temp) > rand){
        originalCityIndexes = Arrays.copyOfRange(newTour, 0, newTour.length);

        if (solution.getCost() < bestChromosome.getCost()){
          bestChromosome.setCities(newTour);
          bestChromosome.calculateCost(newCities);
        }
      }


      temp *= 1 - coolingRate;
    }

    if (TSP.DEBUG){
      System.out.println("Original cost: " + original.getCost());
      System.out.println("New cost: " + bestChromosome.getCost());
      // TSP.scanner.nextLine();
    }
    return bestChromosome.getCost() < original.getCost() ? bestChromosome : original;
  }
}
