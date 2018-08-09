package demo;

public class Threadtest {
    public static void main(String []args) throws Exception {

        RunnableDemo r1=new RunnableDemo();
        Thread t1=new Thread(r1) ;
        t1.start();
        RunnableDemo r2=new RunnableDemo();
        Thread t2=new Thread(r1) ;
        t2.start();
        RunnableDemo r3=new RunnableDemo();
        Thread t3=new Thread(r1) ;
        t3.start();
    }
}
