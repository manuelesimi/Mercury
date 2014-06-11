package org.campagnelab.mercury.messaging;

import org.campagnelab.mercury.messaging.wrappers.ObjectMessageWrapper;
import org.campagnelab.mercury.messaging.wrappers.TextMessageWrapper;

import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

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

    public void publishTextMessage(TextMessageWrapper message) throws Exception{
        TextMessage tm = this.session.createTextMessage(message.getMessageBody());
        this.producer.send(tm);
    }

    public void publishObjectMessage(ObjectMessageWrapper message) throws Exception{
        ObjectMessage om = this.session.createObjectMessage();
        om.setObject(message.getMessageBody());
        this.producer.send(om);
    }

}
