package org.campagnelab.mercury.messaging;

import java.io.Serializable;

/**
 * A wrapper around an object message.
 *
 * @author manuele
 */
public class ObjectMessageWrapper {

    private Serializable messageBody;

    public ObjectMessageWrapper(Serializable messageBody) {
        this.messageBody = messageBody;
    }


    public Serializable getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(Serializable messageBody) {
        this.messageBody = messageBody;
    }
}
