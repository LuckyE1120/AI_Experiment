import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ACO {

    private Ant[] ants; // 蚂蚁
    /*  对于一只蚂蚁来说，他有自己的：禁忌表（走过的城市）、允许搜索城市（还未走的城市）、信息素变化矩阵、距离矩阵、起始城市、当前城市 等重要成员变量*/
    private int antNum; // 蚂蚁数量
    private int cityNum; // 城市数量
    private int MAX_GEN; // 运行代数（迭代次数）
    private float[][] pheromone; // 信息素矩阵
    private int[][] distance; // 距离矩阵
    private int bestLength; // 最佳长度
    private int[] bestTour; // 最佳路径

    // 三个参数
    private float alpha;//a
    private float beta;//b
    private float rho;//p（信息素挥发率）

    public ACO() {

    }

    /**
     * constructor of ACO
     *
     * @param n
     *            城市数量
     * @param m
     *            蚂蚁数量
     * @param g
     *            运行代数
     * @param a
     *            alpha
     * @param b
     *            beta
     * @param r
     *            rho
     *
     **/
    public ACO(int n, int m, int g, float a, float b, float r) {//输入城市数 蚂蚁数 最大迭代数 三个基本参数 a b p
        cityNum = n;
        antNum = m;
        ants = new Ant[antNum];
        MAX_GEN = g;
        alpha = a;
        beta = b;
        rho = r;
    }

    // 给编译器一条指令，告诉它对被批注的代码元素内部的某些警告保持静默
    @SuppressWarnings("resource")
    /**
     * 初始化ACO算法类
     * 做了以下几个事：
     *
     * 读取数据、存城市坐标（入x[]、y[]）、计算距离矩阵、初始化信息素矩阵随机放置蚂蚁
     *
     * @param filename 数据文件名，该文件存储所有城市节点坐标数据
     * @throws IOException
     */
    private void init(String filename) throws IOException {//初始化ACO算法类：读入城市地图信息进相应矩阵（第一列是城市编号 第二列是x坐标 第三列是y坐标）


        // 读取数据
        String strbuff;
        BufferedReader data = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename)));//利用IO流读取城市信息文件



        //存城市坐标
        int[]x = new int[cityNum];//存放x坐标矩阵，因此空间长度要为城市数
        int[]y = new int[cityNum];//存放y坐标矩阵
        for (int i = 0; i < cityNum; i++) {
            // 读取一行数据，数据格式1 6734 1453
            strbuff = data.readLine();//按城市数一行行读入城市信息进strbuff  ，一次循环读一行，如：1 2066 2333
            // 字符分割
            String[] strcol = strbuff.split(" ");//意思是根据空格切分字符串存入strcol数组 ,如此时strcol[0]=1  strcol[1]=6734   strcol[2]=1453
            x[i] = Integer.valueOf(strcol[1]);// x坐标
            y[i] = Integer.valueOf(strcol[2]);// y坐标
        }

        /*        以上，所有的城市的x,y坐标都存入了x[],y[]矩阵中*/


        // 计算距离矩阵
        // 针对具体问题，距离计算方法也不一样，此处用的是TSP10cities.txt作为案例，它有10个城市，距离计算方法为欧氏距离（/10.0才是伪，我用的是真），最优值为10127.552143541276
        distance = new int[cityNum][cityNum];//距离矩阵
        for (int i = 0; i < cityNum - 1; i++) {//将所有城市间的直线距离存入一个二维数组（矩阵），这样以后可以直接通过 如： distance[1][0]  获得城市1到城市0的距离
            distance[i][i] = 0; // 对角线为0，即城市自己到自己的距离为0
            for (int j = i + 1; j < cityNum; j++) {//从i+1开始的目的是避免覆盖初始化的distance[i][i],后面用了distance[j][i] = distance[i][j]对称存入反向距离，因为该案例时对称城市（即dij与dji的距离看作相同）
                double rij = Math
                        .sqrt((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j])
                                * (y[i] - y[j])) ;//欧式距离，其实就是根据坐标值算的二维直线距离
                // 四舍五入，取整
                int tij = (int) Math.round(rij);//java取整函数Math.round(),将算得的城市直线距离rij转为不那么精确的整数型：  tij
                if (tij < rij) {
                    distance[i][j] = tij + 1;
                    distance[j][i] = distance[i][j];//因为是对称城市
                } else {
                    distance[i][j] = tij;
                    distance[j][i] = distance[i][j];
                }//这一段的做法是将取整到的rij向上取整存入距离矩阵（不那么必要）
            }
        }
        distance[cityNum - 1][cityNum - 1] = 0;


        // 初始化信息素矩阵，（此处不严谨，随意设为0.1，应该按照贪心算法算出的大致总距离取倒数乘以蚂蚁数来推断初始信息矩阵）
        pheromone = new float[cityNum][cityNum];//信息素矩阵
        for (int i = 0; i < cityNum; i++) {
            pheromone[i][i]=0.0f;
            for (int j = i+1; j < cityNum; j++) {
                pheromone[i][j] = 0.1f; // 初始化为0.1，全部城市间的信息素矩阵(除自己到自己外)全部为0.1
                pheromone[j][i]=pheromone[i][j];
            }
        }
        bestLength = Integer.MAX_VALUE;//初始化最佳长度为正无穷，等待后续更新
        bestTour = new int[cityNum + 1];//初始化最佳路径向量（长度要比城市数多一，目的是形成回路好看即: 1......1 的形式）


        // 随机放置蚂蚁
        for (int i = 0; i < antNum; i++) {
            ants[i] = new Ant(cityNum);//对于蚂蚁类来说，他只需要知道城市数量即可（因为Ant的构造方法的实参只需cityNum）
            ants[i].init(distance, alpha, beta);//用确定的参数：距离矩阵、阿尔法、贝塔  初始化每只蚂蚁

            /*ants[]中的每个蚂蚁init（初始化）后，会得到属于他们自己的：
            禁忌表（加入起始城市）、未探索城市表（不包含起始城市）、起始城市、当前城市（初始化时当前城市就是起始城市）
            */
        }
    }


    public void solve() {


        // 迭代MAX_GEN次
        for (int g = 0; g < MAX_GEN; g++) {
            // antNum只蚂蚁
            for (int i = 0; i < antNum; i++) {
                // i这只蚂蚁走cityNum步，完整一个TSP
                for (int j = 1; j < cityNum; j++) {//从1开始，走cityNum-1步，正好走了除起始点外的所有城市
                    //每次都调用当前这只蚂蚁的.selectNextCity方法，根据信息素矩阵（该迭代次数下的）pheromone来选择下一个城市
                    ants[i].selectNextCity(pheromone);
                    //所谓的选择下一个城市，起始就是更新该蚂蚁的：禁忌表、未探索城市表、当前城市这三个主要信息。
                    // 所依仗的，就是核心参数：信息素矩阵（该迭代次数下的）pheromone、城市距离矩阵distance（内的各个城市距离）
                    //再根据特定的公式与轮盘赌法来更新三个主要信息（禁忌表、未探索城市表、当前城市，但信息素矩阵不在此更新）
                }
//上面这个小for循环走完，蚂蚁i的旅行信息就出来了，有了一个含有他走过路径的            禁忌表：起始城市,城市1,城市2...城市n,     ——这是蚂蚁一次遍历的目的，得到行走路径禁忌表（此时缺少了起始城市构成环路）

                // 把这只蚂蚁起始城市加入其禁忌表中
                // 禁忌表最终形式：起始城市,城市1,城市2...城市n,起始城市（相当于走过的路径向量们）
                ants[i].getTabu().add(ants[i].getFirstCity());//这一步的目的是构成一个循环：起始城市,城市1,城市2...城市n,起始城市（


                // 查看这只蚂蚁行走路径距离是否比当前距离优秀
                if (ants[i].getTourLength() < bestLength) {//.getTourLength()可以获得该禁忌表的路径长度（也正是因为此，所以禁忌表必须有回到起始城市这一结尾，才方便.getTourLength()计算）
                    // 比当前优秀则拷贝优秀TSP路径
                    bestLength = ants[i].getTourLength();//存入该次迭代下的最好路径长度
                    for (int k = 0; k < cityNum + 1; k++) {
                        bestTour[k] = ants[i].getTabu().get(k).intValue();//存入该次迭代下的最好路径
                    }
                }

                // 更新这只蚂蚁的信息素变化矩阵，对称矩阵（即有了每个城市间的信息素变化矩阵  DeltaTij  ），这个才是最重要的，用于之后更新总的  信息素矩阵
                for (int j = 0; j < cityNum; j++) {
                    ants[i].getDelta()[ants[i].getTabu().get(j).intValue()][ants[i].getTabu().get(j + 1).intValue()]
                            = (float) (1. / ants[i].getTourLength());//更新右上部分

                    ants[i].getDelta()[ants[i].getTabu().get(j + 1).intValue()][ants[i].getTabu().get(j).intValue()]
                            = (float) (1. / ants[i].getTourLength());//更新左下部分
                }
                //以上，就是蚂蚁i在这一次迭代中的使命，它退役了


            }
//以上，所有的蚂蚁在这一次迭代中的使命，它们都退役了

            // 更新信息素
            updatePheromone();//遵循这个公式：Tij=（1-p）Tij【旧的】+DeltaTij【每个蚂蚁（用antNum）这两个城市的信息素更新矩阵值的累加和】
//此次更新意味着本次迭代的任务完成，没错，   更新信息素   就是蚂蚁们迭代的终极目标！

            // 重新初始化蚂蚁
            for (int i = 0; i < antNum; i++) {
                ants[i].init(distance, alpha, beta);//每只蚂蚁都执行一次init即初始化完成
                //得到：禁忌表（加入起始城市）、未探索城市表（不包含起始城市）、起始城市、当前城市（初始化时当前城市就是起始城市）
            }
        }


        // 打印最佳结果
        printOptimal();
    }

    // 更新信息素
    private void updatePheromone() {
        // 信息素挥发
        for (int i = 0; i < cityNum; i++)
            for (int j = 0; j < cityNum; j++)
                pheromone[i][j] = pheromone[i][j] * (1 - rho);
        // 信息素更新
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                for (int k = 0; k < antNum; k++) {
                    pheromone[i][j] += ants[k].getDelta()[i][j];//依靠的就是信息素变化矩阵，这个记录着每只蚂蚁走过的信息
                }
            }
        }
    }

    private void printOptimal() {
        System.out.println("The optimal length is: " + bestLength);
        System.out.println("The optimal tour is: ");
        for (int i = 0; i < cityNum + 1; i++) {
            System.out.print(bestTour[i]+" ");
        }
        System.out.println();
    }


    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        long a=System.currentTimeMillis();

        System.out.println("Start....");
        ACO aco = new ACO(30, 10, 100, 1.f, 5.f, 0.5f);
        aco.init("cities.txt");
        aco.solve();

        long b=System.currentTimeMillis();
        long c=b-a;
        System.out.println("运行时间为:"+c);//输出运行时间
    }

}
