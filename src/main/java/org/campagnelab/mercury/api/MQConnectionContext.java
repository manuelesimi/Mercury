package org.campagnelab.mercury.api;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

/**
 * Load the connection context from the given properties.
 *
 * @author manuele
 */
class MQConnectionContext {

    private static final String CONNECTION_FACTORY_NAME = "JMSFactory";

    private static final String CONNECTION_PROPERTY_NAME = "connectionfactory." + CONNECTION_FACTORY_NAME;

    private Context context;

    protected MQConnectionContext(String hostname, int port, Properties properties) throws Exception{
        String connectionString = properties.getProperty(CONNECTION_PROPERTY_NAME);
        connectionString = connectionString.replaceAll("%%hostname%%", hostname).replaceAll("%%port%%", new Integer(port).toString());
        properties.setProperty(CONNECTION_PROPERTY_NAME, connectionString);
        this.context = new InitialContext(properties);
    }

    protected Connection getConnection() throws Exception {
        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
        return connectionFactory.createConnection();
    }

    protected TopicConnection getTopicConnection() throws Exception {
        TopicConnectionFactory connectionFactory = (TopicConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
        return connectionFactory.createTopicConnection();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        context.close();
    }
}
