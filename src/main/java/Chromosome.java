import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.HashMap;

final class Chromosome {

    private double mutationRate = 0.8;
    private double[] historicalDistances = new double[10];

    /**
     * The list of cities, which are the genes of this chromosome.
     */
    protected int[] cityList;

    /**
     * The cost of following the cityList order of this chromosome.
     */
    protected double cost;

    /**
     * Stores extra data about Chromosome needed for advaned mutations
     * and crossovers
     */
    protected HashMap<String, Object> metaData = new HashMap<>();

    /**
     * The number of times calculateCost() is called
     */
     protected static int numberOfPathLengthCalculations = 0;

     /**
     * Returns how many times the length of a TSP tour was calculated.
     */
    public static int getNumberOfPathLengthCalculations(){
      return numberOfPathLengthCalculations;
    }

    /**
     * Increments how many times the length of a TSP tour was calculated.
     */
    public static void incrementNumberOfPathLengthCalculations(){
         ++numberOfPathLengthCalculations;
    }

    /**
     * @param cities The order that this chromosome would visit the cities.
     */
    Chromosome(int[] cityOrder, double[] historicalDistances) {
      this.cityList = cityOrder;
      this.historicalDistances = historicalDistances;
    }

    Chromosome(int[] cityOrder) {
      this.cityList = cityOrder;
    }

    Chromosome(){}

  Chromosome(int[] cityIndexes, City[] cities,  double[] historicalDistances) {
     this.cityList = cityIndexes;
     calculateCost(cities);
     this.historicalDistances = historicalDistances;
 }

    public void setMutationRate(double mutationRate){
      this.mutationRate = mutationRate;
    }

    public double getMutationRate(){
      return this.mutationRate;
    }

    public double[] getHistoricalDistances(){
      return this.historicalDistances;
    }

    private void adaptMutationRate(double distance){
      int num_better = 0;
      for (int i = 0; i < this.historicalDistances.length; i++){
        if(distance < this.historicalDistances[i]) num_better++;
      }

      if(num_better > 2){
        setMutationRate(getMutationRate() *0.7);
      } else {
        setMutationRate(getMutationRate() /0.7);
      }
    }

    public void addHistoricalDistance(double distance, int index){
      this.historicalDistances[index%10] = distance;

      if (index > 10){
        adaptMutationRate(distance);
      }
    }

    void shuffleChromosome(City[] cities) {
      cityList = new int[cities.length];
      //cities are visited based on the order of an integer representation [o,n] of each of the n cities.
      for (int x = 0; x < cities.length; x++) {
          cityList[x] = x;
      }

      //shuffle the order so we have a random initial order
      for (int y = 0; y < cityList.length; y++) {
          int temp = cityList[y];
          int randomNum = TSP.randomGenerator.nextInt(cityList.length);
          cityList[y] = cityList[randomNum];
          cityList[randomNum] = temp;
      }
    }

    /**
     * Calculate the cost of the specified list of cities.
     *
     * @param cities A list of cities.
     */
    void calculateCost(City[] cities) {
        cost = 0;
        for (int i = 0; i < cityList.length - 1; i++) {
            double dist = cities[cityList[i]].proximity(cities[cityList[i + 1]]);
            cost += dist;
        }

        cost += cities[cityList[0]].proximity(cities[cityList[cityList.length - 1]]); //Adding return home
        incrementNumberOfPathLengthCalculations();

    }

    public int[] getCityIndexes(){
      return this.cityList;
    }

    /**
     * Get the cost for this chromosome. This is the amount of distance that
     * must be traveled.
     */
    double getCost() {
        return cost;
    }

    void setCost(double cost){
      this.cost = cost;
    }

    /**
     * @param i The city you want.
     * @return The ith city.
     */
    int getCity(int i) {
        return cityList[i];
    }

    /**
     * Set the order of cities that this chromosome would visit.
     *
     * @param list A list of cities.
     */
    void setCities(int[] list) {
        for (int i = 0; i < cityList.length; i++) {
            cityList[i] = list[i];
        }
    }

    /**
     * Get the order of cities that this chromosome would visit.
     */
    public int [] getCities() {
        return Arrays.copyOfRange(cityList, 0, cityList.length);
    }

    /**
     * Set the index'th city in the city list.
     *
     * @param index The city index to change
     * @param value The city number to place into the index.
     */
    void setCity(int index, int value) {
        cityList[index] = value;
    }

    /**
     * Sort the chromosomes by their cost.
     *
     * @param chromosomes An array of chromosomes to sort.
     * @param num         How much of the chromosome list to sort.
     */
    public static void sortChromosomes(Chromosome chromosomes[], int num) {
        Chromosome ctemp;
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < num - 1; i++) {
                if (chromosomes[i].getCost() > chromosomes[i + 1].getCost()) {
                    ctemp = chromosomes[i];
                    chromosomes[i] = chromosomes[i + 1];
                    chromosomes[i + 1] = ctemp;
                    swapped = true;
                }
            }
        }
    }

    /**
     * Gets data in metaData HashMap
     * @return MetaData of Chromosome
     */
	public HashMap<String, Object> getMetaData() {
		return metaData;
	}



}
