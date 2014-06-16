package org.campagnelab.mercury.api.wrappers;


/**
 * A wrapper around an object message published/retrieved from the broker.
 *
 * @author manuele
 */
public class MessageToSendWrapper<PAYLOAD> extends MessageWrapper<PAYLOAD> {

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


    public MessageToSendWrapper(PAYLOAD payload, MESSAGE_TYPE type) {
        super(payload, type);
    }

    /**
     * Sets the message's lifetime in hours
     * @param timeToLive lifetime in hours
     */
    public void setTimeToLiveInHours(int timeToLive) {
        this.timeToLive = timeToLive * 1000 * 60;
    }

    /**
     * Gets the message's lifetime in hours
     * @return
     */
    public long getTimeToLiveHours() {
        return this.timeToLive == 0? this.timeToLive: this.timeToLive / 1000 / 60;
    }

    /**
     * Sets the message's lifetime in milliseconds
     * @param timeToLive lifetime in hours
     */
    public void setTimeToLiveInMs(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    /**
     * Gets the message's lifetime in milliseconds
     * @return
     */
    public int getTimeToLiveInMs() {
        return this.timeToLive;
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
