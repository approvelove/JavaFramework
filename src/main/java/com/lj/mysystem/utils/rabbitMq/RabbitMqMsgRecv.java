package com.lj.mysystem.utils.rabbitMq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class RabbitMqMsgRecv {
    private final static String QUEUE_NAME = "q_test_01";
    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = RabbitMqConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //定义队列的消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("Received: " + message);
                //消息确认
                try {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        //关闭自动消息确认，autoAck = false
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
