
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Assignment8_2018
 *
 * Student Name: Aahuya Rakshaka Hari Lakshmi Narasimhan
 * Student Number: 2955114
 */
//Code for main server1 here=======================================================
class Server1 {
    final static int listenFromClient = 1245; // any number > 1024
    final static int listenFromAnotherServer = 2000;
    static ArrayBlockingQueue<Ticket> buffer = new ArrayBlockingQueue<>(50);
    public static void main(String[] args){
        //Creating thread which listens to client
        Thread clientSocketThread = new Thread(new ClientSocket(listenFromClient, new Data(), new Buffer(50)));
        clientSocketThread.start();
        //Create a thread which listens to port 2000
        Thread servSocketThread = new Thread(new ServSocket(listenFromAnotherServer, new Data()));
        servSocketThread.start();
        //Reads from buffer and writes to 2001, in which server 2 is listening to ..
        Thread bufferForTransfer = new Thread(new BufferForTransfer(buffer, 2001));
        bufferForTransfer.start();
    }
}


//Thread which keeps on listening to port and write to data and buffer as well.
class ClientSocket implements Runnable{

    int port;
    Data data;
    Buffer buffer;

    public ClientSocket(int port, Data data, Buffer buffer) {
        this.port = port;
        this.data = data;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                Ticket ticket = new Ticket();
                ticket.readInputStream(in);
                data.add(ticket);
                buffer.put(ticket);
            }
        }
        catch (Exception ignored){}
    }
}

//Thread which keeps on listening to port and write to data only.
class ServSocket implements Runnable{

    int port;
    Data data;


    public ServSocket(int port, Data data) {
        this.port = port;
        this.data = data;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                Ticket ticket = new Ticket();
                ticket.readInputStream(in);
                data.add(ticket);
            }
        }
        catch (Exception ignored){}
    }
}


//Thread which keeps on listening to buffer and write to another port.
class BufferForTransfer implements Runnable{
    private ArrayBlockingQueue<Ticket> buffer;
    int port;

    public BufferForTransfer(ArrayBlockingQueue<Ticket> buffer, int port) {
        this.buffer = buffer;
        this.port = port;
    }

    @Override
    public void run() {
        try{
            Socket socket = new Socket(InetAddress.getLocalHost(),port);
            DataOutputStream out =
                    new DataOutputStream(socket.getOutputStream());
            while(true){
                Ticket ticket = buffer.take();
                ticket.writeOutputStream(out);
            }
        }
        catch (IOException ignored){ }
        catch (InterruptedException ignored){ }
    }
}

//Class ticket
class Ticket {
    private String sellerCode;
    private int[] randomNumbers;
    public Ticket(String sellerCode, int[] randomNumbers){
        this.sellerCode = sellerCode; this.randomNumbers = randomNumbers;
    }
    public Ticket(String sellerCode){
        this.sellerCode = sellerCode;
        for (int i = 0; i < 6; i++) {
            randomNumbers[i] = (int) (Math.random()*40);
        }
    }
    public Ticket(){
        sellerCode = null;
        for (int i = 0; i < 6; i++) { randomNumbers[i] = (int) (Math.random()*40); }
    }
    public String sellerCode(){return sellerCode;}
    public int[] randdomNumber(){return randomNumbers;}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Ticket)) return false;
        Ticket ticket = (Ticket)obj;
        if(!ticket.sellerCode.equals(this.sellerCode)) return false;
        for (int i = 0; i < 6; i++) {
            if(ticket.randomNumbers[i]!=this.randomNumbers[i]) return false;
        }
        return true;
    }

    //To print ticket info.
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(sellerCode +" ,");
        for (int eachNumber: randomNumbers
                ) {
            stringBuilder.append(String.valueOf(eachNumber));
            stringBuilder.append(",");
        }
        //To remove Final comma.
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    //======================================================
    //Methods used to read and write to streams over sockets
    public void writeOutputStream(DataOutputStream out){
        try{
            out.writeUTF(sellerCode);
            for (int eachNumber: randomNumbers) out.writeInt(eachNumber);
        }catch(IOException e){e.printStackTrace();}
    }

    public void readInputStream(DataInputStream in){
        try{
            sellerCode = in.readUTF();
            for (int i = 0; i < 6; i++) { randomNumbers[i] = in.readInt(); }
        }
        catch(IOException e){e.printStackTrace();}
    }
}

//Data class which holds ticket information
class Data{
    private ArrayList<Ticket> data = new ArrayList<>();
    private Lock lock = new ReentrantLock();
    void add(Ticket ticket){
        lock.lock();
        try{
            data.add(ticket);
        }finally{lock.unlock();}
    }
    boolean search(Ticket p){
        lock.lock();
        try{
            return data.contains(p);
        }finally{lock.unlock();}
    }
    ArrayList<Ticket> retrieve(String sellerCode, int[] randomNumbers){
        lock.lock();
        try{
            ArrayList<Ticket> dt = new ArrayList<>();
            Ticket p = new Ticket(sellerCode,randomNumbers); //use for search
            for(int j = 0; j < data.size();j++){
                Ticket ticket1 = data.get(j);
                if(ticket1.equals(p)) dt.add(ticket1);
            }
            return dt;
        }finally{lock.unlock();}
    }
}



//End===============================================================================

//Code for main server2 here=======================================================
class Server2 {
    final static int listenFromClient = 1246; // any number > 1024
    final static int listenFromAnotherServer = 2001;
    static ArrayBlockingQueue<Ticket> buffer = new ArrayBlockingQueue<>(50);
    public static void main(String[] args){
        //Creating thread which listens to client.
        Thread clientSocketThread = new Thread(new ClientSocket(listenFromClient, new Data(), new Buffer(50)));
        clientSocketThread.start();
        //Creating thread which listens to another server.
        Thread servSocketThread = new Thread(new ServSocket(listenFromAnotherServer, new Data()));
        servSocketThread.start();
        //Reads from buffer and writes to 2000, in which server 1 is listening to ..
        Thread bufferForTransfer = new Thread(new BufferForTransfer(buffer, 2000));
        bufferForTransfer.start();
    }
}


//End===============================================================================

//===================================================================================
//Code for Ticket generator=======================================================

class Generator {
    final static int server1port = 1245; // any number > 1024
    final static int server2port = 1246;

    public static void main(String[] args) {
        //Create 50 tickets
        for (int i = 0; i < 50; i++) {
            int[] randomNumbers = new int[6];
            for (int j = 0; j < 6; j++) randomNumbers[j] = (int) (Math.random()*40);
            String sellerCode = "Random";
            if(i%2==0)
                sellerCode = "Random1";
            Ticket ticket = new Ticket(sellerCode,randomNumbers);
            //Send to server1
            if(i%2 ==0 ){
                try{
                    Socket socket = new Socket(InetAddress.getLocalHost(),server1port);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    ticket.writeOutputStream(out);
                }
                catch (IOException ignored){ }
            }
            //Send to server2
            else {
                try{
                    Socket socket = new Socket(InetAddress.getLocalHost(),server2port);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    ticket.writeOutputStream(out);
                }
                catch (IOException ignored){ }
            }
        }
    }

}


//End===============================================================================

