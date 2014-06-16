package org.campagnelab.mercury.api.wrappers;

import com.google.protobuf.GeneratedMessage;

import java.io.IOException;

/**
 * A message with a protocol buffer class attached.
 *
 * @author manuele
 */
public class MessageWithPBAttachmentToSend extends MessageToSendWrapper<GeneratedMessage>{

    public MessageWithPBAttachmentToSend(GeneratedMessage message) throws IOException {
        super(message, MESSAGE_TYPE.PB_CLASS);
        this.addProperty(MESSAGE_PROPERTIES.PB_CLASS_NAME.name(), message.getClass().getCanonicalName());
    }

    public byte[] getPayloadAsBytes() {
         return this.getPayload().toByteArray();
    }
}
