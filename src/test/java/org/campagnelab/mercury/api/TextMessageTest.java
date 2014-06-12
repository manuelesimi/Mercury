package org.campagnelab.mercury.api;


import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import javax.jms.Topic;

/**
 * Tester for text messages.
 * @author manuele
 */
@RunWith(JUnit4.class)
public class TextMessageTest {

    private QueueProducer producer;

    private QueueConsumer consumer;

    private TopicProducer tproducer;

    private TopicConsumer tconsumer, tconsumer2;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testPublishTextMessageInTopic() throws Exception {
        String topicName = "JUnitTopic16";
        MQTopicConnection connection = new MQTopicConnection();
        Topic t = connection.openTopic(topicName);
        this.tproducer = connection.createProducer(t);
        String message = "Hello from the producer";
        this.tproducer.publishTextMessage(new MessageWrapper<String>(message));
        String message2 = "Hello from the producer2";
        this.tproducer.publishTextMessage(new MessageWrapper<String>(message2));
        this.tproducer.close();


        MQTopicConnection connection2 = new MQTopicConnection();
        t = connection2.openTopic(topicName);
        this.tconsumer = connection2.createConsumer(t,"JUnitClient",true);
        this.tconsumer2 = connection2.createConsumer(t,"JUnitClient2",true);

        //check if both consumers can consume the messages
        Assert.assertEquals(message, tconsumer.readTextMessage().getPayload());
        Assert.assertEquals(message2, tconsumer.readTextMessage().getPayload());
        Assert.assertNull(tconsumer.readTextMessage());
        Assert.assertEquals(message, tconsumer2.readTextMessage().getPayload());
        Assert.assertEquals(message2, tconsumer2.readTextMessage().getPayload());
        Assert.assertNull(tconsumer2.readTextMessage());

        connection.close();
        connection2.close();
    }

    @Test
    public void testPublishObjectMessageInTopic() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }
}
