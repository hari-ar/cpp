package com.gcd.cpp;
/**
 * Student name: Aahuya Rakshaka Hari Lakshmi Narasimhan
 * Student number: 2955114
 */

import java.util.ArrayList;
import java.util.concurrent.*;


class Question1 implements Callable<Question1Helper> {
    int[] dataQ1;
    int min, max;

    public Question1(int[] dataQ1, int min, int max) {
        this.dataQ1 = dataQ1;
        this.min = min;
        this.max = max;
    }

    @Override
    public Question1Helper call() throws Exception {
        int minimumNumber = Integer.MAX_VALUE;
        int frequency = 0;

        for (int index = min; index < max; index++) {
            if (dataQ1[index] < minimumNumber) {
                minimumNumber = dataQ1[index];
                frequency = 1;
            } else if (dataQ1[index] == minimumNumber) {
                frequency++;
            }

        }
        return new Question1Helper(minimumNumber, frequency);
    }


}

class Question1Helper {
    int minimumNumber;
    int frequency;

    public Question1Helper(int minimumNumber, int frequency) {
        this.minimumNumber = minimumNumber;
        this.frequency = frequency;
    }
}


public class Assignment3_2018 {
    public static void main(String[] args) {
        //Test for Question 1
        int dataQ1[] = new int[100];

        for (int j = 0; j < dataQ1.length; j++) {
            dataQ1[j] = (int) (Math.random() * dataQ1.length);
        }

        for (int i : dataQ1) {
            System.out.print(i + " ");
        }
        int nProc = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(nProc);
        int[] index = new int[nProc + 1];
        ArrayList<Future<Question1Helper>> futureList = new ArrayList<>();
        for (int i = 0; i <= nProc; i++) {
            index[i] = (i * dataQ1.length) / nProc;
        }
        for (int j = 0; j < index.length - 1; j++) {
            Future<Question1Helper> f = pool.submit(new Question1(dataQ1, index[j], index[j + 1]));
            futureList.add(f);
        }

        ArrayList<Question1Helper> resultList = new ArrayList<>();
        futureList.forEach(eachFuture -> {
            try {
                resultList.add(eachFuture.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        pool.shutdown();

        int minimumNumber = Integer.MAX_VALUE;
        int frequency = 0;

        for (int i = 0; i < resultList.size(); i++) {
            if (minimumNumber > resultList.get(i).minimumNumber) {
                minimumNumber = resultList.get(i).minimumNumber;
                frequency = resultList.get(i).frequency;
            } else if (minimumNumber == resultList.get(i).minimumNumber) {
                frequency = frequency + resultList.get(i).frequency;
            }
        }
        System.out.println("\nLeast number in Array is " + minimumNumber);
        System.out.println("Its frequency " + frequency);


        //calculate min value and frequency of occurrence


        //=======================================================
        //Question 2 - use fork-join pool to optimise algorithm

        //========================================================
    }

}

/* Code for Merge Sort
static void mergeSort(int f[], int lb, int ub){
    	//termination reached when a segment of size 1 reached - lb+1 = ub
    	if(lb+1 < ub){
    		int mid = (lb+ub)/2;
    		mergeSort(f,lb,mid);
    		mergeSort(f,mid,ub);
    		merge(f,lb,mid,ub);
    	}
}
*/

/* class for ForkJoin pool is given below in outline and
// contains the relevant private methods to solve it.
class Sort ...{
 ...
 private void insertionSort(int dt[], int a, int b){
 	 for(int i = a; i < b; i++){
		 int j = i;
		 while(j > a && dt[j] < dt[j-1]){
				int temp = dt[j]; dt[j] = dt[j-1]; dt[j-1] = temp;
				j--;
		 }
	 }
 }
 private void merge(int f[], int lb, int mid, int ub){
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
}

*/
