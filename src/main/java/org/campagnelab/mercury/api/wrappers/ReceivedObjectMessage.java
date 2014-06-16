package org.campagnelab.mercury.api.wrappers;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * A received message with a serialized object.
 *
 * @author manuele
 */

public class ReceivedObjectMessage extends ReceivedMessage<Serializable> {

    public ReceivedObjectMessage(ObjectMessage originalMessage) throws JMSException {
        super(originalMessage, originalMessage.getObject(), TYPE.OBJECT);
    }
}
