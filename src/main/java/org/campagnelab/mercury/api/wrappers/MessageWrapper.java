package org.campagnelab.mercury.api.wrappers;

import java.util.HashMap;
import java.util.Map;

/**
 * Message wrapper.
 *
 * @author manuele
 */
public abstract class MessageWrapper<PAYLOAD> {

    private PAYLOAD payload;

    /**
     * Properties associated to the message.
     */
    protected Map<String,String> properties = new HashMap<String, String>();

    protected MessageWrapper(PAYLOAD payload, MESSAGE_TYPE type) {
        this.payload = payload;
        this.addProperty(MESSAGE_PROPERTIES.MESSAGE_TYPE.name(), type.name());
    }

    /**
     * Gets the payload of this message
     * @return
     */
    public PAYLOAD getPayload() {
        return this.payload;
    }

    /**
     * Gets the payload of this message
     * @return
     */
    public void setPayload(PAYLOAD payload) {
        this.payload = payload;
    }

    /**
     * Gets the type of the message.
     * @return the type
     */
    public MESSAGE_TYPE getMessageType() {
        return MESSAGE_TYPE.valueOf(this.properties.get(MESSAGE_PROPERTIES.MESSAGE_TYPE.name()));
    }

    /**
     * Adds a property to the message. The property is sent/received with the message.
     * @param name
     * @param value
     */
    public void addProperty(String name, String value) {
        this.properties.put(name,value);
    }

    /**
     * Gets the properties associated to this message.
     * @return
     */
    public Map<String,String> getProperties() {
        return this.properties;
    }

    /**
     * Gets the property associated to this message.
     * @return
     */
    public String getProperty(String name) {
        return this.properties.get(name);
    }

    /**
     * Checks if there are properties to associate with the message.
     * @return
     */
    public boolean hasProperties() {
        return (this.properties.size() > 0);
    }
}
