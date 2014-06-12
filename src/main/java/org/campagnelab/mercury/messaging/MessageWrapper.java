package org.campagnelab.mercury.messaging;

/**
 * A wrapper around an object message published/retrieved from the broker.
 *
 * @author manuele
 */
public class MessageWrapper<PAYLOAD> {

    /**
     * Message payload
     */
    private final PAYLOAD payload;

    /**
     * the message's lifetime (in hours)
     */
    private int timeToLive = 0;

    /**
     * the priority for this message
     */
    private int priority = 1;
    /**
     * the delivery mode to use
     */
    private int deliveryMode = 1;

    public MessageWrapper(PAYLOAD payload) {
        this.payload = payload;
    }

    public PAYLOAD getPayload() {
        return payload;
    }

    /**
     * Sets the message's lifetime in hours
     * @param timeToLive lifetime in hours
     */
    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    /**
     * Gets the message's lifetime in hours
     * @return
     */
    public int getTimeToLive() {
        return this.timeToLive;
    }

    /**
     * Gets the message's lifetime in milliseconds
     * @return
     */
    public long getTimeToLiveInMs() {
       return this.timeToLive * 1000 * 60;
    }

    public int getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(int deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
