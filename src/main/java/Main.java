import java.util.Arrays;
import java.util.stream.IntStream;

public class Main{
  private int populationSize;

  private City[] cities;
  private City[] originalCities;
  private Chromosome[] chromosomes;

  private double min;
  private double avg;
  private double max;
  private double sum;
  private double genMin;

  private double[][] distanceMatrix;

  public Main(City[] cities, int populationSize, int runs){
    this.cities = cities;
    this.populationSize = populationSize;
    this.distanceMatrix = new double[populationSize][populationSize];

    this.run(runs);
  }

  private void initChromosomes(){
    this.chromosomes = new Chromosome[this.populationSize];
    for (int x = 0; x<this.populationSize; x++){
      chromosomes[x] = new Chromosome(this.cities);
      chromosomes[x].shuffleChromosome(this.cities);
      chromosomes[x].calculateCost(this.cities);
    }
  }

  private void run(int runs){

    for(int i = 1; i <= runs; i++){
      this.genMin = 0;
      System.out.printf("Run " + i + "\n");

      initChromosomes();
      int generation = 0;
      double bestCostCurrentPopulation = 0;
      Utils.buildMatrix(this.cities, this.distanceMatrix);
      Evolution evolution = new Evolution();

      while (generation < 100){
        this.chromosomes = evolution.evolve(this.chromosomes, this.cities, generation);
        if(generation % 5 == 0){
          this.cities = TSP.MoveCities(this.cities);
          Utils.buildMatrix(this.cities, this.distanceMatrix);
        }

        System.out.println(this.distanceMatrix[1][20]);

        if(generation % 6 == 0 && generation !=0) break;
        generation++;
      }




    }
  }


}
