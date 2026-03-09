package com.autowhouse.itemservice.controller;

import com.autowhouse.itemservice.config.CustomUser;
import com.autowhouse.itemservice.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/item")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(@Qualifier("itemServiceImpl") ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/save")
    public ResponseEntity<String> saveItem(){
        System.out.println("The endpoint has been triggered");
        return ResponseEntity.ok(itemService.test()+" Innit");
    }

    @GetMapping("/create")
    public ResponseEntity<String> checkUser(@AuthenticationPrincipal CustomUser user){
        System.out.println(user.getUsername());
        return ResponseEntity.ok(user.getUsername());
    }
}
