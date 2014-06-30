package org.campagnelab.mercury.api;

import org.campagnelab.mercury.api.wrappers.ByteArray;
import org.campagnelab.mercury.api.wrappers.MessageToSendWrapper;
import org.campagnelab.mercury.api.wrappers.MessageWithPBAttachmentToSend;

import javax.jms.*;
import java.io.Serializable;
import java.util.Map;

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

    public void publishBytesMessage(MessageToSendWrapper<ByteArray> message) throws Exception{
        BytesMessage bm = this.session.createBytesMessage();
        bm.writeBytes(message.getPayload().getArray());
        this.send(bm, message);
    }

    public void publishPBMessage(MessageWithPBAttachmentToSend message) throws Exception {
       this.publishBytesMessage(message);
    }


    private void send(Message message, MessageToSendWrapper<?> messageWrapper) throws Exception{
        if (messageWrapper.hasProperties()) {
            for (Map.Entry<String,String> prop : messageWrapper.getProperties().entrySet())
                message.setStringProperty(prop.getKey(), prop.getValue());
        }
        message.setJMSDeliveryMode(messageWrapper.getDeliveryMode());

        if (messageWrapper.getTimeToLiveInMs() > 0)
            this.producer.send(message, messageWrapper.getDeliveryMode(), messageWrapper.getPriority(), messageWrapper.getTimeToLiveInMs());
        else
            this.producer.send(message, messageWrapper.getDeliveryMode(), messageWrapper.getPriority(), messageWrapper.getTimeToLiveInMs());
    }

    public void close() throws Exception {
        this.producer.close();
    }

}
