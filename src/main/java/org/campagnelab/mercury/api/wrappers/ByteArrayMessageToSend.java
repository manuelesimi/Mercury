package org.campagnelab.mercury.api.wrappers;

import javax.jms.Message;

/**
 * Created by mas2182 on 6/19/14.
 */
public class ByteArrayMessageToSend extends MessageToSendWrapper<ByteArray>{

    protected ByteArrayMessageToSend(ByteArray byteArray) {
        super(byteArray, MESSAGE_TYPE.BYTE_ARRAY);
    }

    protected ByteArrayMessageToSend(ByteArray byteArray, MESSAGE_TYPE type) {
        super(byteArray, type);
    }
}
