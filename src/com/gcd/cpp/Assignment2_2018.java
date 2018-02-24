package com.gcd.cpp;


/**
 *
 * Student name: Aahuya Rakshaka Hari Lakshmi Narasimhan
 * Student number: 2955114
 *
 */
// Code for Question 1 here =======================================

public class Assignment2_2018 {

    public static void main(String args[]){

        int[] aVeryLargeArray = new int[10000000];

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        int[] indexPointer = new int[numberOfThreads+1];
        Helper helperInstance = new Helper();
        Thread workers[] = new ZeroFinder[numberOfThreads];
        for(int i=0;i<aVeryLargeArray.length;i++){
            aVeryLargeArray[i] = (int) (Math.random()*1000);
        }
        for(int i=0;i<=numberOfThreads;i++){
            indexPointer[i] = (i*aVeryLargeArray.length)/numberOfThreads;
            //System.out.println(indexPointer[i]);
        }
        for(int i=0;i<numberOfThreads;i++){
            workers[i] = new ZeroFinder(aVeryLargeArray, indexPointer[i], indexPointer[i+1], i+1, helperInstance);
            workers[i].start();
        }
        for(int i=0;i<numberOfThreads;i++){
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                //Do Nothing
            }
        }
        if(helperInstance.isFound())
        {
            System.out.println("Value is : ");
            System.out.println(helperInstance.getIndexPosition());

        }
        else
        {
            System.out.println("Not found");
        }

        //System.out.println("Array is :");
        /*for (int value : aVeryLargeArray
                ) {
            System.out.print(" "+value);
        }*/
    }

}


class ZeroFinder extends Thread{

    int[] hayStackArray;
    int lowerBound, upperBound;
    int threadId;
    Helper helperInstance;

    public ZeroFinder(int[] hayStackArray, int lowerBound, int upperBuond, int threadId, Helper helperInstance) {
        this.hayStackArray = hayStackArray;
        this.lowerBound = lowerBound;
        this.upperBound = upperBuond;
        this.threadId = threadId;
        this.helperInstance = helperInstance;
        //System.out.println(lowerBound+" .. "+ upperBuond+" .. "+ threadId);
    }

    @Override
    public void run() {
        for(int index =lowerBound; index<upperBound; index++){
            //System.out.println("thread "+ threadId + " index "+ index + " Value : "+hayStackArray[index]);
            if(!helperInstance.isFound() || (helperInstance.isFound() && helperInstance.getFoundByThreadId()>threadId))
            {
                if(hayStackArray[index]==0){
                    helperInstance.setAllValues(threadId,index);
                }
            }
            else{
                break;
            }
        }
    }
}

class Helper
{
    private boolean isFound = false;
    private int foundByThreadId = -1;
    private int indexPosition = -1;

    public int getIndexPosition() {
        return indexPosition;
    }

    public void setIndexPosition(int indexPosition) {
        this.indexPosition = indexPosition;
    }

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }

    public int getFoundByThreadId() {
        return foundByThreadId;
    }

    public void setFoundByThreadId(int foundByThreadId) {
        this.foundByThreadId = foundByThreadId;
    }

    public void setAllValues(int foundByThreadId, int indexPosition) {
        if(this.foundByThreadId<0){
            setFound(true);
            setIndexPosition(indexPosition);
            setFoundByThreadId(foundByThreadId);
        }

        if(this.foundByThreadId>foundByThreadId)
        {
            setFound(true);
            setIndexPosition(indexPosition);
            setFoundByThreadId(foundByThreadId);
        }
    }
}

//End Question 1 =======================================================

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