package com.van.jms.javatpoint.topic.myowncode.jms2x;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class VanSubscriber {
	ConnectionFactory factory;
	Destination destination;
	
	
	public VanSubscriber() throws NamingException {
		InitialContext init = new InitialContext();
		
		factory = (ConnectionFactory)init.lookup("jms/RemoteConnectionFactory");
		destination = InitialContext.doLookup("jms/topic/test");	
	}

	public void subscribe() throws Exception{
		try(JMSContext context = factory.createContext("huy", "huy")){
			
			JMSConsumer consumer = context.createConsumer(destination);
			// or you can include a selector like this:
			//    JMSConsumer consumer = context.createConsumer(destination, "(auctionWinnerEmail is not null) and (auctionPrice > 1000)"); 
			
			consumer.setMessageListener(new TopicListener());
			
			//This is to prevent the program from exiting
			System.out.println("Press enter to finish...");
			System.in.read();
		}catch (Exception e) {
			throw e;
		}
	}
	
	public static void main(String[] args) throws Exception {
		VanSubscriber sub = new VanSubscriber();
		sub.subscribe();
	}
}
