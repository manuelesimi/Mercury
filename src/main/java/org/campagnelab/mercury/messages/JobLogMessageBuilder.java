package org.campagnelab.mercury.messages;

import org.campagnelab.mercury.api.wrappers.MessageWithPBAttachmentToSend;
import static org.campagnelab.mercury.messages.job.JobStatus.*;


import java.io.IOException;
import java.net.UnknownHostException;

/**
 * A message from a job executed on the cluster.
 *
 * @author manuele
 */
public class JobLogMessageBuilder {

    private String description;

    private String category;

    private long timestamp;

    private String phase;

    private String hostname;

    private String slotName;


    private int currentPart = 1, numOfParts = 1;

    public JobLogMessageBuilder() {
        this.timestamp = System.currentTimeMillis();
        try {
            this.hostname = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            this.hostname = "failed to detect";
        }
    }

    public void setCurrentPart(int currentPart) {
        this.currentPart = currentPart;
    }

    public void setNumOfParts(int numOfParts) {
        this.numOfParts = numOfParts;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }


    /**
     * Builds the message for the broker.
     * @return
     */
    public MessageWithPBAttachmentToSend buildMessage() throws IOException {
        JobStatusUpdate.Builder builder = JobStatusUpdate.newBuilder();
        builder.setDescription(this.description);
        builder.setTimestamp(timestamp);
        builder.setCategory(this.category);
        builder.setHostname(this.hostname);
        builder.setSlotName(this.slotName);
        JobStatusUpdate.PartStatus.Builder partBuilder = JobStatusUpdate.PartStatus.newBuilder();
        partBuilder.setPhase(this.phase);
        partBuilder.setCurrentPart(this.currentPart);
        partBuilder.setNumOfParts(this.numOfParts);
        builder.setStatus(partBuilder.build());
        return new MessageWithPBAttachmentToSend(builder.build());

    }
}
