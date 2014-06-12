package org.campagnelab.mercury.messaging;


import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import javax.jms.Queue;
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

    private MQConnection connection;

    @Before
    public void setUp() throws Exception {
        this.connection = new MQConnection();
        Queue q = connection.createQueue("JUnitQueue");
        this.producer = connection.createProducer(q);
        this.consumer = connection.createConsumer(q);
        Topic t = connection.createTopic("JUnitTopic11");
        this.tproducer = connection.createProducer(t);
        this.tconsumer = connection.createConsumer(t,"JUnitClient",true);
        this.tconsumer2 = connection.createConsumer(t,"JUnitClient2",true);

    }

    @Test
    public void testPublishTextMessage() throws Exception {
        String message = "Hello from the producer";
        this.tproducer.publishTextMessage(new MessageWrapper<String>(message));
        String message2 = "Hello from the producer2";
        this.tproducer.publishTextMessage(new MessageWrapper<String>(message2));
        //check if both consumers can consume the messages
        Assert.assertEquals(message, tconsumer.readTextMessage().getPayload());
        Assert.assertEquals(message2, tconsumer.readTextMessage().getPayload());
        Assert.assertNull(tconsumer.readTextMessage());
        Assert.assertEquals(message, tconsumer2.readTextMessage().getPayload());
        Assert.assertEquals(message2, tconsumer2.readTextMessage().getPayload());
        Assert.assertNull(tconsumer2.readTextMessage());
    }

    @Test
    public void testPublishObjectMessageInTopic() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        this.connection.close();
    }
}
