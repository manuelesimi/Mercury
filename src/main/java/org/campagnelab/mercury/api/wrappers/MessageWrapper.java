package org.campagnelab.mercury.api.wrappers;

/**
 * Message wrapper.
 *
 * @author manuele
 */
public abstract class MessageWrapper<PAYLOAD> {

    public enum TYPE {
        TEXT, OBJECT, BYTE_ARRAY
    }

    private final PAYLOAD payload;

    private final TYPE type; //text messages are the default

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
     * Gets the type of the message.
     * @return the type
     */
    public TYPE getMessageType() {
        return this.type;
    }

}
