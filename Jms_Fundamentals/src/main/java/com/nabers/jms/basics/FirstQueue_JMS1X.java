package com.nabers.jms.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstQueue_JMS1X {
    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext initialContext = null;
        Connection connection = null;
        try {

            //Cria contexto inicial baseado na propriedade "java.naming.factory.initial" da config. "jndi.properties".
            initialContext = new InitialContext();

            //Criando ConnectionFactory, usando o contexto, buscando na api JNDI pelo recurso de nome "ConnectionFactory".
            ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");

            //Intanciando uma conexao.
            connection = connectionFactory.createConnection();

            //Criando sessao.
            Session session = connection.createSession();

            //Criando Queue, usando o contexto, buscando na api JNDI pelo recurso de nome "queue/myQueue".
            Queue queue = (Queue) initialContext.lookup("queue/myQueue");

            //Criando producer e definindo o destino das mensagens criadas pelo mesmo.
            MessageProducer producer = session.createProducer(queue);

            TextMessage message = session.createTextMessage("testando!");

            producer.send(message);


            //------------------------------------//

            //Criando consumer e definindo o local de onde as mensagens deverao ser consumidas.
            MessageConsumer consumer = session.createConsumer(queue);

            //Inicia a entrega de mensagens recebidas ao consumer.
            connection.start();

            TextMessage receivedMessage = (TextMessage) consumer.receive();

            System.out.println(receivedMessage.getText());


        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (initialContext != null) {
                initialContext.close();
            }

            if (connection != null) {
                connection.close();
            }

        }

    }
}
