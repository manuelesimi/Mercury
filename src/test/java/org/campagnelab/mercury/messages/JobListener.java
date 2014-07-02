package org.campagnelab.mercury.messages;

import junit.framework.Assert;
import org.campagnelab.mercury.api.ReceivedMessageListener;
import org.campagnelab.mercury.api.wrappers.*;
import org.campagnelab.mercury.messages.job.JobStatus;

/**
 * Created by mas2182 on 7/2/14.
 */
public class JobListener extends ReceivedMessageListener {

    /**
     * Passes a message with a byte array to the listener.
     *
     * @param message the message passed to the listener
     */
    @Override
    public void onByteArrayMessage(ReceivedByteArrayMessage message) {

    }

    /**
     * Passes a message with a Protocol Buffer attachment to the listener.
     *
     * @param message the message passed to the listener
     */
    @Override
    public void onMessageWithPBAttachment(ReceivedMessageWithPBAttachment message) {
        Assert.assertTrue("Unexpected message type", message.getMessageType() == MESSAGE_TYPE.PB_CLASS);
        JobStatus.JobStatusUpdate readLog = (JobStatus.JobStatusUpdate) message.getPayload();
        System.out.println(readLog.getHostname());
        System.out.println(readLog.getDescription());
    }

    /**
     * Passes a message with an object to the listener.
     *
     * @param message the message passed to the listener
     */
    @Override
    public void onObjectMessage(ReceivedObjectMessage message) {

    }

    /**
     * Passes a text message to the listener.
     *
     * @param message the message passed to the listener
     */
    @Override
    public void onTextMessage(ReceivedTextMessage message) {

    }
}
