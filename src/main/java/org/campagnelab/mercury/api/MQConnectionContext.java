package org.campagnelab.mercury.api;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Load the connection context from the given properties.
 *
 * @author manuele
 */
class MQConnectionContext {

    private static final String CONNECTION_FACTORY_NAME = "JMSFactory";

    private Context context;

    protected MQConnectionContext(String hostname, int port, File template) throws Exception {
        Properties properties = new Properties();
        String host_properties = this.buildJNDIFilename(hostname,port);
        File jndi = new File(template.getParent(), host_properties);
        if (jndi.exists()) {
            FileInputStream input = new FileInputStream(jndi);
            properties.load(input);
            input.close();
        } else {
            FileInputStream input = new FileInputStream(template);
            properties.load(input);
            jndi.createNewFile();
            OutputStream output = new FileOutputStream(jndi);
            // replace the properties value
            for (String name: properties.stringPropertyNames()) {
                String value = properties.getProperty(name);
                properties.setProperty(name,
                       properties.getProperty(name)
                        .replaceAll("%%HOSTNAME%%", hostname)
                        .replaceAll("%%PORT%%", new Integer(port).toString())
                        .replaceAll("%%TARGET_URL%%", jndi.getCanonicalPath())
                );
            }
            // save properties next to the template
            properties.store(output, template.getParent());
            output.close();
            input.close();
            properties.clear();
        }

        FileInputStream input = new FileInputStream(jndi);
        properties.load(input);
        input.close();
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

    private String buildJNDIFilename(String host, int port) {
          return String.format("jndi_for_%s_%d.properties", host,port);
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        context.close();
    }
}
