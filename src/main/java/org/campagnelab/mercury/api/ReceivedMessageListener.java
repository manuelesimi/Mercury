package org.campagnelab.mercury.api;

import org.campagnelab.mercury.api.wrappers.*;

import javax.jms.*;

/** A <CODE>ReceivedMessageListener</CODE> object is used to receive asynchronously
 *  messages published in a Topic   .
 *
 * @author manuele
 */
public abstract class ReceivedMessageListener implements MessageListener {

    /**
     * Passes a message with a byte array to the listener.
     *
     * @param message the message passed to the listener
     */
    public abstract void onByteArrayMessage(ReceivedByteArrayMessage message);

    /**
     * Passes a message with a Protocol Buffer attachment to the listener.
     *
     * @param message the message passed to the listener
     */
    public abstract void onMessageWithPBAttachment(ReceivedMessageWithPBAttachment message);

    /**
     * Passes a message with an object to the listener.
     *
     * @param message the message passed to the listener
     */
    public abstract void onObjectMessage(ReceivedObjectMessage message);

    /**
     * Passes a text message to the listener.
     *
     * @param message the message passed to the listener
     */
    public abstract void onTextMessage(ReceivedTextMessage message);

    /**
     * Passes a message to the listener.
     *
     * @param originalMessage the message passed to the listener
     */
    @Override
    public void onMessage(Message originalMessage) {
        try {


        if (originalMessage == null)
            return;
        if (originalMessage instanceof TextMessage) {
            this.onTextMessage(new ReceivedTextMessage((TextMessage) originalMessage));
            return ;
        } else if (originalMessage instanceof ObjectMessage) {
            this.onObjectMessage(new ReceivedObjectMessage((ObjectMessage)originalMessage));
        } else if (originalMessage instanceof BytesMessage) {
            if (this.getMessageType(originalMessage) == MESSAGE_TYPE.PB_CLASS) {
                this.onMessageWithPBAttachment(new ReceivedMessageWithPBAttachment((BytesMessage) originalMessage));
            } else
                this.onByteArrayMessage(new ReceivedByteArrayMessage((BytesMessage) originalMessage));
        }
        } catch (Exception e ){

        }
    }

    /**
     * Gets the type of the message.
     * @return the type
     */
    private MESSAGE_TYPE getMessageType(Message message) throws JMSException {
        return MESSAGE_TYPE.valueOf(message.getStringProperty(MESSAGE_PROPERTIES.MESSAGE_TYPE.name()));
    }
}
