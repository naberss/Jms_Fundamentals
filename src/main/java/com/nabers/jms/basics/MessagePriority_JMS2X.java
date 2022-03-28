package com.nabers.jms.basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class MessagePriority_JMS2X {
    public static void main(String[] args) throws NamingException {

        //Cria contexto inicial baseado na propriedade "java.naming.factory.initial" da config. "jndi.properties".
        InitialContext initialContext = new InitialContext();

        //Criando Queue, usando o contexto, buscando na api JNDI pelo recurso de nome "queue/myQueue".
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        final boolean autostart = true;

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            jmsContext.setAutoStart(autostart);

            // Setando prioridade das mensagens e enfileirando as mesmas. (quanto maior o numero maior a procedencia)

            jmsContext.createProducer().setPriority(2).send(queue, "priority 2");

            jmsContext.createProducer().setPriority(0).send(queue, "priority 0");

            jmsContext.createProducer().setPriority(1).send(queue, "priority 1");

            // Criando copia da fila em memoria para poder navegar dentre seus nodos sem consumi-los.
            Enumeration enumeration = jmsContext.createBrowser(queue).getEnumeration();

            JMSConsumer consumer = jmsContext.createConsumer(queue);

            while (enumeration.hasMoreElements()) {

                TextMessage msg = (TextMessage) enumeration.nextElement();
                System.out.println("enum: "+msg.getText());

                System.out.println("real: "+consumer.receive().getBody(String.class));
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
