package demo;

public class Threadtest {
    public static void main(String []args) throws Exception {

        /*RunnableDemo r1=new RunnableDemo();
        Thread t1=new Thread(r1) ;
        t1.start();
        RunnableDemo r2=new RunnableDemo();
        Thread t2=new Thread(r1) ;
        t2.start();
        RunnableDemo r3=new RunnableDemo();
        Thread t3=new Thread(r1) ;
        t3.start();*/
        RunnableDemo2 r=new RunnableDemo2();
        Thread t1=new Thread(r) ;
        t1.start();
        // RunnableDemo2 r2=new RunnableDemo2();
        Thread t2=new Thread(r) ;
        t2.start();
        //RunnableDemo2 r3=new RunnableDemo2();
        Thread t3=new Thread(r) ;
        t3.start();
    }
}
