package org.campagnelab.mercury.messaging;

import org.campagnelab.mercury.messaging.MQConnection;

import javax.jms.*;
import java.io.Serializable;

/**
 * Created by mas2182 on 6/10/14.
 */
public class Consumer {

    private MessageConsumer messageConsumer;

    private MQConnection connection;

    public Consumer(MQConnection connection, Queue queue) throws Exception {
        this.connection = connection;
        this.messageConsumer = this.connection.getSession().createConsumer(queue);
    }

    public String readTextMessage() throws Exception {
        TextMessage message = (TextMessage)messageConsumer.receive();
        return message.getText();
    }

    public Serializable readObjectMessage() throws Exception {
        ObjectMessage message = (ObjectMessage)messageConsumer.receive();
        return message.getObject();
    }
}
