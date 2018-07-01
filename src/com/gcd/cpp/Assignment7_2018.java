/**
 * Assignment7_2018
 * @author: Aahuya Rakshaka Hari Lakshmi Narasimhan
 * @student number: 2955114
 */
import java.util.*;
import java.util.concurrent.*;
public class Assignment7_2018{


    public static void main(String[] args) {
        //Q1 =======================================
        int arraySize = 5;
        int numberOfThreads = 10;
        int[] inputArray = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            inputArray[i] = (int)(Math.random()*arraySize);
        }
        //Init Semaphore List
        ArrayList<Semaphore> semaphoreList = new ArrayList<>();
        for (int index = 0; index < numberOfThreads; index++) {
            semaphoreList.add(new Semaphore(0));
        }

        //Iterate through threads
        for (int index = 0; index < numberOfThreads; index++) {
            int random = (int) (Math.random()*3);
            //Randomly allocate threads
            //Next Semaphore should be cyclic.
            if(index!=numberOfThreads-1){
                switch (random){
                    case 0:
                        new Thread(new TA(inputArray,semaphoreList.get(index),semaphoreList.get(index+1))).start();
                        break;
                    case 1:
                        new Thread(new TB(inputArray,semaphoreList.get(index),semaphoreList.get(index+1))).start();
                        break;
                    default:
                        new Thread(new TC(inputArray,semaphoreList.get(index),semaphoreList.get(index+1))).start();
                        break;
                }
            }
            //Corner case., can be looped to start initial one as well..!! but that will be cyclic and goes on continously, as long as threads are available
            else
                new Thread(new TA(inputArray,semaphoreList.get(index),null)).start();
        }
        //Release first thread
        semaphoreList.get(0).release();

    }
}
//Threads for Question 1 here ===========================

class TA implements Runnable{

    private int[] array;
    private final int x = 10;
    private Semaphore currentSemaphore,nextSemaphore;

    public TA(int[] array, Semaphore currentSemaphore, Semaphore nextSemaphore) {
        this.array = array;
        this.currentSemaphore = currentSemaphore;
        this.nextSemaphore = nextSemaphore;
    }

    @Override
    public void run() {
        //Basic semaphore lock
        try { currentSemaphore.acquire(); } catch (InterruptedException ignored) { }
        for (int index = 0; index < array.length; index++) {
            array[index] = array[index]+x;
        }
        //Release next semaphore
        if(nextSemaphore!=null)
            nextSemaphore.release();
    }
}

class TB implements Runnable{

    private int[] array;
    private Semaphore currentSemaphore,nextSemaphore;

    public TB(int[] array, Semaphore currentSemaphore, Semaphore nextSemaphore) {
        this.array = array;
        this.currentSemaphore = currentSemaphore;
        this.nextSemaphore = nextSemaphore;
    }

    @Override
    public void run() {
        try {
            currentSemaphore.acquire();
        } catch (InterruptedException ignored) { }
        for (int index = 0; index < array.length; index++) {
            array[index] = array[index]--;
        }
        if(nextSemaphore!=null)
            nextSemaphore.release();
    }
}

class TC implements Runnable{

    private int[] array;
    private Semaphore currentSemaphore,nextSemaphore;

    public TC(int[] array, Semaphore currentSemaphore, Semaphore nextSemaphore) {
        this.array = array;
        this.currentSemaphore = currentSemaphore;
        this.nextSemaphore = nextSemaphore;
    }

    @Override
    public void run() {
        try {
            currentSemaphore.acquire();
        } catch (InterruptedException ignored) { }
        for (int index = 0; index < array.length; index++) {
            array[index] = array[index]*array[index];
        }
        if(nextSemaphore!=null)
            nextSemaphore.release();
    }
}

//=======================================================
//=======================================================
//class WaitEventBarrier{} here

class WaitEventBarrier{

    private Semaphore semaphore,localSemaphore;
    private int count;

    public WaitEventBarrier() {
        semaphore = new Semaphore(0);
        //Give atomic access to count
        localSemaphore = new Semaphore(1);
        count =0;
    }

    public void waitEvent(){
        //Acquire lock to update count
        try { localSemaphore.acquire(); }catch (InterruptedException ignored) { }
        count++;
        localSemaphore.release();
        try { semaphore.acquire(); }catch (InterruptedException ignored) { }
    }
    public void releaseAll(){
        //Aquire lock to block access to count till everything blocked is released.
        //This will ensure all threads locked are released,before new threads join.
        //This behaviour will lock new threads.
        try { localSemaphore.acquire(); }catch (InterruptedException ignored) { }
        for (int i = 0; i < count; i++) {
            semaphore.release();
        }
        count=0;
        localSemaphore.release();

    }
}
//=======================================================
