package org.campagnelab.mercury.messaging;

import javax.jms.*;

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

    public TextMessageWrapper readTextMessage() throws Exception {
        TextMessage message = (TextMessage)messageConsumer.receive();
        return new TextMessageWrapper(message.getText());
    }

    public ObjectMessageWrapper readObjectMessage() throws Exception {
        ObjectMessage message = (ObjectMessage)messageConsumer.receive();
        return new ObjectMessageWrapper(message.getObject());
    }
}
