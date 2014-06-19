package org.campagnelab.mercury.messages;

import org.campagnelab.mercury.api.wrappers.MessageWithPBAttachmentToSend;
import static org.campagnelab.mercury.messages.job.JobStatus.*;


import java.io.IOException;

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

    private int currentPart, numOfParts;

    public JobLogMessageBuilder() {
        this.timestamp = System.currentTimeMillis();
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

    /**
     * Builds the message for the broker.
     * @return
     */
    public MessageWithPBAttachmentToSend buildMessage() throws IOException {
        JobStatusUpdate.Builder builder = JobStatusUpdate.newBuilder();
        builder.setDescription(this.description);
        builder.setTimestamp(timestamp);
        builder.setCategory(this.category);
        JobStatusUpdate.PartStatus.Builder partBuilder = JobStatusUpdate.PartStatus.newBuilder();
        partBuilder.setPhase(this.phase);
        partBuilder.setCurrentPart(this.currentPart);
        partBuilder.setNumOfParts(this.numOfParts);
        builder.setStatus(partBuilder.build());
        return new MessageWithPBAttachmentToSend(builder.build());

    }
}
