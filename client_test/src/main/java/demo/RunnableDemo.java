package demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunnableDemo implements Runnable {
    public static int id = 1;
    private String s = "C:\\Users\\lli214\\Documents\\BookManagementSystem\\client_test\\src\\main\\java\\mockUpDataForInterns.txt";
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(s)), "GBK"));//构造一个BufferedReader类来读取文件

    public RunnableDemo() throws IOException, NullPointerException {
    }

    @Override
    public void run() {
        try {
            String s = null;
            RestTemplate restTemplate = new RestTemplate();
            synchronized (br) {
                while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                    if (s.length() == 0|s.equals("\t\t\t\t")){
                        break;
                    }
                    String a[] = s.split("\t");
                    if (a[0].equals("bookName")) {
                        break;
                    }
                    for (int i = 0; i < a.length; i++) {
                        // a[i]= a[i].replaceAll("\\?", "").replaceAll("\"", "").trim();
                        String item = a[i].replaceAll("\\?", "");
                        item = item.replaceAll("\"", "");
                        item = item.trim();
                        a[i] = item;
                       // System.out.println(a[i]);
                    }
                    String isbn = (a[2].length() == 14) ? (a[2].replace("-", "")) : (a[2].length() == 0 ? (978 + a[3]) : a[2]);

                    String url = "http://localhost:8080/add";
                    MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
                    data.add("id", String.valueOf(id++));
                    data.add("bookname", a[0]);
                    data.add("press", a[1]);
                    data.add("isbn", isbn);
                    data.add("category", a[4]);
                    ResponseEntity<String> response = restTemplate.postForEntity(url, data, String.class);
                    System.out.println(response.toString());
                    System.out.println(response.getStatusCode().equals(HttpStatus.OK));
                    // w = (a[2].length() == 14) ? (a[2].replace("-", "")) : (a[2].length() == 0 ? (978 + a[3]) : a[2]);
                    //String url = "http://localhost:8080/2/add?bookname=" + a[0] + "&press=" + a[1] + "&isbn=" + w + "&category=" + a[4];
                    // ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                    //System.out.println(Thread.currentThread()+": "+ Arrays.toString(a));
                    br.wait(100);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
