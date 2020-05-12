package com.netty.datastructure;

/**
 * 最小二乘法 线性回归
 * y = a x + b 
 *
 * b = sum( y ) / n - a * sum( x ) / n
 *
 * a = ( n * sum( xy ) - sum( x ) * sum( y ) ) / ( n * sum( x^2 ) - sum(x) ^ 2 )
 *
 * @author tian.yj
 *
 */
public class LineCoefficient {

    public static void main(String[] args) {
//        double[] x = { 0 , 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 ,10, 11} ;
//        //double[] y = {23 , 44 , 32 , 56 , 33 , 34 , 55 , 65 , 45 , 55 } ;
//        double[] y = { 27532, 54769 , 81968 , 107715 , 27726 , 54069 , 79832 , 105786 , 28026 , 57241 ,86664, 116716} ;
//        System.out.println( estimate(x, y, x.length ));

        System.out.println(Integer.toBinaryString((byte)28));
    }

    /**
     * 预测
     * @param x
     * @param y
     * @param i
     * @return
     */
    public static double estimate( double[] x , double[] y , int i ) {
        double a = getXc( x , y ) ;
        double b = getC( x , y , a ) ;
        return a * i + b ;
    }

    /**
     * 计算 x 的系数
     * @param x
     * @param y
     * @return
     */
    public static double getXc( double[] x , double[] y ){
        int n = x.length ;
        return ( n * pSum( x , y ) - sum( x ) * sum( y ) )
                / ( n * sqSum( x ) - Math.pow(sum(x), 2) ) ;
    }

    /**
     * 计算常量系数
     * @param x
     * @param y
     * @param a
     * @return
     */
    public static double getC( double[] x , double[] y , double a ){
        int n = x.length ;
        return sum( y ) / n - a * sum( x ) / n ;
    }

    /**
     * 计算常量系数
     * @param x
     * @param y
     * @return
     */
    public static double getC( double[] x , double[] y ){
        int n = x.length ;
        double a = getXc( x , y ) ;
        return sum( y ) / n - a * sum( x ) / n ;
    }

    private static double sum(double[] ds) {
        double s = 0 ;
        for( double d : ds ) s = s + d ;
        return s ;
    }

    private static double sqSum(double[] ds) {
        double s = 0 ;
        for( double d : ds ) s = s + Math.pow(d, 2) ;
        return s ;
    }

    private static double pSum( double[] x , double[] y ) {
        double s = 0 ;
        for( int i = 0 ; i < x.length ; i++ ) s = s + x[i] * y[i] ;
        return s ;
    }
}