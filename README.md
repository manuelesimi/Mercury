Mercury Messaging API
=============================

A JMS-based messaging API to publish and consume messages using [Apache ActiveMQ] message broker.

It builds on top of the Apache JMS Client and the [OpenWire] protocol.

Relevant features:
* Producers and consumers for the following classes of messages:
    * Text Messages
    * Bytes Messages
    * Messages with serializable object attachments
    * Messages with dynamic Protocol Buffer attachments
* Dynamic creation of Queues and Topics
* Durable Topics that are persistent at message broker side and can be consumed by multiple consumers
* Named Connections that allow creating multiple connections withing the same JVM
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

The conf folder contains the configuration files used to test the API. `activemq.xml` enables only OpenWire transport connector
and creates the authorization and authentication settings expected by the API.

These files can be copied in the following folder (make a copy of the original ones before):

   shell> <activemq_home>/conf

and further customized according to the desired configuration.

[OpenWire]: http://activemq.apache.org/openwire-version-2-specification.html
[Apache ActiveMQ]: http://activemq.apache.org

