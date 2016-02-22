package com.van.jms.javatpoint.topic.myowncode.jms1x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class VanPublisher implements AutoCloseable{
	TopicConnectionFactory factory;
	TopicConnection connection;
	Topic topic;
	TopicSession session;
	TopicPublisher publisher;
	TextMessage textMsg;
	
	public VanPublisher() throws NamingException, JMSException {
		InitialContext init = new InitialContext();
		
		factory = (TopicConnectionFactory)init.lookup("jms/RemoteConnectionFactory");
		
		connection = factory.createTopicConnection("huy", "huy"); //create a connection in "stopped" mode, meaning no message will be sent unless we call connection.start();
		connection.start(); // necessary
		
		session = (TopicSession) connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		
		topic = (Topic)init.lookup("jms/topic/test");
		publisher = session.createPublisher(topic);
		textMsg = session.createTextMessage();
		
	}
	
	public void publish() throws IOException, JMSException{
		BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
		while(true)  
        {  
            System.out.println("Enter a message to send. Enter \"quit\" to terminate:");  
            String s=b.readLine();  
            if (s.equals("quit"))  
                break;  
            
            textMsg.setText(s);
            publisher.publish(textMsg);
        }  
        //Make sure to close the connection.  
        connection.close(); 
	}
	
	public static void main(String[] args) {
		try (VanPublisher pub = new VanPublisher()){
			pub.publish();
		} catch (NamingException | JMSException | IOException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}

	@Override
	public void close() throws Exception {
		if(connection!=null)
			connection.close();
		
	}

}
