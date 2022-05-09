package com.visma.shopApp.shop;

import com.item.ItemDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("shop")
public class ShopController {

    private RestTemplate restTemplate;
    private final String warehouseUrl;
    private final String authentication;

    public ShopController(@Value("${warehouse.url}") String warehouseUrl,
                          @Value("${authentication}") String authentication) {
        this.restTemplate = new RestTemplate();
        this.warehouseUrl = warehouseUrl;
        this.authentication = authentication;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showItems(Model model) {
        model.addAttribute("greeting", getItems());
        return "home";
    }

    @GetMapping("/items")
    public List<ItemDTO> getItems(){

        String url = warehouseUrl + "warehouseApp-0.0.1-SNAPSHOT/warehouse/items";
        System.out.println(url);
        ResponseEntity<List<ItemDTO>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getAuthentication(),
                new ParameterizedTypeReference<List<ItemDTO>>() {} );

        List<ItemDTO> items = result.getBody();
        return items;
    }

    @GetMapping("/items/{id}/sell/{amount}")
    public ResponseEntity<String> sellItem(@PathVariable int id, @PathVariable int amount){

        String url = warehouseUrl + "warehouseApp-0.0.1-SNAPSHOT/warehouse/items/"+ id + "/sell/" + amount;
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, getAuthentication(), String.class);

        return result;
    }

    public HttpEntity<String> getAuthentication(){

        byte[] authToBytes = authentication.getBytes();
        byte[] base64Encode = Base64.encodeBase64(authToBytes);
        String authValue = new String(base64Encode);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + authValue);
        HttpEntity<String> requestHeaders = new HttpEntity<String>(headers);

        return requestHeaders;
    }
}
