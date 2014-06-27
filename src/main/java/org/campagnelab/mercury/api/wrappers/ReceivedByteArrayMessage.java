package org.campagnelab.mercury.api.wrappers;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

/**
 * A received message with bytes.
 *
 * @author manuele
 */
public class ReceivedByteArrayMessage extends ReceivedMessageWrapper<ByteArray> {


    public ReceivedByteArrayMessage(BytesMessage originalMessage) throws JMSException {
        this(originalMessage, MESSAGE_TYPE.BYTE_ARRAY);

    }

    /**
     * Gives to subclasses the opportunity to customize the type.
     * @param originalMessage
     * @param type
     */
    public ReceivedByteArrayMessage(BytesMessage originalMessage, MESSAGE_TYPE type) throws JMSException {
        super(originalMessage, null, type);
        byte[] bytes = new byte[(int) originalMessage.getBodyLength()];
        originalMessage.readBytes(bytes);
        this.setPayload(new ByteArray(bytes));
        originalMessage.reset();
    }
}
