package demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.lang.*;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunnableDemo implements Runnable {
    private String s = "C:\\Users\\lli214\\Desktop\\mockUpDataForInterns.txt";
    //InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(s)), "GBK");
    //StringBuffer sb = new StringBuffer();
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(s)), "GBK"));//构造一个BufferedReader类来读取文件

    public RunnableDemo() throws IOException,NullPointerException {
    }
    @Override
    public void run() {
       /* while (reader.ready()) {
            try {
                sb.append((char) reader.read());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 转成char加到StringBuffer对象中
        }
        System.out.println(sb.toString());*/
        try {
            String s = null;
            RestTemplate restTemplate = new RestTemplate();
          /*synchronized (br){
               while ((s = br.readLine())!= null) {//使用readLine方法，一次读一行
                   System.out.println(Thread.currentThread()+"  "+s);
                   br.wait(100);
               }}*/
            while ((s = br.readLine())!= null) {//使用readLine方法，一次读一行
                if(s.length()==0)  {break;}
                String str=null;
                String w=null;
                // String regEx="[`~@#$%^*()=|{}';'\\[\\]</?~@#￥%……*（）——|{}【】\"*‘；”“’    。、？]";
               /*Pattern p = Pattern.compile(regEx);
               Matcher m = p.matcher(s);
                str= m.replaceAll("").trim();*/
                //System.out.println(Thread.currentThread()+": "+str);
                String a[] = s.split("\t");
                if (a[0].equals("bookName")) {break;}
                for(int i=0;i<a.length;i++){
                    String item = a[i].replaceAll("\\?","");
                    item = item.replaceAll("\"","");
                    item =item.trim();
                    a[i]=item;
                 /* String regEx= "^(\\?\\s{3}?)|(\")|(\\?)";
                       String regEx= "^[^\\w]+|[^[\\w\\+\\!]+]$";
                   Pattern p = Pattern.compile(regEx);
                   Matcher m = p.matcher(a[i]);
                   a[i]= m.replaceAll("").trim();*/
                    System.out.println(a[i]);
                      /* String regE= "\\?$";
                       Pattern q = Pattern.compile(regE);
                       Matcher n = q.matcher(e);
                       a[i]= n.replaceAll("").trim();*/
                }

                 /* w=(a[2].length()==14)?(a[2].replace("-","")):(a[2].length()==0?(978+a[3]):a[2]);
                  String url = "http://localhost:8080/2/add?bookname="+a[0]+"&press="+a[1]+"&isbn="+w+"&category="+a[4];
                   ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);*/
                //System.out.println(Thread.currentThread()+": "+ Arrays.toString(a));
                //br.wait(100);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        } /*catch (InterruptedException e) {
           e.printStackTrace();
       }*/
    }
}
