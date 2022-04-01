package com.nabers.jms.fc.reservation;

import com.nabers.jms.fc.model.Passenger;
import com.nabers.jms.fc.reservation.listener.ReservationListener;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Collections;

public class ReservationApp {
    public static void main(String[] args) throws NamingException {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        JMSContext jmsContext = cf.createContext();

        while (true) {
            try {

                int size = Collections.list(jmsContext.createBrowser(requestQueue).getEnumeration()).size();

                if (size > 1) {
                    jmsContext.createConsumer(requestQueue).setMessageListener(new ReservationListener(1));
                    jmsContext.createConsumer(requestQueue).setMessageListener(new ReservationListener(2));
                } else if (size > 0) {
                    jmsContext.createConsumer(requestQueue).setMessageListener(new ReservationListener(1));
                }

                //-------------------------------//
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }


    }


}
