package org.campagnelab.mercury.api;

import javax.jms.*;
import java.io.Serializable;
import java.nio.ByteBuffer;

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
    public void publishTextMessage(MessageWrapper<String> message) throws Exception {
        TextMessage tm = this.session.createTextMessage(message.getPayload());
        this.publish(tm,message);
    }

    public void publishObjectMessage(MessageWrapper<Serializable> message) throws Exception{
        ObjectMessage om = this.session.createObjectMessage();
        om.setObject(message.getPayload());
        this.publish(om,message);
    }

    public void publishBytesMessage(MessageWrapper<ByteBuffer> message) throws Exception{
        BytesMessage bm = this.session.createBytesMessage();
        bm.writeBytes(message.getPayload().array());
        this.publish(bm,message);
    }

    private void publish(Message message, MessageWrapper<?> messageWrapper) throws Exception{
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
