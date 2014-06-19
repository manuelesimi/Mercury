package org.campagnelab.mercury.api;

import org.campagnelab.mercury.api.wrappers.ByteArray;
import org.campagnelab.mercury.api.wrappers.MessageToSendWrapper;
import org.campagnelab.mercury.api.wrappers.MessageWithPBAttachmentToSend;

import javax.jms.*;
import java.io.Serializable;
import java.util.Map;

/**
 * A producer bound to a Topic.
 * Instances of this producer can send messages only to the topic with which they were created ({@link MQTopicConnection})
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
    public void publishTextMessage(MessageToSendWrapper<String> message) throws Exception {
        TextMessage tm = this.session.createTextMessage(message.getPayload());
        this.publish(tm,message);
    }

    public void publishObjectMessage(MessageToSendWrapper<Serializable> message) throws Exception{
        ObjectMessage om = this.session.createObjectMessage();
        om.setObject(message.getPayload());
        this.publish(om,message);
    }

    public void publishBytesMessage(MessageToSendWrapper<ByteArray> message) throws Exception{
        BytesMessage bm = this.session.createBytesMessage();
        bm.writeBytes(message.getPayload().getArray());
        this.publish(bm,message);
    }

    public void publishPBMessage(MessageWithPBAttachmentToSend message) throws Exception{
        this.publishBytesMessage(message);
    }


    private void publish(Message message, MessageToSendWrapper<?> messageWrapper) throws Exception{
        if (messageWrapper.hasProperties()) {
            for (Map.Entry<String,String> prop : messageWrapper.getProperties().entrySet())
              message.setStringProperty(prop.getKey(), prop.getValue());
        }
        if (messageWrapper.getTimeToLiveInMs() > 0)
            this.publisher.publish(message,messageWrapper.getDeliveryMode(),messageWrapper.getPriority(),messageWrapper.getTimeToLiveInMs());
        else
            this.publisher.publish(message,messageWrapper.getDeliveryMode(),messageWrapper.getPriority(),messageWrapper.getTimeToLiveInMs());
    }
    /**
     * Gets the name of the topic to which this producer can publish.
     * @return
     * @throws JMSException
     */
    public String getTopicName() throws JMSException {
        return this.publisher.getTopic().getTopicName();
    }

    public void close() throws Exception {
        this.publisher.close();
    }
}
