package cn.com.xyc.study.activemq.p2p;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer {
    //private final String SELECTOR_0 = "age > 25";
    private final String SELECTOR_1 = "color= 'blue'";
    private final String SELECTOR_2 = "color= 'blue' AND sal > 2000";
    private final String SELECTOR_3 = "receiver= 'A'";

    /**
     * 1 连接工厂
     */
    private ConnectionFactory factory;
    /**
     * 连接对象
     */
    private Connection connection;
    /**
     * Session对象
     */
    private Session session;
    /**
     * 消费者
     */
    private MessageConsumer messageConsumer;
    /**
     * 目标地址
     */
    private Destination destination;

    public Consumer() {
        try {
            this.factory = new ActiveMQConnectionFactory("admin", "123456", "tcp://192.168.2.102:61616");
            this.connection = this.factory.createConnection();
            this.connection.start();
            this.session = this.connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            this.destination = this.session.createQueue("first");
            this.messageConsumer = this.session.createConsumer(this.destination, SELECTOR_2);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void receiver() {
        try {
            this.messageConsumer.setMessageListener(new Listener());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    class Listener implements MessageListener {
        @Override
        public void onMessage(Message message) {
            try {
                if (message instanceof TextMessage) {

                }
                if (message instanceof MapMessage) {
                    MapMessage ret = (MapMessage) message;
                    System.out.println(ret.toString());
                    System.out.println(ret.getString("name"));
                    System.out.println(ret.getString("age"));

                }
                // ...
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        consumer.receiver();
    }
}
