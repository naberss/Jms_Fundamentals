package com.nabers.jms.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstTopic_JMS1X {
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

            //Criando Topic, usando o contexto, buscando na api JNDI pelo recurso de nome "topic/myTopic".
            Topic topic = (Topic) initialContext.lookup("topic/myTopic");

            //Criando producer e definindo o destino das mensagens criadas pelo mesmo.
            MessageProducer producer = session.createProducer(topic);

            //Criando consumer e definindo o local de onde as mensagens deverao ser consumidas.
            MessageConsumer consumer1 = session.createConsumer(topic);
            MessageConsumer consumer2 = session.createConsumer(topic);

            TextMessage message = session.createTextMessage("testando!");

            producer.send(message);


            //------------------------------------//

            //Inicia a entrega de mensagens recebidas ao consumer.
            connection.start();

            TextMessage receivedMessage1 = (TextMessage) consumer1.receive();
            TextMessage receivedMessage2 = (TextMessage) consumer2.receive();

            System.out.println(receivedMessage1.getText());
            System.out.println(receivedMessage2.getText());


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
