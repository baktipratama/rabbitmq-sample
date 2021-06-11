package com.bakti.tutorial.delay;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class Send {
    private final static String EXCHANGE_NAME = "delay_exchange";


    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("x-delayed-type", "direct");
            channel.exchangeDeclare(EXCHANGE_NAME, "x-delayed-message", true, false, args);

            Map map = new HashMap<String,Object>();
            map.put("x-delay", 10000);

            AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder();
            props.headers(map);

            String message = "Hello Bakti! Yo sorry the message is delayed";
            channel.basicPublish(EXCHANGE_NAME, "", props.build(), message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
