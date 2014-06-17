package org.campagnelab.mercury.api;

import org.apache.log4j.Logger;

import javax.jms.*;
import java.util.Properties;

/**
 * A connection with the messaging broker that works with {@link javax.jms.Topic}s.
 *
 * @author manuele
 */
public class MQTopicConnection {

    protected static final Logger logger = Logger.getLogger(MQTopicConnection.class);

    private final TopicConnection tconnection;

    private final TopicSession tsession;

    /**
     * Opens a new connection with the messaging broker
     * @param hostname
     * @param port
     * @throws Exception
     */
    public MQTopicConnection(String hostname, int port) throws Exception {
        logger.info(String.format("Opening a new Topic connection with %s:%d" , hostname, port));
        Properties properties = new Properties();
        properties.load(MQTopicConnection.class.getResourceAsStream("/connection.properties"));
        MQConnectionContext context = new MQConnectionContext(hostname, port, properties);
        this.tconnection = context.getTopicConnection();
        this.tconnection.start();
        this.tsession = tconnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    }


    /**
     * Opens a new topic in the message broker. If the topic does not exist, it is created.
     *
     * @param topicName the name to assign to the topic
     * @return the topic
     * @throws JMSException
     */
    public Topic openTopic(String topicName) throws JMSException {
        return this.tsession.createTopic(topicName);
    }

    /**
     * Creates a new consumer for messages published in the topic.
     * @param topic        the topic from which the consumer will receive messages.
     * @param consumerName the name to assign to the consumer.
     * @param durable      if true, the JMS provider retains a record of the consumer and insures that all messages from the topic's
     *                     publishers are retained until they are acknowledged by the durable consumer or they have expired.
     * @return the topic consumer
     * @throws Exception
     */
    public TopicConsumer createConsumer(Topic topic, String consumerName, boolean durable) throws Exception {
        if (durable)
            return new TopicConsumer(tsession.createDurableSubscriber(topic, consumerName), tsession);
        else
            return new TopicConsumer(tsession.createSubscriber(topic), tsession);
    }

    /**
     * Creates a new producer able to publish in the topic.
     * @param topic the topic in which the producer will publish messages
     * @return  the topci producer
     * @throws Exception
     */
    public TopicProducer createProducer(Topic topic) throws Exception {
        return new TopicProducer(tsession.createPublisher(topic), tsession);

    }

    /**
     * @return
     */
    public Session getSession() {
        return this.tsession;
    }

    /**
     * Closes the connection with the broker.
     *
     * @throws Exception
     */
    public void close() throws Exception {
        this.tsession.close();
        this.tconnection.close();
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.close();
    }

}
