package cn.com.xyc.study.activemq.helloworld;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Receiver {
    public static void main(String[] args) throws JMSException {
        //第一步：建立ConnectionFactory工厂对象
        /*ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://192.168.2.102:61616");*/
        ConnectionFactory factory = new ActiveMQConnectionFactory("admin",
                "123456",
                "tcp://192.168.2.102:61616");
        //第二步：通过工厂对象创建一个连接,并且调用Connection的start方法开启连接，Connection默认是关闭的
        Connection connection = factory.createConnection();
        connection.start();
        //第三步：通过连接创建会话(上下文对象)，用于接收消息，参数配置1为是否启用事务，参数配置2为签收模式,一般设置自动签收
        //Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

        //使用CLIENT签收方式
        Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);

        //第四步：通过会话创建Destination对象，指的是一个客户端用来指定生产消息目标和消费来源的对象，
        // 在PTP模式中，Destination被称为Queue即队列；
        // 在Pub/Sub模式，Destination被称作Topic即主题。
        // 在程序中可以使用多个Queue和Topic
        Destination destination = session.createQueue("queue1");
        //第五步：我们需要通过Session对象创建消息的发送和接收对象(生产者和消费者)MessageProducer/MessageConsumer
        MessageConsumer consumer = session.createConsumer(destination);
        //第六步：
        while (true) {
            TextMessage msg = (TextMessage) consumer.receive();
            //手工去签收消息，另启一个线程，去通知我们的MQ服务确认签收
            msg.acknowledge();

            if (msg == null) {
                break;
            }
            System.out.println("收到的内容：" + msg.getText());
        }
        if (connection != null) {
            //调用该方法，它内部会自动关闭它里面的连接或会话
            connection.close();
        }

    }
}
