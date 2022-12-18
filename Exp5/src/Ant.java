import java.util.Random;
import java.util.Vector;

public class Ant implements Cloneable {

    private Vector<Integer> tabu; // 禁忌表,Vector<Integer>与List<Integer>
    private Vector<Integer> allowedCities; // 允许搜索的城市
    private float[][] delta; // 信息素变化矩阵
    private int[][] distance; // 距离矩阵
    private float alpha;
    private float beta;

    private int tourLength; // 路径长度
    private int cityNum; // 城市数量
    private int firstCity; // 起始城市
    private int currentCity; // 当前城市

    public Ant() {
        cityNum = 30;
        tourLength = 0;
    }

    /**
     * Constructor of Ant
     *
     * @param num
     *            蚂蚁数量
     */
    public Ant(int num) {
        cityNum = num;
        tourLength = 0;
    }

    /**
     * 初始化蚂蚁，随机选择起始位置
     *
     * @param distance
     *            距离矩阵
     * @param a
     *            alpha
     * @param b
     *            beta
     */

    public void init(int[][] distance, float a, float b) {

                    /*ants[]中的每个蚂蚁init（初始化）后，会得到属于他们自己的：
            禁忌表（加入起始城市）、未探索城市表（不包含起始城市）、起始城市、当前城市（初始化时当前城市就是起始城市）
            */

        alpha = a;
        beta = b;
        // 初始允许搜索的城市集合
        allowedCities = new Vector<Integer>();
        // 初始禁忌表
        tabu = new Vector<Integer>();
        // 初始距离矩阵
        this.distance = distance;
        // 初始信息素变化矩阵为0
        delta = new float[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            Integer integer = new Integer(i);//这里指的是把Int型的数字i转化成Integer类型赋值给integer
            allowedCities.add(integer);//因为是初始化，所以当前这个蚂蚁是可以走所有城市的
            for (int j = 0; j < cityNum; j++) {
                delta[i][j] = 0.f;//刚开始，所有城市的信息素变化值都为0
            }
        }
        // 随机挑选一个城市作为起始城市
        Random random = new Random(System.currentTimeMillis());//设种子为一个绝对不会重复到的数（当前时间），避免每次循环下的城市都一样
        firstCity = random.nextInt(cityNum);//在0-城市数中随机一个值给蚂蚁，当做起始城市
        // 允许搜索的城市集合中移除起始城市
        for (Integer i : allowedCities) {
            if (i.intValue() == firstCity) {
                allowedCities.remove(i);
                break;
            }
        }
        // 将起始城市添加至禁忌表
        tabu.add(Integer.valueOf(firstCity));
        // 当前城市为起始城市
        currentCity = firstCity;
    }

    /**
     *
     * 选择下一个城市
     *
     * @param pheromone
     *            信息素矩阵
     */

    public void selectNextCity(float[][] pheromone) {
        float[] p = new float[cityNum];
        float sum = 0.0f;


        // 计算分母部分      （因为是根据未探索城市数组来求和计算的，所以用增强for循环遍历未探索城市数组allowedCities，从而才能把所有未探索城市到当前城市的所需值（信息素值、能见度值）给用到）
        for (Integer i : allowedCities) {
            sum += Math.pow(pheromone[currentCity][i.intValue()], alpha)//此处就是信息素矩阵下，当前矩阵到其他一个矩阵（遍历未探索矩阵的情况下）的     信息素值的α次方
                    * Math.pow(1.0 / distance[currentCity][i.intValue()], beta);//此处是距离矩阵下，当前矩阵到其他一个矩阵（遍历未探索矩阵的情况下）的     能见度（距离的倒数）的β次方
           /*
           他们的乘积的和就是随机化比例的分母（总概率），单个的乘积就是分子（即去单个城市概率）
           */
        }


        // 计算概率矩阵        （根据信息素与能见度求得的）
        for (int i = 0; i < cityNum; i++) {
            boolean flag = false;//定义一个前往概率状态，默认没有前往概率
            for (Integer j : allowedCities) {//给每一个在未探索城市表中的城市一个p[i]，使得等会可以在轮盘赌法中有被选中的机会（概率根据信息素与能见度还有上面的分母部分求得）
                if (i == j.intValue()) {//未探索城市数组中的城市才需要更新 当前蚂蚁的 概率矩阵
                    p[i] = (float)
                            (Math.pow(pheromone[currentCity][i], alpha) * Math.pow(1.0 / distance[currentCity][i], beta))//分子就是当前城市与遍历时的待求城市的相关信息
                            /
                            sum;
                    /*
                    这里的p[i]是一个范围比例值（相当于百分比），这个值越大，在轮盘中被选中的机会越多
                    * */
                    flag = true;//当前城市标记为拥有前往概率状态
                    break;
                }
            }
            if (flag == false) {//不在未探索城市数组中的城市就是禁忌表中的城市了呗，概率为0，不存在前往的可能
                p[i] = 0.f;
            }
        }


        // 轮盘赌选择下一个城市                 轮盘赌法，相当于先确定指针位置（抽签值），然后把随机化比例一点点加起来，直到一个值查过这个签值，就是当前次数这个对象
        Random random = new Random(System.currentTimeMillis());
        float sleectP = random.nextFloat();//抽签值
        int selectCity = 0;//选择的下一个城市编号
        float sum1 = 0.0f;
        for (int i = 0; i < cityNum; i++) {
            sum1 += p[i];//把每个城市的随机化比例一次次加起来（因为没有概率的为0，加了等于没加，不存在选到可能）
            if (sum1 >= sleectP) {//当某一个城市随机化比例达到抽签值时，说明抽中了（其实与加的次数没关，结果已经在各城市的随机化比例确定、抽签值确定时，就已经知道结果了，累加只是为了让城市的编号被选中而已）
                selectCity = i;
                break;
            }
        }

        // 从允许选择的城市中去除select city
/*        for (Integer i : allowedCities) {
            if (i.intValue() == selectCity) {
                allowedCities.remove(i);//List<>去除后会自动补位，特别方便
                break;
            }
        }*/

/**
 *  allowedCities.remove(selectCity);//为什么这样不行？
 *  因为allowedCities是会越来越少的，而selectCity是跟整个城市的数组有关，直接.remove(selectCity)是会出现数组越界的情况
 *  记住：城市的编号selectCity 不等于 数组allowedCities的下标！
 *
 *  但是，可以这样：
 *  allowedCities.removeElement(selectCity);//可以根据值来remove
 * */
        allowedCities.removeElement(selectCity);//可以根据值来remove

        // 在禁忌表中添加select city
        tabu.add(Integer.valueOf(selectCity));


        // 将当前城市改为选择的城市
        currentCity = selectCity;


    }

    /**
     * 计算路径长度
     *
     * @return 路径长度
     */
    private int calculateTourLength() {//把禁忌表里所有的城市们两两提溜出来，把它们在距离矩阵中的距离累加，就有了该禁忌表的路径长度
        int len = 0;
        //禁忌表tabu最终形式：起始城市,城市1,城市2...城市n,起始城市
        for (int i = 0; i < cityNum; i++) {
            len += distance[this.tabu.get(i).intValue()][this.tabu.get(i + 1)
                    .intValue()];
        }
        return len;
    }

    public Vector<Integer> getAllowedCities() {
        return allowedCities;
    }

    public void setAllowedCities(Vector<Integer> allowedCities) {
        this.allowedCities = allowedCities;
    }

    public int getTourLength() {
        tourLength = calculateTourLength();
        return tourLength;
    }

    public void setTourLength(int tourLength) {
        this.tourLength = tourLength;
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }

    public Vector<Integer> getTabu() {
        return tabu;
    }

    public void setTabu(Vector<Integer> tabu) {
        this.tabu = tabu;
    }

    public float[][] getDelta() {
        return delta;
    }

    public void setDelta(float[][] delta) {
        this.delta = delta;
    }

    public int getFirstCity() {
        return firstCity;
    }

    public void setFirstCity(int firstCity) {
        this.firstCity = firstCity;
    }

}