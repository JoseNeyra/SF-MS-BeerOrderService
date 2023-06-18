package com.joseneyra.beer.order.service.sm.actions;

import com.joseneyra.beer.order.service.config.JmsConfig;
import com.joseneyra.beer.order.service.domain.BeerOrder;
import com.joseneyra.beer.order.service.domain.BeerOrderEvent;
import com.joseneyra.beer.order.service.domain.BeerOrderStatus;
import com.joseneyra.beer.order.service.repositories.BeerOrderRepository;
import com.joseneyra.beer.order.service.services.BeerOrderManagerImpl;
import com.joseneyra.beer.order.service.services.MessagingService;
import com.joseneyra.beer.order.service.web.mappers.BeerOrderMapper;
import com.joseneyra.brewery.model.BeerOrderDto;
import com.joseneyra.brewery.model.events.ValidateBeerOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidateOrderAction implements Action<BeerOrderStatus, BeerOrderEvent> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final MessagingService messagingService;

    @Override
    public void execute(StateContext<BeerOrderStatus, BeerOrderEvent> context) {
        log.debug("Validate Order Action was called");

        // Get the BeerOrderId from the context
        String beerOrderId = Objects.requireNonNull(context.getMessage().getHeaders().get(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER)).toString();

        // Get the beerOrder from the repository
        Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(UUID.fromString(beerOrderId));

        beerOrderOptional.ifPresentOrElse( beerOrder -> {
            log.debug("BeerOrder Found");
            // Convert to DTO
            BeerOrderDto beerOrderDto = beerOrderMapper.beerOrderToDto(beerOrder);
            log.debug("BeerOrderDTO Mapped Successfully");

            messagingService.sendMessage(ValidateBeerOrderRequest.builder()
                            .beerOrderDto(beerOrderDto).build()
                    , JmsConfig.VALIDATE_ORDER_QUEUE);
        }, () -> log.error("Beer Order Not Found, beerOrderId: " + beerOrderId));
    }
}
