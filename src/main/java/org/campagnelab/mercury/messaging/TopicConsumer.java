package org.campagnelab.mercury.messaging;

import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

/**
 * Created by mas2182 on 6/11/14.
 */
public class TopicConsumer {

    final TopicSubscriber consumer;
    final TopicSession session;

    protected TopicConsumer(TopicSubscriber consumer, TopicSession session) throws Exception {
       this.consumer = consumer;
       this.session = session;
    }
}
