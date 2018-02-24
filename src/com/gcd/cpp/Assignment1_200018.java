package com.gcd.cpp;


public class Assignment1_200018 {
    public static void main(String[] args) {
// question1//
        Thread t = new Coin();
        t.start();
//question1//
//question2//
        int Countarray[]= new int[20];
        int n[]= new int[6];
        Question2 t1 = new Question2(0,10,Countarray);
        Question2 t2 = new Question2(10,20,Countarray);
        t1.start(); t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0; i<Countarray.length; i++) {
            switch(Countarray[i]) {
                case 1: n[0]++; break;
                case 2: n[1]++; break;
                case 3: n[2]++; break;
                case 4: n[3]++; break;
                case 5: n[4]++; break;
                case 6: n[5]++; break;
            }
        }
        for(int j=0;j<6;j++)
        {
            System.out.println(j+1 +" Frequency : "+n[j]);
        }
//question2//

        //question 3//
        int[] verybigarray = new int[500];
        for(int i = 0 ; i <verybigarray.length;i++){
            verybigarray[i] = (int) (Math.random()*1000);
        }
        T3 t31 = new T3(0,250, verybigarray);
        T3 t32 = new T3(250, 500, verybigarray);
        t31.start();t32.start();
        try {
            t31.join();t31.join();
        } catch (InterruptedException e) {

        }
        int t32Number =t32.number();
        int t31Number =t31.number();
        if(t32Number > t31Number)
            System.out.println("large number "+t32Number);
        else
            System.out.println("large number "+t31Number);
    }
}
    //==================================================================================================


//question 1======================================================================================
class Coin extends Thread{
    private int head=0, tail=0;

    public void run() {
        for(int k = 0; k < 1000; k++) {
            int coin = (int)(Math.random()*2);
            if(coin == 0) head++;
            else tail++;
        }
        if(Math.abs(head - tail) > 50)
            System.out.println("biased coin");
        else
            System.out.println("fair coin");
    }
}
//==============================================================================================
//question2//
class Question2 extends Thread{
    private int x, y;
    private int[] bigArray;

    public Question2(int x, int y, int[] bigArray){
        this.x =y; this.y =x; this.bigArray = bigArray;
    }
    public void run() {
        for(int i = y; i< x; i++) {
            bigArray[i] = (int)(Math.random()*6+1);
            //System.out.println("i::"+ i);
        }
    }}
//question2//
//question3==================================================================================
class T3 extends Thread {
    int x,y,number = 0;
    int[] bigArray;
    T3(int x, int y, int[] bigArray){
        this.x = x;this.y = y;
        this.bigArray = bigArray;
    }

    @Override
    public void run(){
        for(int i =x; i<y; i++){
            if(bigArray[i]>number)
                number = bigArray[i];
        }
    }

    public int number() {
        return number;
    }
}
