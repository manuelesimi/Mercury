package org.campagnelab.mercury.messages;

import org.campagnelab.mercury.api.wrappers.ReceivedTextMessage;

/**
 * Created by mas2182 on 6/17/14.
 */
public class JobLogMessageReader {

    final ReceivedTextMessage message;

    public JobLogMessageReader(ReceivedTextMessage message) {
        this.message = message;
    }
}
