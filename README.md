Mercury Messaging API
=============================

A messaging API for [AMQP] 1.0 protocol based on [Apache Qpid] JMS client.

The API virtually works with any AMQP-enabled message broker. It has been tested with [Apache ActiveMQ] assuming that:

* a user named `admin` with password `admin` has been configured
* you have added the AMQP 1.0 transport connector (more details here http://activemq.apache.org/amqp.html)

Relevant features:
* Producers and consumers for the following classes of messages:
    * Text Messages
    * Bytes Messages
    * Messages with serializable object attachments
    * Messages with dynamic Protocol Buffer attachments
* Dynamic Queues and Topics
* Error handling and reporting
* Command Line Interface for Jobs
* Target Messaging Broker configuration from API or command line parameters


Compiling the API
-----------------
Tests bundled with the API assume that there is an ActiveMQ instance running on localhost:5672. If it is not the case,
the software has to be compiled with the following command:

    shell> mvn install -Dmaven.test.skip=true


Starting ActiveMQ
-----------------

You can start the Apache ActiveMQ broker in a shell:

    shell> <activemq_home>/bin/activemq start

The conf folder contains the configuration files used to test the API. `activemq.xml` enables only AMPQ transport connector
and creates the authorization and authentication settings expected by the API.

These files can be copied in the following folder (make a copy of the original ones before):

   shell> <activemq_home>/conf

and further customized according to the desired configuration.

[AMQP]: http://www.amqp.org
[Apache Qpid]: http://qpid.apache.org
[Apache ActiveMQ]: http://activemq.apache.org

