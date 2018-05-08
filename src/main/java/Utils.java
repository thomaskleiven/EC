import java.lang.Math;

public class Utils{

  public static double getMean(double[] data){
    double mean = 0.0;
    for (int i = 0; i < data.length; i++) {
        mean += data[i];
    }
    return mean /= data.length;
  }

  public static double getVariance(double[] data){
    double variance = 0.0;
    double mean = getMean(data);
    for (int i = 0; i < data.length; i++) {
        variance += (data[i] - mean) * (data[i] - mean);
    }
    return variance /= data.length;
  }

  public static double getStd(double[] data){
    return Math.sqrt(getVariance(data));
  }

}
