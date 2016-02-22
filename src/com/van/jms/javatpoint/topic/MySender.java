package com.van.jms.javatpoint.topic;

import java.io.BufferedReader;  
import java.io.InputStreamReader;  
import javax.naming.*;  
import javax.jms.*;  
  
public class MySender {  
    public static void main(String[] args) {  
        try  
        {   //Create and start connection  
            InitialContext ctx=new InitialContext();  
            TopicConnectionFactory f=(TopicConnectionFactory)ctx.lookup("jms/RemoteConnectionFactory"); 
            
            
            TopicConnection con=f.createTopicConnection();  
            con.start();  
              
            //3) get the Topic object  
            Topic topic =(Topic)ctx.lookup("jms/topic/test");   
            
            //2) create queue session  
            TopicSession ses=con.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            //4)create TopicPublisher object          
            TopicPublisher publisher=ses.createPublisher(topic );  
            //5) create TextMessage object  
            TextMessage msg=ses.createTextMessage();  
              
            //6) write message  
            BufferedReader b=new BufferedReader(new InputStreamReader(System.in));  
            while(true)  
            {  
                System.out.println("Enter Msg, quit to terminate:");  
                String s=b.readLine();  
                if (s.equals("quit"))  
                    break;  
                msg.setText(s);  
                //7) send message  
                publisher.publish(msg);  
                System.out.println("Message successfully sent.");  
            }  
            //8) connection close  
            con.close();  
        }catch(Exception e){System.out.println(e);}  
    }  
}
