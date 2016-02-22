import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class Receiver implements MessageListener {
	private ConnectionFactory cf;
	 private Connection c;
	 private Session s;
	 private Destination d;
	 private MessageConsumer mc;

 public Receiver() throws NamingException, JMSException {
	 cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
	 d = InitialContext.doLookup("jms/queue/test");
	 
	 /*  JMS 1.x version
	  InitialContext init = new InitialContext();
	  cf = (ConnectionFactory) init.lookup("jms/RemoteConnectionFactory");
	  d = (Destination) init.lookup("jms/queue/PlayQueue");
	  c = (Connection) this.cf.createConnection("joao", "pedro");
	  c.start();
	  s = this.c.createSession(false, Session.AUTO_ACKNOWLEDGE);
	  mc = s.createConsumer(d);
	  
	  * */
 }

 /**
  * Example of using a synchronous receive().
  * Better to use the asyn one. See below.
  * @throws JMSException
  */
 private void receiveSynchronousJMS_2x() throws JMSException {
	 try(JMSContext context = cf.createContext("huy", "huy")){
		 JMSConsumer c = context.createConsumer(d);
	     String msg = c.receiveBody(String.class);
	     System.out.println(msg);
		 
		 
		 
	 }catch (Exception e) {
		 e.printStackTrace();
  	}
 }
 
 private String receiveSynchronousJMS_1x() throws JMSException {
	  TextMessage msg = (TextMessage) mc.receive();
	  c.close();
	  return msg.getText();
 }



/*
 * The MessageListener itself is not the whole thing. It can only react of the receival of a message.
 * MessageListener cannot work alone, it has to be added to a JMSConsumer. And in order to create a JMSConsumer, we need
 * all the JNDI info as in the constructor. 
 */
 public void registerMessageListener(){
	 try(JMSContext context = cf.createContext("huy", "huy")){
		 JMSConsumer c = context.createConsumer(d);
	     c.setMessageListener(this);
	     
	     System.out.println("Press enter to finish...");
	     System.in.read();
	     
	     
	 }catch (Exception e) {
		 e.printStackTrace();
  	}
 }
 
@Override
public void onMessage(Message arg0)  {
	TextMessage textMsg = (TextMessage)arg0;
	try{
		String str = textMsg.getText();
		System.out.println(str);
	}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	
}

public static void main(String[] args) throws NamingException, JMSException {
	  Receiver r = new Receiver();
	  r.registerMessageListener();
	  
	 }

}