package com.nabers.jms.messages.ObjectMessagesDemo;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.StreamMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ObjectMessages_JMS2X {
    public static void main(String[] args) throws NamingException, JMSException {

        //Cria contexto inicial baseado na propriedade "java.naming.factory.initial" da config. "jndi.properties".
        InitialContext initialContext = new InitialContext();

        //Criando Queue, usando o contexto, buscando na api JNDI pelo recurso de nome "queue/myQueue".
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            JMSProducer requestProducer = jmsContext.createProducer();

            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            TestObject testObject = new TestObject(1,"testando");
            objectMessage.setObject(testObject);

            requestProducer.send(requestQueue, objectMessage);

            JMSConsumer requestConsumer = jmsContext.createConsumer(requestQueue);

            ObjectMessage receivedMsg = (ObjectMessage) requestConsumer.receive();

            TestObject receivedTestObj = (TestObject) receivedMsg.getObject();

            System.out.println(receivedTestObj.getId());
            System.out.println(receivedTestObj.getName());
            System.out.println(receivedMsg.getObject().toString());


        }

    }
}
