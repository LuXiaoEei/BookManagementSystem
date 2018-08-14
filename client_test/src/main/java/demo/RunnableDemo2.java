package demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class RunnableDemo2 implements Runnable {
    private String s = Paths.get("","src","main","java","mockUpDataForInterns.txt").toString();
//   private String s = "\\src\\main\\java\\mockUpDataForInterns.txt";
    private BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(s)), "GBK"));//构造一个BufferedReader类来读取文件

    public RunnableDemo2() throws IOException, NullPointerException {
    }

    @Override
    public void run() {
        try {
            String s = null;
            RestTemplate restTemplate = new RestTemplate();
            while ((s = br.readLine()) != null) {
                //BufferedReader的内部方式不会脏读
                if (s.length() == 0|s.equals("\t\t\t\t")) {
                    break;
                }
                String a[] = s.split("\t");
                if (a[0].equals("bookName")) {
                    break;
                }
                //处理特殊字符
                for (int i = 0; i < a.length; i++) {
                    String item = a[i].replaceAll("\\?", "").replaceAll("\\-","");
                    item = item.replaceAll("\"", "");
                    item = item.trim();
                    a[i] = item;
                }

                String isbn = (a[2].length() == 14) ? (a[2].replace("-", "")) : (a[2].length() == 0 ? (978 + a[3]) : a[2]);
                //传url
                String url = "http://localhost:8080/add";
                MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
                data.add("id", String.valueOf(UUID.randomUUID()));
                data.add("bookname", a[0]);
                data.add("press", a[1]);
                data.add("isbn", isbn);
                data.add("category", a[4]);
                ResponseEntity<String> response = restTemplate.postForEntity(url, data, String.class);
                System.out.println(response.toString());
                System.out.println(response.getStatusCode().equals(HttpStatus.OK));
                System.out.println(Thread.currentThread()+" :"+" bookname: "+a[0]+" press: "+ a[1]+" isbn: "+isbn+" category: "+ a[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
