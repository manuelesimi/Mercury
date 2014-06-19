package org.campagnelab.mercury.api.wrappers;

import com.google.protobuf.GeneratedMessage;

import java.io.IOException;

/**
 * A message with a protocol buffer class attached.
 *
 * @author manuele
 */
public class MessageWithPBAttachmentToSend extends ByteArrayMessageToSend {

    public MessageWithPBAttachmentToSend(GeneratedMessage message) throws IOException {
        super(new ByteArray(message.toByteArray()), MESSAGE_TYPE.PB_CLASS);
        this.addProperty(MESSAGE_PROPERTIES.PB_CLASS_NAME.name(), message.getClass().getName());
    }
}
