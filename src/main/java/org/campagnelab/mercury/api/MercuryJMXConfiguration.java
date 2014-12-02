package org.campagnelab.mercury.api;

import org.apache.activemq.web.config.AbstractConfiguration;

import javax.jms.ConnectionFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.NamingException;
import java.util.Collection;

/**
 * Configuration to access the
 */
public class MercuryJMXConfiguration extends AbstractConfiguration {

    private final MQConnectionContext context;

    public MercuryJMXConfiguration(MQConnectionContext context) {
        this.context = context;
    }

    @Override
    public ConnectionFactory getConnectionFactory() {
        try {
            return this.context.lookupConnectionFactory();
        } catch (NamingException e) {
            return null;
        }
    }

    @Override
    public Collection<JMXServiceURL> getJmxUrls() {
        try {
            String url = (String) this.context.getSourceContext().getEnvironment().get("jmxURL");
            Collection<JMXServiceURL> coll = this.makeJmxUrls(url);
            return coll;
        } catch (NamingException e) {
            return null;
        }
    }

    @Override
    public String getJmxUser() {
        /*try {
            return (String) this.context.getSourceContext().getEnvironment().get("userName");
        } catch (NamingException e) {
            return null;
        } */
        return null; //force to do not use SSL
    }

    @Override
    public String getJmxPassword() {
        /*try {
            return (String) this.context.getSourceContext().getEnvironment().get("password");
        } catch (NamingException e) {
            return null;
        } */
        return null;  //force to do not use SSL
    }
}
