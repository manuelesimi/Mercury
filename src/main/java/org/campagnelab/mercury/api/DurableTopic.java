package org.campagnelab.mercury.api;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSession;

/**
 * A durable topic is a topic that has at least one durable subscriber.
 * Having a durable subscriber is the condition for the broker to persist messages published in the topic.
 * @author manuele
 */
class DurableTopic {

    protected Topic topic;

    private TopicSession tsession;

    protected DurableTopic(String name, TopicSession tsession) throws JMSException {
        this.tsession = tsession;
        this.topic = tsession.createTopic(name);
        this.createDefaultSubscriber(name);
    }

    private void createDefaultSubscriber(String name) throws JMSException {
        tsession.createDurableSubscriber(topic, "DefaultConsumer");
    }

    protected Topic getTopic() {
        return topic;
    }

}
