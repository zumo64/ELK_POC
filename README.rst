Plugin demo
============

This directory contains a demo of our commercial plugins.

Setup
-----

Assuming you have working ``docker`` with ``docker-compose``, run::

    docker-compose build
    docker-compose up

This will start a data node and a client node and publish the client node's
9200 port.

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

The current setup uses 3 docker services : client, data1, data2


JMeter
-------

JMeter can be run from docker as well. In ``jmeter-compose.yml`` you can find
an example that will run:

1. an es cluster for marvel (1 node)

2. elasticsearch cluster (client and 2 data nodes), shipping marvel data to marvel cluster

3. two jmeter servers

4. jmeter client

The jmeter client will start, wait for 20 seconds to give the clusters some
time to come up and then run the test in ``load-test/test1.jmx`` on the two
servers. The test is configured to work with shield and will log the results
into the cluster itself.

