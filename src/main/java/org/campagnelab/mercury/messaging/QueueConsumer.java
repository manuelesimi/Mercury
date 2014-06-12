package org.campagnelab.mercury.messaging;

import javax.jms.*;
import java.io.Serializable;

/**
 * @author manuele
 */
public class QueueConsumer {

    final MessageConsumer messageConsumer;

    final Session session;

    protected QueueConsumer(MessageConsumer messageConsumer, Session session) throws Exception {
        this.messageConsumer = messageConsumer;
        this.session = session;
    }

    public MessageWrapper<String> readTextMessage() throws Exception {
        Message originalMessage = messageConsumer.receive(1000);
        if (originalMessage == null)
            return null;
        TextMessage message = (TextMessage)originalMessage;
        return new MessageWrapper<String>(message.getText());
    }

    public MessageWrapper<Serializable> readObjectMessage() throws Exception {
        ObjectMessage message = (ObjectMessage)messageConsumer.receive();
        return new MessageWrapper<Serializable>(message.getObject());
    }
}
