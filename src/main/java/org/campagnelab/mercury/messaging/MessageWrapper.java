package org.campagnelab.mercury.messaging;

/**
 * A wrapper around an object message published/retrieved from the broker.
 *
 * @author manuele
 */
public class MessageWrapper<PAYLOAD> {

    final PAYLOAD payload;

    public MessageWrapper(PAYLOAD payload) {
        this.payload = payload;
    }


    public PAYLOAD getPayload() {
        return payload;
    }

}
