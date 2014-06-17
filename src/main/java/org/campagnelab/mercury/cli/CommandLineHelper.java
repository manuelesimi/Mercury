package org.campagnelab.mercury.cli;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class to parse arguments with JSAP, and detect semantic error not supported directly by JSAP.
 *
 * @author Fabien Campagne
 *         Date: 4/7/13
 *         Time: 12:18 PM
 */
public class CommandLineHelper {
    protected static final org.apache.log4j.Logger logger = Logger.getLogger(CommandLineHelper.class);
    private Class client;

    //the errors occurred during the last invocation of configure()
    private List<String> lastCollectedErrors = new ArrayList<String>();
    /**
     * Create a command line helper. Client is the class that directly uses the helper. The class is
     * needed to locate the .jsap file associated with the client. This file should be located just next
     * to the client class in the classpath.
     *
     * @param client Class that uses this helper and provides a .jsap file.
     */
    public CommandLineHelper(Class client) {
        this.client = client;
    }

    /**
     * Parse the command line with JSap and produce a  JSAPResult result. When errors are detected,
     * error messages are printed to standard error.
     *
     * @param args Command line arguments, from main method.
     * @param additionalParameters additional parameters to for the JSAP interface
     * @return A parsed JSAPResult instance, or null if an error occurred.
     */
    public JSAPResult configure(String args[], List<Parameter> additionalParameters) {
        String shortClassName = client.getSimpleName();
        if (client.getResource(shortClassName + ".jsap") == null) {
            logger.fatal("unable to find the JSAP configuration file");
            System.err.println("unable to find the JSAP configuration file");
            return null;
        }
        JSAP jsap = null;
        try {
            jsap = new JSAP(client.getResource(shortClassName + ".jsap"));
        } catch (IOException e) {
            logger.fatal("unable to load the JSAP configuration file");
            System.err.println("unable to find the JSAP configuration file");
            return null;
        } catch (JSAPException e) {
            logger.fatal("unable to parse the JSAP configuration file");
            System.err.println("unable to find the JSAP configuration file");
            return null;
        }
        for (Parameter parameter : additionalParameters)
            try {
                jsap.registerParameter(parameter);
            } catch (JSAPException e) {
                logger.fatal(String.format("unable to add parameter %s to JSAP", parameter.getID()));
                System.err.println(String.format("unable to add parameter %s to JSAP", parameter.getID()));
            }

        JSAPResult config = jsap.parse(args);

        // check whether the command line was valid, and if it wasn't,
        // display usage information and exit.
        lastCollectedErrors.clear();
        if (!config.success() || config.userSpecified("help") || hasError(config, lastCollectedErrors)) {
            collectJSapError(config, lastCollectedErrors);
            System.err.println(jsap.getHelp());
            System.err.println();
            System.err.println("Usage: java " + client.getName());
            System.err.println("                " + jsap.getUsage());
            System.err.println();

            if (lastCollectedErrors.size() > 0) {
                for (String error : lastCollectedErrors)
                    System.err.println("Error: " + error);
            }

            return null;
        }
        return config;
    }

    /**
     * Gets the errors occurred in the last configure invocation, if any.
     * @return the errors
     */
    public List<String> getErrors() {
        return lastCollectedErrors;
    }

    /**
     * Parse the command line with JSap and produce a  JSAPResult result. When errors are detected,
     * error messages are printed to standard error.
     *
     * @param args Command line arguments, from main method.
     * @return A parsed JSAPResult instance, or null if an error occurred.
     */
    public JSAPResult configure(String[] args) {
        return configure(args, Collections.EMPTY_LIST);
    }


    /**
     * Return true if an error was detected.  Override this method to check error conditions that Jsap cannot detect.
     *
     * @param config
     * @param errors
     * @return
     */
    protected void collectJSapError(JSAPResult config, List<String> errors) {
        if (!config.success()) {

            for (java.util.Iterator errs = config.getErrorMessageIterator(); errs.hasNext(); ) {
                errors.add(errs.next().toString());
            }
        }
    }

    /**
     * Callback method.
     * Override this method to check error conditions that Jsap cannot detect (i.e., semantic errors
     * that result from invalid combinations of JSAP arguments).
     *
     * @param config The JSAP config prepared by configure.
     * @param errors  A list of error messages that the method should populate if any semantic error is detected.
     * @return  Return true if an error was detected.
     */

    protected boolean hasError(JSAPResult config, List<String> errors) {
        return false;
    }

}
