package com.joseneyra.beer.order.service.services;

import com.joseneyra.beer.order.service.domain.BeerOrder;
import com.joseneyra.beer.order.service.domain.BeerOrderEvent;
import com.joseneyra.beer.order.service.domain.BeerOrderStatus;
import com.joseneyra.beer.order.service.repositories.BeerOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class BeerOrderStateChangedInterceptor extends StateMachineInterceptorAdapter<BeerOrderStatus, BeerOrderEvent> {

    private final BeerOrderRepository beerOrderRepository;

    // Intercepts a state change, and if a message is present,
    // gets the headers from the message, then gets the beerOrderId, and stores the beerOrder with its state
    // in the db

    public void preStateChange(State<BeerOrderStatus, BeerOrderEvent> state, Message<BeerOrderEvent> message,
                               Transition<BeerOrderStatus, BeerOrderEvent> transition,
                               StateMachine<BeerOrderStatus, BeerOrderEvent> stateMachine) {
        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER, " ")))
                .ifPresent(beerOrderId -> {
                    log.debug("Saving state for order id: " + beerOrderId + " Status: " + state.getId());

                    BeerOrder beerOrder = beerOrderRepository.getReferenceById(UUID.fromString(beerOrderId));
                    beerOrder.setOrderStatus(state.getId());
                    beerOrderRepository.saveAndFlush(beerOrder);        // Usually spring data jpa and hibernate do lazy writes,
                                                                        // saveAndFlush forces hibernate to persist the object right away
                });
    }
}
