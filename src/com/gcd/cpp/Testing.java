package com.gcd.cpp;

public class Testing {
    public static void main(String args[]){
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        long start = System.currentTimeMillis();
        int N = 200000000;
        int question2BigArray[] = new int[N];
        int count = 0;

        Thread parallelThreads[] = new Qon2Helper1[numberOfThreads];
        for(int i = 0; i < question2BigArray.length ; i++){
            question2BigArray[i] = i;
        }
        question2BigArray[0] = 0;
        question2BigArray[1] = 0;

        int index = 2;
        while(index<(int)(Math.sqrt(N)))
        {
            for(int i = 0; i< numberOfThreads; i++){
                index = getNextPrime(N,index, question2BigArray);
                Qon2Helper1 serialHelperOperation = new Qon2Helper1(question2BigArray,index);
                serialHelperOperation.run(); //Running as method.. not as thread..!!
                index++;
            }
            if(index<(int)(Math.sqrt(N))) {
                for (int i = 0; i < numberOfThreads; i++) {
                    index++;
                    index = getNextPrime(N, index, question2BigArray);
                    parallelThreads[i] = new Qon2Helper1(question2BigArray, index);
                    parallelThreads[i].start();
                }


                try {
                    for (int i = 0; i < numberOfThreads; i++) {
                        parallelThreads[i].join();
                    }
                } catch (InterruptedException ignored) {
                }

            } index++;
        }

        for(int i = 0; i< N;i++){
            if(question2BigArray[i]!=0){
                count++;
                //        System.out.println(i);
            }
        }
        long end = System.currentTimeMillis();
        long diff = end-start;
        System.out.println("diff is"+ diff);
        System.out.println("Total number of currentPrime numbers are "+count);

    }

    private static int getNextPrime(int N, int currentIndex, int[] question2BigArray) {
        while(currentIndex < (int)(Math.sqrt(N)) && question2BigArray[currentIndex] == 0) {
            currentIndex++;
        }
        return currentIndex;
    }

}
class Qon2Helper1 extends Thread{


    private int question2BigArray[];
    private int currentPrime;



    Qon2Helper1(int[] question2BigArray, int currentPrime) {
        this.question2BigArray = question2BigArray;
        this.currentPrime = currentPrime;
    }

    @Override
    public void run(){
        int j = currentPrime*currentPrime;
        while(j < question2BigArray.length){
            question2BigArray[j] = 0; //mark it as non currentPrime
            j = j + currentPrime;
        }
    }
}
