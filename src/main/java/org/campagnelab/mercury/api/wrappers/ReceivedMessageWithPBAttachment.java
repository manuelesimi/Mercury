package org.campagnelab.mercury.api.wrappers;

import com.google.protobuf.GeneratedMessage;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import java.lang.reflect.Method;


/**
 * A message with a protocol buffer class attached.
 *
 * @author manuele
 */
public class ReceivedMessageWithPBAttachment extends ReceivedMessageWrapper<GeneratedMessage> {

    public ReceivedMessageWithPBAttachment(BytesMessage originalMessage) throws JMSException, ClassNotFoundException {
        super(originalMessage, null, MESSAGE_TYPE.PB_CLASS);
        byte[] bytes = new byte[(int) originalMessage.getBodyLength()];
        originalMessage.readBytes(bytes);
        this.generatePayloadFromClass(bytes);
    }

    private void generatePayloadFromClass(byte[] bytes) throws IllegalArgumentException {
        Class<? extends GeneratedMessage> pbClass = null;
        try {
            pbClass = (Class<? extends GeneratedMessage>) Class.forName(this.getSerializedClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("PB class referred in the message ("+ this.getSerializedClassName() +") is not available.", e);
        }
        try {
            this.setPayload(this.asMessage(bytes, pbClass));
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to deserialize the PB object from the message", e);
        }
    }

    /**
     * Creates and loads a PB message from the given array of bytes.
     * @param array
     * @param messageClass the concrete class of the PB message to load.
     * @param <MESSAGE> the class of the PB message to load.
     * @return
     * @throws Exception
     */
    private <MESSAGE extends GeneratedMessage> MESSAGE asMessage(byte[] array,  Class<MESSAGE> messageClass ) throws Exception {
        Method method = messageClass.getMethod("parseFrom", byte[].class);
        MESSAGE message = (MESSAGE) method.invoke(null, array);
        return message;
    }

    /**
     * The name of the class serialized in the ByteArray.
     */
    public String getSerializedClassName() {
        return this.getProperty(MESSAGE_PROPERTIES.PB_CLASS_NAME.name());
    }
}
