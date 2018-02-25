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

    public static void main(String args[]){

        int numberOfThreads = Runtime.getRuntime().availableProcessors();


//        int[] question1VeryLargeArray = new int[10]; Enabled for debugging. Comment the line below.
        System.out.println("\n================Begininng of Question1================");

        //Initial Declaration//
        int[] question1VeryLargeArray = new int[1000];

        int[] question1IndexPointer = new int[numberOfThreads+1];
        Question1Helper question1Question1HelperInstance = new Question1Helper();
        Thread workers[] = new Question1ZeroFinder[numberOfThreads];

        //Initializing array with random values
        for(int i=0;i<question1VeryLargeArray.length;i++){
            question1VeryLargeArray[i] = (int) (Math.random()*50000);
        }

        //Code to distribute load between threads
        for(int i=0;i<=numberOfThreads;i++){
            question1IndexPointer[i] = (i*question1VeryLargeArray.length)/numberOfThreads;
        }

        //Starting the threads.
        for(int i=0;i<numberOfThreads;i++){
            workers[i] = new Question1ZeroFinder(question1VeryLargeArray, question1IndexPointer[i], question1IndexPointer[i+1], question1Question1HelperInstance);
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
        int N = 10000000;
        int question2BigArray[] = new int[N];
        int count = 0;

        Question2Helper parallelThreads[] = new Question2Helper[numberOfThreads];
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
                Question2Helper serialHelperOperation = new Question2Helper(question2BigArray,index);
                serialHelperOperation.run(); //Running as method.. not as thread..!!
                index++;
            }
            if(index<(int)(Math.sqrt(N))) {
                for (int i = 0; i < numberOfThreads; i++) {
                    index++;
                    index = getNextPrime(N, index, question2BigArray);
                    //System.out.println("index" + index);
                    parallelThreads[i] = new Question2Helper(question2BigArray, index);
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
        System.out.println("Total number of prime numbers are "+count);


}

    private static int getNextPrime(int N, int currentIndex, int[] question2BigArray) {
        while(currentIndex < (int)(Math.sqrt(N)) && question2BigArray[currentIndex] == 0) {
            currentIndex++;
        }
        return currentIndex;
    }

}

class Question2Helper extends Thread{

    private int question2BigArray[];
    private int currentPrime;


    int getCurrentPrime() {
        return currentPrime;
    }

    Question2Helper(int[] question2BigArray, int currentPrime) {
        this.question2BigArray = question2BigArray;
        this.currentPrime = currentPrime;
    }

    @Override
    public void run(){
            int j = currentPrime*currentPrime;
            while(j < question2BigArray.length){
                if(question2BigArray[j] % currentPrime == 0)
                {
                    question2BigArray[j] = 0; //mark it as non prime
                }
                j = j + currentPrime;
            }
    }
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