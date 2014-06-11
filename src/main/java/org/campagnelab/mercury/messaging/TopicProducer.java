package org.campagnelab.mercury.messaging;

import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

/**
 * Created by mas2182 on 6/11/14.
 */
public class TopicProducer {

    final TopicPublisher publisher;

    final TopicSession tsession;

    protected TopicProducer(TopicPublisher publisher, TopicSession tsession) throws Exception {
        this.publisher = publisher;
        this.tsession = tsession;
    }
}
