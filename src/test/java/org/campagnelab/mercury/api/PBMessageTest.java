package org.campagnelab.mercury.api;

import junit.framework.Assert;
import org.campagnelab.mercury.messages.JobLogMessageBuilder;
import org.campagnelab.mercury.messages.job.JobStatus;
import org.campagnelab.mercury.api.wrappers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.jms.Topic;
import java.io.File;

/**
 * Tester for messages with PB attachments.
 * @author manuele
 */
@RunWith(JUnit4.class)
public class PBMessageTest {

    private TopicProducer tproducer;

    private TopicConsumer tconsumer;

    private MQTopicConnection connection, connection2;

    @Before
    public void setUp() throws Exception {
        connection = new MQTopicConnection("localhost", 5672, new File("mercury.properties"), "connection1");
        connection2 = new MQTopicConnection("localhost", 5672, new File("mercury.properties"), "connection2");
    }

    @Test
    public void fakeTest() {
        return;
    }

    @Test
    public void testProducer() throws Exception {
        String topicName = "JUnitTopicBytes14";
        Topic t = connection.openTopic(topicName);
        this.tproducer = connection.createProducer(t);
        JobLogMessageBuilder builder = new JobLogMessageBuilder();
        builder.setPhase("align");
        builder.setCategory("DEBUG");
        builder.setDescription("A second message");
        builder.setNumOfParts(5);
        builder.setCurrentPart(2);
        this.tproducer.publishPBMessage(builder.buildMessage());

        JobLogMessageBuilder builder2 = new JobLogMessageBuilder();
        builder2.setPhase("align2");
        builder2.setCategory("DEBUG");
        builder2.setDescription("A third message");
        builder2.setNumOfParts(5);
        builder2.setCurrentPart(3);
        this.tproducer.publishPBMessage(builder2.buildMessage());
        this.tproducer.close();

        t = connection2.openConsumerTopic(topicName);
        this.tconsumer = connection2.createConsumer(t,"JUnitClient",true);

        //first PB
        MessageWrapper response = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response.getMessageType() == MESSAGE_TYPE.PB_CLASS);
        JobStatus.JobStatusUpdate readLog = (JobStatus.JobStatusUpdate) response.getPayload();
        Assert.assertEquals("A second message", readLog.getDescription());
        Assert.assertEquals("align", readLog.getStatus().getPhase());

        //second PB
        MessageWrapper response2 = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response2.getMessageType() == MESSAGE_TYPE.PB_CLASS);

        JobStatus.JobStatusUpdate readLog2 = (JobStatus.JobStatusUpdate)response2.getPayload();
        Assert.assertEquals("A third message", readLog2.getDescription());
        Assert.assertEquals("align2", readLog2.getStatus().getPhase());


    }

    @After
    public void tearDown() throws Exception {

        connection.close();
        connection2.close();
    }

}