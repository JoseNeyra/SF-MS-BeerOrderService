package com.joseneyra.beer.order.service.testComponents;

import com.joseneyra.beer.order.service.config.JmsConfig;
import com.joseneyra.brewery.model.events.ValidateBeerOrderRequest;
import com.joseneyra.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(Message<ValidateBeerOrderRequest> msg) {
        boolean isValid = true;
        boolean sendResponse = true;

        ValidateBeerOrderRequest request = msg.getPayload();

        if (request.getBeerOrderDto().getCustomerRef() != null) {
            if (request.getBeerOrderDto().getCustomerRef().equals("fail-validation")){
                isValid = false;
            } else if (request.getBeerOrderDto().getCustomerRef().equals("dont-validate")) {
                sendResponse = false;
            }
        }

        if (sendResponse) {
            jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE,
                    ValidateOrderResult.builder()
                            .isValid(isValid)
                            .orderId(request.getBeerOrderDto().getId())
                            .build());
        }
    }
}
