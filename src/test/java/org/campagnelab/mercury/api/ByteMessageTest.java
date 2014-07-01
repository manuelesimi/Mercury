package org.campagnelab.mercury.api;

import org.campagnelab.mercury.api.wrappers.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.jms.Queue;
import javax.jms.Topic;
import java.io.File;

/**
 * Created by mas2182 on 6/26/14.
 */

@RunWith(JUnit4.class)
public class ByteMessageTest {

    private TopicProducer tproducer;

    private TopicConsumer tconsumer;

    private MQTopicConnection connection;

    private MQQueueConnection queueConnection;

    private Topic t1;

    private Queue q1;

    private final String message = "Hello from the byte producer";

    private String topicName = "JUnitTopicBytes1";

    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void testTopicProducer() throws Exception {
        connection = new MQTopicConnection("toulouse.med.cornell.edu", 5672, new File("mercury.properties"));
        t1 = connection.openTopic(topicName);
        this.tproducer = connection.createProducer(t1);
        this.tproducer.publishBytesMessage(new ByteArrayMessageToSend(new ByteArray(message.getBytes())));
        this.tproducer.close();
        connection.close();
    }

    @Test
    public void testTopicConsumer() throws Exception {
        connection = new MQTopicConnection("toulouse.med.cornell.edu", 5672, new File("mercury.properties"));
        t1 = connection.openConsumerTopic(topicName);
        this.tconsumer = connection.createConsumer(t1,"JUNITCase2",true);
        ReceivedMessageWrapper receivedMessage = this.tconsumer.readNextMessage();
        Assert.assertNotNull(receivedMessage);
        Assert.assertTrue("Unexpected message type", receivedMessage.getMessageType() == MESSAGE_TYPE.BYTE_ARRAY);
        String str = new String(((ReceivedByteArrayMessage) receivedMessage).getPayload().getArray(), "UTF-8");
        Assert.assertEquals(message, str);
        this.tconsumer.close();
        connection.close();
    }


    @Test
    public void testQueueProducer() throws Exception {
        queueConnection = new MQQueueConnection("toulouse.med.cornell.edu", 5672, new File("mercury.properties"));
        q1 = queueConnection.openQueue(topicName);

        QueueProducer queueProducer = queueConnection.createProducer(q1);
        queueProducer.publishBytesMessage(new ByteArrayMessageToSend(new ByteArray(message.getBytes())));
        queueProducer.close();
    }

    @Test
    public void testQueueConsumer() throws Exception {
        queueConnection = new MQQueueConnection("toulouse.med.cornell.edu", 5672, new File("mercury.properties"));
        q1 = queueConnection.openQueue(topicName);
        QueueConsumer queueConsumer = queueConnection.createConsumer(q1);
        ReceivedMessageWrapper receivedMessage = queueConsumer.readNextMessage();
        Assert.assertNotNull(receivedMessage);
        Assert.assertTrue("Unexpected message type", receivedMessage.getMessageType() == MESSAGE_TYPE.BYTE_ARRAY);
        String str = new String(((ReceivedByteArrayMessage) receivedMessage).getPayload().getArray(), "UTF-8");
        Assert.assertEquals(message, str);
        queueConsumer.close();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }
}