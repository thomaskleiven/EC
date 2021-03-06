import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Paths;
import java.sql.Time;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.*;
import java.util.Scanner;
import java.util.stream.IntStream;

import javax.swing.*;

public final class TSP {

  private static final int cityShiftAmount = 60; //DO NOT CHANGE THIS.

  public static boolean TOURNAMENT = true;
  public static boolean ELITIST = true;
  public static Scanner scanner = new Scanner(System.in);
  public static int simCount = 0; // Number of local search
  public static Random randomGenerator = new Random();
  public static boolean GUI = false; // Hopefully forever.

  /**
  * How many cities to use.
  */
  protected static int cityCount;

  /**
  * How many chromosomes to use.
  */
  protected static int populationSize = 100; //DO NOT CHANGE THIS.

  /**
  * The part of the population eligable for mating.
  */
  protected static int matingPopulationSize;

  /**
  * The part of the population selected for mating.
  */
  protected static int selectedParents;

  /**
  * The current generation
  */
  protected static int generation;

  /**
  * The list of cities (with current movement applied).
  */
  protected static City[] cities;

  /**
  * The list of cities that will be used to determine movement.
  */
  private static City[] originalCities;

  /**
  * The list of chromosomes.
  */
  protected static Chromosome[] chromosomes;

  /**
  * Frame to display cities and paths
  */
  private static JFrame frame;

  /**
  * Integers used for statistical data
  */
  private static double min;
  private static double avg;
  private static double max;
  private static double sum;
  private static double genMin;

  /**
  * Width and Height of City Map, DO NOT CHANGE THESE VALUES!
  */
  private static int width = 600;
  private static int height = 600;


  private static Panel statsArea;
  private static TextArea statsText;


  /*
  * Writing to an output file with the costs.
  */
  public static void writeLog(String content) {
    String filename = "results_thomas.out";
    FileWriter out;

    try {
      out = new FileWriter(filename, true);
      out.write(content + "\n");
      out.close();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /*
  *  Deals with printing same content to System.out and GUI
  */
  public static void print(boolean guiEnabled, String content) {
    if(guiEnabled) {
      statsText.append(content + "\n");
    }

    System.out.println(content);
  }

  public static void evolve(int generation) {
    // chromosomes = Evolution.Evolve(chromosomes, cities, generation);
  }

  /**
  * Update the display
  */
  public static void updateGUI() {
    Image img = frame.createImage(width, height);
    Graphics g = img.getGraphics();
    FontMetrics fm = g.getFontMetrics();

    g.setColor(Color.black);
    g.fillRect(0, 0, width, height);

    if (true && (cities != null)) {
      for (int i = 0; i < cityCount; i++) {
        int xpos = cities[i].getx();
        int ypos = cities[i].gety();
        g.setColor(Color.green);
        g.fillOval(xpos - 5, ypos - 5, 10, 10);

        //// SHOW Outline of movement boundary
        // xpos = originalCities[i].getx();
        // ypos = originalCities[i].gety();
        // g.setColor(Color.darkGray);
        // g.drawLine(xpos + cityShiftAmount, ypos, xpos, ypos + cityShiftAmount);
        // g.drawLine(xpos, ypos + cityShiftAmount, xpos - cityShiftAmount, ypos);
        // g.drawLine(xpos - cityShiftAmount, ypos, xpos, ypos - cityShiftAmount);
        // g.drawLine(xpos, ypos - cityShiftAmount, xpos + cityShiftAmount, ypos);
      }

      g.setColor(Color.gray);
      for (int i = 0; i < cityCount; i++) {
        int icity = chromosomes[0].getCity(i);
        if (i != 0) {
          int last = chromosomes[0].getCity(i - 1);
          g.drawLine(
          cities[icity].getx(),
          cities[icity].gety(),
          cities[last].getx(),
          cities[last].gety());
        }
      }

      int homeCity = chromosomes[0].getCity(0);
      int lastCity = chromosomes[0].getCity(cityCount - 1);

      //Drawing line returning home
      g.drawLine(
      cities[homeCity].getx(),
      cities[homeCity].gety(),
      cities[lastCity].getx(),
      cities[lastCity].gety());
    }
    frame.getGraphics().drawImage(img, 0, 0, frame);
  }

  private static City[] LoadCitiesFromFile(String filename, City[] citiesArray) {
    ArrayList<City> cities = new ArrayList<City>();
    try
    {
      FileReader inputFile = new FileReader(filename);
      BufferedReader bufferReader = new BufferedReader(inputFile);
      String line;
      while ((line = bufferReader.readLine()) != null) {
        String [] coordinates = line.split(", ");
        cities.add(new City(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
      }

      bufferReader.close();

    }
    catch (Exception e) {
      System.out.println("Error while reading file line by line:" + e.getMessage());
    }

    citiesArray = new City[cities.size()];
    return cities.toArray(citiesArray);
  }

  public static City[] MoveCities(City[]cities) {
    City[] newPositions = new City[cities.length];
    // Random randomGenerator = new Random();
    // randomGenerator.setSeed(10);

    for(int i = 0; i < cities.length; i++) {
      int x = cities[i].getx();
      int y = cities[i].gety();

      int position = randomGenerator.nextInt(5);

      if(position == 1) {
        y += cityShiftAmount;
      }
      else if(position == 2) {
        x += cityShiftAmount;
      }
      else if(position == 3) {
        y -= cityShiftAmount;
      }
      else if(position == 4) {
        x -= cityShiftAmount;
      }

      newPositions[i] = new City(x, y);
    }

    return newPositions;
  }

  public static void main(String[] args) {
    /*
    Enable assertions
    */
    ClassLoader loader = ClassLoader.getSystemClassLoader();
    loader.setDefaultAssertionStatus(true);

    // Set seed for repeatability
    randomGenerator.setSeed(20139); // Random seed

    DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    Date today = Calendar.getInstance().getTime();
    String currentTime  = df.format(today);

    int runs;
    boolean display = false;
    String formatMessage = "Usage: java TSP 1 [gui] \n java TSP [Runs] [gui]";

    if (args.length < 1) {
      System.out.println("Please enter the arguments");
      System.out.println(formatMessage);
      display = false;
    }
    else {

      if (args.length > 1 && args[1].equals("--tournament")) TOURNAMENT=true;
      if (args.length > 1 && args[1].equals("y")) display = true;
      if (args.length > 1 && args[1].equals("--elite")) ELITIST = true;
      if (args.length > 2 && args[2].equals("--elite")) ELITIST = true;

      try {
        cityCount = 50;
        populationSize = 100;
        runs = Integer.parseInt(args[0]);

        if(display) {
          GUI = true;
          frame = new JFrame("Traveling Salesman");
          statsArea = new Panel();

          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.pack();
          frame.setSize(width + 300, height);
          frame.setResizable(false);
          frame.setLayout(new BorderLayout());

          statsText = new TextArea(35, 35);
          statsText.setEditable(false);

          statsArea.add(statsText);
          frame.add(statsArea, BorderLayout.EAST);

          frame.setVisible(true);
        }


        min = 0;
        avg = 0;
        max = 0;
        sum = 0;

        String currentWorkingDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
        originalCities = cities = LoadCitiesFromFile(currentWorkingDirectory+"/"+"CityList.txt", cities);

        writeLog("Run Stats for experiment at: " + currentTime);
        double startTime = System.currentTimeMillis();


        // Run this for parallell computation

        // IntStream.range(0, runs).parallel().forEach(i -> {
        //   Main main = new Main(Arrays.copyOfRange(cities, 0, cities.length), originalCities, populationSize, runs);
        //
        //   if (main.getGenMin() > max) max = main.getGenMin();
        //   if (main.getGenMin() < min || min == 0) min = main.getGenMin();
        //   sum +=  main.getGenMin();
        // });


        // Run this for non-parallel computation
        for (int i = 0; i < runs; i++){
          Main main = new Main(Arrays.copyOfRange(cities, 0, cities.length), originalCities, populationSize, runs);
          if (main.getGenMin() > max) max = main.getGenMin();
          if (main.getGenMin() < min || min == 0) min = main.getGenMin();
          sum +=  main.getGenMin();
        }


        avg = sum / runs;
        // writeAverage(String.valueOf(avg), uid);
        print(display, "Statistics after " + runs + " runs");
        print(display, "Solution found after " + generation + " generations." + "\n");
        print(display, "Statistics of minimum cost from each run \n");
        print(display, "Lowest: " + min + "\nAverage: " + avg + "\nHighest: " + max + "\n");


        System.out.printf("Simcount: %s\n", simCount);

        System.out.printf("Computational time: %s seconds\n", (double) (System.currentTimeMillis() - startTime) / 1000.0);
      }
      catch (NumberFormatException e) {
        System.out.println("Please ensure you enter integers for cities and population size");
        System.out.println(formatMessage);
      }
    }
  }
}
