package com.van.jms.javatpoint.topic.myowncode.jms1x;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TopicListener implements MessageListener{

	@Override
	public void onMessage(Message msg) {
		TextMessage textMessage = (TextMessage) msg;
		try {
			System.out.println("Recieved: "+textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}		
	}

}
