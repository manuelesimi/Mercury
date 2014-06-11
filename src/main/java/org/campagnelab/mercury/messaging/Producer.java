package org.campagnelab.mercury.messaging;

import org.campagnelab.mercury.messaging.MQConnection;

import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.io.Serializable;

/**
 * Created by mas2182 on 6/10/14.
 */
public class Producer {

    private MessageProducer producer;

    private MQConnection connection;

    public Producer(MQConnection connection, Queue queue) throws Exception {
        this.connection = connection;
        this.producer = connection.getSession().createProducer(queue);
    }

    public void publishTextMessage(TextMessageWrapper message) throws Exception{
        TextMessage tm = this.connection.getSession().createTextMessage(message.getMessageBody());
        this.producer.send(tm);
    }

    public void publishObjectMessage(ObjectMessageWrapper message) throws Exception{
        ObjectMessage om = this.connection.getSession().createObjectMessage();
        om.setObject(message.getMessageBody());
        this.producer.send(om);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.producer.close();
    }
}
