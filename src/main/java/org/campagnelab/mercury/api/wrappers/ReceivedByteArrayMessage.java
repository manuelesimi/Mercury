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
        super(originalMessage, null, MESSAGE_TYPE.BYTE_ARRAY);
        byte[] bytes = new byte[(int) originalMessage.getBodyLength()];
        originalMessage.readBytes(bytes);
        this.setPayload(new ByteArray(bytes));
    }

}
