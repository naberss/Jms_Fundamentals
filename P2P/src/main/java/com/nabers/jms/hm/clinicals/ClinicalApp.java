package com.nabers.jms.hm.clinicals;

import com.nabers.jms.hm.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClinicalApp {

    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()){

            JMSProducer jmsProducer = jmsContext.createProducer();

            ObjectMessage objectMessage = jmsContext.createObjectMessage();

            Patient patient = new Patient();
            patient.setId(1);
            patient.setName("teste");
            patient.setBill(1350.95);

            objectMessage.setObject(patient);

            jmsProducer.send(requestQueue,objectMessage);

            //--------------------------------------------//t

            MapMessage replyMessage = (MapMessage) jmsContext.createConsumer(replyQueue).receive(30000);
            System.out.println("response: "+replyMessage.getBoolean("tested"));
        }

    }
}
