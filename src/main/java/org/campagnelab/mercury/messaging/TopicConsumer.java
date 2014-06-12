package org.campagnelab.mercury.messaging;

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

    /**
     * Reads the next message available.
     * @return the message or null if no message is pending in the topic.
     * @throws Exception
     */
    public MessageWrapper<String> readTextMessage() throws Exception {
        Message originalMessage = subscriber.receive(TIMEOUT);
        if (originalMessage == null)
            return null;
        TextMessage message = (TextMessage)originalMessage;
        return new MessageWrapper<String>(message.getText());
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public MessageWrapper<Serializable> readObjectMessage() throws Exception {
        ObjectMessage message = (ObjectMessage) subscriber.receive(TIMEOUT);
        return new MessageWrapper<Serializable>(message.getObject());
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

