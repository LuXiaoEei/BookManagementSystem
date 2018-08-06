package com.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class test_clinet {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/person/mysql/add?name=lxl&age=11&email=1233jcfxz4";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.toString());
        System.out.println(response.getStatusCode().equals(HttpStatus.OK));
    }

}
