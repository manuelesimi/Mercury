package org.campagnelab.mercury.messaging;

import javax.jms.*;
import java.io.Serializable;

/**
 * A producer bound to a Topic.
 * Instances of this producer can send messages only to the topic with which they were created ({@link org.campagnelab.mercury.messaging.MQConnection})
 *
 * @author manuele
 */
public class TopicProducer {

    final TopicPublisher publisher;

    final TopicSession session;

    protected TopicProducer(TopicPublisher publisher, TopicSession session) throws Exception {
        this.publisher = publisher;
        this.session = session;
    }

    /**
     * Publishes a text message to the topic.
     * @param message
     * @throws JMSException
     */
    public void publishTextMessage(MessageWrapper<String> message) throws JMSException {
        TextMessage tm = this.session.createTextMessage(message.getPayload());
        this.publisher.publish(tm);
    }

    public void publishObjectMessage(MessageWrapper<Serializable> message) throws Exception{
        ObjectMessage om = this.session.createObjectMessage();
        om.setObject(message.getPayload());
        this.publisher.publish(om);
    }
    /**
     * Gets the name of the topic to which this producer can publish.
     * @return
     * @throws JMSException
     */
    public String getTopicName() throws JMSException {
        return this.publisher.getTopic().getTopicName();
    }
}
