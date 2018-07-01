package com.gcd.cpp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MSc Assignment5_2018
 *
 * Student Name: Aahuya Rakshaka Hari Lakshmi Narasimhan
 * Student Number: 2955114
 *
 */





public class Assignment5_2018{
    public static void main(String[] args) {
        //Question 1
        //===============================================
        Thread[] threadArray = new TempClass[20];

        ViewingStand pradoMuseum = new ViewingStand(5);

        //Creating 20 viewers fighting for 5 seats..!!
        for (int i = 0; i < threadArray.length; i++) {
            threadArray[i] = new TempClass(pradoMuseum);
            threadArray[i].start();
            //Question 2
            //==============================================

            //===============================================
        }
    }

}

//A Thread class to simulate Movie
class TempClass extends Thread{

    ViewingStand pradoMuseum;

    public TempClass(ViewingStand pradoMuseum) {
        this.pradoMuseum = pradoMuseum;
    }


    @Override
    public void run() {
        int seat = pradoMuseum.findSeat();
        int sleepTime = (int) (Math.random()*5000);
        System.out.println("Got Seat "+seat+" for "+sleepTime+" ms");
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) { }

        pradoMuseum.leaveSeat(seat);
        System.out.println("Left From "+seat);
    }
}

//Question 1 ===================================================
class ViewingStand{
    // data structure here
    private int numberOfSeats;
    private boolean[] seats;
    private Lock[] lock = null;

    public ViewingStand(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
        lock = new ReentrantLock[numberOfSeats];
        seats = new boolean[numberOfSeats];

        for(int i=0;i<numberOfSeats;i++){
            seats[i] = false;
            lock[i] = new ReentrantLock();
        }
    }


    /*

    Alternate Approach to findSeat, not sure which one works better..!!

    public int findSeat(){
        int index = 0;
        boolean seatsFound = false;
        while(!seatsFound){
            index = (int) (Math.random()*numberOfSeats);
            try{
                if(lock[index].tryLock() ){
                    if(!seats[index])
                    {
                        seats[index] = true;
                        seatsFound = true;
                    }
                }
                else{
                    index = (int) (Math.random()*numberOfSeats);
                }
            }
            finally {
                lock[index].unlock();

            }
        }
        return index;
    }*/



    public int findSeat(){
        int index = 0;

        //Fine Grain Approach, which searches for next available seat if current seat is not available.
        while(true){
            for(index=0;index<seats.length;index++){
                if(lock[index].tryLock())
                {
                    try{
                        if(!seats[index])
                        {
                            seats[index] = true;
                            return index;
                        }
                    }
                    finally {
                        lock[index].unlock();
                    }
                }
            }
        }
    }

    //Leaving seat, needs to get the lock on..!! So using lock.lock(), instead of try lock.
    public void leaveSeat(int seat){
        lock[seat].lock();
        try{
            seats[seat] = false;
        }
        finally{
            lock[seat].unlock();
        }

    }
}

//Question 2 ======================================================
class SharedArray{
    private int data[];
    private Lock locks[];
    public SharedArray(int f[]){
        data = new int[f.length];
        locks = new ReentrantLock[f.length];
        for(int j = 0; j < f.length;j++){
            data[j] = f[j];
            locks[j] = new ReentrantLock();
        }
    }
    public void assign(int j, int x){
        //assume 0 <= j < data.length
        locks[j].lock();
        try{
            data[j] = x;
        }finally{locks[j].unlock();}
    }

    public int sum(){
        int sum =0;
        //Creating list of index, to maintain track of indices for which lock was not obtained first time, so as to retry.
        List<Integer> retryList = new ArrayList<>();
        for(int i=0;i<data.length;i++){
            retryList.add(i);
        }
        //Retry till, list of retry is zero.
        while(retryList.size()>0){
            //Iterate through list
            for(int i=0;i<retryList.size();i++){
                //Try to get lock
                if(locks[i].tryLock()){
                    try {
                        //Add sum to sum
                        sum+=data[retryList.get(i)];
                    }
                    finally {
                        //Unlock and remove from retry list.
                        locks[i].unlock();
                        retryList.remove(i);
                        break; //Added to offset list shift left
                    }
                }
            }
        }

        return sum;
    }
}






















//===================================================================