package com.van.jms.javatpoint.topic.myowncode.jms1x;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class VanSubsriber implements AutoCloseable{
	TopicConnectionFactory factory;
	TopicConnection connection;
	TopicSession session;
	Topic topic;
	TopicSubscriber subscriber;
	TopicListener listener;
	
	public VanSubsriber() throws NamingException, JMSException {
		InitialContext init = new InitialContext();
		factory = (TopicConnectionFactory) init.lookup("jms/RemoteConnectionFactory");
		connection = factory.createTopicConnection("huy","huy");
		connection.start();
		
		session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		
		topic = (Topic) init.lookup("jms/topic/test");
		subscriber = session.createSubscriber(topic);
		listener = new TopicListener();
		
  	}
	
	public void subsribe() throws IOException, JMSException{
		subscriber.setMessageListener(listener);

		//this is to keep the program from exiting
		System.out.println("Press Enter to finish...");
	    System.in.read();
	    connection.close();
	}
	
	public static void main(String[] args) {
		try (VanSubsriber sub = new VanSubsriber()){
			sub.subsribe();
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
