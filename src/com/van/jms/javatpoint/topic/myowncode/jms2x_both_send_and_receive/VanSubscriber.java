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

public class VanSubscriber implements MessageListener, Runnable, AutoCloseable{
	ConnectionFactory factory;
	Destination destination;
	Destination replyToDestination;
	JMSContext context;
	
	public VanSubscriber() throws NamingException {
		InitialContext init = new InitialContext();
		
		factory = (ConnectionFactory)init.lookup("jms/RemoteConnectionFactory");
		destination = InitialContext.doLookup("jms/topic/test");
		//replyToDestination = InitialContext.doLookup("jms/topic/test1");
		context = factory.createContext("huy", "huy");
		
		Thread thread = new Thread(this);
		thread.start();
		
        System.out.println("Receiver, please enter a message to send. Enter \"quit\" to terminate:");  

	}

	public void subscribe() throws Exception{
		
			
			JMSConsumer consumer = context.createConsumer(destination);
			// or you can include a selector like this:
			//    JMSConsumer consumer = context.createConsumer(destination, "(auctionWinnerEmail is not null) and (auctionPrice > 1000)"); 
			
			consumer.setMessageListener(this);
			
			while(true){}
		
	}
	
	public static void main(String[] args) {
		
		try(VanSubscriber sub = new VanSubscriber()){
			sub.subscribe();
		}catch (Exception e) {
			e.printStackTrace();
 		}
	}

	@Override
	public void close() throws Exception {
        if(context!=null)
        	context.close();
	}

	@Override
	public void run() {
		System.out.println("Sub is strying to send something back to pub.");
		while(true){
			System.out.println(replyToDestination == null);
			
			//replyToDestination will bet set when the first message is received from Publisher. See onMessage() method
			if(replyToDestination==null){
				
				try {
					Thread.sleep(8000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("replyTo has been set for sub");
				break;
			}
		}
		System.out.println("Sub got out of while(true) loop.");
		try{
			JMSProducer producer = context.createProducer();
			
			BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
			while(true)  
	        {  
	            String s=b.readLine();  
	            if (s.equals("quit"))  
	                break;  
	            TextMessage textMsg = context.createTextMessage();
	            textMsg.setText(s);
	            textMsg.setStringProperty("sender", "subscriber"); 
	            
	            producer.send(replyToDestination, textMsg);
	            //System.out.println("sent: "+s);
	        }  
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void onMessage(Message msg) {
		TextMessage textMessage = (TextMessage) msg;
		
		try {
			if(replyToDestination==null){
				replyToDestination = textMessage.getJMSReplyTo();
				System.out.println("set replyToDestination for subscriber.");
				System.out.println(replyToDestination==null);
			}
			System.out.println("from "+textMessage.getStringProperty("sender")+" : "+textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
}
