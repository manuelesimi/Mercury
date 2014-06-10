package org.campagnelab.mercury.messaging;

import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 * Created by mas2182 on 6/10/14.
 */
public class Queue {

    private final Destination destination;


    public Queue(Destination destination) {
       this.destination = destination;
    }

    public Destination getDestination() {
        return destination;
    }
}
