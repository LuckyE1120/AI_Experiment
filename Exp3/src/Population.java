import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Population {

    private Individual[] population;
    private double populationFitness=-1;
    public Population(int populationSize)
    {
        this.population=new Individual[populationSize];
    }
    public Population(int populationSize,int chromosomeLength)
    {
        this(populationSize);
        for(int individualCount=0;individualCount<populationSize;individualCount++)
            this.population[individualCount]=new Individual(chromosomeLength);
    }

    public Individual[] getIndividuals()
    {
        return this.population;
    }
    public void setPopulationFitness(double populationFitness)
    {
        this.populationFitness=populationFitness;
    }
    public double getPopulationFitness()
    {
        return this.populationFitness;
    }
    public int size()
    {
        return this.population.length;
    }
    public Individual setIndividual(int offset, Individual individual)
    {
        return this.population[offset] = individual;
    }
    public Individual getIndividual(int offset)
    {
        return this.population[offset];
    }
    public Individual getFittest(int offset)
    {
        Arrays.sort(population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2)
            {
                return Double.compare(o2.getFitness(), o1.getFitness());
            }
        });
        return this.population[offset];
    }
    public void shuffle()
    {
        Random rnd = new Random();
        for(int individ=population.length-1 ; individ>0 ; individ--)
        {
            int randi = rnd.nextInt(individ+1);
            Individual temp=this.population[individ];
            this.population[individ] = this.population[randi];
            this.population[randi] = temp;
        }
    }


}
