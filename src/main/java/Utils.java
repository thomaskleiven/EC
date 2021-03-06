import java.lang.Math;
import java.util.Arrays;

public class Utils{

  // Get mean of data
  public static double getMean(double[] data){
    double mean = 0.0;
    for (int i = 0; i < data.length; i++) {
      mean += data[i];
    }
    return mean /= data.length;
  }

  // Get variance of data
  public static double getVariance(double[] data){
    double variance = 0.0;
    double mean = getMean(data);
    for (int i = 0; i < data.length; i++) {
      variance += (data[i] - mean) * (data[i] - mean);
    }
    return variance /= data.length;
  }

  // Get std of data
  public static double getStd(double[] data){
    return Math.sqrt(getVariance(data));
  }

  /**
  * Get the distance of a given tour
  * @return The accumulated distance
  */

  public static double getDistanceOfTour(int[] tour, double[][] distanceMatrix){
    double neighborDistance = 0;
    for (int i = 0; i < (tour.length-1); i++){
      neighborDistance += distanceMatrix[tour[i]][tour[i+1]];
    }

    // Add distance back home
    neighborDistance += distanceMatrix[tour[tour.length-1]][tour[0]];

    return neighborDistance;
  }

  /**
  * The RSM mutation procedure
  * @param int cityIndexes
  * @return cityIndexes.
  */

  public static int[] RSM(int[] cityIndexes){
    int start = TSP.randomGenerator.nextInt(50);
    int end = TSP.randomGenerator.nextInt(50);
    start = Math.min(start, end);
    end = Math.max(start, end);

    int half = start + ((end + 1) - start) / 2;
    int endCount = end;

    for (int startCount=start; startCount<half; startCount++){
      int store = cityIndexes[startCount];
      cityIndexes[startCount] = cityIndexes[endCount];
      cityIndexes[endCount] = store;
      endCount--;
    }

    return cityIndexes;
  }

  /**
  * Build the distance matrix
  * @param City The list of cities
  * @param double[][] The distance matrix
  */

  public static void buildMatrix(City[] cities, double[][] distanceMatrix){
    for (int from = 0; from < cities.length; from++){
      for (int to = 0; to < cities.length; to++){
        distanceMatrix[from][to] = cities[from].proximity(cities[to]);
      }
    }
  }

  public static<T> int find(T[] arr, T target) {
    for (int i = 0; i < arr.length; i++) {
      if (target.equals(arr[i])) {
        return i;
      }
    }

    return -1;
  }

  public static int find(int[] list, int elem) {
    for (int i=0; i<list.length;i++) {
      if (elem == list[i]) {
        return i;
      }
    }
    return -1;
  }
}
