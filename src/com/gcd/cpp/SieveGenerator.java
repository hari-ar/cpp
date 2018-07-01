package com.gcd.cpp;

public class SieveGenerator{

    static long start = System.currentTimeMillis();
    static int N = 1000000;
    public static void main(String args[]){
        int question2BigArray[] = new int[N];
        for(int i = 0; i < question2BigArray.length;i++){question2BigArray[i] = i;}
        question2BigArray[0] = 0; question2BigArray[1] = 0; //eliminate these cases
        int index = 2;

        while(index < (int)(Math.sqrt(N))){
            removeNonPrime(question2BigArray,index);
            //get next index
            index++;
            while(index < (int)(Math.sqrt(N)) && question2BigArray[index] == 0) index++;
        }
        //count primes
        int total = 0;
        System.out.println();
        for(int j = 0; j < question2BigArray.length; j++){

            if(question2BigArray[j] != 0) {
                //System.out.println(question2BigArray[j]);
                total++;
                //System.out.println(j);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.printf("Number of primes up to %d = %d",question2BigArray.length,total);
    }
    static void removeNonPrime(int array[], int prime){
        //array[currentPrime] == currentPrime number
        int j = prime*prime;
        while(j < array.length) {
                array[j] = 0; //mark it as non currentPrime
            j = j + prime;
        }
    }
}