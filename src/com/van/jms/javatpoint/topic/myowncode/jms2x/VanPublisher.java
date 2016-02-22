package com.van.jms.javatpoint.topic.myowncode.jms2x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class VanPublisher {
	ConnectionFactory factory;
	Destination destination;

	
	public VanPublisher() throws NamingException {
		InitialContext init = new InitialContext();
		
		factory = (ConnectionFactory)init.lookup("jms/RemoteConnectionFactory");
		
		//Note that it does not matter whether this is a queue or a topic. JMS2 abstracts all these and provide common API.
		destination = InitialContext.doLookup("jms/topic/test");
	
	}
	
	public void publish() throws IOException, JMSException{
		try(JMSContext context = factory.createContext("huy","huy")){
			JMSProducer producer = context.createProducer();
			
			BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
			while(true)  
	        {  
	            System.out.println("Enter a message to send. Enter \"quit\" to terminate:");  
	            String s=b.readLine();  
	            if (s.equals("quit"))  
	                break;  
	            TextMessage textMsg = context.createTextMessage();
	            textMsg.setText(s);
	            
	            
	            producer.send(destination, textMsg);
	           System.out.println("sent: "+s);
	        }  
		}catch (IOException e) {
			throw e;
		}
		
        
	}
	
	public static void main(String[] args) throws NamingException, IOException, JMSException {
		VanPublisher pub = new VanPublisher();
		pub.publish();
	}

}
