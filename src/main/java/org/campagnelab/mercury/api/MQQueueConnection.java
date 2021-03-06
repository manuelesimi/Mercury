package org.campagnelab.mercury.api;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import java.io.File;
import java.util.Properties;


/**
 * A connection with the message broker that works with {@link javax.jms.Queue}s.
 *
 * @author manuele
 */
public class MQQueueConnection {

    /**
     * A connection for publishing/consuming messages from queues.
     */
    private final Connection connection;


    private final Session session;


    public MQQueueConnection(String hostname, int port, File template) throws Exception {
        MQConnectionContext context = new MQConnectionContext(hostname, port, template);
        this.connection = context.getConnection();
        connection.start();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    }

    public MQQueueConnection(String hostname, int port) throws Exception {
        Properties properties = new Properties();
        properties.load(MQTopicConnection.class.getResourceAsStream("/mercury.properties"));
        MQConnectionContext context = new MQConnectionContext(hostname, port, properties);
        this.connection = context.getConnection();
        connection.start();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    }

    /**
     * The current active session.
     *
     * @return the session
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * Opens a queue in the message broker. If the queue does not exist, it is created.
     *
     * @param queueName the name to assign to the queue
     * @return the queue
     * @throws javax.jms.JMSException
     */
    public Queue openQueue(String queueName) throws JMSException {
        return this.session.createQueue(queueName);
    }

    public QueueConsumer createConsumer(Queue queue) throws Exception {
        return new QueueConsumer(session.createConsumer(queue), session);
    }

    public QueueProducer createProducer(Queue queue) throws Exception {
        return new QueueProducer(session.createProducer(queue), session);
    }

    /**
     * Closes the connection with the broker.
     *
     * @throws Exception
     */
    public void close() throws Exception {
        this.session.close();
        this.connection.close();
    }

}
