package com.javapractice.inventoryservice.respository;

import com.javapractice.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long>{
    Optional<Inventory> findBySkuCode(String ckuCode);

    List<Inventory> findBySkuCodeIn(List<String> ckuCode);
}