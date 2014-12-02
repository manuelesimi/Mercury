package org.campagnelab.mercury.cli;

import junit.framework.Assert;
import org.campagnelab.mercury.api.MQTopicConnection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

/**
 * Tester for the management interface.
 *
 * @author manuele
 */

@RunWith(JUnit4.class)
public class ManagementInterfaceTest {

    private static final String hostname = "toulouse.med.cornell.edu";

    private static final int port = 5672;

    private static final String topicName = "FCAFYXK";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testDeleteTopic() throws Exception {
        String[] args = new String[]{
                "--broker-hostname", hostname,
                "--broker-port", Integer.toString(port),
                "--topic-name", topicName,
                "--action", "delete",
                "--jndi-config", "mercury.properties"
        };
        Assert.assertEquals("Failed to delete the topic", 0, ManagementInterface.processAPI(args));
    }
}
