package com.netty.datastructure.algorithm.dynamic;

/**
 * 背包问题
 * 主要是两种情况
 *    w[i-1]>j =>v[i][j]=v[i-1][j]
 *    w[i-1]<=j =>v[i][j] =Max(v[i-1][j],val[i]+v[i-1][j-w[i]];)
 * @author 86136
 */
public class KnapsackProblem {
    public static void main(String[] args) {
        int[] w={1,4,3};
        int[] val={1500,3000,2000};

        //容量
        int capacity=4;
        //货物种类数量
        int count=w.length;

        //v[i][j] 表示在前i个物品中能够装入容量为j的背包中的最大价值
        int[][] v = new int[count + 1][capacity + 1];

        //记录放入商品的情况
        int[][] path=new int[count + 1][capacity + 1];

        //处理边界
        for(int i=0 ;i<v.length;i++){
            v[i][0]=0;
        }
        for(int i=0;i<v[0].length;i++){
            v[0][i]=0;
        }

        //设置值
        for(int i=1;i<v.length;i++){

            for(int j=1;j<v[i].length;j++){
                //因为二维数组v 中 是从1开始的所以从w 中获取重量是w[i-1]
                if(w[i-1]>j){
                    //如果i的重量大于当前容量 取v[i-1][j]
                    //这里是因为当前这个种类是无法放入背包的，只能取前一个的
                    v[i][j]=v[i-1][j];
                }else{
                    //如果i重量小于当前容量，判断前一个的价值，也就是不放入i 和放入i的价值哪个大
                    int last=v[i-1][j];
                    //同样因为二维数组v 中 是从1开始的所以从w 中获取价值是是val[i-1]
                    int andi=val[i-1]+v[i-1][j-w[i-1]];

                    if(last>andi){
                        v[i][j]=v[i-1][j];
                    }else{
                        v[i][j]=andi;

                        //同时在path中标记放入情况
                        path[i][j]=1;
                    }
                }
            }
        }

        //遍历
        //输出一下v 看看目前的情况
        for(int i =0; i < v.length;i++) {
            for(int j = 0; j < v[i].length;j++) {
                System.out.print(v[i][j] + " ");
            }
            System.out.println();
        }

        //输入放入情况
        int i = path.length - 1;
        int j = path[0].length - 1;
        while(i > 0 && j > 0 ) {
            if(path[i][j] == 1) {
                System.out.printf("第%d个商品放入到背包\n", i);
                j -= w[i-1];
            }
            i--;
        }
    }
}
