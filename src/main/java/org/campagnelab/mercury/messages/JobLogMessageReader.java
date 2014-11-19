package org.campagnelab.mercury.messages;

import org.campagnelab.mercury.messages.job.JobStatus;

/**
 * Reader for messages publisher by jobs.
 *
 * @author manuele
 */
public class JobLogMessageReader {

    final JobStatus.JobStatusUpdate message;

    private JobLogMessageReader() {message = null;}

    public JobLogMessageReader(JobStatus.JobStatusUpdate message) {
        this.message = message;
    }

    public String getSourceHostname() {
        return this.message.getHostname();
    }

    public long getTimestamp() {
        return this.message.getTimestamp();
    }

    public String getDescription() {
        return this.message.getDescription();
    }

    public String getCategory() {
        return this.message.getCategory();
    }

    public String getSlotName() {
        return this.message.getSlotName();
    }

    public JobLogMessageReader.StatusReader getStatusReader() {
        return this.message.hasStatus()? new StatusReader(this.message.getStatus()): null;
    }

    public class StatusReader {

        JobStatus.JobStatusUpdate.PartStatus status;

        StatusReader(JobStatus.JobStatusUpdate.PartStatus status) {
           this.status = status;
        }

        public String getPhase() {
          return this.status.getPhase();
        }

        public int getCurrentPart() {
            return this.status.getCurrentPart();
        }

        public int getNumOfParts() {
            return this.status.getNumOfParts();
        }

    }
}
