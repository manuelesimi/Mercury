package org.campagnelab.mercury.messaging;

import javax.jms.*;
import java.io.Serializable;

/**
 * A Topic consumer.
 * Instances of this consumer can read messages only from the Topic with which they were created ({@link org.campagnelab.mercury.messaging.MQConnection})
 *
 * @author manuele
 */
public class TopicConsumer {

    final TopicSubscriber messageConsumer;
    final TopicSession session;

    protected TopicConsumer(TopicSubscriber consumer, TopicSession session) throws Exception {
       this.messageConsumer = consumer;
       this.session = session;
    }

    /**
     * Reads the next message available.
     * @return the message or null if no message is pending in the topic.
     * @throws Exception
     */
    public MessageWrapper<String> readTextMessage() throws Exception {
        Message originalMessage = messageConsumer.receive(1000);
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
        ObjectMessage message = (ObjectMessage)messageConsumer.receive();
        return new MessageWrapper<Serializable>(message.getObject());
    }

    /**
     * Gets the name of the topic to which this consumer was subscribed.
     * @return
     * @throws JMSException
     */
    public String getTopicName() throws JMSException {
        return this.messageConsumer.getTopic().getTopicName();
    }
}

