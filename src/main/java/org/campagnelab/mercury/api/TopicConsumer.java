package org.campagnelab.mercury.api;

import org.campagnelab.mercury.api.wrappers.*;

import javax.jms.*;

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
     * null if one is not available or the type of the message can't be handled
     *
     * @exception Exception if the Consumer provider fails to receive the next
     *                         message due to some internal error.
     */
    public ReceivedMessage readNextMessage() throws Exception {
        Message originalMessage = subscriber.receive(TIMEOUT);
        if (originalMessage == null)
            return null;
        if (originalMessage instanceof TextMessage) {
            return new ReceivedTextMessage((TextMessage) originalMessage);
        } else if (originalMessage instanceof ObjectMessage) {
            return new ReceivedObjectMessage((ObjectMessage)originalMessage);
        } else if (originalMessage instanceof BytesMessage) {
            return new ReceivedByteArrayMessage((BytesMessage) originalMessage);
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

