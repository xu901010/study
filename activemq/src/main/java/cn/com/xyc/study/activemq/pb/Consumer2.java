package cn.com.xyc.study.activemq.pb;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer2 {
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public Consumer2(){
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
                System.out.println("c2收到消息---------");
                try {
                    System.out.println(((TextMessage)message).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws JMSException {
        Consumer2 a1 = new Consumer2();
        a1.receive();
    }
}
