package org.campagnelab.mercury.api;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.*;
import java.util.Properties;

/**
 * Load the connection context from the given properties.
 *
 * @author manuele
 */
class MQConnectionContext {

    private static final String CONNECTION_FACTORY_NAME = "ConnectionFactory";

    private InitialContext context;

    private final String hostname;

    private final int port;

    protected MQConnectionContext(String hostname, int port, Properties template) throws Exception {
        this.hostname = hostname;
        this.port = port;
        String host_properties = this.buildJNDIFilename(hostname,port);
        File jndi = new File( System.getProperty("java.io.tmpdir"), host_properties);
        this.initialize(hostname,port,jndi,template);
    }

    protected MQConnectionContext(String hostname, int port, File template) throws Exception {
        this.hostname = hostname;
        this.port = port;
        Properties properties = new Properties();
        String host_properties = this.buildJNDIFilename(hostname,port);
        File jndi = new File(template.getParent(), host_properties);
        FileInputStream input = new FileInputStream(template);
        properties.load(input);
        input.close();
        this.initialize(hostname,port,jndi,properties);
    }

    private void initialize(String hostname, int port, File jndi, Properties properties)  throws Exception {
        if (jndi.exists()) {
            FileInputStream input = new FileInputStream(jndi);
            properties.load(input);
            input.close();
        } else {
            jndi.createNewFile();
            OutputStream output = new FileOutputStream(jndi);
            // replace the properties value
            for (String name: properties.stringPropertyNames()) {
                String value = properties.getProperty(name);
                properties.setProperty(name,
                        properties.getProperty(name)
                                .replaceAll("%%HOSTNAME%%", hostname)
                                .replaceAll("%%PORT%%", new Integer(port).toString())
                );
            }
            // save properties next to the template
            properties.store(output, jndi.getParent());
            output.close();
            properties.clear();
        }
        FileInputStream input = new FileInputStream(jndi);
        properties.load(input);
        input.close();
        this.context = new InitialContext(properties);
    }

    /**
     * Gets a queue connection with a default name.
     * @return
     * @throws Exception
     */
    protected Connection getConnection() throws Exception {
        ConnectionFactory connectionFactory = this.lookupConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("MercuryAPI" + System.currentTimeMillis());
        return connection;
    }

    /**
     * Looks up for the connection factory.
     * @return
     * @throws NamingException
     */
    protected ConnectionFactory lookupConnectionFactory() throws NamingException {
      return (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
    }

    /**
     * Ges a queue connection with the client ID specified in the parameter.
     * @param name
     * @return
     * @throws Exception
     */
    protected Connection getConnection(String name) throws Exception {
        ConnectionFactory connectionFactory = this.lookupConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        connection.setClientID(name);
        return connection;
    }
    /**
     * Gets a topic connection with a default name.
     * @return
     * @throws Exception
     */
    protected TopicConnection getTopicConnection() throws Exception {
        TopicConnectionFactory connectionFactory = (TopicConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
        TopicConnection connection = connectionFactory.createTopicConnection();
        connection.setClientID("MercuryAPI" + System.currentTimeMillis());
        return connection;
    }

    protected InitialContext getSourceContext() {
        return context;
    }

    protected String getHostname() {
        return hostname;
    }

    protected int getConnectionPort() {
        return port;
    }

    /**
     * Gets a topic connection with the client ID specified in the parameter.
     * @param name
     * @return
     * @throws Exception
     */
    protected TopicConnection getTopicConnection(String name) throws Exception {
        TopicConnectionFactory connectionFactory = (TopicConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
        TopicConnection connection = connectionFactory.createTopicConnection();
        connection.setClientID(name);
        return connection;
    }

    private String buildJNDIFilename(String host, int port) {
          return String.format("jndi_for_%s_%d.properties", host,port);
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        context.close();
    }
}
