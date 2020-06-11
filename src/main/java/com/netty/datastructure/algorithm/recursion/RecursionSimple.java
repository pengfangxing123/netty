package com.netty.datastructure.algorithm.recursion;

/**
 * 递归
 * @author 86136
 */
public class RecursionSimple {
    public static void main(String[] args) {
        Maze maze = new Maze(8, 7);
        maze.list();
        maze.setWay(1,1);
        maze.list();
    }
}

class Maze {
    private int [][] map;
    private int width;
    private int height;

    public Maze(int height,int width) {
        this.width=width;
        this.height=height;
        this.map = new int[height][width];
        init();
    }

    private void init() {
        //上下置为 1
        for(int i=0;i<width;i++){
            map[0][i]=1;
            map[height-1][i]=1;
        }

        //左右置为 1
        for(int i=0;i<height;i++){
            map[i][0]=1;
            map[i][width-1]=1;
        }
        //挡板
        map[6][2]=1;
        map[7][2]=1;
    }

    public void list(){
        for(int i=0;i<height;i++){
            for (int j=0;j<width;j++){
                System.out.print(map[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * 找到map[heighr-2][width-2]位置为到终点
     * 当map[][]为位置为0表示该点没有走过，1表示为墙，2表示可走，3表示该点已经走过，但是走不通
     * 走迷宫时，需要确定一个策略，下->右->上->左，如果该点走不通，再回溯
     * @param i 从哪个位置开始找
     * @param j
     * @return
     */
    public boolean setWay(int i,int j){
        if(map[height-2][width-2]==2){
            return true;
        }
        //当前点还没走过
        if(map[i][j]==0){
            //假定该点是可以走的
            map[i][j]=2;

            if(setWay(i+1,j)){
                //向下走
                return true;
            }else if(setWay(i,j+1)){
                //向右走
                return true;
            }else if(setWay(i-1,j)){
                //向上走
                return true;
            }else if(setWay(i,j-1)){
                //向左走
                return true;
            }else{
                //该点不通
                map[i][j]=3;
                return false;
            }
        }else{
            //1 为墙，2 该点已经走过(不能再从这里走了。不然会死循环)，3 为走过但是不通
            return false;
        }
    }
}
