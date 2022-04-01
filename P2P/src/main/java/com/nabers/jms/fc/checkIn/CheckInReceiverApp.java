package com.nabers.jms.fc.checkIn;

import com.nabers.jms.fc.checkIn.listener.CheckInListener;
import com.nabers.jms.fc.reservation.listener.ReservationListener;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Collections;

public class CheckInReceiverApp {
    public static void main(String[] args) throws NamingException {

        InitialContext initialContext = new InitialContext();
        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        JMSContext jmsContext = cf.createContext();

        while (true) {
            jmsContext.createConsumer(replyQueue).setMessageListener(new CheckInListener());
        }
    }
}
