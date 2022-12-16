package com.lucky.exp2_3;

import java.util.Stack;

public class HannoTower_Stack {

    public static void main(String[] args) {
        Hanoi(3,"A","B","C");
    }

    public static void Hanoi(int num,String a,String b,String c){
        Stack<State> starks = new Stack<> ();
        State state = new State(num,a,b,c,false);
        starks.push (state);
        long startTime = System.nanoTime();
        while (starks.size()>0){
            if(starks.peek().title){
                HanoiPrint(starks.pop ());
            }
            else {
                State NumLinShi=starks.pop ();
                if (NumLinShi.Num==1){
                    HanoiPrint(NumLinShi);
                }
                else {
                    State NumLinA_B = new State (NumLinShi.Num-1,NumLinShi.A,NumLinShi.C,NumLinShi.B,false);
                    State NumLinA_C = new State (NumLinShi.Num,NumLinShi.A,NumLinShi.B,NumLinShi.C,true);
                    State NumLinB_C = new State (NumLinShi.Num-1,NumLinShi.B,NumLinShi.A,NumLinShi.C,false);
                    starks.push (NumLinB_C);
                    starks.push (NumLinA_C);
                    starks.push (NumLinA_B);
                }
            }
        }
        long endTime = System.nanoTime();
        System.out.println("cost time:" + (endTime - startTime) + "ns");
    }

    public static void HanoiPrint(State str){
        System.out.println ("把编号为\t"+str.Num+"\t的圆盘从\t"+str.A+"\t移动到\t"+str.C+"\t盘子上");
    }
}

class State {
    public int Num;
    public String A;
    public String B;
    public String C;
    public boolean title;

    public State(int num, String a, String b, String c, boolean title) {
        Num = num;
        A = a;
        B = b;
        C = c;
        this.title = title;
    }
}
