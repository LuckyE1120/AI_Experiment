import java.util.Arrays;
import java.util.stream.IntStream;

public class GeneticAlgorithm {

    //    public cross
    // @param
    private int populationSize;
    private double crossrate;
    private double mutationrate;
    private int elitismCount;
    protected int tournamentSize;
    public GeneticAlgorithm(int populationSize,double crossrate,double mutationrate,
                            int elitismCount,int tournamentSize)
    {
        this.populationSize=populationSize;
        this.crossrate=crossrate;
        this.mutationrate=mutationrate;
        this.elitismCount=elitismCount;
        this.tournamentSize=tournamentSize;
    }
    public Population initPopulation(int chromosomeLength)
    {
        return new Population(populationSize,chromosomeLength);
    }
    public double calcFitness(Individual individual,City[] cities)//个体适应度
    {
        Route route = new Route(individual,cities);
        double fitness = 1/route.getDistance();
        individual.setFitness(fitness);
        return fitness;
    }
    public final void evalPopulation(Population population, City[] cities)
    {
        double populationFitness=0;

        IntStream.range(0,populationSize).parallel().forEach(i-> this.calcFitness(population.getIndividual(i),cities) );

        for(Individual individual:population.getIndividuals())
        {
//            Thread.sleep();
            populationFitness += individual.getFitness();
        }

//        for(Individual individual:population.getIndividuals())
//            populationFitness += this.calcFitness(individual,cities);
        double avgFitness = populationFitness/population.size();
        population.setPopulationFitness(avgFitness);
    }
    public Individual selectParent(Population population)// 锦标赛选择
    {
        Population tournament = new Population(this.tournamentSize);
        // 打乱 population
        population.shuffle();
        for(int i=0;i<tournamentSize;i++)
        {
            Individual tournamentIndividual = population.getIndividual(i);
            tournament.setIndividual(i,tournamentIndividual);
        }
        // 返回随机选中的 个体中，最优秀的
        return tournament.getFittest(0);
    }
    public Population crossoverPopulation(Population population)
    {
        Population newPopulation  = new Population(population.size());
        // 遍历个体，两两交叉
        for(int index=0;index<population.size();index++)
        {
            Individual parent1 = population.getFittest(index);
            // 创建空白子代
//            Individual offspring = new Individual(parent1.getchromosomeLength());
            int[] offspringChromosome = new int[parent1.getChromosomeLength()];
            Arrays.fill(offspringChromosome, -1);
            Individual offspring = new Individual(offspringChromosome);

            if (crossrate > Math.random() && index >= elitismCount)  //精英群体保存，不交叉
            {
                Individual parent2 = selectParent(population);
                // 顺序交叉
                int substirng1 = (int) (Math.random() * (parent1.getChromosomeLength()));
                int substring2 = (int) (Math.random() * (parent1.getChromosomeLength()));
                int startPostion = Math.min(substirng1, substring2);
                int finalPostion = Math.max(substirng1, substring2);

                for (int i = startPostion; startPostion < finalPostion; startPostion++)
                    offspring.setGene(i, parent1.getGene(i));
                for (int i = 0; i < parent2.getChromosomeLength(); i++)
                {
                    int parent2Gene = i + finalPostion;
                    if (parent2Gene >= parent2.getChromosomeLength())
                        parent2Gene -= parent2.getChromosomeLength();
                    if (offspring.containsGene(parent2.getGene(parent2Gene)) == false)//若无则添加
                    {
                        for(int off_i=0;off_i<offspring.getChromosomeLength();off_i++)
                        {
                            if (offspring.getGene(off_i) == -1)
                            {
                                offspring.setGene(off_i, parent2.getGene(parent2Gene));
                                break; //交叉基因完就退出
                            }
                        }
                    }
                }
                newPopulation.setIndividual(index,offspring);
            }
            else
            {
                newPopulation.setIndividual(index, parent1);
            }
        }
        return newPopulation;
    }
    public Population mutatePopulation(Population population)
    {
        Population newPopulation = new Population(this.populationSize);
        // 每个个体 自身变异
        for(int index=0;index<population.size();index++)
        {
            Individual individual = population.getFittest(index);
            if(index>=elitismCount)
            {
                // 遍历 个体的基因
                for(int genIndex=0;genIndex<individual.getChromosomeLength();genIndex++)
                {
                    if (mutationrate > Math.random())
                    {
                        int mutatePos = (int) (Math.random()* individual.getChromosomeLength());
                        int gene1 = individual.getGene(genIndex);
                        int gene2= individual.getGene(mutatePos);
                        individual.setGene(genIndex,gene2);
                        individual.setGene(mutatePos,gene1);
                    }
                }
            }
            newPopulation.setIndividual(index,individual);
        }
        return newPopulation;
    }
    public boolean isTerminationConditionMet(int generation, int MaxGenerarion)
    {
        return generation>MaxGenerarion;
    }

}
