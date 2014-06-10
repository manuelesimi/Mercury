package org.campagnelab.mercury.messaging.consumer;

import junit.framework.Assert;
import org.campagnelab.mercury.messaging.MQConnection;
import org.campagnelab.mercury.messaging.Consumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by mas2182 on 6/10/14.
 */
@RunWith(JUnit4.class)
public class ConsumerTest {

    private MQConnection connection;

    private Consumer consumer;

    @Before
    public void setUp() throws Exception {
        this.connection = new MQConnection();
        this.consumer = new Consumer(connection);
    }

    @Test
    public void testReadMessage() throws Exception {
        String message = consumer.readMessage();
        Assert.assertEquals("Hello from the producer",message);
    }

    @After
    public void tearDown() throws Exception {
        this.connection.close();
    }
}
