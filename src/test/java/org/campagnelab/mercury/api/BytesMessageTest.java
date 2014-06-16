package org.campagnelab.mercury.api;

import junit.framework.Assert;
import org.campagnelab.mercury.test.protos.FileSetMetadata;
import org.campagnelab.mercury.api.wrappers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.jms.Topic;

/**
 * Tester for bytes' messages.
 * @author manuele
 */
@RunWith(JUnit4.class)
public class BytesMessageTest {

    private TopicProducer tproducer;

    private TopicConsumer tconsumer, tconsumer2;

    private MQTopicConnection connection, connection2;

    @Before
    public void setUp() throws Exception {
        connection = new MQTopicConnection("localhost", 5672);
        connection2 = new MQTopicConnection("localhost", 5672);
    }

    @Test
    public void testPublishBytesMessageInTopic() throws Exception {
        String topicName = "JUnitTopicBytes13   ";
        Topic t = connection.openTopic(topicName);
        this.tproducer = connection.createProducer(t);
        FileSetMetadataWriter writer = new FileSetMetadataWriter("testName","1.0","testTag", "testOwner");
        FileSetMetadata.Metadata producerMetadata = writer.serialize();
        MessageWithPBAttachmentToSend messageToSend = new MessageWithPBAttachmentToSend(producerMetadata);
        this.tproducer.publishPBMessage(messageToSend);

        FileSetMetadataWriter writer2 = new FileSetMetadataWriter("testName2","2.0","testTag2", "testOwner2");
        FileSetMetadata.Metadata producerMetadata2 = writer2.serialize();
        MessageWithPBAttachmentToSend messageToSend2 = new MessageWithPBAttachmentToSend(producerMetadata2);
        this.tproducer.publishPBMessage(messageToSend2);
        this.tproducer.close();

        t = connection2.openTopic(topicName);
        this.tconsumer = connection2.createConsumer(t,"JUnitClient",true);

        //first PB
        MessageWrapper response = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response.getMessageType() == MESSAGE_TYPE.PB_CLASS);
        FileSetMetadata.Metadata metadata = (FileSetMetadata.Metadata)response.getPayload();
        //FileSetMetadata.Metadata metadata = (FileSetMetadata.Metadata) Converter.asMessage(buffer, registry.getMessageClass(1)); //TODO: 1 should come from the message properties set by producer
        Assert.assertEquals("testName", metadata.getName());
        Assert.assertEquals("1.0", metadata.getVersion());
        Assert.assertEquals("testTag", metadata.getTag());
        Assert.assertEquals("testOwner", metadata.getOwner());

        //second PB
        MessageWrapper response2 = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response2.getMessageType() == MESSAGE_TYPE.PB_CLASS);

        //ByteArray buffer2 = (ByteArray)response2.getPayload();
        //FileSetMetadata.Metadata metadata2 = (FileSetMetadata.Metadata) Converter.asMessage(buffer2, registry.getMessageClass(1)); //TODO: 1 should come from the message properties set by producer
        FileSetMetadata.Metadata metadata2 = (FileSetMetadata.Metadata)response2.getPayload();
        Assert.assertEquals("testName2", metadata2.getName());
        Assert.assertEquals("2.0", metadata2.getVersion());
        Assert.assertEquals("testTag2", metadata2.getTag());
        Assert.assertEquals("testOwner2", metadata2.getOwner());

    }

    @After
    public void tearDown() throws Exception {

        connection.close();
        connection2.close();
    }

}