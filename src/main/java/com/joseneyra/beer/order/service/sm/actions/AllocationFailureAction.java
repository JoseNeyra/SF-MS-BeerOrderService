package com.joseneyra.beer.order.service.sm.actions;

import com.joseneyra.beer.order.service.config.JmsConfig;
import com.joseneyra.beer.order.service.domain.BeerOrderEvent;
import com.joseneyra.beer.order.service.domain.BeerOrderStatus;
import com.joseneyra.beer.order.service.services.BeerOrderManagerImpl;
import com.joseneyra.brewery.model.events.AllocationFailureEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class AllocationFailureAction implements Action<BeerOrderStatus, BeerOrderEvent> {

    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatus, BeerOrderEvent> context) {
        log.debug("Validate Order Action was called");

        // Get the BeerOrderId from the context
        String beerOrderId = Objects.requireNonNull(context.getMessage().getHeaders()
                .get(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER)).toString();

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_FAILURE_QUEUE, AllocationFailureEvent.builder()
                        .orderId(UUID.fromString(beerOrderId))
                        .build());

        log.debug("Sent Allocation Failure Message to queue for order id " + beerOrderId);
    }
}