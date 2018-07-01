package com.gcd.cpp;
/**
 * Student name: Aahuya Rakshaka Hari Lakshmi Narasimhan
 * Student number: 2955114
 */

import java.util.ArrayList;
import java.util.concurrent.*;



public class Assignment3_2018 {
    public static void main(String[] args) {
        //=======================================================
        //Question 2 - use fork-join pool to optimise algorithm

        //========================================================
        int dataQ2[] = new int[10000000];
        System.out.println("Input Array Size is "+dataQ2.length);
        for (int j = 0; j < dataQ2.length; j++) {
            dataQ2[j] = (int) (Math.random() * 10000);
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(new MergeSort(0,dataQ2.length,dataQ2));

    }

}

    class MergeSort extends RecursiveAction{

        int lowerBound,upperBound;

        int[] data;

        public MergeSort(int lowerBound, int upperBound, int[] data) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.data = data;
        }

        @Override
        protected void compute() {
            if(Math.abs(upperBound-lowerBound)> 1){
                int mid = (upperBound+lowerBound)/2;
                MergeSort left = new MergeSort(lowerBound,mid,data);
                MergeSort right = new MergeSort(mid,upperBound,data);
                invokeAll(left,right);
                left.join();right.join();
                merge(data,lowerBound,mid,upperBound);
            }
            else{
                insertionSort(data,lowerBound,upperBound);
            }
        }


        private  void merge(int f[], int lb, int mid, int ub){
            int c[] = new int[ub-lb];
            int k = 0;int j = lb; int h = mid;
            while(j < mid && h < ub){
                if(f[j] <= f[h]){
                    c[k] = f[j];
                    j++;
                }
                else{
                    c[k] = f[h];
                    h++;
                }
                k++;
            }
            while(j < mid){ c[k] = f[j];  k++; j++; }
            while(h < ub){c[k] = f[h]; k++; h++;}
            //Now copy data back to array
            for(int p = 0; p < c.length;p++)
                f[lb+p] = c[p];
        }

        private void insertionSort(int dt[], int a, int b){
            for(int i = a; i < b; i++){
                int j = i;
                while(j > a && dt[j] < dt[j-1]){
                    int temp = dt[j]; dt[j] = dt[j-1]; dt[j-1] = temp;
                    j--;
                }
            }
        }
    }
