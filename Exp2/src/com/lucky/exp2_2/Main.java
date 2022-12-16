package com.lucky.exp2_2;

import java.util.Scanner;

public class Main {
        static Scanner s=new Scanner(System.in);


        public static void main(String[] args) {
            String[] names={"Drobeta","Mehadia","Lugoj","Timisoara","Arad","Zerind","Oradea","Craiova","Rimnicu Vilcea","Sibiu","Pitesti",
                    "Bucharest","Fagaras"};
            arc[] arc={new arc("Arad","Zerind",75),new arc("Arad","Sibiu",140),new arc("Arad","Timisoara",118),new arc("Timisoara","Lugoj",111),
                    new arc("Lugoj","Mehadia",70),new arc("Mehadia","Drobeta",75),new arc("Drobeta","Craiova",120),new arc("Craiova","Pitesti",138),new arc("Craiova","Rimnicu Vilcea",146),
                    new arc("Pitesti","Bucharest",101),new arc("Bucharest","Fagaras",211),
                    new arc("Rimnicu Vilcea","Pitesti",97),new arc("Rimnicu Vilcea","Sibiu",80),new arc("Sibiu","Fagaras",99),new arc("Sibiu","Oradea",151),new arc("Oradea","Zerind",71)};
            Graph gra = createGraph(names,arc);
            System.out.println(gra);
            int path[]=Dijkstra(gra,"Arad");
            Dispath(gra,path,"Arad","Bucharest");
        }

        static Graph createGraph(String[] names,arc[] arc){//String储存节点名称，arc储存每一个节点的边所到到的边和权值

            int n=names.length;
            Graph graph =new Graph(n);
            for(int i=0;i<n;i++  ){
                graph.graph[i]=new vexnode();
                graph.graph[i].name=names[i];
            }
            for(int i=0;i<arc.length;i++){
                add(arc[i].name1,arc[i].name2,arc[i].weight,graph);
                add(arc[i].name2,arc[i].name1,arc[i].weight,graph);
            }

            return graph;


        }

        static void add(String name1,String name2,int weight,Graph graph){
            int i=find(name1,graph);
            int j=find(name2,graph);
            if(i==-1||j==-1)
            {
                System.out.println(name1+"->"+name2+"失败");
                return;

            }



            arcnode ar=new arcnode();
            ar.ver=j;
            ar.weight=weight;
            ar.nextarc=null;
            arcnode pre = null;
            arcnode temp=graph.graph[i].firstarc;
            if(temp==null){//顶点之前无边
                graph.graph[i].firstarc=ar;
            }
            else
            {while(temp!=null)//顶点之前无边
            {
                pre=temp;
                temp=temp.nextarc;

            }
                pre.nextarc=ar;
            }
        }

        static int find(String name,Graph graph){
            int n=graph.graph.length;
            for(int i=0;i<n;i++  )
                if(graph.graph[i].name.equals(name))
                    return i;
            return -1;

        }
        static int[] Dijkstra(Graph graph,String start){
            int v=find(start,graph);
            int[] path=new int[graph.graph.length];
            for(int i=0;i<graph.graph.length;i++)
                path[i]=-1;
            int[] dist=new int[graph.graph.length];
            for(int i=0;i<graph.graph.length;i++)
                dist[i]=1000000;
            int[] s=new int[graph.graph.length];//s保存那哪些结点已经被访问过
            if(v==-1){
                System.out.println("无"+start+"这个节点");
                return null;
            }

            arcnode p =graph.graph[v].firstarc;
            while(p!=null)
            {
                path[p.ver]=v;
                dist[p.ver]=p.weight;
                p=p.nextarc;
            }
            s[v]=1;//顶点V加入s中
            int u=0,mindis=1000000;
            for(int i=0;i<graph.graph.length;i++){
                mindis=1000000;
                for(int j=0;j<graph.graph.length;j++)
                    if((s[j]==0)&&dist[j]<mindis){
                        u=j;
                        mindis=dist[j];
                    }
                s[u]=1;//顶点u加入s中
                p =graph.graph[u].firstarc;
                while(p!=null)
                {
                    if(s[p.ver]==0)
                        if(dist[p.ver]>dist[u]+p.weight){
                            dist[p.ver]=dist[u]+p.weight;
                            path[p.ver]=u;
                        }
                    p=p.nextarc;
                }
            }
            return path;
        }
        static void Dispath(Graph graph,int[] path,String start,String end){
            int sta=find(start,graph);
            int en=find(end,graph);
            if(sta==-1||en==-1){
                System.out.println("没有找到相关结点");
                return;
            }
            if(path[en]==-1){
                System.out.println("路径有误");
                return;
            }
            else{
                int temp=0,i=1;
                int[] path1=new int[path.length];
                path1[0]=en;
                temp=path1[0];
                while(path[temp]!=sta){
                    path1[i]=path[temp];
                    temp=path1[i];
                    i++;
                }
                path1[i]=sta;
                System.out.println(start+"到"+end+"最短路径为：");
                for(int j=i;j>0;j--){
                    System.out.print(graph.graph[path1[j]].name+"-->");
                }
                System.out.println(graph.graph[path1[0]].name);
            }

        }

/*static Graph createGraph(){//交互界面


 //交互界面

  int n;
  System.out.println("请输入总节点数：");
  n=s.nextInt();
  Graph graph =new Graph(n);
  System.out.println("请输入所有的节点名称，中间用空格隔开");
  for(int i=0;i<n;i++  ){
   String name=s.next();
   graph.graph[i]=new vexnode();
   graph.graph[i].name=name;
        }
  for(int i=0;i<n;i++  ){
   while (true){
   System.out.println("请输入节点"+graph.graph[i].name+"的边，先输入这条边所到的节点名称，空格，输入权值，输入0结束当前节点边的建立");
   String name=s.next();
   if(name.equals("0"))
     break;
   int weight=s.nextInt();
   for(int j=0;j<n;j++){
    if(graph.graph[j].name.equals(name))
    {
     arcnode arc=new arcnode();
     arc.ver=j;
     arc.weight=weight;
     arc.nextarc=null;
     arcnode pre = null;
     arcnode temp=graph.graph[i].firstarc;
     if(temp==null){//顶点之前无边
      graph.graph[i].firstarc=arc;
      System.out.println(graph.graph[i].name+"到"+name+"添加成功");
      break;
     }
     while(temp!=null)//顶点之前无边
     {
      pre=temp;
      temp=temp.nextarc;

     }
     System.out.println(graph.graph[i].name+"到"+name+"添加成功");
     pre.nextarc=arc;
           }
        }
       }
                            }
       return graph;} */
    }


    class arcnode{//arc，网弧节点
        int ver=-1;//指向边结点
        int weight=0;//权值
        arcnode nextarc=null;
    }

    class vexnode//vertex顶点节点
    {
        String name;
        arcnode firstarc=null;//指向第一条弧节点
    }  //顶点结点

    class Graph{//储存图
        vexnode[]  graph;//一个叫graph的vexnode
        Graph(int num){
            graph=new vexnode[num];
        }

        public String toString(){
            String[] names=new String[graph.length];
            for(int i=0;i<graph.length;i++)
            {
                names[i]=graph[i].name;

            }

            for(int i=0;i<graph.length;i++)
            {
                arcnode pre = null;
                arcnode p =graph[i].firstarc;
                while(p!=null)
                {
                    names[i]=names[i]+" "+graph[p.ver].name+" "+p.weight;
                    p=p.nextarc;

                }

            }
            String s="";
            for(int i=0;i<graph.length;i++)
                s+=names[i]+"\n";
            return s;

        }
    }
    class arc{
        String name1=null;
        String name2=null;
        int weight;
        arc(String name1,String name2,int weight){
            this.name1=name1;
            this.name2=name2;
            this.weight=weight;

        }
}

