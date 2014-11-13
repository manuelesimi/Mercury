package org.campagnelab.mercury.messages;

import java.util.ArrayList;
import java.util.Collection;

/**
 * List of messages.
 *
 * @author manuele
 */
public class JobMessageList extends ArrayList<JobLogMessageReader> {
    public JobMessageList(int i) {
        super(i);
    }

    public JobMessageList() {
        super();
    }

    public JobMessageList(Collection<? extends JobLogMessageReader> jobLogMessageReaders) {
        super(jobLogMessageReaders);
    }
}
