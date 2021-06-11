package com.bakti.tutorial.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;


public class Send {
    private final static String EXCHANGE_NAME = "logs";
    private final static String QUEUE1 = "queue1";
    private final static String QUEUE2 = "queue2";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.queueBind(QUEUE1, EXCHANGE_NAME, "");
            channel.queueBind(QUEUE2, EXCHANGE_NAME, "");

            String message = "Hello Bakti!";
            for(int i=0;i<10;i++){
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }
}
