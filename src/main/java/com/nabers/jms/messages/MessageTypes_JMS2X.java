package com.nabers.jms.messages;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.BytesMessage;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageTypes_JMS2X {
    public static void main(String[] args) throws NamingException, JMSException {

        //Cria contexto inicial baseado na propriedade "java.naming.factory.initial" da config. "jndi.properties".
        InitialContext initialContext = new InitialContext();

        //Criando Queue, usando o contexto, buscando na api JNDI pelo recurso de nome "queue/myQueue".
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            JMSProducer requestProducer = jmsContext.createProducer();
            BytesMessage bytesMessage = jmsContext.createBytesMessage();
            bytesMessage.writeUTF("byteMessageUTF");
            bytesMessage.writeLong(123);

            requestProducer.send(requestQueue, bytesMessage);

            JMSConsumer requestConsumer = jmsContext.createConsumer(requestQueue);

            BytesMessage receivedMsg = (BytesMessage) requestConsumer.receive();

            System.out.println(receivedMsg.readUTF());
            System.out.println(receivedMsg.readLong());



        }

    }
}
