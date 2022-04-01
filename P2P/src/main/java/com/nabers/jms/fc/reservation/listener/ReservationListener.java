package com.nabers.jms.fc.reservation.listener;

import com.nabers.jms.fc.model.Passenger;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ReservationListener implements MessageListener {

    int ListenerID;

    public ReservationListener(int listenerID) {
        ListenerID = listenerID;
    }

    @Override
    public void onMessage(Message message) {

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext() ){

            InitialContext initialContext = new InitialContext();
            Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

            Passenger passenger = message.getBody(Passenger.class);

            MapMessage mapMessage = jmsContext.createMapMessage();
            if(passenger.getLastName().contains("moreira")){
                mapMessage.setString("response","Listener ID:"+ ListenerID +
                                                       "\nPassenger ID: "+passenger.getId()+
                                                       "\nZUP BROOOOO");
            }else{
                mapMessage.setString("response","Listener ID:"+ ListenerID +
                                                       "\nPassenger ID: "+passenger.getId()+
                                                       "\nNO SHITT\n");
            }

            jmsContext.createProducer().send(replyQueue,mapMessage);

            System.out.println("message sent!");
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }


    }
}
