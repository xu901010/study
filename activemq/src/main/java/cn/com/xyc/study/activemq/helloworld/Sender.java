package cn.com.xyc.study.activemq.helloworld;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {
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

        //使用事务的方式进行消息的发送
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

        //使用CLIENT端签收的方式
        //Session session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);
        //第四步：通过会话创建Destination对象，指的是一个客户端用来指定生产消息目标和消费来源的对象，
        // 在PTP模式中，Destination被称为Queue即队列；
        // 在Pub/Sub模式，Destination被称作Topic即主题。
        // 在程序中可以使用多个Queue和Topic
        Destination destination = session.createQueue("queue1");
        //第五步：我们需要通过Session对象创建消息的发送和接收对象(生产者和消费者)MessageProducer/MessageConsumer
        //MessageProducer producer = session.createProducer(destination);
        MessageProducer producer = session.createProducer(null);
        //第六步：我们可以使用MessageProducer的setDeliveryMode方法为其设置持久化和非持久特性(DeliveryMode)
        //producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        //第七步：最后我们使用JMS规范的TextMessage形式创建数据(通过Session对象)，并且MessageProducer的send方法发送数据，同理客户端使用
        for (int i = 5; i < 10; i++) {
            TextMessage message = session.createTextMessage();
            message.setText("我是消息内容,id为：" + i);
            //producer.send(message);
            //目的地、消息、是否持久化、优先级(0-9 0-4表示普通消息 5-9表示加急 默认为4)
            producer.send(destination,message,DeliveryMode.NON_PERSISTENT,i,1000*60*2);
        }
        //使用事务提交
        session.commit();
        if (connection != null) {
            //调用该方法，它内部会自动关闭它里面的连接或会话
            connection.close();
        }
    }
}
