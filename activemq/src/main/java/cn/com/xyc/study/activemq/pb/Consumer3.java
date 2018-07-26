package cn.com.xyc.study.activemq.pb;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer3 {
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public Consumer3(){
        try {
            factory = new ActiveMQConnectionFactory("admin","123456","tcp://192.168.2.102:61616");
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void receive() throws JMSException {
        Destination destination = session.createTopic("topic1");
        consumer = session.createConsumer(destination);
        consumer.setMessageListener(new Listener());
    }

    class Listener implements MessageListener{

        @Override
        public void onMessage(Message message) {
            if (message instanceof TextMessage){
                System.out.println("c3收到消息---------");
                try {
                    System.out.println(((TextMessage)message).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws JMSException {
        Consumer3 a1 = new Consumer3();
        a1.receive();
    }
}
