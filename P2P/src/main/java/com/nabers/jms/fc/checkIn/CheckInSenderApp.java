package com.nabers.jms.fc.checkIn;

import com.nabers.jms.fc.checkIn.listener.CheckInListener;
import com.nabers.jms.fc.model.Passenger;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class CheckInSenderApp {

    public static void main(String[] args) throws NamingException, JMSException {

        Scanner scan = new Scanner(System.in);

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
        int duplicates;

        while (true) {
            try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext jmsContext = cf.createContext()) {
                jmsContext.createConsumer(replyQueue).setMessageListener(new CheckInListener());
                Passenger passenger = new Passenger();

                System.out.println("\npassenger name: ");
                passenger.setFirstName(scan.next());

                System.out.println("passenger lastName: ");
                passenger.setLastName(scan.next());

                ObjectMessage objectMessage = jmsContext.createObjectMessage();

                System.out.println("how many duplicates: ");
                duplicates = scan.nextInt();



                for (int i = 0; i < duplicates; i++) {
                    passenger.setId(i);
                    objectMessage.setObject(passenger);

                    jmsContext.createProducer().send(requestQueue, objectMessage);
                }

                //-------------------------------//


            }
        }

    }
}
