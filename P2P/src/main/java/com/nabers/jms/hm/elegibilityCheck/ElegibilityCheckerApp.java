package com.nabers.jms.hm.elegibilityCheck;

import com.nabers.jms.hm.elegibilityCheck.listener.ElegibilityCheckListener;
import com.nabers.jms.hm.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ElegibilityCheckerApp {

    public static void main(String[] args) throws NamingException, JMSException, InterruptedException {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()){

             JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
            consumer.setMessageListener(new ElegibilityCheckListener());

            Thread.sleep(10000);

        }

    }

}
