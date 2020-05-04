package com.netty.datastructure.structure.sort;

import java.util.Arrays;

/**
 * 希尔排序
 * @author 86136
 */
public class ShellSort {
    public static void main(String[] args) {
        int [] array = {8, 9,1, 7, 2, 3, 5, 4, 6, 0,-1,12,10,-4};
        System.out.println("原始数据："+ Arrays.toString(array));
        ShellSort shellSort = new ShellSort();
        shellSort.shellSort(array);
//        //推导过程
//        for (int i=5;i<array.length;i++){
//            for(int j=i-5;j>=0;j-=5){
//                if(array[j]>array[j+5]){
//                    int temp=array[j];
//                    array[j]=array[j+5];
//                    array[j+5]=temp;
//                }
//            }
//        }
//        System.out.println("第一次："+ Arrays.toString(array));
//
//        /**
//         * 这里为什么是从2开始，因为分为2组，每组5个，每组分别要自己排好序，
//         * 所以就是从2开始于0比，3和1比；再有是4和2比，再和0比，5和3比，再和1比，
//         * 6和4，2，0；7和5，3，2，1
//         * 利用插是冒泡排序将，组内后面的值分别放到到组的合适的位置
//         * 整理就是，分为n组，就会前面的n个元素(也就是组内的第一个元素)，会被0+步长(也就是组内的第二个元素)拿来比较，
//         * 所以起始是这个步长
//         *
//         * 还有这个是和array长度的奇数，还是偶数是没关系的，因为奇数知识分组后，有的组会多出一个值而已，没区别
//         */
//        for (int i=2;i<array.length;i++){
//            for(int j=i-2;j>=0;j-=2){
//                if(array[j]>array[j+2]){
//                    int temp=array[j];
//                    array[j]=array[j+2];
//                    array[j+2]=temp;
//                }
//            }
//        }
//
//        System.out.println("第二次："+ Arrays.toString(array));
//
//        for (int i=1;i<array.length;i++){
//            for(int j=i-1;j>=0;j-=1){
//                if(array[j]>array[j+1]){
//                    int temp=array[j];
//                    array[j]=array[j+1];
//                    array[j+1]=temp;
//                }
//            }
//        }
//
//        System.out.println("第三次："+ Arrays.toString(array));
    }

    /**
     * 对上面进行总结
     * 交换方
     * @param array
     */
    public void shellSort(int [] array){

        //1
//        for(int gap=array.length/2 ;gap>0;gap/=2){
//            for(int i=gap;i<array.length;i++){
//                for(int j=i-gap;j>=0;j-=gap){
//                    if(array[j]>array[j+gap]){
//                        int temp=array[j];
//                        array[j]=array[j+gap];
//                        array[j+gap]=temp;
//                    }
//                }
//            }
//            System.out.println("分为"+gap+"组："+ Arrays.toString(array));
//        }

        //2
        // 区别在于1 先往前进一个步长，前几个步长和当前比，直到当前组的第一位
        // 2则是当前直接和前一位比较，直到当前组的第二位，因为这个时候已经区分了第一位和第二位的大小
//        for(int gap=array.length/2 ;gap>0;gap/=2){
//            for(int i=gap;i<array.length;i++){
//                for(int j=i;j>=gap;j-=gap){
//                    if(array[j]<array[j-gap]){
//                        int temp=array[j];
//                        array[j]=array[j-gap];
//                        array[j-gap]=temp;
//                    }else{
//                        //3，这里加一个else 的break 因为是向前排序，前面的都是有序的，
//                        // 当大于等于前一位时，那么肯定就会大于前面所有的，就可以跳出循环了
//                        break;
//                    }
//                }
//            }
//            System.out.println("分为"+gap+"组："+ Arrays.toString(array));
//        }

        //如果用冒泡排序的话就就需要 得到 每一组 的第一个元素，int gapStart=i%gap 当前位置除以分组数的余数，
        //但是这个方法没有一点意义，纯粹是自己加深理解
//        for(int gap=array.length/2; gap>0; gap/=2){
//            for(int i=gap;i<array.length;i++){
//                int i1 ;
//                int gapStart=i%gap;
//                for(int j=gapStart;j<array.length-gap;j+=gap){
//                    if(array[j]>array[j+gap]){
//                        i1=array[j+gap];
//                        array[j+gap]=array[j];
//                        array[j]=i1;
//                    }
//                }
//
//
//            }
//            System.out.println("分为"+gap+"组："+ Arrays.toString(array));
//        }

        // 将组内交换比较，换成插入比较 ，因为交换比较 是组内所有的元素都要比较一次，那么我们分组比较的组内排好顺序就没有意义了，
        // 因为最后只有1组的时候反正要全部比较一次，但是加了 上面 else break操作后，就会和这里的插入比较效率一样，因为都是到了准确的位置不再继续往前比较
        //但是换成插入比较，因为先前已经基本排好了顺序，插入比较的次数会少很多，从而提高效率
        for(int gap=array.length/2 ;gap>0;gap/=2){
            for(int i=gap;i<array.length;i++){
                int i1 = array[i];
                int index=i-gap;
                while (index>=0&&i1<array[index]){
                    array[index+gap]=array[index];
                    index-=gap;
                }
                array[index+gap]=i1;
            }
            System.out.println("分为"+gap+"组："+ Arrays.toString(array));
        }
    }

}
