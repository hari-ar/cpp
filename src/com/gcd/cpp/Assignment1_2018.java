/*
*
* Assignment 1
*
* @Author : Aahuya Rakshaka Hari Lakshmi Narasimhan
*
* @Student Number : 2955114
*
*
* */



//package com.gcd.cpp;

import java.util.HashMap;

public class Assignment1_2018 {

    public static void main(String[] args) {

        //Question 1//
        //====================================================================================================//
        System.out.println("//=======================Question 1 Begins==========================//");
        Thread question1Thread1 = new Question1();
        question1Thread1.start();
        try {
            question1Thread1.join(); //Added to print Question 1's results ahead of Question 2
        } catch (InterruptedException e) {

        }
        System.out.println("//=======================Question 1 Ends==========================//");
        System.out.println("\n");
        //====================================================================================================//
        //Question 1//


        //Question 2//
        //====================================================================================================//
        System.out.println("//=======================Question2 Begins==========================//");
        //An array to be accessed by both the threads
        int[] question2IntArray = new int[1000];
        Thread question2Thread1 = new Question2(0,question2IntArray.length/2, question2IntArray);
        Thread question2Thread2 = new Question2(question2IntArray.length/2, question2IntArray.length, question2IntArray);
        question2Thread1.start();question2Thread2.start();
        //A Map to store the count of each occurance. Key will be the die number and value will be the count.
        HashMap<Integer,Integer> countMap = new HashMap<Integer, Integer>();
        try {
            question2Thread1.join();
            question2Thread2.join();
        } catch (InterruptedException e) {

        }
        //Iterate through array
        for (int key : question2IntArray) {
            if(countMap.containsKey(key)){
                countMap.put(key,countMap.get(key)+1); //Increment count of occurrence
            }
            else
                countMap.put(key,1); //Insert new key if not already present.
        }
        countMap.forEach((key,value) -> {
            System.out.println("Frequency of "+key+" is "+value);
        });
        System.out.println("//=======================Question 2 Ends============================//");
        System.out.println("\n");
        //====================================================================================================//
        //Question 2//


        //Question 3//
        //====================================================================================================//
        System.out.println("//===========================Question 3 Begins======================//");
        int[] question3IntArray = new int[5000];
        for(int i = 0 ; i <question3IntArray.length;i++){
            question3IntArray[i] = (int) (Math.random()*100000); //Large number
        }
        Question3 question3Thread1 = new Question3(0,question3IntArray.length/2, question3IntArray);
        Question3 question3Thread2 = new Question3(question3IntArray.length/2, question3IntArray.length, question3IntArray);
        question3Thread1.start();question3Thread2.start();
        try {
            question3Thread1.join();
            question3Thread1.join();
        } catch (InterruptedException e) {

        }

        if(question3Thread2.getLargestNumber() > question3Thread1.getLargestNumber()) {
            System.out.println("Largest number in array is "+question3Thread2.getLargestNumber());
        }
        else{
            System.out.println("Largest number in array is "+question3Thread1.getLargestNumber());
        }
        System.out.println("//=======================Question 3 Ends============================//");


    }


}



class Question1 extends Thread{

    int headsCount = 0;
    int tailsCount = 0;

    @Override
    public void run() {
        for(int i=0;i<1000;i++){

            if(Math.random()<0.5)
                headsCount++;
            else
                tailsCount++;
        }

        if(Math.abs(headsCount-tailsCount)>=50){
            System.out.println("Biased coin");
        }
        else
            System.out.println("Fair Coin");

    }
}


class Question2 extends Thread {

    private int lowerBound;
    private int upperBound;
    private int[] intArrayToAccess;

    Question2(int lowerBound, int upperBound, int[] intArrayToAccess){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.intArrayToAccess = intArrayToAccess;
    }

    @Override
    public void run(){
        for(int i =lowerBound; i<upperBound; i++){
            intArrayToAccess[i] = (int) (Math.random()*6) + 1;
        }
    }

}


class Question3 extends Thread {
    private int lowerBound;
    private int upperBound;
    private int[] intArrayToAccess;
    private int largestNumber = 0;

    Question3(int lowerBound, int upperBound, int[] intArrayToAccess){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.intArrayToAccess = intArrayToAccess;
    }

    @Override
    public void run(){

        for(int i =lowerBound; i<upperBound; i++){
            if(intArrayToAccess[i]>largestNumber)
                largestNumber = intArrayToAccess[i];
        }
    }

    public int getLargestNumber() {
        return largestNumber;
    }
}
