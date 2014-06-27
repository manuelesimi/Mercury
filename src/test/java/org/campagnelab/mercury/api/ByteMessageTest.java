package org.campagnelab.mercury.api;

import org.campagnelab.mercury.api.wrappers.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.jms.Topic;
import java.io.File;
import java.io.StringWriter;

/**
 * Created by mas2182 on 6/26/14.
 */

@RunWith(JUnit4.class)
public class ByteMessageTest {

    private TopicProducer tproducer;

    private TopicConsumer tconsumer;

    private MQTopicConnection connection;

    private Topic t1;

    private final String message = "Hello from the byte producer";

    @Before
    public void setUp() throws Exception {
        connection = new MQTopicConnection("toulouse.med.cornell.edu", 5672, new File("mercury.properties"));
        String topicName = "JUnitTopicBytes9";
        t1 = connection.openTopic(topicName);
    }

    @Test
    public void testProducer() throws Exception {
        this.tproducer = connection.createProducer(t1);
        this.tproducer.publishBytesMessage(new ByteArrayMessageToSend(new ByteArray(message.getBytes())));
        this.tproducer.close();
    }

    @Test
    public void testConsumer() throws Exception {
        this.tconsumer = connection.createConsumer(t1,"JUNITCase",true);
        ReceivedMessageWrapper receivedMessage = this.tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", receivedMessage.getMessageType() == MESSAGE_TYPE.BYTE_ARRAY);
        String str = new String(((ReceivedByteArrayMessage) receivedMessage).getPayload().getArray(), "UTF-8");
        Assert.assertEquals(message, str);
        this.tconsumer.close();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }
}