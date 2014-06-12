package org.campagnelab.mercury.messaging;

import javax.jms.*;
import java.io.IOException;
import java.util.Properties;

/**
 * A connection with the message broker.
 *
 * @author manuele
 */
public class MQConnection {

    /**
     * A connection for publishing/consuming messages from queues.
     */
    private final Connection connection;

    /**
     * A connection for publishing/consuming messages from topics.
     */
    private final TopicConnection tconnection;

    private final TopicSession tsession;

    private final Session session;

    public MQConnection() throws Exception {
            Properties properties = new Properties();
            properties.load(MQConnection.class.getResourceAsStream("/connection.properties"));
            MQConnectionContext context = new MQConnectionContext(properties);
            this.connection = context.getConnection();
            connection.start();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            this.tconnection = context.getTopicConnection();
            this.tconnection.start();
            this.tsession = tconnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    /**
     *
     * @return
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * Creates a new queue in the message broker.
     * @param queueName the name to assign to the queue
     * @return the queue
     * @throws JMSException
     */
    public Queue createQueue(String queueName) throws JMSException {
        return this.session.createQueue(queueName);
    }

    /**
     * Creates a new topic in the message broker.
     * @param topicName the name to assign to the topic
     * @return the topci
     * @throws JMSException
     */
    public Topic createTopic(String topicName) throws JMSException {
        return this.tsession.createTopic(topicName);
    }

    public QueueConsumer createConsumer(Queue queue) throws Exception {
        return new QueueConsumer(session.createConsumer(queue),session);
    }

    /**
     *
     * @param topic the topic from which the consumer will receive messages.
     * @param consumerName the name to assign to the consumer.
     * @param durable if true, the JMS provider retains a record of the consumer and insures that all messages from the topic's
     * publishers are retained until they are acknowledged by the durable consumer or they have expired.
     * @return the topic consumer
     * @throws Exception
     */
    public TopicConsumer createConsumer(Topic topic, String consumerName, boolean durable) throws Exception {
        if (durable)
            return new TopicConsumer(tsession.createDurableSubscriber(topic, consumerName),tsession);
        else
            return new TopicConsumer(tsession.createSubscriber(topic),tsession);
    }

    public QueueProducer createProducer(Queue queue) throws Exception {
         return new QueueProducer(session.createProducer(queue),session);
    }

    public TopicProducer createProducer(Topic topic) throws Exception {
        return new TopicProducer(tsession.createPublisher(topic), tsession);

    }

    /**
     * Closes the connection with the broker.
     * @throws Exception
     */
    public void close() throws Exception {
        this.session.close();
        this.connection.close();
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.close();
    }

}
