import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class Sender {
 private ConnectionFactory connectionFactory; 
 private Connection connection; 
 private Session session;
 private Destination destination; 
 private MessageProducer messageProducer; 

 
 /**
  * 
  * A simple demo of using JMS.
  * This is a sender; Note that the connectionFactory and the destination are created using the info from WildFly.
  * See the JMS_Setup_Instruction.txt file for details.
  */
 public Sender() throws NamingException, JMSException {
	 //This block is when you don't want to create a jndi.properties file in the src dir, and instead just pass an instance of Properties to the constructor of InitialContext when you create the init object.
	 //final Properties env = new Properties();
     //env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
     //env.put(Context.PROVIDER_URL, "remote://localhost:4447");
     //env.put(Context.SECURITY_PRINCIPAL, "huy");
     //env.put(Context.SECURITY_CREDENTIALS, "huy");
   
 //This block for for JMS 1.x
  InitialContext init = new InitialContext();
  connectionFactory = (ConnectionFactory) init.lookup("jms/RemoteConnectionFactory");
  destination = (Destination) init.lookup("jms/queue/test");
  connection = (Connection) connectionFactory.createConnection("huy", "huy");
  connection.start();
  session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
  messageProducer = session.createProducer(destination);
  
  /*
     // This block is for JMS 2.x, much simpler than for JMS 1.x
	 connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
	 destination = InitialContext.doLookup("jms/queue/test");
  */	 
 }
 
 private void insideEEEnvironment_MessageProducer(){
	 /*
	 @Inject
	 @JMSConnectionFactory("jms/QueueConnectionFactory")
	 private JMSContext context;
	 
	 @Resource(name="jms/ShippingRequestQueue")
	 private Destination destination;
	 
	 //Note that ShippingRequest has to implement Serializable interface
	 ShippingRequest shippingRequest = new ShippingRequest();
	 shippingRequest.setItem("item");
	 shippingRequest.setShippingAddress("address");
	 shippingRequest.setShippingMethod("method");
	 shippingRequest.setInsuranceAmount(100.50);
	 
	 ObjectMessage om = context.createObjectMessage();
	 om.setObject(shippingRequest);
	 
	 JMSProducer producer = context.createProducer();
	 producer.send(destination, om);
	 */
 }
 
 private void insideEEEnvironment_MessageConsumer(){
	/* 
	         @MessageDriven(activationConfig = {
			      @ActivationConfigProperty(propertyName = "destinationType",
			                           propertyValue = "javax.jms.Queue"),
			      @ActivationConfigProperty(propertyName = "destinationLookup",
			                           propertyValue = "jms/ShippingRequestQueue")
			 })
			 public class TurtleShippingRequestMessageBean implements MessageListener{
				 @PersistenceContext()
				 EntityManager entityManager;
				 
				 @Override
				 public void onMessage(Message message) {
					 try {
						 ObjectMessage om = (ObjectMessage) message;
						 Object o = om.getObject();
						 ActionBazaarShippingRequest sr = (ActionBazaarShippingRequest) o;
						 
						 Logger.getLogger(TurtleShippingRequestMessageBean.class.getName())
						                 .log(Level.INFO, String.format("Got message: %s", sr));
						 
						 TurtleShippingRequest tr = new TurtleShippingRequest();
						 tr.setInsuranceAmount(sr.getInsuranceAmount());
						 tr.setItem(sr.getItem());
						 tr.setShippingAddress(sr.getShippingAddress());
						 tr.setShippingMethod(sr.getShippingMethod());
						 
						 entityManager.persist(tr);
						 
				     } catch (JMSException ex) {
						 Logger.getLogger(TurtleShippingRequestMessageBean.class.getName())
						 .log(Level.SEVERE, null, ex);
					 }
				 }
			 }
   */			 
 }
 
 /**
  * This is for JMS 1.x, more sophisticated than for JMS 2.x
  * @param str
  * @throws JMSException
  */
 private void sendJMS_1x(String str) throws JMSException{
	 TextMessage tm = session.createTextMessage(str);
	 
	 //you can also create any type of Message, such as ObjectMessage, like below:
	 //    ObjectMessage om = session.createObjectMessage();
	 //    om.setObject(anyObject);
	 //    messageProducer.send(om);
	 
	 messageProducer.send(tm);
	 connection.close();
 }

 /**
  * This for JMS 2.x, used together with the block for JAMS 2.x int he constructor
  * @param string
  * @throws JMSException
  */
 private void sendJMS_2x(String string) throws JMSException {
	 try(JMSContext context = connectionFactory.createContext("huy", "huy")){
		 JMSProducer p = context.createProducer();
		 
		 //you can create and send an ObjectMessage like this:
		 //	   ObjectMessage om = context.createObjectMessage();
		 //    om.setObject(anyObject)
		 //    p.send(destination, om);
		 //then on the receiver's side, cast Message to ObjectMessage, which in turn has a method getObject() to get the anyObject
		 

		 p.send(destination, string);
		 
		 
	 }catch (Exception e) {
		 e.printStackTrace();
  	}
 }
 public static void main(String[] args) throws NamingException, JMSException {
  Sender s = new Sender();
  s.sendJMS_1x("How are you today?!");
 
 }


}