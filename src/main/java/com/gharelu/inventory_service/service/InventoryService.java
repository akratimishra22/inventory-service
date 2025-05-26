package com.gharelu.inventory_service.service;

import com.gharelu.inventory_service.model.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    Inventory createInventory(Inventory product);
    List<Inventory> getAllInventories();
    Optional<Inventory> getInventoryById(Long id);
    void deleteInventory(Long id);
    Inventory incrementQuantity(Long productId, Integer quantity);
    Inventory decrementQuantity(Long productId, Integer quantity);
}

