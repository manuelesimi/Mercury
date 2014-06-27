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

    private Topic t1;

    private MQTopicConnection connection, connection2;
    @Before
    public void setUp() throws Exception {
        connection = new MQTopicConnection("toulouse.med.cornell.edu", 5672, new File("mercury.properties"));
        connection2 = new MQTopicConnection("toulouse.med.cornell.edu", 5672, new File("mercury.properties"));
        String topicName = "JUnitTopic22";
        t1 = connection.openTopic(topicName);
    }

    @Test
    public void testPublishTextMessageInTopic() throws Exception {
        this.tproducer = connection.createProducer(t1);
        String message = "Hello from the producer";
        this.tproducer.publishTextMessage(new TextMessageToSend(message));
        String message2 = "Hello from the producer2";
        this.tproducer.publishTextMessage(new TextMessageToSend(message2));
        this.tproducer.close();
        this.tconsumer = connection2.createConsumer(t1,"JUnitClient",true);
        this.tconsumer2 = connection2.createConsumer(t1,"JUnitClient2",true);

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
        this.tconsumer.close();
        this.tconsumer2.close();

    }

    @Test
    public void testConsumer() throws Exception {
        String message = "Hello from the producer";
        this.tconsumer2 = connection2.createConsumer(t1,"JUnitClient2",true);
        MessageWrapper response = tconsumer2.readNextMessage();
        Assert.assertTrue("Unexpected message type", response.getMessageType() == MESSAGE_TYPE.TEXT);
        Assert.assertEquals(message, (String)response.getPayload());
        this.tconsumer2.close();

    }

    @After
    public void tearDown() throws Exception {

        connection.close();
        connection2.close();
    }
}
