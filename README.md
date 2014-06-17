Mercury Messaging API
=============================

A messaging API for [AMQP] 1.0 protocol based on [Apache Qpid] JMS client.

The API virtually works with any AMQP-enabled message broker. It has been tested with [Apache ActiveMQ] assuming that:

* a user named `admin` with password `admin` has been configured
* you have added the AMQP 1.0 transport connector (more details here http://activemq.apache.org/amqp.html)

Relevant features:

* ...
* ...


Compiling the API
-----------------
Tests bundled with the API assume that there is an ActiveMQ instance running on localhost:5672. If it is not the case,
the software has to be compiled with the following command:

    shell> mvn install -Dmaven.test.skip=true


Starting ActiveMQ
-----------------

You can start the Apache ActiveMQ broker in a shell:

    shell> <activemq_home>/bin/activemq start

In the conf folder, you can find the configuration file (`activemq.xml`) used to test the API. It enables only AMPQ transport connector
and creates the authorization and authentication settings expected by the API.

You can place this file in the following folder (make a copy of the original one before):

   shell> <activemq_home>/conf

and customize it according to the desired configuration.

[AMQP]: http://www.amqp.org
[Apache Qpid]: http://qpid.apache.org
[Apache ActiveMQ]: http://activemq.apache.org

