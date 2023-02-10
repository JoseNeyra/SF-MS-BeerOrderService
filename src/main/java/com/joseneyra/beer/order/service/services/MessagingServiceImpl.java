package com.joseneyra.beer.order.service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.io.Serializable;

@RequiredArgsConstructor
@Slf4j
@Component
public class MessagingServiceImpl implements MessagingService {

    private final JmsTemplate jmsTemplate;
//    private final ObjectMapper objectMapper;

    @Override
    public void sendMessage(Serializable message, String destinationQueue) {
        log.debug("Sending message {}", message.toString());

        jmsTemplate.convertAndSend(destinationQueue, message);
    }

    @Override
    public void sendAndReceiveMessage(Serializable message) throws JMSException {

    }

    @Override
    public void listen(Serializable message) {

    }
}
