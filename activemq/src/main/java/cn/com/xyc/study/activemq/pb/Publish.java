package cn.com.xyc.study.activemq.pb;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Publish {
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;

    public Publish() {
        try {
            this.factory = new ActiveMQConnectionFactory("admin", "123456", "tcp://192.168.2.102:61616");
            this.connection = factory.createConnection();
            this.connection.start();
            this.session = this.connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            this.producer = this.session.createProducer(null);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() throws JMSException {
        Destination destination = this.session.createTopic("topic1");
        TextMessage message = session.createTextMessage("我是内容");
        producer.send(destination, message);
    }

    public static void main(String[] args) throws JMSException {
        Publish publish = new Publish();
        publish.sendMessage();
    }
}
