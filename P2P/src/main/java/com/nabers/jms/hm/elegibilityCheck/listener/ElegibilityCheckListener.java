package com.nabers.jms.hm.elegibilityCheck.listener;

import com.nabers.jms.hm.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ElegibilityCheckListener implements MessageListener {
    @Override
    public void onMessage(Message message) {

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            Patient patient = message.getBody(Patient.class);

            InitialContext initialContext = new InitialContext();
            Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

            MapMessage mapMessage = jmsContext.createMapMessage();

            if (patient.getBill() >= 0) {
                mapMessage.setBoolean("tested", true);
            } else {
                mapMessage.setBoolean("tested", false);
            }

            jmsContext.createProducer().send(replyQueue, mapMessage);
            System.out.println("sending reply message");

        } catch (JMSException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }


    }
}
