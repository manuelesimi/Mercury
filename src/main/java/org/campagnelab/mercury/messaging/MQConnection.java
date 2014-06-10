package org.campagnelab.mercury.messaging;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

/**
 * A connection with the message broker.
 *
 * @author manuele
 */
public class MQConnection {


    private Connection connection;

    private Session session;

    private Queue queue;

    public MQConnection() {
        try {
            Properties properties = new Properties();
            properties.load(MQConnection.class.getResourceAsStream("/connection.properties"));
            ConnectionContext context = new ConnectionContext(properties);
            this.connection = context.getConnection();
            connection.start();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            this.queue = context.getDefaultQueue();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public Session getSession() {
        return this.session;
    }

    public Queue getDefaultQueue() {
        return this.queue;
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
