package com.nabers.jms.messages;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RequestReply_JMS2X {
    public static void main(String[] args) throws NamingException, JMSException {

        //Cria contexto inicial baseado na propriedade "java.naming.factory.initial" da config. "jndi.properties".
        InitialContext initialContext = new InitialContext();

        //Criando Queue, usando o contexto, buscando na api JNDI pelo recurso de nome "queue/myQueue".
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
        /*Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");*/

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            Queue replyQueue = jmsContext.createTemporaryQueue();
            JMSProducer requestProducer = jmsContext.createProducer();

             /* toda mensagem enviada a partir desta queue, possuira uma propriedade de nome 'JMSReplyTo'
                 que bascicamente, armazena o destino da response (reply) para onde a mesma deve ser enviada */
            requestProducer.setJMSReplyTo(replyQueue);

            TextMessage requestMessage = jmsContext.createTextMessage("io");

             /* Setando o header replyTo a nivel da mensagem, mas so seria necessario caso o mesmo ja
                 nao estivesse sendo definido a nivel do producer */
            requestMessage.setJMSReplyTo(replyQueue);

            requestProducer.send(requestQueue, requestMessage);

            System.out.println("ID: "+requestMessage.getJMSMessageID());

            JMSConsumer requestConsumer = jmsContext.createConsumer(requestQueue);

            System.out.println(requestConsumer.receiveBody(String.class));

            //-----------------------------------------------------------//
            JMSProducer replyProducer = jmsContext.createProducer();

            TextMessage replyMessage = jmsContext.createTextMessage("got u");

            /* Correlaciona a mensagem de resposta (reply msg) ao envio inicial (request msg),
                 vide ID da mensagem consumida (request msg) */
            replyMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());

            /* Envia a confirmação de chegada da mensagem a partir da propriedade de replyTo incluida no header */
            replyProducer.send(requestMessage.getJMSReplyTo(),replyMessage);


            // Consome a mensagem de resposta
            JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);

            TextMessage ConsumedMsg = (TextMessage) replyConsumer.receive();

            System.out.println("Text: "+ConsumedMsg.getText());
            System.out.println("CorrelationID: "+ConsumedMsg.getJMSCorrelationID());

        }

    }
}
