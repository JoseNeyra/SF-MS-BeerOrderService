package com.joseneyra.beer.order.service.services;

import com.joseneyra.beer.order.service.domain.BeerOrder;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
}
