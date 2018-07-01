package com.gcd.cpp;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Assignment4_2018{
    public static void main(String args[]){
        //No test code required for this assignment
    }
}
//Question 1 class =======================================
final class Point{
    private final int x, y;
    public Point(int x0, int y0){x = x0; y = y0;}
    public int x(){return x;}
    public int y(){return y;}
    public String String(){return "("+x+","+y+")";}


    public boolean equals(Object ob){
        if(!(ob instanceof Point)) return false;
        Point p = (Point)ob;
        return p.x == x && p.y == y;
    }
    public int hashCode(){return 31*x*y;}
}

class SetPoint{
    private Set<Point> set = new HashSet<>();

    synchronized void add(Point point){
        //Since point is final creating new point wouldn't be needed
        set.add(point);
    }

    synchronized List<Point> getAllX(int x){
        return set.stream().filter(point -> point.x()==x).map(mappedPoint ->
                //Mapping to new point, to copy point info instead of getting reference of that point.
                new Point(mappedPoint.x(),mappedPoint.y())).collect(Collectors.toList());
    }

    synchronized boolean search(Point point){
        for(Point point1:set){
        if (point1==point){
            return true;}
        }
            return set.stream().anyMatch((Point point1) -> point1.equals(point));
    }

     synchronized public String toString(){
             StringBuilder stringBuilder = new StringBuilder();
             set.forEach(eachPoint-> stringBuilder.append(eachPoint.String()+"\n"));
             return stringBuilder.toString();
    }

}

//Question 2 ==============================================
//Implementation of class CircularQueue<E>

class CircularQueue<T> implements Iterable<T>{
    private  T queue[];
    private int head, tail, size;
    public CircularQueue(){
        queue = (T[])new Object[20];
        head = 0; tail = 0; size = 0;
    }
    public CircularQueue(int n){ //assume n >=0
        queue = (T[])new Object[n];
        size = 0; head = 0; tail = 0;
    }
    public synchronized boolean join(T x){
        if(size < queue.length){
            queue[tail] = x;
            tail = (tail+1)%queue.length;
            size++;
            return true;
        }
        else return false;
    }
    public synchronized T top(){
        if(size > 0)
            return queue[head];
        else
            return null;
    }
    public synchronized boolean leave(){
        if(size == 0) return false;
        else{
            head = (head+1)%queue.length;
            size--;
            return true;
        }
    }
    public synchronized boolean full(){
        return (size == queue.length);
    }

    public synchronized boolean empty(){
        return (size == 0);}

    public Iterator<T> iterator(){
        return new QIterator<T>(queue, head, size);
    }

    private static class QIterator<T> implements Iterator<T>{
        private T[] d; private int index;
        private int size; private int returned = 0;
        QIterator( T[] dd, int head, int s){
                d = dd.clone(); index = head; size = s;
        }
        public boolean hasNext(){
            synchronized (this)
            {
                return returned < size;
            }
        }
        public T next(){
            synchronized (this){
                if(returned == size) throw new NoSuchElementException();
                T item = (T)d[index];
                index = (index+1) % d.length;
                returned++;
                return item;
            }
        }
        public void remove(){}
    }
}
