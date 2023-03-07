package com.joseneyra.beer.order.service.sm.actions;

import com.joseneyra.beer.order.service.config.JmsConfig;
import com.joseneyra.beer.order.service.domain.BeerOrder;
import com.joseneyra.beer.order.service.domain.BeerOrderEvent;
import com.joseneyra.beer.order.service.domain.BeerOrderStatus;
import com.joseneyra.beer.order.service.repositories.BeerOrderRepository;
import com.joseneyra.beer.order.service.services.BeerOrderManagerImpl;
import com.joseneyra.beer.order.service.web.mappers.BeerOrderMapper;
import com.joseneyra.brewery.model.events.DeAllocateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeAllocateOrderAction implements Action<BeerOrderStatus, BeerOrderEvent> {

    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

    @Override
    public void execute(StateContext<BeerOrderStatus, BeerOrderEvent> context) {
        String beerOrderId = Objects.requireNonNull(context.getMessage().getHeaders()
                .get(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER)).toString();

        Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(UUID.fromString(beerOrderId));
        beerOrderOptional.ifPresentOrElse( beerOrder -> {
            // Convert the beerOrder object to DTO and send it
            jmsTemplate.convertAndSend(JmsConfig.DEALLOCATE_ORDER_QUEUE,
                    DeAllocateOrderRequest.builder()
                            .beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder))
                            .build());

            log.debug("Sent De-Allocation Request for order id: " + beerOrderId);
        }, () -> log.error("Beer Order not found, beerOrderId: " + beerOrderId));
    }
}
