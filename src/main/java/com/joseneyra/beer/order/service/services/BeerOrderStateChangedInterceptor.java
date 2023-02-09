package com.joseneyra.beer.order.service.services;

import com.joseneyra.beer.order.service.domain.BeerOrder;
import com.joseneyra.beer.order.service.domain.BeerOrderEvent;
import com.joseneyra.beer.order.service.domain.BeerOrderStatus;
import com.joseneyra.beer.order.service.repositories.BeerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerOrderStateChangedInterceptor extends StateMachineInterceptorAdapter<BeerOrderStatus, BeerOrderEvent> {

    private final BeerOrderRepository beerOrderRepository;

    // Intercepts a state change, and if a message is present,
    // gets the headers from the message, then gets the beerOrderId, and stores the beerOrder with its state
    // in the db

    public void preStateChange(State<BeerOrderStatus, BeerOrderEvent> state, Message<BeerOrderEvent> message,
                               Transition<BeerOrderStatus, BeerOrderEvent> transition,
                               StateMachine<BeerOrderStatus, BeerOrderEvent> stateMachine) {
        Optional.ofNullable(message).flatMap(msg -> Optional.ofNullable((UUID) msg.getHeaders().getOrDefault(BeerOrderServiceImpl.BEER_ORDER_ID_HEADER, -1L)))
                .ifPresent(beerOrderId -> {
                    BeerOrder beerOrder = beerOrderRepository.getReferenceById(beerOrderId);
                    beerOrder.setOrderStatus(state.getId());
                    beerOrderRepository.save(beerOrder);
                });
    }
}
