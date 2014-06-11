package org.campagnelab.mercury.messaging;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.jms.Queue;

/**
 * Tester for text messages.
 * @author manuele
 */
@RunWith(JUnit4.class)
public class ObjectMessageTest {

    private Producer producer;

    private Consumer consumer;

    private MQConnection connection;

    @Before
    public void setUp() throws Exception {
        this.connection = new MQConnection();
        Queue q = connection.createNewQueue("JUnitQueue");
        this.producer = new Producer(connection,q);
        this.consumer = new Consumer(connection,q);

    }

    @Test
    public void testPublishObjectMessage() throws Exception {
        String message = "Hello from the producer";
        MySerializableObject obj = new MySerializableObject(message);
        this.producer.publishObjectMessage(new ObjectMessageWrapper(obj));
        ObjectMessageWrapper wrapper = consumer.readObjectMessage();
        Assert.assertEquals(message, ((MySerializableObject) wrapper.getMessageBody()).getBody() );
    }

    @After
    public void tearDown() throws Exception {
        this.connection.close();
    }


}
