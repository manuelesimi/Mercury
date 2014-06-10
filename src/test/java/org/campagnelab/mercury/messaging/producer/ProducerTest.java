package org.campagnelab.mercury.messaging.producer;


import org.campagnelab.mercury.messaging.MQConnection;
import org.campagnelab.mercury.messaging.Producer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author manuele
 */
@RunWith(JUnit4.class)
public class ProducerTest {

    private Producer producer;

    private MQConnection connection;

    @Before
    public void setUp() throws Exception {
        this.connection = new MQConnection();
        this.producer = new Producer(connection);
    }

    @Test
    public void testPublishTextMessage() throws Exception {
        this.producer.publishTextMessage("Hello from the producer");

    }

    @After
    public void tearDown() throws Exception {
        this.connection.close();
    }
}
