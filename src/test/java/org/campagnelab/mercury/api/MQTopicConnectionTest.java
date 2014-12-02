package org.campagnelab.mercury.api;

import org.junit.Before;
import org.junit.Test;

import java.io.File;


public class MQTopicConnectionTest {

    private MQTopicConnection connection;

    @Before
    public void setUp() throws Exception {
        connection = new MQTopicConnection("toulouse.med.cornell.edu", 5672, new File("mercury.properties"), "connection1");
    }
    @Test
    public void testDeleteTopic() throws Exception {
      connection.deleteTopic("BNGACZF");
    }
}