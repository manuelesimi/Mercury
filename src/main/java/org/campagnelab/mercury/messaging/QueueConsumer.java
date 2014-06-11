package org.campagnelab.mercury.messaging;

import org.campagnelab.mercury.messaging.wrappers.ObjectMessageWrapper;
import org.campagnelab.mercury.messaging.wrappers.TextMessageWrapper;

import javax.jms.*;

/**
 * Created by mas2182 on 6/11/14.
 */
public class QueueConsumer {

    final MessageConsumer messageConsumer;

    final Session session;

    protected QueueConsumer(MessageConsumer messageConsumer, Session session) throws Exception {
        this.messageConsumer = messageConsumer;
        this.session = session;
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
