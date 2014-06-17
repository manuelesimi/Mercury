package org.campagnelab.mercury.api;

import junit.framework.Assert;
import org.campagnelab.mercury.test.protos.SamplePBMessage;
import org.campagnelab.mercury.api.wrappers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.jms.Topic;

/**
 * Tester for bytes' messages.
 * @author manuele
 */
@RunWith(JUnit4.class)
public class BytesMessageTest {

    private TopicProducer tproducer;

    private TopicConsumer tconsumer, tconsumer2;

    private MQTopicConnection connection, connection2;

    @Before
    public void setUp() throws Exception {
        connection = new MQTopicConnection("localhost", 5672);
        connection2 = new MQTopicConnection("localhost", 5672);
    }

    @Test
    public void fakeTest() {
        return;
    }

    @Test
    public void testPublishBytesMessageInTopic() throws Exception {
        String topicName = "JUnitTopicBytes14";
        Topic t = connection.openTopic(topicName);
        this.tproducer = connection.createProducer(t);
        SamplePBMessage.Message.Builder log = SamplePBMessage.Message.newBuilder();
        log.setPhase("align");
        log.setText("A first message");
        log.setCategory("DEBUG");
        MessageWithPBAttachmentToSend messageToSend = new MessageWithPBAttachmentToSend(log.build());
        this.tproducer.publishPBMessage(messageToSend);

        SamplePBMessage.Message.Builder log2 = SamplePBMessage.Message.newBuilder();
        log2.setPhase("align2");
        log2.setText("A second message");
        log2.setCategory("DEBUG");
        MessageWithPBAttachmentToSend messageToSend2 = new MessageWithPBAttachmentToSend(log2.build());
        this.tproducer.publishPBMessage(messageToSend2);
        this.tproducer.close();

        t = connection2.openTopic(topicName);
        this.tconsumer = connection2.createConsumer(t,"JUnitClient",true);

        //first PB
        MessageWrapper response = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response.getMessageType() == MESSAGE_TYPE.PB_CLASS);
        SamplePBMessage.Message readLog = (SamplePBMessage.Message)response.getPayload();
        Assert.assertEquals("A first message", readLog.getText());
        Assert.assertEquals("align", readLog.getPhase());

        //second PB
        MessageWrapper response2 = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response2.getMessageType() == MESSAGE_TYPE.PB_CLASS);

        SamplePBMessage.Message readLog2 = (SamplePBMessage.Message)response2.getPayload();
        Assert.assertEquals("A second message", readLog2.getText());
        Assert.assertEquals("align2", readLog2.getPhase());


    }

    @After
    public void tearDown() throws Exception {

        connection.close();
        connection2.close();
    }

}