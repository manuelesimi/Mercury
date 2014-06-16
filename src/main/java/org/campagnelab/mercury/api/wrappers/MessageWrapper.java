package org.campagnelab.mercury.api.wrappers;

import java.util.HashMap;
import java.util.Map;

/**
 * Message wrapper.
 *
 * @author manuele
 */
public abstract class MessageWrapper<PAYLOAD> {

    public enum TYPE {
        TEXT, OBJECT, BYTE_ARRAY
    }

    private PAYLOAD payload;

    private final TYPE type; //text messages are the default

    /**
     * Properties associated to the message.
     */
    protected Map<String,String> properties = new HashMap<String, String>();

    protected MessageWrapper(PAYLOAD payload, TYPE type) {
        this.payload = payload;
        this.type = type;
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
    public TYPE getMessageType() {
        return this.type;
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
