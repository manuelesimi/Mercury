package org.campagnelab.mercury.api;

import javax.jms.*;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * A Topic consumer.
 * Instances of this consumer can read messages only from the Topic with which they were created ({@link MQTopicConnection})
 *
 * @author manuele
 */
public class TopicConsumer {

    private final TopicSubscriber subscriber;

    private final TopicSession session;

    /**
     * the timeout value (in milliseconds) for receiving messages
     */
    private static long TIMEOUT = 1000;

    protected TopicConsumer(TopicSubscriber consumer, TopicSession session) throws Exception {
       this.subscriber = consumer;
       this.session = session;
    }

    /**
     * Reads the next message available as text.
     * @return the message or null if no message is pending in the topic.
     * @throws Exception
     */
    public MessageWrapper<String> readTextMessage() throws Exception {
        Message originalMessage = subscriber.receive(TIMEOUT);
        if (originalMessage == null)
            return null;
        TextMessage message = (TextMessage)originalMessage;
        return new MessageWrapper<String>(message.getText());
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public MessageWrapper<Serializable> readObjectMessage() throws Exception {
        Message originalMessage = subscriber.receive(TIMEOUT);
        if (originalMessage == null)
            return null;
        ObjectMessage message = (ObjectMessage) originalMessage;
        return new MessageWrapper<Serializable>(message.getObject());
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public MessageWrapper<ByteBuffer> readBytesMessage() throws Exception {
        Message originalMessage = subscriber.receive(TIMEOUT);
        if (originalMessage == null)
            return null;
        BytesMessage message = (BytesMessage) originalMessage;
        ByteBuffer buffer = ByteBuffer.allocate(Long.valueOf(message.getBodyLength()).intValue());
        byte[] array = new byte[1024];
        while (message.readBytes(array) !=-1 ){
            buffer.put(array);
        }
        return new MessageWrapper<ByteBuffer>(buffer);
    }

    /**
     * Gets the name of the topic to which this consumer was subscribed.
     * @return
     * @throws JMSException
     */
    public String getTopicName() throws JMSException {
        return this.subscriber.getTopic().getTopicName();
    }


    public void close() throws Exception {
        this.subscriber.close();
    }
}

