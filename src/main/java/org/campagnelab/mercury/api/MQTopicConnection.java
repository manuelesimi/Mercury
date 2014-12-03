package org.campagnelab.mercury.api;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.web.RemoteJMXBrokerFacade;
import org.apache.activemq.web.config.SystemPropertiesConfiguration;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.io.File;
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

    private final MQConnectionContext context;

    private final String hostname;

    /**
     * Opens a new connection with the messaging broker
     * @param hostname
     * @param port
     * @throws Exception
     */
    public MQTopicConnection(String hostname, int port, File template, String ... name) throws Exception {
        this.hostname = hostname;
        logger.info(String.format("Opening a new Topic connection with %s:%d" , hostname, port));
        this.context = new MQConnectionContext(hostname, port, template);
        this.tconnection = (name != null && name.length>0) ?context.getTopicConnection(name[0]) : context.getTopicConnection();
        this.tconnection.start();
        this.tsession = tconnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    /**
     * Opens a new connection with the messaging broker
     * @param hostname
     * @param port
     * @throws Exception
     */
    public MQTopicConnection(String hostname, int port, String ... name) throws Exception {
        this.hostname = hostname;
        logger.info(String.format("Opening a new Topic connection with %s:%d" , hostname, port));
        Properties properties = new Properties();
        properties.load(MQTopicConnection.class.getResourceAsStream("/mercury.properties"));
        this.context = new MQConnectionContext(hostname, port, properties);
        this.tconnection = (name != null && name.length>0) ?context.getTopicConnection(name[0]) : context.getTopicConnection();
        this.tconnection.start();
        this.tsession = tconnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    }


    /**
     * Opens a new topic in the message broker to publish/receive messages. If the topic does not exist, it is created.
     *
     * @param topicName the name to assign to the topic
     * @return the topic
     * @throws JMSException
     */
    public Topic openTopic(String topicName) throws Exception {
        return new DurableTopic(topicName+"?consumer.retroactive=true", this.tsession, this.context).getTopic(); //see http://activemq.apache.org/retroactive-consumer.html
    }

    /**
     * Opens a new topic in the message broker to receive messages. If the topic does not exist, it is created.
     *
     * @param topicName the name to assign to the topic
     * @return the topic
     * @throws JMSException
     */
    public Topic openConsumerTopic(String topicName) throws JMSException {
        return this.tsession.createTopic(topicName + "?consumer.retroactive=true"); //see http://activemq.apache.org/retroactive-consumer.html
    }

    /**
     * Creates a consumer for messages published in the topic able to read messages in a synchronous way.
     * @param topic        the topic from which the consumer will receive messages.
     * @param consumerName the name to assign to the consumer.
     * @param durable      if true, the JMS provider retains a record of the consumer and insures that all messages from the topic's
     *                     publishers are retained until they are acknowledged by the durable consumer or they have expired.
     * @return the topic consumer
     * @throws Exception
     */
    public TopicConsumer createSyncConsumer(Topic topic, String consumerName, boolean durable) throws Exception {
        if (durable)
            return new TopicConsumer(tsession.createDurableSubscriber(topic, consumerName), tsession);
        else
            return new TopicConsumer(tsession.createSubscriber(topic), tsession);
    }

    /**
     * Creates a consumer for messages published in the topic able to be notified in an asynchronous way.
     * @param topic        the topic from which the consumer will receive messages.
     * @param consumerName the name to assign to the consumer.
     * @param durable      if true, the JMS provider retains a record of the consumer and insures that all messages from the topic's
     *                     publishers are retained until they are acknowledged by the durable consumer or they have expired.
     * @param listener the listener that will receive notifications about new messages delivered by the broker.
     * @return the topic consumer
     * @throws Exception
     */
    public TopicConsumer createAsyncConsumer(Topic topic, String consumerName, boolean durable, ReceivedMessageListener listener) throws Exception {
        TopicConsumer consumer = this.createSyncConsumer(topic,consumerName,durable);
        consumer.setListener(listener);
        return consumer;
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
     * Deletes a Topic from the broker.
     * @param name the topic name
     * @return true if the topic was successfully deleted, false otherwise.
     */
    public boolean deleteTopic(String name) {
        try {
            RemoteJMXBrokerFacade brokerFacade = new RemoteJMXBrokerFacade();
            MercuryJMXConfiguration configuration = new MercuryJMXConfiguration(this.context);
            brokerFacade.setConfiguration(configuration);
            BrokerViewMBean brokerViewMBean = brokerFacade.getBrokerAdmin();
            brokerViewMBean.removeTopic(name);
        } catch (Exception e) {
            logger.error("Failed to delete topic " + name, e);
            return false;
        }
        logger.info(String.format("Topic %s successfully deleted", name));
        return true;
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
