package com.van.jms.javatpoint.topic.myowncode.jms2x_both_send_and_receive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class VanPublisher implements Runnable, AutoCloseable, MessageListener{
	ConnectionFactory factory;
	Destination destination;
	Destination replyToDestination;
	
	JMSContext context;

	
	public VanPublisher() throws NamingException {
		InitialContext init = new InitialContext();
		
		factory = (ConnectionFactory)init.lookup("jms/RemoteConnectionFactory");
		
		//Note that it does not matter whether this is a queue or a topic. JMS2 abstracts all these and provide common API.
		destination = InitialContext.doLookup("jms/topic/test");
		replyToDestination = InitialContext.doLookup("jms/topic/test1");
		
		System.out.println(replyToDestination==null);
		
		context = factory.createContext("huy","huy");
		
		//a separate thread for consuming messages
		Thread thread = new Thread(this);
		thread.start();
	
		System.out.println("Publisher, please Enter a message to send. Enter \"quit\" to terminate:");  
	}
	
	public void publish() throws IOException, JMSException{
		
			JMSProducer producer = context.createProducer();
			
			BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
			while(true)  
	        {  
	 
	            String s=b.readLine();  
	            if (s.equals("quit"))  
	                break;  
	            TextMessage textMsg = context.createTextMessage();
	            textMsg.setText(s);
	            textMsg.setStringProperty("sender", "publisher"); 
	            textMsg.setJMSReplyTo(replyToDestination);
	            
	            producer.send(destination, textMsg);
	            //System.out.println("sent: "+s);
	        }  
		
        
	}
	
	public static void main(String[] args)  {
		try(VanPublisher pub = new VanPublisher()){
			pub.publish();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onMessage(Message msg) {
		TextMessage textMessage = (TextMessage) msg;
		
		try {
			System.out.println("from "+textMessage.getStringProperty("sender")+" : "+textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void run() {
				
			JMSConsumer consumer = context.createConsumer(replyToDestination);			
			consumer.setMessageListener(this);
			
			while(true){}
		
		
	}

	@Override
	public void close() throws Exception {
         if(context!= null)
        	 context.close();
	}

}
