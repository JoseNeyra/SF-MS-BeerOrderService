package com.joseneyra.beer.order.service.services;

import com.joseneyra.beer.order.service.domain.BeerOrder;
import com.joseneyra.brewery.model.BeerOrderDto;

import java.util.UUID;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);

    void processValidationResult(UUID beerOrderId, Boolean isValid);

    void beerOrderAllocationPassed(BeerOrderDto beerOrderDto);

    void beerOrderAllocationPendingInventory(BeerOrderDto beerOrderDto);

    void beerOrderAllocationFailed(BeerOrderDto beerOrderDto);

    void beerOrderPickedUp(UUID beerOrderId);

    void cancelOrder(UUID beerOrderId);
}
