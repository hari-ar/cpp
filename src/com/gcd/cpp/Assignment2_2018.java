package com.gcd.cpp;


/**
 *
 * Student name: Aahuya Rakshaka Hari Lakshmi Narasimhan
 * Student number: 2955114
 *
 */
// Code for Question 1 here =======================================

class ZeroFinder extends Thread{

    private int[] hayStackArray;
    private int lowerBound, upperBound;
    private int threadId;
    private Helper helperInstance;


    //Constructor
    ZeroFinder(int[] hayStackArray, int lowerBound, int upperBuond, int threadId, Helper helperInstance) {
        this.hayStackArray = hayStackArray;
        this.lowerBound = lowerBound;
        this.upperBound = upperBuond;
        this.threadId = threadId;
        this.helperInstance = helperInstance;
    }

    @Override
    public void run() {
        for(int index =lowerBound; index<upperBound; index++){
            //System.out.println("thread "+ threadId + " index "+ index + " Value : "+hayStackArray[index]); //Enabled for debuggin.

            //Check if a zero is already found at a lower index
            if(!helperInstance.isFound() || (helperInstance.isFound() && index<helperInstance.getIndexPosition()))
            {
                if(hayStackArray[index]==0){
                    helperInstance.setAllValues(threadId,index);
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
class Helper
{
    private boolean isFound = false;
    private int indexPosition = -1;

    int getIndexPosition() {
        return indexPosition;
    }

    boolean isFound() {
        return isFound;
    }

    void setAllValues(int foundByThreadId, int indexPosition) {
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

    public static void main(String args[]){

//        int[] aVeryLargeArray = new int[10]; Enabled for debugging. Comment the line below.
        System.out.println("\n================Begininng of Question1================");

        //Initial Declaration//
        int[] aVeryLargeArray = new int[1000000];
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        int[] indexPointer = new int[numberOfThreads+1];
        Helper helperInstance = new Helper();
        Thread workers[] = new ZeroFinder[numberOfThreads];

        //Initializing array with random values
        for(int i=0;i<aVeryLargeArray.length;i++){
            aVeryLargeArray[i] = (int) (Math.random()*50000);
        }

        //Code to distribute load between threads
        for(int i=0;i<=numberOfThreads;i++){
            indexPointer[i] = (i*aVeryLargeArray.length)/numberOfThreads;
        }

        //Starting the threads.
        for(int i=0;i<numberOfThreads;i++){
            workers[i] = new ZeroFinder(aVeryLargeArray, indexPointer[i], indexPointer[i+1], i+1, helperInstance);
            workers[i].start();
        }

        //Code to let the threads join back.
        for(int i=0;i<numberOfThreads;i++){
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                //Do Nothing
            }
        }

        //Getting Index of first 0 occurance.
        if(helperInstance.isFound())
            System.out.println("Zero is found in Array. Index Value of first occurance is : "+helperInstance.getIndexPosition());
        else
            System.out.println("Not found in the Array.!");


        System.out.println("===================End of Question1===================\n");
        System.out.println("================Begininng of Question2================");
        //Enabled for debugging.
        //System.out.println("Array is :");
        /*for (int value : aVeryLargeArray
                ) {
            System.out.print(" "+value);
        }*/
    }
//============================================================================//
//
// End of all Question 1 info.
//
//============================================================================//

    //End Question 1 =======================================================


}






//Code for Sieve of Erathostenes





//========================================================


//=======================================================================
/* Below is the code for a single threaded program for the Sieve of Erathostenes
   You should copy this program to your own java file and give N a value to execute it

class SieveGenerator{
	static int N = ????;
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
          if(f[j] != 0) total++;
	  }
	  System.out.printf("Number of primes up to %d = %d",f.length,total);
}
	static void removeNonPrime(int ff[], int k){
		//ff[k] == prime number
		int j = k*k;
		while(j < ff.length){
			if(ff[j] % k == 0) ff[j] = 0; //mark it as non prime
			j = j + ff[k];
		}
	}
}
*/
//=======================================================================