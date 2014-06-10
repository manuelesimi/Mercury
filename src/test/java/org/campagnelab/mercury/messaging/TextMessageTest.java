package org.campagnelab.mercury.messaging;


import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tester for text messages.
 * @author manuele
 */
@RunWith(JUnit4.class)
public class TextMessageTest {

    private Producer producer;

    private Consumer consumer;

    private MQConnection connection;

    @Before
    public void setUp() throws Exception {
        this.connection = new MQConnection();
        this.producer = new Producer(connection);
        this.consumer = new Consumer(connection);

    }

    @Test
    public void testPublishTextMessage() throws Exception {
        String message = "Hello from the producer";
        this.producer.publishTextMessage(message);
        Assert.assertEquals(message, consumer.readTextMessage());

    }

    @After
    public void tearDown() throws Exception {
        this.connection.close();
    }
}
