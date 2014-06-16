package org.campagnelab.mercury.api;

import org.campagnelab.mercury.api.wrappers.MessageToSendWrapper;

import javax.jms.*;
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

    public void publishTextMessage(MessageToSendWrapper<String> message) throws Exception{
        TextMessage tm = this.session.createTextMessage(message.getPayload());
        this.producer.send(tm);
        this.send(tm,message);
    }

    public void publishObjectMessage(MessageToSendWrapper<Serializable> message) throws Exception{
        ObjectMessage om = this.session.createObjectMessage();
        om.setObject(message.getPayload());
        this.send(om, message);
    }

    private void send(Message message, MessageToSendWrapper<?> messageWrapper) throws Exception{
        if (messageWrapper.getTimeToLiveInMs() > 0)
            this.producer.send(message, messageWrapper.getDeliveryMode(), messageWrapper.getPriority(), messageWrapper.getTimeToLiveInMs());
        else
            this.producer.send(message, messageWrapper.getDeliveryMode(), messageWrapper.getPriority(), messageWrapper.getTimeToLiveInMs());
    }

    public void close() throws Exception {
        this.producer.close();
    }

}
