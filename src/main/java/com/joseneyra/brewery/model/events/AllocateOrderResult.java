package com.joseneyra.brewery.model.events;

import com.joseneyra.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllocateOrderResult {

    private BeerOrderDto beerOrderDto;

    private Boolean allocationError = false;
    private Boolean pendingInventory = false;
}
