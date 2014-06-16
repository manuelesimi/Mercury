package org.campagnelab.mercury.api.wrappers;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Map;

/**
 * Created by mas2182 on 6/16/14.
 */
public abstract class ReceivedMessage<PAYLOAD> extends MessageWrapper<PAYLOAD> {

    private final Message originalMessage;

    protected ReceivedMessage(Message message, PAYLOAD payload, TYPE type) {
        super(payload, type);
        this.originalMessage = message;
    }

    @Override
    public String getProperty(String name) {
        try {
            return this.originalMessage.getStringProperty(name);
        } catch (JMSException e) {
            return null;
        }
    }

    /**
     * Adds a property to the message. The property is sent/received with the message.
     *
     * @param name
     * @param value
     */
    @Override
    public void addProperty(String name, String value) {
        throw new UnsupportedOperationException("Can't add a property to a received message");
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
