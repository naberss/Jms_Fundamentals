package com.nabers.jms.basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;


import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContextDemo_JMS2X {
    public static void main(String[] args) throws NamingException {

        //Cria contexto inicial baseado na propriedade "java.naming.factory.initial" da config. "jndi.properties".
        InitialContext initialContext = new InitialContext();

        //Criando Queue, usando o contexto, buscando na api JNDI pelo recurso de nome "queue/myQueue".
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            jmsContext.createProducer().send(queue, "testando txt msg");

            System.out.println(jmsContext.createConsumer(queue).receiveBody(String.class));

        }

    }
}
