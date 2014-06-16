package org.campagnelab.mercury.api;

import junit.framework.Assert;
import org.campagnelab.gobyweb.mercury.test.protos.FileSetMetadata;
import org.campagnelab.mercury.api.wrappers.ByteArray;
import org.campagnelab.mercury.api.wrappers.MessageToSendWrapper;
import org.campagnelab.mercury.api.wrappers.MessageWrapper;
import org.campagnelab.mercury.messages.Converter;
import org.campagnelab.mercury.messages.PBClassRegistry;
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

    private PBClassRegistry registry = new PBClassRegistry();

    @Before
    public void setUp() throws Exception {
        connection = new MQTopicConnection();
        connection2 = new MQTopicConnection();
        registry.registerPBClass(1, FileSetMetadata.Metadata.class);

    }

    @Test
    public void testPublishBytesMessageInTopic() throws Exception {
        String topicName = "JUnitTopicBytes12";
        Topic t = connection.openTopic(topicName);
        this.tproducer = connection.createProducer(t);
        FileSetMetadataWriter writer = new FileSetMetadataWriter("testName","1.0","testTag", "testOwner");
        FileSetMetadata.Metadata producerMetadata = writer.serialize();
        this.tproducer.publishBytesMessage(new MessageToSendWrapper<ByteArray>(Converter.asByteArray(producerMetadata),MessageWrapper.TYPE.BYTE_ARRAY));
        FileSetMetadataWriter writer2 = new FileSetMetadataWriter("testName2","2.0","testTag2", "testOwner2");
        FileSetMetadata.Metadata producerMetadata2 = writer2.serialize();
        this.tproducer.publishBytesMessage(new MessageToSendWrapper<ByteArray>(Converter.asByteArray(producerMetadata2),MessageWrapper.TYPE.BYTE_ARRAY));
        this.tproducer.close();

        t = connection2.openTopic(topicName);
        this.tconsumer = connection2.createConsumer(t,"JUnitClient",true);

        //first PB
        MessageWrapper response = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response.getMessageType() == MessageWrapper.TYPE.BYTE_ARRAY);
        ByteArray buffer = (ByteArray)response.getPayload();
        FileSetMetadata.Metadata metadata = (FileSetMetadata.Metadata) Converter.asMessage(buffer, registry.getMessageClass(1)); //TODO: 1 should come from the message properties set by producer
        Assert.assertEquals("testName", metadata.getName());
        Assert.assertEquals("1.0", metadata.getVersion());
        Assert.assertEquals("testTag", metadata.getTag());
        Assert.assertEquals("testOwner", metadata.getOwner());

        //second PB
        MessageWrapper response2 = tconsumer.readNextMessage();
        Assert.assertTrue("Unexpected message type", response2.getMessageType() == MessageWrapper.TYPE.BYTE_ARRAY);

        ByteArray buffer2 = (ByteArray)response2.getPayload();
        FileSetMetadata.Metadata metadata2 = (FileSetMetadata.Metadata) Converter.asMessage(buffer2, registry.getMessageClass(1)); //TODO: 1 should come from the message properties set by producer
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