package example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class test_clinet {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/2/add?isbn=1234567890123&bookname=aaa&press=aaa&category=aaa";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.toString());
        System.out.println(response.getStatusCode().equals(HttpStatus.OK));
    }

}
