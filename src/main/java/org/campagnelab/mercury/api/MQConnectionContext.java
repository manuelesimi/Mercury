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

    private Context context;

    protected MQConnectionContext(Properties properties) throws Exception{
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
