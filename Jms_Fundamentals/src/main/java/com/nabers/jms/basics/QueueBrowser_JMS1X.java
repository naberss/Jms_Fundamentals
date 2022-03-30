package com.nabers.jms.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class QueueBrowser_JMS1X {
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

            TextMessage message1 = session.createTextMessage("testando! 1");
            TextMessage message2 = session.createTextMessage("testando! 2");

            producer.send(message1);
            producer.send(message2);

            MessageConsumer consumer = session.createConsumer(queue);

            // Criando browserque permitira navegar dentra todas as mensagens enfileiradas sem consumi-las
            QueueBrowser browser =  session.createBrowser(queue);

            // Componente em memoria que armazena as mensagens da fila/topico
            Enumeration enumeration = browser.getEnumeration();

            // Permite que consumers possam consumir as mensagens enfileiradas
            connection.start();

            /* Enquanto o enumerator das mensagens referentes a fila possuir elementos
               posteriores continua o loop */
            while(enumeration.hasMoreElements()) {

                // Exibe a mensagem no nodo vigente do enumerator
                TextMessage EnumeratedMsg = (TextMessage) enumeration.nextElement();
                System.out.println("Enumerated msg preview: "+EnumeratedMsg.getText());

                // Consume a mensagem da fila na ordem em que foi enviada
                TextMessage ReceivedMsg = (TextMessage) consumer.receive();
                System.out.println("Received msg preview: "+ReceivedMsg.getText());
            }


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
