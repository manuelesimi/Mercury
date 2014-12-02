package org.campagnelab.mercury.cli;

import com.martiansoftware.jsap.JSAPResult;
import org.apache.log4j.Logger;
import org.campagnelab.mercury.api.MQTopicConnection;

import java.util.List;

/**
 * Management interface for the Broker.
 *
 * @author manuele
 */
public class ManagementInterface {

    protected static final org.apache.log4j.Logger logger = Logger.getLogger(ManagementInterface.class);

    private static CommandLineHelper jsapHelper = new CommandLineHelper(ManagementInterface.class) {
        @Override
        protected boolean hasError(JSAPResult config, List<String> errors) {
            if (config.getString("action").equalsIgnoreCase("delete")) {
                return false;
            }  else {
                return (config.userSpecified("help"));
            }
        }
    };

    public static void main(String[] args) throws Exception {
        System.exit(process(args, false));
    }

    public static int processAPI(String[] args) {
        return process(args, true);
    }

    private static int process(String[] args, boolean fromAPI) {
        JSAPResult config = jsapHelper.configure(args);
        if (config == null) return 1;
        MQTopicConnection connection = null;
        try {
            connection = new MQTopicConnection(config.getString("broker-hostname"), config.getInt("broker-port"), config.getFile("jndi-config"));
        } catch (Exception e) {
            logger.fatal(String.format("Unable to connect to the messaging broker at: %s:%d.",
                    config.getString("broker-hostname"), config.getInt("broker-port") ), e);
            return 1;
        }

        if (config.getString("action").equalsIgnoreCase("delete")) {
            if (! connection.deleteTopic(config.getString("topic-name")))
                return 2;
        }
        return 0;
    }
}