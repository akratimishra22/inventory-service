package com.gharelu.inventory_service.controller;

import com.gharelu.inventory_service.model.Inventory;
import com.gharelu.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;
    @Autowired
    ApplicationContext ctx;

    private String isTokenValid(String authHeader) {
        WebClient webClient = ctx.getBean("authServiceWebClientEurekaDiscovered", WebClient.class);
        String authResponse = webClient.get()
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return authResponse;
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                     @RequestBody Inventory inventory) {
        String authResponse = isTokenValid(authHeader);
        if(authResponse.equals("Valid"))
            return ResponseEntity.ok(service.createInventory(inventory));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventories(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String authResponse = isTokenValid(authHeader);
        if(authResponse.equals("Valid"))
            return ResponseEntity.ok(service.getAllInventories());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                  @PathVariable Long id) {
        String authResponse = isTokenValid(authHeader);
        if(authResponse.equals("Valid"))
            return service.getInventoryById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/increment/{productId}")
    public ResponseEntity<Inventory> incrementInventory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                        @PathVariable Long productId,
                                                        @RequestParam Integer quantity) {
        String authResponse = isTokenValid(authHeader);
        if(authResponse.equals("Valid"))
            return ResponseEntity.ok(service.incrementQuantity(productId, quantity));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/decrement/{productId}")
    public ResponseEntity<Inventory> decrementInventory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                        @PathVariable Long productId,
                                                        @RequestParam Integer quantity) {
        String authResponse = isTokenValid(authHeader);
        if(authResponse.equals("Valid"))
            return ResponseEntity.ok(service.decrementQuantity(productId, quantity));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                @PathVariable Long id) {
        String authResponse = isTokenValid(authHeader);
        if(authResponse.equals("Valid")){
            service.deleteInventory(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
