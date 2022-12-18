import java.util.Random;

public class Individual {

    private int[] chromosome;
    private double fitness=-1;
    public Individual(int chromosomeLength)
    {
        this.chromosome=new int[chromosomeLength];
        for(int gene=0;gene<chromosomeLength;gene++)
            this.chromosome[gene]=gene;
        shuffle();   //打乱顺序，构造随机排序的城市数组
    }
    public Individual(int[] chromosome)
    {
        this.chromosome=chromosome;
    }
    public int[] getChromosome()
    {
        return this.chromosome;
    }
    public int getChromosomeLength()
    {
        return this.chromosome.length;
    }
    public void setFitness(double fitness)
    {
        this.fitness=fitness;
    }
    public double getFitness()
    {
        return this.fitness;
    }

    public void shuffle()
    {
        Random rnd=new Random();
        for(int gene=0;gene<chromosome.length;gene++)
        {
            int randint = rnd.nextInt(gene+1);
            int temp= chromosome[gene];
            this.chromosome[gene] = this.chromosome[randint];
            this.chromosome[randint] = temp;
        }
    }
    public void setGene(int Index,int gene)
    {
        this.chromosome[Index]=gene;
    }
    public int getGene(int Index)
    {
        return this.chromosome[Index];
    }
    public String toString()
    {
        String str="";
        if(this.chromosome.length>0)
            str+= this.chromosome[0];
        for(int gene=1;gene< chromosome.length;gene++)
            str+=","+chromosome[gene];
        return str;
    }
    public boolean containsGene(int gene)
    {
        for(int Gene:this.chromosome)
            if (Gene==gene)
                return true;
        return false;
    }

}
