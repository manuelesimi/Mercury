package org.campagnelab.mercury.api;

import javax.jms.*;
import java.util.UUID;

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
        String defaultConsumer = "Consumer-" + UUID.randomUUID().toString();
        TopicConnection temporaryConnection = context.getTopicConnection(defaultConsumer);
        temporaryConnection.start();
        temporaryConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE).createDurableSubscriber(topic, defaultConsumer);
        temporaryConnection.close();

    }

    protected Topic getTopic() {
        return topic;
    }

}
