package com.lucky.exp2_3;

import org.junit.Test;

import java.util.Scanner;

public class Main {

    static int count = 0;

   public static void move(int n,char A,char B){
       System.out.println("将第" + n + "号盘子从" + A + "移动到" + B );
   }

   public static void hanoi(int n,char A,char B,char C){
        if (n==1){
            move(1,A,C);
        }
        else{
            hanoi(n-1,A,C,B);
            count += 1;
            move(n,A,C);
            hanoi(n-1,B,A,C);
            count += 1;
        }
   }

   @Test
    public static void main(String[] args) {
       int num = 3;
       long startTime=System.nanoTime();
       hanoi(num, 'A', 'B', 'C');
       long endTime=System.nanoTime();
       System.out.println("盘子数："+num);
       System.err.println("运行时间： "+(endTime-startTime)+"ns");
       System.out.println("递归调用次数："+count);
       count = 0;
    }

}
