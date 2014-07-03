package org.campagnelab.mercury.messages;

import org.campagnelab.mercury.api.ReceivedMessageListener;
import org.campagnelab.mercury.api.wrappers.ReceivedByteArrayMessage;
import org.campagnelab.mercury.api.wrappers.ReceivedMessageWithPBAttachment;
import org.campagnelab.mercury.api.wrappers.ReceivedObjectMessage;
import org.campagnelab.mercury.api.wrappers.ReceivedTextMessage;
import org.campagnelab.mercury.messages.job.JobStatus;

/**
 * A listener for log messages published by a job.
 *
 * @author manuele
 */
public abstract class JobLogMessageListener extends ReceivedMessageListener {
    /**
     * Passes a message with a byte array to the listener.
     *
     * @param message the message passed to the listener
     */
    @Override
    public void onByteArrayMessage(ReceivedByteArrayMessage message) {
        //ignore the message
    }

    /**
     * Passes a message with a Protocol Buffer attachment to the listener.
     *
     * @param message the message passed to the listener
     */
    @Override
    public void onMessageWithPBAttachment(ReceivedMessageWithPBAttachment message) {
        JobStatus.JobStatusUpdate readLog = (JobStatus.JobStatusUpdate) message.getPayload();
        this.onJobLogMessage(new JobLogMessageReader(readLog));
    }

    public abstract void onJobLogMessage(JobLogMessageReader reader);

    /**
     * Passes a message with an object to the listener.
     *
     * @param message the message passed to the listener
     */
    @Override
    public void onObjectMessage(ReceivedObjectMessage message) {
      //ignore the message
    }

    /**
     * Passes a text message to the listener.
     *
     * @param message the message passed to the listener
     */
    @Override
    public void onTextMessage(ReceivedTextMessage message) {
        //ignore the message
    }
}
