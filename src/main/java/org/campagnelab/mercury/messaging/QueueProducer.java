package org.campagnelab.mercury.messaging;

import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.Serializable;

/**
 * Created by mas2182 on 6/11/14.
 */
public class QueueProducer {

    final MessageProducer producer;

    final Session session;

    protected QueueProducer(MessageProducer producer, Session session) throws Exception {
        this.producer = producer;
        this.session = session;
    }

    public void publishTextMessage(MessageWrapper<String> message) throws Exception{
        TextMessage tm = this.session.createTextMessage(message.getPayload());
        this.producer.send(tm);
    }

    public void publishObjectMessage(MessageWrapper<Serializable> message) throws Exception{
        ObjectMessage om = this.session.createObjectMessage();
        om.setObject(message.getPayload());
        this.producer.send(om);
    }

}
