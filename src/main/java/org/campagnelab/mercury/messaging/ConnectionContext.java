package org.campagnelab.mercury.messaging;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Load the connection context from the given properties.
 *
 * @author manuele
 */
class ConnectionContext {

    private static final String CONNECTION_FACTORY_NAME = "JMSFactory";

    private static final String DESTINATION_NAME = "queue/simple";

    private Context context;

    protected ConnectionContext(Properties properties) throws Exception{
        this.context = new InitialContext(properties);
    }

    protected Connection getConnection() throws Exception {
        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
        return connectionFactory.createConnection();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        context.close();
    }
}
