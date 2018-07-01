package com.gcd.cpp.test;


import java.util.concurrent.Semaphore;

public class Question1 {
    public static void main(String[] args) {
        //Q1 =======================================
        int[] a = new int[10];
        Semaphore[] s = new Semaphore[10];
        for (int i = 0; i < a.length; i++) {
            a[i] = (int)(Math.random()*2);
            s[i] = new Semaphore(0);
        }
        for (int i=0; i<s.length;i++ ){
            if(i!=s.length-1){
                if(i%3==0)
                    new TA(a,s[i],s[i+1]).start();
                else if (i%3==1)
                    new TB(a,s[i],s[i+1]).start();
                else
                    new TC(a,s[i],s[i+1]).start();
            }
            else {
                new TC(a,s[i],s[0]).start();
            }
        }
        s[0].release();

    }
}

class TA extends Thread{

    private int[] a;
    private Semaphore s1, s2;

    public TA(int[] a, Semaphore s1, Semaphore s2) {
        this.a = a;
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public void run() {
        int x=1;
        try { s1.acquire(); } catch (InterruptedException e) { e.printStackTrace();}
        //Write logic to add x to each element
        for(int i=0; i<a.length; i++) {
            a[i]= a[i]+x;
        }
        System.out.println("Add operation complete..!!");
        s2.release();
    }
}

class TB extends Thread{

    private int[] a;
    private Semaphore s1, s2;

    public TB(int[] a, Semaphore s1, Semaphore s2) {
        this.a = a;
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public void run() {
        int x=1;
        try { s1.acquire(); } catch (InterruptedException e) { e.printStackTrace();}
        //Write logic to decrement each element
        for(int i=0; i<a.length; i++) {
            a[i]--;
        }
        System.out.println("Decrement operation complete..!!");
        s2.release();
    }
}

class TC extends Thread{

    private int[] a;
    private Semaphore s1, s2;

    public TC(int[] a, Semaphore s1, Semaphore s2) {
        this.a = a;
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public void run() {
        try { s1.acquire(); } catch (InterruptedException e) { e.printStackTrace();}
        //Write logic to square
        for(int i=0;i<a.length; i++) {
            int square=a[i]*a[i];
        }
        System.out.println("Square operation complete..!!");
        s2.release();
    }

}