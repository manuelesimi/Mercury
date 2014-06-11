package org.campagnelab.mercury.messaging.wrappers;

/**
 * A wrapper around an object message.
 *
 * @author manuele
 */
public class TextMessageWrapper {

    private String messageBody;

    public TextMessageWrapper(String messageBody) {
        this.messageBody= messageBody;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
