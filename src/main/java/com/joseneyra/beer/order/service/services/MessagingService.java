package com.joseneyra.beer.order.service.services;

import javax.jms.JMSException;
import java.io.Serializable;

public interface MessagingService {

    public void sendMessage(Serializable message, String destinationQueue);

    public void sendAndReceiveMessage(Serializable message) throws JMSException;

    public void listen(Serializable message);
}
