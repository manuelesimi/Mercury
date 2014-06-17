package org.campagnelab.mercury.api.wrappers;

/**
 * A text message to send.
 *
 * @author manuele
 */
public class TextMessageToSend extends MessageToSendWrapper<String> {

    public TextMessageToSend(String text) {
        super(text, MESSAGE_TYPE.TEXT);
    }
}
