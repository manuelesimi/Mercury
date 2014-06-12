package org.campagnelab.mercury.messaging;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.jms.Queue;
import javax.jms.Topic;
import java.io.Serializable;

/**
 * Tester for text messages.
 * @author manuele
 */
@RunWith(JUnit4.class)
public class ObjectMessageTest {

    private QueueProducer qproducer;

    private QueueConsumer qconsumer;

    private TopicProducer tproducer;

    private TopicConsumer tconsumer;

    private MQConnection connection;

    @Before
    public void setUp() throws Exception {
        this.connection = new MQConnection();
        Queue q = connection.createQueue("JUnitQueue");
        this.qproducer = connection.createProducer(q);
        this.qconsumer = connection.createConsumer(q);

        Topic t = connection.createTopic("JUnitTopic");
        this.tproducer = connection.createProducer(t);
        this.tconsumer = connection.createConsumer(t,"JUnitClient",true);

    }

    @Test
    public void testPublishObjectMessageInQueue() throws Exception {
        String message = "Hello from the producer";
        MySerializableObject obj = new MySerializableObject(message);
        this.qproducer.publishObjectMessage(new MessageWrapper<Serializable>(obj));
        MessageWrapper<Serializable> wrapper = qconsumer.readObjectMessage();
        Assert.assertEquals(message, ((MySerializableObject) wrapper.getPayload()).getBody() );
    }

    @Test
    public void testPublishObjectMessageInTopic() throws Exception {
        String message = "Hello from the producer";
        MySerializableObject obj = new MySerializableObject(message);
        //this.tproducer.publishObjectMessage(new ObjectMessageWrapper(obj));
        //ObjectMessageWrapper wrapper = tconsumer.readObjectMessage();
        //Assert.assertEquals(message, ((MySerializableObject) wrapper.getMessageBody()).getBody() );
    }


    @After
    public void tearDown() throws Exception {
        this.connection.close();
    }


}
