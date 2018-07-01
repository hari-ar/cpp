package com.gcd.cpp;

/*
 *  M.Sc. Assignment 6_2018
 *
 * Student Name: Aahuya Rakshaka Hari Lakshmi Narasimhan
 * Student Number: 2955114
*/
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;
public class Assignment6_2018 {
    public static void main(String[] args) {


        //Test Q1 ==================================================
        int bufferSize = 10;
        Buffer<Integer> producerFilterBuffer = new Buffer<>(bufferSize);
        Buffer<Integer> filterConsumerBuffer = new Buffer<>(bufferSize);
        //Start Producer
        int dataSize = 20;
        new Thread(new Producer(producerFilterBuffer,dataSize)).start();
        new Thread(new Filter(producerFilterBuffer,filterConsumerBuffer)).start();
        Consumer consumer = new Consumer(filterConsumerBuffer);
        Thread t = new Thread(consumer);
        t.start();
        System.out.println("Waiting to join");
        try {
            t.join(); //Needed to wait till Sorter/ Consumer joins. To print sorted values.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread Joined");
        consumer.getSortedList().forEach(eachValue -> System.out.print(eachValue+" "));
        //End Q1 ==================================================






        //Test Q2 =================================================
        int maxThreads = 10;
        LatchBarrier latchBarrier = new LatchBarrier(maxThreads);
        for(int i=0 ; i<maxThreads-1 ; i++ ){
            new Thread(new TestBarrier(latchBarrier)).start();
        }
        System.out.println("\nAdded maxThreads -1 to barrier, barrier is not broken..!! ");
        //Used random number.. need not be maxThreads
        for(int i=0;i<maxThreads;i++){
            //Wait for 10 seconds to see if thread comes alive.
            int balance = maxThreads-i;
            try {
                System.out.println("Sleeping for "+balance+" seconds");
                Thread.sleep(1000);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Breaking condition in barrier");
        new Thread(new TestBarrier(latchBarrier)).start();

        //Sleeping for 3 seconds before adding second set
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Testing Barrier Again. No threads to be stuck");
        for(int i=0 ; i<maxThreads ; i++ ){
            new Thread(new TestBarrier(latchBarrier)).start();
        }
        //End Q2 ==================================================
    }
}
//========================================================
// Code for Buffer class
// Do not edit this class.

class Buffer<E>{
    private int max;
    private int size = 0;
    private ArrayList<E> buffer;
    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    public Buffer(int s){
        buffer = new ArrayList<E>();
        max = s;
    }
    public void put(E x){
        lock.lock();
        try{
            while(size == max){
                try{
                    notFull.await();
                }catch(InterruptedException e){}
            }
            buffer.add(x);
            size++;
            if(size - 1 == 0) notEmpty.signal();
        }finally{lock.unlock();}
    }
    public E get(){
        lock.lock();
        try{
            while(size == 0){
                try{
                    notEmpty.await();
                }catch(InterruptedException e){}
            }
            E temp = buffer.get(0);
            buffer.remove(0);
            size--;
            if(size + 1 == max) notFull.signal();
            return temp;
        }finally{lock.unlock();}
    }
}
//============================================================
// put thread code for Question 1 here
    class Producer implements Runnable{

    Buffer<Integer> buffer;
    int size;

    public Producer(Buffer<Integer> buffer, int size) {
        this.buffer = buffer;
        this.size = size;
    }


    @Override
    public void run() {
        for(int i=0;i<size;i++){
            int randomNumber = (int) (Math.random()*1000);
            //System.out.println("Random number = "+randomNumber);
            buffer.put(randomNumber);
            try {
                //System.out.println("Producer Sleeps");
                Thread.sleep(100);
                //System.out.println("Producer Awakes");
            } catch (InterruptedException e) { e.printStackTrace();}
        }
        //Add null in the end to signal, end of data..
        buffer.put(null);
    }
}



    class Consumer implements Runnable{
        Buffer<Integer> buffer;
    private List<Integer> list;


    public Consumer(Buffer<Integer> buffer) {
        this.buffer = buffer;
        list = new ArrayList<>();
    }

    @Override
    public void run() {
        Integer currentValue = buffer.get();
        //Checking if null is received. Which is our breaking condition
        while(currentValue!=null){
            //System.out.println("Current Value in Consumer "+currentValue);
            list.add(currentValue);
            try {
              //  System.out.println("Consumer Sleeps");
                Thread.sleep(200);
              //  System.out.println("Consumer Awakes");
            } catch (InterruptedException e) { e.printStackTrace();}
            currentValue = buffer.get();
        }

        //List sort with lambda comparator..!!
        //Since we used Integer..
        list.sort((item1, item2) -> {
            if(item1>item2)
                return +1;
            else if (item1<item2)
                return -1;
            return 0;
        });
    }

        public List<Integer> getSortedList() {
            return list;
        }
    }


    class Filter implements Runnable{
    Buffer<Integer> inputBuffer,outputBuffer;



    public Filter(Buffer<Integer> inputBuffer, Buffer<Integer> outputBuffer) {
        this.inputBuffer = inputBuffer;
        this.outputBuffer = outputBuffer;
    }

    @Override
    public void run() {
        Integer currentValue = inputBuffer.get();
        //Checking if null is received. Which is our breaking condition
        while(currentValue!=null) {
            //If they are not multiples of 5 or 7 or 11, it will be added to the output buffer
            if (currentValue%5 != 0 && currentValue%7 != 0 && currentValue%11 != 0) {
                outputBuffer.put(currentValue);
            }
            currentValue = inputBuffer.get();
            try {
                //System.out.println("Filter sleeping");
                Thread.sleep(500);
                //System.out.println("Filter Awake");
            }
            catch (InterruptedException e){ e.printStackTrace(); }
        }
        outputBuffer.put(currentValue);

    }
}


//End Question 1 =============================================
//============================================================
//Question 2 =================================================
// put code for LatchBarrier class here


class LatchBarrier {

    private int numberOfThreadsAtRendezvousPoint;
    private int maxThreads;
    private Lock lock = new ReentrantLock();
    private Condition numberOfThreadsCondition = lock.newCondition();

    public LatchBarrier(int maxThreads) {
        this.maxThreads = maxThreads;
        numberOfThreadsAtRendezvousPoint = 0;

    }

    void waitBarrier(){
        lock.lock();
        try {
            numberOfThreadsAtRendezvousPoint++;
            //Waiting on to see if minimum condition is reached.!!
            while(numberOfThreadsAtRendezvousPoint<maxThreads){
                try {
                    numberOfThreadsCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Required to signal everyone to awake..!!
            numberOfThreadsCondition.signalAll();
            System.out.println("Barrier Broken..!! Count = "+numberOfThreadsAtRendezvousPoint);
        }
        finally{lock.unlock();}
    }
}


//End LatchBarrier ==========================================
// put code for threads testing LatchBarrier here


class TestBarrier implements Runnable{
    LatchBarrier latchBarrier;

    public TestBarrier(LatchBarrier latchBarrier) {
        this.latchBarrier = latchBarrier;
    }

    @Override
    public void run() {
        latchBarrier.waitBarrier();
    }
}
//End Question 2  ===========================================