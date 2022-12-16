public class City {

    private double x;
    private double y;
    public City(double x,double y)
    {
        this.x=x;
        this.y=y;
    }
    public City(City city)
    {
        this(city.x, city.y);
    }

    public double distanceFrom(City city)
    {
        double Distance=0;
        double delta_x=Math.pow(this.getX()-city.getX(),2);
        double delta_y=Math.pow(this.getY()-city.getY(),2);

        Distance = Math.sqrt(Math.abs(delta_x+delta_y));
        return Distance;
    }
    public double getX()
    {
        return this.x;
    }
    public double getY()
    {
        return this.y;
    }


}
