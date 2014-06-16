package org.campagnelab.mercury.api;

import org.campagnelab.mercury.api.wrappers.MessageToSendWrapper;
import org.campagnelab.mercury.api.wrappers.MessageWrapper;

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

    public MessageToSendWrapper<String> readTextMessage() throws Exception {
        Message originalMessage = messageConsumer.receive(1000);
        if (originalMessage == null)
            return null;
        TextMessage message = (TextMessage)originalMessage;
        return new MessageToSendWrapper<String>(message.getText(), MessageWrapper.TYPE.TEXT);
    }

    public MessageToSendWrapper<Serializable> readObjectMessage() throws Exception {
        ObjectMessage message = (ObjectMessage)messageConsumer.receive();
        return new MessageToSendWrapper<Serializable>(message.getObject(),MessageWrapper.TYPE.OBJECT);
    }
}
