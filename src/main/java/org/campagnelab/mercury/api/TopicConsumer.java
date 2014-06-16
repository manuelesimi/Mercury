package org.campagnelab.mercury.api;

import org.campagnelab.mercury.api.wrappers.ByteArray;
import org.campagnelab.mercury.api.wrappers.MessageWrapper;
import org.campagnelab.mercury.api.wrappers.ReceivedMessageWrapper;

import javax.jms.*;
import java.io.Serializable;

/**
 * A Topic consumer.
 * Instances of this consumer can read messages only from the Topic with which they were created ({@link MQTopicConnection})
 *
 * @author manuele
 */
public class TopicConsumer {

    private final TopicSubscriber subscriber;

    private final TopicSession session;

    /**
     * the timeout value (in milliseconds) for receiving messages
     */
    private static long TIMEOUT = 1000;

    protected TopicConsumer(TopicSubscriber consumer, TopicSession session) throws Exception {
       this.subscriber = consumer;
       this.session = session;
    }

    /** Receives the next message if one is immediately available.
     *
     * @return the next message produced for this message consumer, or
     * null if one is not available
     *
     * @exception Exception if the Consumer provider fails to receive the next
     *                         message due to some internal error.
     */
    public ReceivedMessageWrapper<?> readNextMessage() throws Exception {
        Message originalMessage = subscriber.receive(TIMEOUT);
        if (originalMessage == null)
            return null;
        if (originalMessage instanceof TextMessage) {
            ReceivedMessageWrapper<String> response = new ReceivedMessageWrapper<String>(originalMessage,
                    ((TextMessage)originalMessage).getText(), MessageWrapper.TYPE.TEXT);
            return response;
        } else if (originalMessage instanceof ObjectMessage) {
            ReceivedMessageWrapper<Serializable> response = new ReceivedMessageWrapper<Serializable>(originalMessage,
                    ((ObjectMessage) originalMessage).getObject(), MessageWrapper.TYPE.OBJECT);
            return response;
        } else if (originalMessage instanceof BytesMessage) {
            BytesMessage message = (BytesMessage) originalMessage;
            byte[] bytes = new byte[(int) message.getBodyLength()];
            message.readBytes(bytes);
            ReceivedMessageWrapper<ByteArray> response = new ReceivedMessageWrapper<ByteArray>(originalMessage,
                    new ByteArray(bytes), MessageWrapper.TYPE.BYTE_ARRAY);
            return response;
        }
        return null;
    }

    /**
     * Gets the name of the topic to which this consumer was subscribed.
     * @return
     * @throws JMSException
     */
    public String getTopicName() throws JMSException {
        return this.subscriber.getTopic().getTopicName();
    }


    public void close() throws Exception {
        this.subscriber.close();
    }
}

