package com.lucky.exp1_1;

import org.junit.Test;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("请输入猴子所在位置：");
        Monkey monkey = new Monkey(input.next());

        System.out.println("请输入箱子所在位置：");
        Box box = new Box(input.next());

        System.out.println("请输入香蕉所在位置：");
        Banana banana = new Banana(input.next());

        //System.out.println(monkey.getPosition() + box.getPosition() + banana.getPosition());

        if (banana.getPosition().equals(box.getPosition())){
            System.out.println("第一步：猴子从"+ monkey.getPosition() + "移动到" + box.getPosition() +"处。");
            monkey = new Monkey(box.getPosition());
            System.out.println("第二步：猴子爬上了箱子。");
            monkey.climb(box);
            System.out.println("第三步：猴子拿到了香蕉。");
            monkey.pick(banana);
        }else{
            System.out.println("第一步：猴子从"+ monkey.getPosition() + "移动到" + box.getPosition() +"处。");
            monkey = new Monkey(box.getPosition());
            System.out.println("第二步：猴子在"+ box.getPosition() + "处拿到了箱子。");
            System.out.print("第三步：");
            box = monkey.move(box, banana.getPosition());
            monkey = new Monkey(banana.getPosition());
            System.out.println("第四步：猴子爬上了箱子。");
            monkey.climb(box);
            System.out.println("第五步：猴子拿到了香蕉。");
            monkey.pick(banana);
        }

    }

}
