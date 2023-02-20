package com.joseneyra.beer.order.service.sm.actions;

import com.joseneyra.beer.order.service.config.JmsConfig;
import com.joseneyra.beer.order.service.domain.BeerOrder;
import com.joseneyra.beer.order.service.domain.BeerOrderEvent;
import com.joseneyra.beer.order.service.domain.BeerOrderStatus;
import com.joseneyra.beer.order.service.repositories.BeerOrderRepository;
import com.joseneyra.beer.order.service.services.BeerOrderManagerImpl;
import com.joseneyra.beer.order.service.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class AllocateOrderAction implements Action<BeerOrderStatus, BeerOrderEvent> {

    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

    @Override
    public void execute(StateContext<BeerOrderStatus, BeerOrderEvent> context) {
        String beerOrderId = Objects.requireNonNull(context.getMessage().getHeaders()
                .get(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER)).toString();
        BeerOrder beerOrder = beerOrderRepository.findOneById(UUID.fromString(beerOrderId));

        // Convert the beerOrder object to DTO and send it
        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_QUEUE, beerOrderMapper.beerOrderToDto(beerOrder));

        log.debug("Sent Allocation Request for order id: " + beerOrderId);

    }
}
