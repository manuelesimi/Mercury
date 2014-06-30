package org.campagnelab.mercury.api;

import org.campagnelab.mercury.api.wrappers.*;

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
        return new MessageToSendWrapper<String>(message.getText(), MESSAGE_TYPE.TEXT);
    }

    public MessageToSendWrapper<Serializable> readObjectMessage() throws Exception {
        ObjectMessage message = (ObjectMessage)messageConsumer.receive();
        return new MessageToSendWrapper<Serializable>(message.getObject(), MESSAGE_TYPE.OBJECT);
    }

    /** Receives the next message if one is immediately available.
     *
     * @return the next message produced for this message consumer, or
     * null if one is not available or the type of the message can't be handled
     *
     * @exception Exception if the Consumer provider fails to receive the next
     *                         message due to some internal error.
     */
    public ReceivedMessageWrapper readNextMessage() throws Exception {
        Message originalMessage = messageConsumer.receive(1000);
        if (originalMessage == null)
            return null;
        if (originalMessage instanceof TextMessage) {
            return new ReceivedTextMessage((TextMessage) originalMessage);
        } else if (originalMessage instanceof ObjectMessage) {
            return new ReceivedObjectMessage((ObjectMessage)originalMessage);
        } else if (originalMessage instanceof BytesMessage) {
            if (this.getMessageType(originalMessage) == MESSAGE_TYPE.PB_CLASS) {
                return new ReceivedMessageWithPBAttachment((BytesMessage) originalMessage);
            } else
                return new ReceivedByteArrayMessage((BytesMessage) originalMessage);
        }
        return null;
    }

    /**
     * Gets the type of the message.
     * @return the type
     */
    public MESSAGE_TYPE getMessageType(Message message) throws JMSException {
        return MESSAGE_TYPE.valueOf(message.getStringProperty(MESSAGE_PROPERTIES.MESSAGE_TYPE.name()));
    }

    public void close() {
        try {
            this.messageConsumer.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
