package com.javapractice.inventoryservice;

import com.javapractice.inventoryservice.model.Inventory;
import com.javapractice.inventoryservice.respository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("Iphne14");
			inventory.setQuantity(100);
			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("Iphne15");
			inventory1.setQuantity(10);
			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}

}
