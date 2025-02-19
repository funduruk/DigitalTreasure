package com.DigitalShop.DigitalTreasure.Controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class HomeController {
    @GetMapping("Store/")
    public ResponseEntity<?> homePage() {
        return ResponseEntity.ok("homePage");
    }
}
