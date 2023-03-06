package com.joseneyra.beer.order.service.sm.actions;

import com.joseneyra.beer.order.service.domain.BeerOrderEvent;
import com.joseneyra.beer.order.service.domain.BeerOrderStatus;
import com.joseneyra.beer.order.service.services.BeerOrderManagerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidationFailureAction implements Action<BeerOrderStatus, BeerOrderEvent> {
    @Override
    public void execute(StateContext<BeerOrderStatus, BeerOrderEvent> context) {
        String beerOrderId = (String) context.getMessage().getHeaders().get(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER);
        // Compensate Transaction Here
        log.error("Compensating Transaction ... Validation Failed, Beer Order Id: " + beerOrderId);
    }
}
