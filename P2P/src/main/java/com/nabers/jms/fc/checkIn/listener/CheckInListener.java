package com.nabers.jms.fc.checkIn.listener;

import com.nabers.jms.fc.model.Passenger;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class CheckInListener implements MessageListener {

    @Override
    public void onMessage(Message message) {

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {

            MapMessage mapMessage = (MapMessage) message;

            System.out.println("\n"+mapMessage.getString("response"));

            System.out.println("message received!");
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
