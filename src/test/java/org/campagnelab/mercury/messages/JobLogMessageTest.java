package org.campagnelab.mercury.messages;

import junit.framework.Assert;
import org.campagnelab.mercury.cli.JobInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tester for the job interface.
 *
 * @author manuele
 */

@RunWith(JUnit4.class)
public class JobLogMessageTest {


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testProducer() throws Exception {
        String[] args = new String[] {
           "--broker-hostname", "localhost",
           "--broker-port", "5672",
           "--job-tag", "ABCDEFG",
           "--text-message", "A text message sent from the JobInterface"
        };
        Assert.assertEquals("Failed to publish the message", 0, JobInterface.processAPI(args));
    }
}
