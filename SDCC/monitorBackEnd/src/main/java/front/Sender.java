package main.java.front;

/**
 * Author : Simone D'Aniello
 * Date :  13-Feb-18.
 */


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import main.java.Message;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.concurrent.TimeoutException;


@SuppressWarnings("ALL")
public class Sender {

    private String ID;

    public Sender(String ID){
        this.ID = ID;
    }

    public void sendMessage(String address, Message message, String queue, String topic) throws java.io.IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(address);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(queue, BuiltinExchangeType.DIRECT);
        System.out.println(" [x] Sent message with ID '" + message.getID() + "' by sender: " + ID);

        byte[] data = SerializationUtils.serialize((Serializable) message);
        channel.basicPublish(queue, topic, null, data);


        channel.close();
        connection.close();
    }

}