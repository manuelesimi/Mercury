package org.campagnelab.mercury.messaging;


import org.campagnelab.mercury.messaging.wrappers.TextMessageWrapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import javax.jms.Queue;

/**
 * Tester for text messages.
 * @author manuele
 */
@RunWith(JUnit4.class)
public class TextMessageTest {

    private QueueProducer producer;

    private QueueConsumer consumer;

    private MQConnection connection;

    @Before
    public void setUp() throws Exception {
        this.connection = new MQConnection();
        Queue q = connection.createQueue("JUnitQueue");
        this.producer = connection.createProducer(q);
        this.consumer = connection.createConsumer(q);

    }

    @Test
    public void testPublishTextMessage() throws Exception {
        String message = "Hello from the producer";
        this.producer.publishTextMessage(new TextMessageWrapper(message));
        Assert.assertEquals(message, consumer.readTextMessage().getMessageBody());

    }

    @After
    public void tearDown() throws Exception {
        this.connection.close();
    }
}
