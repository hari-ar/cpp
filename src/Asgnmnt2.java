
public class Asgnmnt2 {
    static final int N= 10000000;
    public static void main(String[] args) {
        int data[]=  new int[N];
        counter th[] = new counter[4];
        int core = 10000000/4;
        for(int j=0;j< 4; j++){
            th[j] = new counter(data,j*core,(j+1)*core);
            th[j].start();
        }
        try {
            for(int j=0;j< 4; j++)
                th[j].join();
        }
        catch(InterruptedException e){}
        int result = -1;
        for(int j=0;j< 4; j++){
            if(result == -1 && result < th[j].result){
                result = th[j].result;
                break;
            }
        }

        if(result!=-1)
            System.out.println("Found at "+result);
        else
            System.out.println("Not found");

        int index = 0;
        for(counter t:th) index += t.result();
        System.out.println(index);
    }
}
class counter extends Thread{
    int data[];
    int lb, ub;
    int result;

    counter(int data[], int l, int u){
        this.data = data; lb = l; ub = u;
    }
    public void run(){
        result = -1;
        for(int j = lb; j < ub; j++)
            if(data[j] == 0)
            {
                result = j;
                break;
            }

            else
                System.out.println("no zero");
    }
    public int result(){return result;}
}
