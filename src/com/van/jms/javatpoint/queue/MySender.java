package com.van.jms.javatpoint.queue;

import java.io.BufferedReader;  
import java.io.InputStreamReader;  
import javax.naming.*;  
import javax.jms.*;  
  
public class MySender {  
    public static void main(String[] args) {  
        try  
        {   //Create and start connection  
            InitialContext ctx=new InitialContext();  
            QueueConnectionFactory factory=(QueueConnectionFactory)ctx.lookup("jms/RemoteConnectionFactory");   
            QueueConnection con=factory.createQueueConnection();  
            con.start();  
            //2) create queue session  
            QueueSession session=con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);   
            //3) get the Queue object  
            Queue queue=(Queue)ctx.lookup("jms/queue/test");   
            //4)create QueueSender object         
            QueueSender sender=session.createSender(queue);  
            //5) create TextMessage object  
            TextMessage msg=session.createTextMessage();  
              
            //6) write message  
            BufferedReader b=new BufferedReader(new InputStreamReader(System.in));  
            while(true)  
            {  
                System.out.println("Enter Msg, \"quit\" to terminate:");  
                String s=b.readLine();  
                if (s.equals("quit"))  
                    break;  
                msg.setText(s);  
                //7) send message  
                sender.send(msg);  
                System.out.println("Sent:  "+s);  
            }  
            //8) connection close  
            con.close();  
        }catch(Exception e){
        	System.out.println(e);
        }  
    }  
}  