package com.lucky.exp1_1;

public class Monkey {

    private final String position;//表示猴子所在位置
    private boolean status;//表示猴子是否站在箱子上：true-->猴子站在箱子上

    public Monkey(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    //
    public Box move(Box box,String newPosition){
        if(this.position.equals(box.getPosition())&&!this.status){
            System.out.println("猴子在" + this.position + "处拿到了箱子，并把箱子推到了" + newPosition + "处。");
            return new Box(newPosition);
        }
        return box;
    }

    //
    public void climb(Box box){
        if(!this.status&&this.position.equals(box.getPosition())){
            this.status = true;
            System.out.println("猴子在" + this.position + "处爬上了箱子。");
        }
    }

    //
    public void pick(Banana banana){
        if(this.status&&this.position.equals(banana.getPosition())){
            System.out.println("猴子在" + this.position + "处拿到了香蕉。");
        }
    }

}
