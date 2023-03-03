package com.joseneyra.beer.order.service.services.listeners;

import com.joseneyra.beer.order.service.config.JmsConfig;
import com.joseneyra.beer.order.service.services.BeerOrderManager;
import com.joseneyra.brewery.model.events.AllocateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeerOrderAllocationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_RESPONSE_QUEUE)
    public void listen(AllocateOrderResult result) {
        if (!result.getAllocationError() && !result.getPendingInventory()) {
            // Allocated normally
            beerOrderManager.beerOrderAllocationPassed(result.getBeerOrderDto());
        } else if (!result.getAllocationError() && result.getPendingInventory()) {
            // Pending Inventory
            beerOrderManager.beerOrderAllocationPendingInventory(result.getBeerOrderDto());
        } else if (result.getAllocationError()) {
            // Allocation Error
            beerOrderManager.beerOrderAllocationFailed(result.getBeerOrderDto());
        }
    }
}
