package com.gcd.cpp;

public class SieveGenerator{
    static int N = 121121;
    public static void main(String args[]){
        int f[] = new int[N];
        for(int j = 0; j < f.length;j++){f[j] = j;}
        f[0] = 0; f[1] = 0; //eliminate these cases
        int p = 2;
        while(p < (int)(Math.sqrt(N))){
            removeNonPrime(f,p);
            //get next prime
            p++;
            while(p < (int)(Math.sqrt(N)) && f[p] == 0) p++;
        }
        //count primes
        int total = 0;
        System.out.println();
        for(int j = 0; j < f.length; j++){

            if(f[j] != 0) {
                total++;
                System.out.println(j);
            }
        }
        System.out.printf("Number of primes up to %d = %d",f.length,total);
    }
    static void removeNonPrime(int ff[], int k){
        //ff[k] == prime number
        int j = k*k;
        while(j < ff.length) {
                ff[j] = 0; //mark it as non prime
            j = j + ff[k];
        }
    }
}