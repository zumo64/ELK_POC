Plugin demo
============

This directory contains a demo of our commercial plugins.

Setup
-----

Assuming you have working ``docker`` with ``docker-compose``, run::

    docker-compose build
    docker-compose up

This will start a data node and a client node and publish the client node's
9200 port. The data nodes are not running http transport.

Configuration
-------------

All config files are in the ``config`` directory, everything is setup with
Shield and Marvel. There are 2 Users for Marvel (``marvel_user`` to be used to
access the UI and ``marvel_agent`` used by the agent) and an all-powerful
``es_admin`` user. All users use ``password`` as their password.

All data will be stored in the ``data`` directory and thus preseved during
docker restarts.

To change any of the user information run the ``esusers`` script::

    docker run --volume $PWD/config:/config elastic ./shield/esusers

Logstash
--------

``logstash-cluster.yml`` is a docker compose file for a demo with logstash, it
uses the cluster with all the configured plugins as described above, a simple
redis instance for queueing and 2 logstash containers - ``logstash-input`` and
``logstash-output``. Logstash input listens on port ``1234`` for any json and
passes it into the redis container (called ``queue``). From there it is picked
up by ``logstash-output`` and stuck directly into elasticsearch using the
``es_admin`` user. Relevant configuration files are in ``config/logstash.conf``
and ``config/logstash-input.conf``.

Kibana
------
Kibana credentials  are kibana/password


Watcher
-------

To create a watch run the ``0-setup.sh`` script from the ``watcher`` directory.
The watch definition itself can be found in ``watch.yaml``. The script
``1-generate-data.sh`` will then create documents in elasticsearch that should
trigger the watch.

.. note::

    The demo scripts assume anonymous login is enabled, either edit the scripts
    or enable anonymous access to your cluster by uncommenting the relevant
    portion of the es config.


Elastic
--------
The current setup uses 3 data nodes docker services : client, data1, data2
The client  service is started as a client node or as a data node if a  Marvel console is required. (Can't marvel on a client node)


JMeter
-------
Look Diagram in jmeter/JMeterELKArchitectureDoc.png

build the jmeter container :
 docker-compose build -t jmeter jmeter

ES target instance:
Start the target ES instance (the instance we are testing) 
on my setup its a ES 1.7.1 on bare metal.
Set it so it sends marvel data back to the jmeter docker instance :
marvel.agent.exporter.es.hosts: ["http://marvel_export:password@192.168.99.100:9200"]
 
JMeter container
config is elastic-monitor.yml

launch from compose:
docker-compose -f jmeter-compose.yml up

The container runs a JMETER server instance  and a ES node, with marvel monitoring the target ES instance (on bare metal)


JMeter GUI Controller 
install Jmeter on the mac . Modify Jmeter.properties with the followinf setting:
remote_hosts=192.168.99.100:30000

1. put the following jars in Jmeter/lib 
lucene-analyzers-common-4.10.4.jar
lucene-core-4.10.4.jar
elasticsearch-1.7.1.jar
elasticsearch-shield-1.3.2.jar

2. in jmeter/lib/ext
jmeter-elasticsearch.jar

3.Start Jmeter GUI
4. open test1.jmx 
5. start  test
  it should FAIL as the ES listener not connecting to the ES docker instance. 
  ( working on that as on Monday 14/09) 
  
6. Start remote test
it should fail as well as the jars have not been copied in the docker jmeter image
(TODO that and test connection)  





