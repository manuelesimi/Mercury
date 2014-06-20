package org.campagnelab.mercury.api;


import org.campagnelab.mercury.api.wrappers.MESSAGE_TYPE;
import org.campagnelab.mercury.api.wrappers.MessageToSendWrapper;
import org.campagnelab.mercury.api.wrappers.MessageWrapper;
import org.campagnelab.mercury.api.wrappers.TextMessageToSend;
import org.junit.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import javax.jms.Topic;
import java.io.File;

/**
 * Tester for text messages.
 * @author manuele
 */
@RunWith(JUnit4.class)
public class TextMessageTest {

    private TopicProducer tproducer;

    private TopicConsumer tconsumer, tconsumer2;

    private MQTopicConnection connection, connection2;
    @Before
    public void setUp() throws Exception {
        connection = new MQTopicConnection("toulouse.med.cornell.edu", 5672, new File("mercury.properties"));
        connection2 = new MQTopicConnection("toulouse.med.cornell.edu", 5672, new File("mercury.properties"));
    }

    @Test
    public void testPublishTextMessageInTopic() throws Exception {
        String topicName = "JUnitTopic20    ";
        Topic t = connection.openTopic(topicName);
        this.tproducer = connection.createProducer(t);
        String message = "Hello from the producer";
        this.tproducer.publishTextMessage(new TextMessageToSend(message));
        String message2 = "Hello from the producer2";
        this.tproducer.publishTextMessage(new TextMessageToSend(message2));
        this.tproducer.close();
        t = connection2.openTopic(topicName);
        this.tconsumer = connection2.createConsumer(t,"JUnitClient",true);
        this.tconsumer2 = connection2.createConsumer(t,"JUnitClient2",true);
        //check if both consumers can consume the messages
        MessageWrapper response = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response.getMessageType() == MESSAGE_TYPE.TEXT);
        Assert.assertEquals(message, (String)response.getPayload());
        response = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response.getMessageType() == MESSAGE_TYPE.TEXT);
        Assert.assertEquals(message2, (String)response.getPayload());

        response = tconsumer2.readNextMessage();
        Assert.assertTrue("Unexpected message type", response.getMessageType() == MESSAGE_TYPE.TEXT);
        Assert.assertEquals(message, (String)response.getPayload());
        response = tconsumer2.readNextMessage();
        Assert.assertTrue("Unexpected message type", response.getMessageType() == MESSAGE_TYPE.TEXT);
        Assert.assertEquals(message2, (String)response.getPayload());

        Assert.assertNull(tconsumer2.readNextMessage());

        connection.close();
        connection2.close();
    }

    @Test
    public void testPublishObjectMessageInTopic() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

        connection.close();
        connection2.close();
    }
}
