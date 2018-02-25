package com.gcd.cpp;


/**
 *
 * Student name: Aahuya Rakshaka Hari Lakshmi Narasimhan
 * Student number: 2955114
 *
 */
// Code for Question 1 here =======================================

class Question1ZeroFinder extends Thread{

    private int[] hayStackArray;
    private int lowerBound, upperBound;
    private Question1Helper question1HelperInstance;


    //Constructor
    Question1ZeroFinder(int[] hayStackArray, int lowerBound, int upperBound, Question1Helper question1HelperInstance) {
        this.hayStackArray = hayStackArray;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.question1HelperInstance = question1HelperInstance;
    }

    @Override
    public void run() {
        for(int index =lowerBound; index<upperBound; index++){
            //System.out.println("thread "+ threadId + " index "+ index + " Value : "+hayStackArray[index]); //Enabled for debuggin.

            //Check if a zero is already found at a lower index
            if(!question1HelperInstance.isFound() || (question1HelperInstance.isFound() && index< question1HelperInstance.getIndexPosition()))
            {
                if(hayStackArray[index]==0){
                    question1HelperInstance.setAllValues(index);
                }
            }
            else{
                //Do not go further..!! Added for optimization.
                break;
            }
        }
    }
}

//POJO helper
class Question1Helper
{
    private boolean isFound = false;
    private int indexPosition = -1;

    int getIndexPosition() {
        return indexPosition;
    }

    boolean isFound() {
        return isFound;
    }

    void setAllValues(int indexPosition) {
        if(!isFound){
            isFound = true;
            this.indexPosition = indexPosition;
        }
        if(this.indexPosition>indexPosition)
        {
            isFound = true;
            this.indexPosition = indexPosition;
        }
    }

}

//============================================================================//
//
// Main Method For the Assignment. Above classes are exclusive to question 1.
//
//============================================================================//

public class Assignment2_2018 {

    private static int numberOfThreads = Runtime.getRuntime().availableProcessors();


    public static void main(String args[]){




//        int[] question1VeryLargeArray = new int[10]; Enabled for debugging. Comment the line below.
        System.out.println("\n================Begininng of Question1================");

        //Initial Declaration//
        int[] question1VeryLargeArray = new int[1000];

        int[] question1IndexPointer = new int[numberOfThreads+1];
        Question1Helper question1Question1HelperInstance = new Question1Helper();
        Thread question1Workers[] = new Question1ZeroFinder[numberOfThreads];

        //Initializing array with random values
        for(int i=0;i<question1VeryLargeArray.length;i++){
            question1VeryLargeArray[i] = (int) (Math.random()*50);
        }

        //Code to distribute load between threads
        for(int i=0;i<=numberOfThreads;i++){
            question1IndexPointer[i] = (i*question1VeryLargeArray.length)/numberOfThreads;
        }

        //Starting the threads.
        for(int i=0;i<numberOfThreads;i++){
            question1Workers[i] = new Question1ZeroFinder(question1VeryLargeArray, question1IndexPointer[i], question1IndexPointer[i+1], question1Question1HelperInstance);
            question1Workers[i].start();
        }

        //Code to let the threads join back.
        for(int i=0;i<numberOfThreads;i++){
            try {
                question1Workers[i].join();
            } catch (InterruptedException e) {
                //Do Nothing
            }
        }

        //Getting Index of first 0 occurance.
        if(question1Question1HelperInstance.isFound())
            System.out.println("Zero is found in Array. Index Value of first occurance is : "+ question1Question1HelperInstance.getIndexPosition());
        else
            System.out.println("Not found in the Array.!");


        System.out.println("===================End of Question1===================\n");
        System.out.println("================Begininng of Question2================");
        //Enabled for debugging.
        //System.out.println("Array is :");
        /*for (int value : question1VeryLargeArray
                ) {
            System.out.print(" "+value);
        }*/
        long start = System.currentTimeMillis();
        int N = 200000000;
        int question2BigArray[] = new int[N];

        int count = 0;


        for(int i = 0; i < question2BigArray.length ; i++){
            question2BigArray[i] = i;
        }
        question2BigArray[0] = 0;
        question2BigArray[1] = 0;
        int prime = 2;

        while(prime < (int)(Math.sqrt(N)))
        {
            removeNonPrime(prime,question2BigArray);
            prime++;
            while(prime < (int)(Math.sqrt(N)) && question2BigArray[prime] == 0) {
                prime++;
            }
        }

        for(int i = 0; i< N;i++){
            if(question2BigArray[i]!=0){
               // System.out.println(question2BigArray[i]);
                count++;
            }
        }
        long end = System.currentTimeMillis();
        long diff = end-start;
        System.out.println("diff is"+ diff);
        System.out.println("Total number of prime numbers are "+count);
    }

    private static void removeNonPrime(int prime, int[] question2BigArray) {
        int lowerBound = prime*prime;
        int question2IndexPointer[] = new int[numberOfThreads+1];
        Thread question2Workers[] = new Question2Helper[numberOfThreads];

        //Code to distribute load between threads
        for(int i=0;i<=numberOfThreads;i++){
            question2IndexPointer[i] = lowerBound+(i*question2BigArray.length)/numberOfThreads;
        }

        //Starting the threads.
        for(int i=0;i<numberOfThreads;i++){
            question2Workers[i] = new Question2Helper(question2BigArray,question2IndexPointer[i],question2IndexPointer[i+1],prime);
            question2Workers[i].start();
        }

        //Code to let the threads join back.
        for(int i=0;i<numberOfThreads;i++){
            try {
                question2Workers[i].join();
            } catch (InterruptedException ignoreException) { //Do nothing
                 }
        }
    }
}

class Question2Helper extends Thread{

    private int question2BigArray[];
    private int currentPrime;
    private int lowerBound, upperBound;

    Question2Helper(int[] question2BigArray, int lowerBound, int upperBound, int currentPrime) {
        this.question2BigArray = question2BigArray;
        this.currentPrime = currentPrime;
        int lowerBoundRemainder = lowerBound%currentPrime;
        int upperBoundRemainder = upperBound%currentPrime;
        if(lowerBoundRemainder==0)
            this.lowerBound = lowerBound;
        else
        {
            if(lowerBound-lowerBoundRemainder>0)
                this.lowerBound = lowerBound-(lowerBound%currentPrime);
            else
                this.lowerBound = lowerBound+(lowerBound%currentPrime);
        }
        if(upperBound<question2BigArray.length)
        {
            if(upperBoundRemainder==0)
                this.upperBound = upperBound;
            else
                this.upperBound = upperBound - upperBoundRemainder;
        }
        else {
            this.upperBound = question2BigArray.length;
        }
    }

    @Override
    public void run(){
        if(lowerBound<upperBound){
            //System.out.println(currentPrime + ":" +lowerBound + ":" +upperBound);
            int j = lowerBound;
            while(j < upperBound){
                question2BigArray[j] = 0; //mark it as non prime
                j = j + currentPrime;
            }
        }
    }
}





//================================================
//
//
//
// A
//
//
//
// =======================