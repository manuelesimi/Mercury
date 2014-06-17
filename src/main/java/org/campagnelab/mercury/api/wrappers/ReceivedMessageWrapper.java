package org.campagnelab.mercury.api.wrappers;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Map;

/**
 * Base wrapper for messages received from the broker.
 *
 * @author manuele
 */
public abstract class ReceivedMessageWrapper<PAYLOAD> extends MessageWrapper<PAYLOAD> {

    private final Message originalMessage;

    protected ReceivedMessageWrapper(Message message, PAYLOAD payload, MESSAGE_TYPE type) {
        super(payload, type);
        this.originalMessage = message;
    }

    @Override
    public String getProperty(String name) {
        try {
            if (this.originalMessage.propertyExists(name))
                return this.originalMessage.getStringProperty(name);
            else
                return this.properties.get(name);
        } catch (JMSException e) {
            return null;
        }
    }


    /**
     * Gets the properties associated to this message.
     *
     * @return
     */
    @Override
    public Map<String, String> getProperties() {
        throw new UnsupportedOperationException("Can't list properties from a received message");

    }

    /**
     * Checks if there are properties to associate with the message.
     *
     * @return
     */
    @Override
    public boolean hasProperties() {
        throw new UnsupportedOperationException("Can't list properties from a received message");
    }

}
