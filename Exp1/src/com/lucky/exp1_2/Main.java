package com.lucky.exp1_2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static int N;//野人和牧师的数量
    static int C;//船最大载人数
    static int count = 0;
    static List<status> statuses = new ArrayList<status>();//存储路线
    static int minDeep = 50000000;//最短路径


    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("Please input n:");
        N = input.nextInt();

        System.out.println("Please input c:");
        C = input.nextInt();

        //初始状态
        status  s = new status(N,N,1);
        statuses.add(s);
        dfs(s);
        System.out.print("Successed or Failed?:");
        if (count == 0){
            System.out.println("Failed");
        }
        else {
            System.out.println("Successed");
            System.out.println("count:" + count);
            System.out.println("minDeep:" + minDeep);
        }


    }

    /**
     * 深搜：输出所有符合条件的序列
     * @param s
     */
    public static void dfs(status s) {

        //输出状态迁移过程
        if (s.leftMonk == 0 && s.leftSavage == 0 && s.boatStatus == 0){
            System.out.print("Optimal Procedure: ");
            for (int i = 0 ; i < statuses.size() ; i++){
                if (i == statuses.size() - 1)
                    System.out.println("(0,0,0)");
                else
                    statuses.get(i).output();
            }
            count++;
            if (statuses.size() < minDeep)
                minDeep = statuses.size();
            return;
        }

        //判断船上最多可以有多少牧师和野人
        int k1,k2;
        if(s.boatStatus == 1){//船在左岸
            k1 = C>=s.leftMonk?s.leftMonk:C;
            k2 = C>=s.leftSavage?s.leftSavage:C;
        }else{
            k1 = C>=(N-s.leftMonk)?(N- s.leftMonk):C;
            k2 = C>=(N-s.leftSavage)?(N- s.leftSavage):C;
        }

        //深度优先算法找出所有可以过河的情况
        for (int i = 0 ; i <= k1 ; i++){
            int m = i;//m为上船的修道士数
            for (int j = 0 ; j <= ((C-m)>=k2?k2:(C-m)) ; j++){
                int n = j;//n为上船的野人数
                /**
                 * tLeftM：表示当前左岸剩余的牧师
                 * tLeftS：表示当前左岸剩余的野人
                 * tBoatS：表示当前船位于左岸还是右岸
                 */
                int tLeftM,tLeftS,tBoatS;
                if (m == 0 && n == 0) continue;
                status ts;
                //左岸-->右岸
                if (s.boatStatus == 1){//当前状态是合法条件，船在左岸可以开往右岸
                    tBoatS = 0;
                    tLeftM = N - ((N-s.leftMonk)+m);
                    tLeftS = N - ((N-s.leftSavage)+n);
                    ts = new status(tLeftM,tLeftS,tBoatS);
                    if (check(statuses,ts)){
                        statuses.add(ts);
                        dfs(ts);
                        statuses.remove(ts);//回溯
                    }
                }else{//右岸-->左岸
                    tBoatS = 1;
                    tLeftM = N - ((N-s.leftMonk)-m);
                    tLeftS = N - ((N-s.leftSavage)-n);
                    ts = new status(tLeftM,tLeftS,tBoatS);
                    if (check(statuses,ts)){
                        statuses.add(ts);
                        dfs(ts);
                        statuses.remove(ts);
                    }
                }
            }
        }

        return;

    }

    /**
     * 检查当前状态插入后是否合法
     * @param ts
     * @return
     */
    public static boolean check(List<status> s,status ts) {

        /**
         * 1.第一种不合法情况：状态不能重复
         * 出现重复就意味着陷入无限循环
         */
        if (!s.isEmpty()){
            for (int i = 0 ; i < s.size() ; i++){
                if (s.get(i).leftSavage == ts.leftSavage && s.get(i).leftMonk == ts.leftMonk && s.get(i).boatStatus == ts.boatStatus)
                    return false;
            }
        }

        /**
         * 2.第二种不合法情况：左岸或右岸或船上野人数大于牧师数
         */
        //左岸
        if (ts.leftMonk != 0 && (ts.leftMonk < ts.leftSavage))
            return false;
        //右岸
        if ((N- ts.leftMonk!=0) && ((N- ts.leftMonk) < (N - ts.leftSavage)))
            return false;

        return true;

    }

}

class status{

    int leftMonk;//左岸牧师数
    int leftSavage;//左岸野人数
    int boatStatus;//船的状态，0-->船在右岸(终点岸) 1-->船在左岸(起始岸)

    public status(int leftMonk, int leftSavage, int boatStatus) {
        this.leftMonk = leftMonk;
        this.leftSavage = leftSavage;
        this.boatStatus = boatStatus;
    }

    public void output(){
        System.out.print("("+ this.leftMonk + "," + this.leftSavage +"," + this.boatStatus +")-->");
    }

}
