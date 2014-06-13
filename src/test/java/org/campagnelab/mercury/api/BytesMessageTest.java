package org.campagnelab.mercury.api;

import junit.framework.Assert;
import org.campagnelab.gobyweb.mercury.test.protos.FileSetMetadata;
import org.campagnelab.mercury.messages.Converter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.jms.Topic;
import java.nio.ByteBuffer;

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
        connection = new MQTopicConnection();
        connection2 = new MQTopicConnection();

    }

    @Test
    public void testPublishBytesMessageInTopic() throws Exception {
        String topicName = "JUnitTopicBytes6";
        Topic t = connection.openTopic(topicName);
        this.tproducer = connection.createProducer(t);
        FileSetMetadataWriter writer = new FileSetMetadataWriter("testName","1.0","testTag", "testOwner");
        FileSetMetadata.Metadata producerMetadata = writer.serialize();
        this.tproducer.publishBytesMessage(new MessageWrapper<ByteArray>(Converter.asByteArray(producerMetadata)));
        FileSetMetadataWriter writer2 = new FileSetMetadataWriter("testName2","2.0","testTag2", "testOwner2");
        FileSetMetadata.Metadata producerMetadata2 = writer2.serialize();
        this.tproducer.publishBytesMessage(new MessageWrapper<ByteArray>(Converter.asByteArray(producerMetadata2)));
        this.tproducer.close();


        t = connection2.openTopic(topicName);
        this.tconsumer = connection2.createConsumer(t,"JUnitClient",true);

        //first PB
        ByteArray buffer = tconsumer.readBytesMessage().getPayload();
        FileSetMetadata.Metadata metadata = Converter.asMessage(buffer, FileSetMetadata.Metadata.class);
        Assert.assertEquals("testName", metadata.getName());
        Assert.assertEquals("1.0", metadata.getVersion());
        Assert.assertEquals("testTag", metadata.getTag());
        Assert.assertEquals("testOwner", metadata.getOwner());

        //second PB
        ByteArray buffer2 = tconsumer.readBytesMessage().getPayload();
        FileSetMetadata.Metadata metadata2 = Converter.asMessage(buffer2, FileSetMetadata.Metadata.class);
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