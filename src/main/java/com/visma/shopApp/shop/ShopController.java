package com.visma.shopApp.shop;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("shop")
public class ShopController {

    RestTemplate restTemplate = new RestTemplate();

    public ShopController() {
        restTemplate = new RestTemplate();
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello() {

        String url = "http://127.0.0.1:8080/warehouseApp-0.0.1-SNAPSHOT/warehouse/hello";
        //String result = restTemplate.getForObject(uri, String.class);

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, getAuthentication(), String.class);
        return result;
    }

    @GetMapping("/items")
    public ResponseEntity<String> getItems(){

        String url = "http://127.0.0.1:8080/warehouseApp-0.0.1-SNAPSHOT/warehouse/items";

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, getAuthentication(), String.class);

        return result;
    }

    @GetMapping("/items/{id}/sell")
    public ResponseEntity<String> sellItem(@PathVariable int id){
        String url = "http://127.0.0.1:8080/warehouseApp-0.0.1-SNAPSHOT/warehouse/Items/"+ id + "/sell";

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, getAuthentication(), String.class);

        return result;
    }

    public HttpEntity<String> getAuthentication(){
        String auth = "user:password";
        byte[] authToBytes = auth.getBytes();
        byte[] base64Encode = Base64.encodeBase64(authToBytes);
        String authValue = new String(base64Encode);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + authValue);
        HttpEntity<String> requestHeaders = new HttpEntity<String>(headers);

        return requestHeaders;
    }
}
