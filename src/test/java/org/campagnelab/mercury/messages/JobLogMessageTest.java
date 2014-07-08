package org.campagnelab.mercury.messages;

import junit.framework.Assert;
import org.campagnelab.mercury.api.MQTopicConnection;
import org.campagnelab.mercury.api.TopicConsumer;
import org.campagnelab.mercury.api.wrappers.MESSAGE_TYPE;
import org.campagnelab.mercury.api.wrappers.MessageWrapper;
import org.campagnelab.mercury.cli.JobInterface;
import org.campagnelab.mercury.messages.job.JobStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.jms.Topic;
import java.io.File;
import java.io.IOException;

/**
 * Tester for the job interface.
 *
 * @author manuele
 */

@RunWith(JUnit4.class)
public class JobLogMessageTest {

    private static final String hostname = "toulouse.med.cornell.edu";

    private static final int port = 5672;

    private static final String job = "ABCDEFGHD3";

    private MQTopicConnection connection;

    @Before
    public void setUp() throws Exception {
        connection = new MQTopicConnection(hostname, port, "JUnitConnection");
    }

    @Test
    public void testProducer() throws Exception {
        String message = "A text message sent from the JobInterface 3";
        String[] args = new String[] {
           "--broker-hostname", hostname,
           "--broker-port", Integer.toString(port),
           "--job-tag", job,
           "--description", message,
           "--phase", "ALIGN",
           "--category", "INFO",
           "--index", "2",
           "--num-of-parts", "5",
           "--jndi-config", "mercury.properties"
        };
        Assert.assertEquals("Failed to publish the message", 0, JobInterface.processAPI(args));
        //try to consume the message with the API

        Topic t = connection.openConsumerTopic(job);
        TopicConsumer tconsumer = connection.createSyncConsumer(t, "JUnitClient", true);
        MessageWrapper response = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response.getMessageType() == MESSAGE_TYPE.PB_CLASS);
        JobStatus.JobStatusUpdate readLog = (JobStatus.JobStatusUpdate) response.getPayload();
        Assert.assertEquals(message, readLog.getDescription());
        Assert.assertEquals("INFO", readLog.getCategory());
        Assert.assertEquals(java.net.InetAddress.getLocalHost().getHostName(), readLog.getHostname());
        Assert.assertEquals("ALIGN", readLog.getStatus().getPhase());
        Assert.assertEquals(2, readLog.getStatus().getCurrentPart());
        Assert.assertEquals(5, readLog.getStatus().getNumOfParts());

        TopicConsumer tconsumer2 = connection.createSyncConsumer(t, "JUnitClient2", true);
        MessageWrapper response2 = tconsumer2.readNextMessage();
        Assert.assertTrue("Unexpected message type", response2.getMessageType() == MESSAGE_TYPE.PB_CLASS);
        JobStatus.JobStatusUpdate readLog2 = (JobStatus.JobStatusUpdate) response2.getPayload();
        Assert.assertEquals(message, readLog2.getDescription());
        Assert.assertEquals("INFO", readLog2.getCategory());
        Assert.assertEquals(java.net.InetAddress.getLocalHost().getHostName(), readLog2.getHostname());
        Assert.assertEquals("ALIGN", readLog2.getStatus().getPhase());
        Assert.assertEquals(2, readLog2.getStatus().getCurrentPart());
        Assert.assertEquals(5, readLog2.getStatus().getNumOfParts());

        tconsumer.close();

    }
    @Test
    public void testSyncConsumer() throws Exception {
        Topic t = connection.openConsumerTopic("RZGKYXH");
        TopicConsumer tconsumer = connection.createSyncConsumer(t, "JUnitClient9", true);
        int i= 0;
        while (true) {
            System.out.println(String.format("Reading message #%d", i++));
            try {
                MessageWrapper response = tconsumer.readNextMessage();
                Assert.assertTrue("Unexpected message type", response.getMessageType() == MESSAGE_TYPE.PB_CLASS);
                JobStatus.JobStatusUpdate readLog = (JobStatus.JobStatusUpdate) response.getPayload();
                System.out.println(readLog.getHostname());
                System.out.println(readLog.getDescription());
            } catch (Exception e) { /*break; */ }

        }
       // tconsumer.close();

    }

    @Test
    public void testAsyncConsumer() throws Exception {
        Topic t = connection.openConsumerTopic("RZGKYXH");
        TopicConsumer tconsumer = connection.createAsyncConsumer(t, "AsyncJUnitClient5", true, new JobListener());
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tconsumer.close();

    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }
}
