package org.campagnelab.mercury.api;

import javax.jms.*;

/**
 * A durable topic is a topic that has at least one durable subscriber.
 * Having a durable subscriber is the condition for the broker to persist messages published in the topic.
 * @author manuele
 */
class DurableTopic {

    private Topic topic;

    protected DurableTopic(String name, TopicSession tsession, MQConnectionContext context) throws Exception {
        this.topic = tsession.createTopic(name);
        this.createDefaultSubscriber(context);
    }

    private void createDefaultSubscriber(MQConnectionContext context) throws Exception {
        TopicConnection temporaryConnection = context.getTopicConnection("DefaultConsumer");
        temporaryConnection.start();
        temporaryConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE).createDurableSubscriber(topic, "DefaultConsumer");
        temporaryConnection.close();

    }

    protected Topic getTopic() {
        return topic;
    }

}
