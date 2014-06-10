package org.campagnelab.mercury.messaging;

import org.campagnelab.mercury.messaging.MQConnection;

import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import java.io.Serializable;

/**
 * Created by mas2182 on 6/10/14.
 */
public class Producer {

    private MessageProducer producer;

    private MQConnection connection;

    public Producer(MQConnection connection) throws Exception {
        this.connection = connection;
        this.producer = connection.getSession().createProducer(connection.getDefaultQueue().getDestination());
    }

    public void publishTextMessage(String message) throws Exception{
        TextMessage tm = this.connection.getSession().createTextMessage(message);
        this.producer.send(tm);
    }

    public void publishObjectMessage(Serializable message) throws Exception{
        ObjectMessage om = this.connection.getSession().createObjectMessage();
        om.setObject(message);
        this.producer.send(om);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.producer.close();
    }
}
