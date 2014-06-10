package org.campagnelab.mercury.messaging;

import org.campagnelab.mercury.messaging.MQConnection;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import java.io.Serializable;

/**
 * Created by mas2182 on 6/10/14.
 */
public class Consumer {

    private MessageConsumer messageConsumer;

    private MQConnection connection;

    public Consumer(MQConnection connection) throws Exception {
        this.connection = connection;
        this.messageConsumer = connection.getSession().createConsumer(connection.getDefaultQueue().getDestination());
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
