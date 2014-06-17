package org.campagnelab.mercury.api.wrappers;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * A received text message.
 *
 * @author manuele
 */
public class ReceivedTextMessage extends ReceivedMessageWrapper<String> {

    public ReceivedTextMessage(TextMessage originalMessage) throws JMSException {
        super(originalMessage, originalMessage.getText(), MESSAGE_TYPE.TEXT);
    }
}
