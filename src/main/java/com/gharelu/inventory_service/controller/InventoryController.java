package com.gharelu.inventory_service.controller;

import com.gharelu.inventory_service.model.Inventory;
import com.gharelu.inventory_service.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        return ResponseEntity.ok(service.createInventory(inventory));
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventories() {
        return ResponseEntity.ok(service.getAllInventories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventory(@PathVariable Long id) {
        return service.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/increment/{productId}")
    public ResponseEntity<Inventory> incrementInventory(@PathVariable Long productId,
                                                        @RequestParam Integer quantity) {
        return ResponseEntity.ok(service.incrementQuantity(productId, quantity));
    }

    @PutMapping("/decrement/{productId}")
    public ResponseEntity<Inventory> decrementInventory(@PathVariable Long productId,
                                                        @RequestParam Integer quantity) {
        return ResponseEntity.ok(service.decrementQuantity(productId, quantity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        service.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}
