My own code, easier and more organized.
Demo of using a topic with JMS 1.0
It is very similar for queue. Just instead of using Topic, use queue. For example:
  TopicConnection -> QueueConnection
  TopicSession -> QueueSession 
  etc.
  
Note that I added two subscribers here to demonstrate that both of them get the message from the publisher.
In case of queue, only one receiver can get a message (seems like round-robin style)  
