package org.campagnelab.mercury.api.wrappers;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * A received text message.
 *
 * @author manuele
 */
public class ReceivedTextMessage extends ReceivedMessage<String> {

    public ReceivedTextMessage(TextMessage originalMessage) throws JMSException {
        super(originalMessage, originalMessage.getText(), TYPE.TEXT);
    }
}
