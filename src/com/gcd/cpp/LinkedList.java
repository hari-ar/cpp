package com.gcd.cpp;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;


class Node<E>{
    volatile Node<E> next;
    volatile E item;
}

public class LinkedList<E> {
    private AtomicReference<Node<E>> head = new AtomicReference<Node<E>>();

    public void put(E item){
        Node<E> newHead = new Node<E>();
        newHead.item = item;
        Node<E> oldHead = head.get();
        newHead.next = oldHead;
        while (!head.compareAndSet(oldHead,newHead)){
            oldHead = head.get();
            newHead.next = oldHead;
        }
         int sum = 0;
        ArrayList<Future<Integer>> l=new ArrayList();
        l.stream().filter(eachItemInList -> System.out.println(eachItemInList))

        for(Future<Integer> eachFuture : l){
            try{
                sum+= eachFuture.get();
            }
            catch (Exception e){}
        }
    }

}
