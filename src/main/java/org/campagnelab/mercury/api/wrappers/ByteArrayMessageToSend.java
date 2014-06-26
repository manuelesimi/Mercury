package org.campagnelab.mercury.api.wrappers;

/**
 * Wrapper for messages with a byte array attached.
 *
 * @author manuele
 */
public class ByteArrayMessageToSend extends MessageToSendWrapper<ByteArray>{

    public ByteArrayMessageToSend(ByteArray byteArray) {
        super(byteArray, MESSAGE_TYPE.BYTE_ARRAY);
    }

    protected ByteArrayMessageToSend(ByteArray byteArray, MESSAGE_TYPE type) {
        super(byteArray, type);
    }
}
