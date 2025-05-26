package com.gharelu.inventory_service.service;

import com.gharelu.inventory_service.model.Inventory;
import com.gharelu.inventory_service.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;

    public InventoryServiceImpl(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        return repository.save(inventory);
    }

    @Override
    public List<Inventory> getAllInventories() {
        return repository.findAll();
    }

    @Override
    public Optional<Inventory> getInventoryById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteInventory(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Inventory incrementQuantity(Long productId, Integer quantity) {
        return repository.findById(productId)
                .map(existingInventory -> {
                    existingInventory.setAvailableQuantity(existingInventory.getAvailableQuantity() + quantity);
                    return repository.save(existingInventory);
                })
                .orElseThrow(() -> new RuntimeException("Inventory not found for productId " + productId));
    }

    @Override
    public Inventory decrementQuantity(Long productId, Integer quantity) {
        return repository.findById(productId)
                .map(inventory -> {
                    int current = inventory.getAvailableQuantity();
                    if (current < quantity) {
                        throw new RuntimeException("Not enough inventory for productId " + productId);
                    }
                    inventory.setAvailableQuantity(current - quantity);
                    return repository.save(inventory);
                })
                .orElseThrow(() -> new RuntimeException("Inventory not found for productId " + productId));
    }

}

