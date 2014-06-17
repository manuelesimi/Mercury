package org.campagnelab.mercury.messages;

import org.campagnelab.mercury.api.wrappers.TextMessageToSend;

import java.util.Date;

/**
 * A text message from a job executed on the cluster.
 *
 * @author manuele
 */
public class JobLogMessageBuilder {

    private String text;

    private String category;

    private Date timestamp;

    private String phase;

    public JobLogMessageBuilder() {
        this.timestamp = new Date();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    /**
     * Builds the message for the broker.
     * @return
     */
    public TextMessageToSend buildMessage() {
        TextMessageToSend message = new TextMessageToSend(this.text);

        return message;
    }
}
