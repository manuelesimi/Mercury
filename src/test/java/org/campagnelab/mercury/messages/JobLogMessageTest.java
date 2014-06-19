package org.campagnelab.mercury.messages;

import junit.framework.Assert;
import org.campagnelab.mercury.api.MQTopicConnection;
import org.campagnelab.mercury.api.TopicConsumer;
import org.campagnelab.mercury.api.wrappers.MESSAGE_TYPE;
import org.campagnelab.mercury.api.wrappers.MessageWrapper;
import org.campagnelab.mercury.cli.JobInterface;
import org.campagnelab.mercury.messages.job.JobStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.jms.Topic;

/**
 * Tester for the job interface.
 *
 * @author manuele
 */

@RunWith(JUnit4.class)
public class JobLogMessageTest {

    private static final String hostname = "localhost";

    private static final int port = 5672;

    private static final String job = "ABCDEFG";

    private TopicConsumer tconsumer;

    private MQTopicConnection connection;

    @Before
    public void setUp() throws Exception {
        connection = new MQTopicConnection("localhost", 5672);
    }

    @Test
    public void testProducer() throws Exception {
        String[] args = new String[] {
           "--broker-hostname", hostname,
           "--broker-port", Integer.toString(port),
           "--job-tag", job,
           "--description", "A text message sent from the JobInterface",
           "--phase", "ALIGN",
           "--category", "INFO",
           "--index", "2",
           "--num-of-parts", "5"
        };
        Assert.assertEquals("Failed to publish the message", 0, JobInterface.processAPI(args));
        //try to consume the message with the API

        Topic t = connection.openTopic(job);
        this.tconsumer = connection.createConsumer(t,"JUnitClient",true);
        MessageWrapper response = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response.getMessageType() == MESSAGE_TYPE.PB_CLASS);
        JobStatus.JobStatusUpdate readLog = (JobStatus.JobStatusUpdate) response.getPayload();
        Assert.assertEquals("A text message sent from the JobInterface", readLog.getDescription());
        Assert.assertEquals("INFO", readLog.getCategory());
        Assert.assertEquals("ALIGN", readLog.getStatus().getPhase());
        Assert.assertEquals(2, readLog.getStatus().getCurrentPart());
        Assert.assertEquals(5, readLog.getStatus().getNumOfParts());



    }
}
