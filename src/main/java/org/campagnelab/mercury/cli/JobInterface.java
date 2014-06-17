package org.campagnelab.mercury.cli;

import com.martiansoftware.jsap.JSAPResult;
import org.apache.log4j.Logger;
import org.campagnelab.mercury.api.MQTopicConnection;
import org.campagnelab.mercury.api.TopicProducer;
import org.campagnelab.mercury.messages.JobLogMessageBuilder;

import javax.jms.JMSException;
import javax.jms.Topic;
import java.util.List;

/**
 * Interface for registering text messages from jobs.
 *
 * @author manuele
 */
public class JobInterface {

    protected static final org.apache.log4j.Logger logger = Logger.getLogger(JobInterface.class);

    private static CommandLineHelper jsapHelper = new CommandLineHelper(JobInterface.class) {
        @Override
        protected boolean hasError(JSAPResult config, List<String> errors) {
            return (config.userSpecified("help"));
        }
    } ;
    public static void main(String[] args) throws Exception {
        System.exit(process(args,false));
    }

    public static int processAPI(String[] args) {
       return process(args, true);
    }

    private static int process(String[] args, boolean fromAPI) {
        JSAPResult config = jsapHelper.configure(args);
        if (config==null) return 1;
        MQTopicConnection connection = null;
        try {
            connection = new MQTopicConnection(config.getString("broker-hostname"), config.getInt("broker-port"));
        } catch (Exception e) {
            logger.fatal(String.format("Unable to connect to the messaging broker at: %s:%d.",
                    config.getString("broker-hostname"), config.getInt("broker-port") ), e);
            return 1;
        }
        Topic topic = null;
        try {
            topic = connection.openTopic(config.getString("job-tag"));
        } catch (JMSException e) {
            logger.fatal(String.format("Unable to open topic %s on the messaging broker at: %s:%d.",
                    config.getString("job-tag"), config.getString("broker-hostname"), config.getInt("broker-port") ), e);
            return 2;
        }
        TopicProducer producer = null;
        try {
            producer = connection.createProducer(topic);
        } catch (Exception e) {
            logger.fatal(String.format("Unable to create a producer for topic %s on the messaging broker at: %s:%d.",
                    config.getString("job-tag"), config.getString("broker-hostname"), config.getInt("broker-port")), e);
            return 3;
        }
        JobLogMessageBuilder builder = new JobLogMessageBuilder();
        builder.setText(config.getString("text-message"));
        try {
            producer.publishTextMessage(builder.buildMessage());
        } catch (Exception e) {
            logger.fatal("Failed to publish the message.",e);
        }
        logger.info("Message published with success.");
        return 0;
    }

}
