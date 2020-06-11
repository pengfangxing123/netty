package com.netty.datastructure.data.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 图
 * @author 86136
 */
public class GraphDemo {
    /**
     * 顶点集合
     */
    private List<String> vertexList;

    /**
     * 邻接矩阵
     */
    private int[][] edges;

    /**
     * 表示边得数目
     */
    private int numOfEdges;

    /**
     * 遍历时记录节点是否已经被访问
     */
    private boolean[] isVisited;

    public GraphDemo(int n) {
        this.numOfEdges = 0;
        vertexList=new ArrayList<>(n);
        edges =new int[n][n];
        isVisited=new boolean[n];
    }


    public static void main(String[] args) {
        int n = 8;  //结点的个数
        //String Vertexs[] = {"A", "B", "C", "D", "E"};
        String Vertexs[] = {"1", "2", "3", "4", "5", "6", "7", "8"};
        //创建图对象
        GraphDemo graph = new GraphDemo(n);
        System.out.println(graph.edges.length);

        //循环的添加顶点
        for(String vertex: Vertexs) {
            graph.insertVertex(vertex);
        }

        //添加边
        //A-B A-C B-C B-D B-E
//		graph.insertEdge(0, 1, 1); // A-B
//		graph.insertEdge(0, 2, 1); //
//		graph.insertEdge(1, 2, 1); //
//		graph.insertEdge(1, 3, 1); //
//		graph.insertEdge(1, 4, 1); //

        //更新边的关系
        graph.insertEdge(0, 1, 1);
        graph.insertEdge(0, 2, 1);
        graph.insertEdge(1, 3, 1);
        graph.insertEdge(1, 4, 1);
        graph.insertEdge(3, 7, 1);
        graph.insertEdge(4, 7, 1);
        graph.insertEdge(2, 5, 1);
        graph.insertEdge(2, 6, 1);
        graph.insertEdge(5, 6, 1);

        System.out.println(Arrays.deepToString(graph.edges));
        graph.dfs();
        //graph.bfs();
    }

    /**
     * 这个不能遍历出非节点非连通的情况
     * 比如第一个节点 没有和任意节点相连，这个时候就再处理第一个节点的时候就直接返回了
     * 所以再在上面重载一个方法
     * @param n
     */
    public void dfs(int n){
        if(n>edges.length-1){
            return ;
        }
        for(int i=0;i<edges[n].length;i++){
            //判断下一个是否是关联节点
            if(edges[n][i]!=0){
                //是关联节点，判断是否已经访问过
                if(isVisited[i]){
                    //已经访问过,获取当前节点的下一个节点，作为新的节点重新开始遍历,edges[0][1]被访问过，那么edges[0][1+n]中是否还有
                    //所以是直接继续往下走
                }else{
                    //没有访问过，输出i节点，i作为新的节点重新开始遍历
                    System.out.println(i);
                    isVisited[i]=true;
                    dfs(i);
                }

            }
        }
    }

    /**
     * 解决节点不连通的节点没有输出
     * 这样判断就有点冗余
     */
    public void dfs(){
        for(int n=0;n<edges.length;n++){
            if(!isVisited[n]){
                System.out.println(n);
                isVisited[n]=true;
            }
            dfs(n);
        }
    }


    /**
     * 广度优先
     */
    public void bfs(){
        //防止节点不连续没一层都要遍历一次，
        for(int n=0;n<edges.length;n++){
            LinkedList<Integer> queue = new LinkedList<>();
            //先把当前第一个放入到队列中
            if(!isVisited[n]){
                queue.offer(n);
            }
            bfs(queue);
        }
    }

    /**
     * 广度优先
     */
    public void bfs(LinkedList<Integer> queue){
        while (!queue.isEmpty()){
            //取出队列中第一个，并输出，将改节置为已访问
            Integer n = queue.poll();
            System.out.println(n);
            isVisited[n]=true;

            //将该层所有没被访问的节点都放入队列中
            for(int j=0;j<edges.length;j++){
                if(edges[n][j]!=0 && !isVisited[j] && !queue.contains(j)){
                    queue.offer(j);
                }
            }
        }
    }



    /**
     * 返回结点的个数
     * @return
     */
    public int getNumOfVertex() {
        return vertexList.size();
    }

    /**
     * 显示图对应的矩阵
     */
    public void showGraph() {
        for(int[] link : edges) {
            System.err.println(Arrays.toString(link));
        }
    }

    /**
     * 得到边的数目
     * @return
     */
    public int getNumOfEdges() {
        return numOfEdges;
    }

    /**
     * 返回结点i(下标)对应的数据 0->"A" 1->"B" 2->"C"
     * @param i
     * @return
     */
    public String getValueByIndex(int i) {
        return vertexList.get(i);
    }
    /**
     *返回v1和v2的权值
     */
    public int getWeight(int v1, int v2) {
        return edges[v1][v2];
    }

    /**
     * 插入结点
     * @param vertex
     */
    public void insertVertex(String vertex) {
        vertexList.add(vertex);
    }

    /**
     * 加边
     * @param v1 表示点的下标即使第几个顶点  "A"-"B" "A"->0 "B"->1
     * @param v2 第二个顶点对应的下标
     * @param weight 表示
     */
    public void insertEdge(int v1, int v2, int weight) {
        edges[v1][v2] = weight;
        edges[v2][v1] = weight;
        numOfEdges++;
    }
}
