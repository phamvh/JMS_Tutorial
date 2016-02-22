package com.van.jms.javatpoint.topic;

import javax.jms.*;  
import javax.naming.InitialContext;  
  
public class MyReceiver {  
    public static void main(String[] args) {  
        try {  
            //1) Create and start connection  
            InitialContext ctx=new InitialContext();  
            TopicConnectionFactory f=(TopicConnectionFactory)ctx.lookup("jms/RemoteConnectionFactory"); 
            
            //try with resource, not need to close TopicConnection explicitly.
            try(TopicConnection con=f.createTopicConnection()){  
	            con.start();  
	            
	            //2) create topic session  
	            TopicSession ses=con.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);  
	            //3) get the Topic object  
	            Topic topic=(Topic)ctx.lookup("jms/topic/test");   
	            //4)create TopicSubscriber  
	            TopicSubscriber receiver=ses.createSubscriber(topic);  
	              
	            //5) create listener object  
	            MyListener listener=new MyListener();  
	              
	            //6) register the listener object with subscriber  
	            receiver.setMessageListener(listener);  
	                          
	            System.out.println("Subscriber1 is ready, waiting for messages...");  
	            System.out.println("press Ctrl+c to shutdown...");  
	            while(true){                  
	                Thread.sleep(1000);  
	            }  
            }catch (Exception e) {
				// TODO: handle exception
			}
        }catch(Exception e){
        	System.out.println(e);
        }  
    }  
  
}
