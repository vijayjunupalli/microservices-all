package com.javapractice.inventoryservice.Service;


import com.javapractice.inventoryservice.dto.InventoryResponse;
import com.javapractice.inventoryservice.model.Inventory;
import com.javapractice.inventoryservice.respository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> ckuCode) {

        return inventoryRepository.findBySkuCodeIn(ckuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0).build())
                .collect(Collectors.toList());
    }
}

