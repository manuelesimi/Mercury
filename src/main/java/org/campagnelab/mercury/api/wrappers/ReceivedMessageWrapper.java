package org.campagnelab.mercury.api.wrappers;

import javax.jms.Message;


/**
 * A message received from the broker.
 *
 * @author manuele
 */
public class ReceivedMessageWrapper<PAYLOAD> extends MessageWrapper<PAYLOAD> {

    final Message originalMessage;

    public ReceivedMessageWrapper(Message originalMessage, PAYLOAD payload, TYPE type) {
        super(payload, type);
        this.originalMessage = originalMessage;
    }
}
