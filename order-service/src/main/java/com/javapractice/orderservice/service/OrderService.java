package com.javapractice.orderservice.service;


import com.javapractice.orderservice.dto.InventoryResponse;
import com.javapractice.orderservice.dto.OrderLineItemsDto;
import com.javapractice.orderservice.dto.OrderRequest;
import com.javapractice.orderservice.model.Order;
import com.javapractice.orderservice.model.OrderLineItems;
import com.javapractice.orderservice.respository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemsList(orderRequest.getOrderLineItemsDtoList()
               .stream().map(orderLineItemsDto ->mapToDto(orderLineItemsDto)).collect(Collectors.toList()));

        List<String> skuCodes =order.getOrderLineItemsList().stream().map(orderLineItems -> orderLineItems.getSkuCode()).toList();

        //call inventory service and place order if product is available
        InventoryResponse[] result = webClient.get().
                uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve().bodyToMono(InventoryResponse[].class).block();

        boolean isProductInStock = Arrays.stream(result).allMatch(inventoryResponse -> inventoryResponse.isInStock());

        if (isProductInStock){
            orderRepository.save(order);
        }else{
            throw new IllegalArgumentException("Product is not in stock");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setId(orderLineItemsDto.getId());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItems.getQuantity());
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());
        return orderLineItems;


    }
}
