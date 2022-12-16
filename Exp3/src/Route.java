public class Route {

    public City[] route;
    public double distance;

    public Route(Individual individual, City[] cities) {
        this.distance = 0;
        int[] chromosome = individual.getChromosome();
        this.route = new City[cities.length];
        for (int gene = 0; gene < chromosome.length; gene++)
            this.route[gene] = cities[chromosome[gene]];
    }

    public double getDistance() {
        if (this.distance > 0)
            return this.distance;
        double distance = 0;
        for (int CityIndex = 0; CityIndex < route.length; CityIndex++)
            distance += route[CityIndex].distanceFrom(route[(CityIndex + 1) % route.length]);
        this.distance = distance;
        return this.distance;
    }

}
