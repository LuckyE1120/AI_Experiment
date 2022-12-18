import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class TSP {

    public static int maxGenerations = 500;
    public static double tolerance = 1e-5;
    public static void main(String[] args) throws FileNotFoundException {
        long startTimes = System.currentTimeMillis();
        // maxToleranceTimes
        double pre_best_distance = 0;
        int times = 0 ;   //差值小于tolerance 的次数
        int tolerance_maxTimes = 100;

        // Create cities
        int numCities = 10;
        City[] cities = new City[numCities];
        File file = new File("cities.txt");
        Scanner in = new Scanner(file);
        // Loop to create random cities
        for (int cityIndex = 0; cityIndex < numCities; cityIndex++){
            int xPos = in.nextInt();
            int yPos = in.nextInt();
//            System.out.println(xPos);
//            System.out.println(yPos);
            System.out.println("第"+ cityIndex + "个城市坐标为：(" + xPos + "," + yPos + ")");
            cities[cityIndex] = new City(xPos,yPos);
        }
//        for (int cityIndex = 0; cityIndex < numCities; cityIndex++) {
//            // Generate x,y position
//            int xPos = (int) (100 * Math.random());
//            int yPos = (int) (100 * Math.random());
//            // Add city
//            System.out.println("第"+ cityIndex + "个城市坐标为：(" + xPos + "," + yPos + ")");
//            cities[cityIndex] = new City(xPos, yPos);
//        }


        // Initial GA
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.9, 0.01, 2, 4);

        // Initialize population
        Population population = ga.initPopulation(cities.length);

        // Evaluate population
        ga.evalPopulation(population, cities);

        Route startRoute = new Route(population.getFittest(0), cities);
        System.out.println("Start Distance: " + startRoute.getDistance());

        // Keep track of current generation
        int generation = 1;
        // ************************** Start evolution loop ******************************
        while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
            // Print fittest individual from population
            Route route = new Route(population.getFittest(0), cities);
            System.out.println("G"+generation+" Best distance: " + route.getDistance());

            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population);

            // Evaluate population
            ga.evalPopulation(population, cities);

            // Increment the current generation
            generation++;

            // tolerance times   isTerminated 2
            if(pre_best_distance-route.getDistance() <tolerance)
                times++;
            else
                times=0 ;
            if(times>tolerance_maxTimes)
                break;
            pre_best_distance = route.getDistance();   //update pre_distance
        }
        // ************************** end of the loop ******************************

        System.out.println("Stopped after " + (generation-1) + " generations.");
        Route route = new Route(population.getFittest(0), cities);
        System.out.println("Best distance: " + route.getDistance());
        System.out.println("route: ("+population.getFittest(0).toString()+")");
        System.out.println("用时"+(double)(System.currentTimeMillis()-startTimes)/1000+"s");

    }


}
