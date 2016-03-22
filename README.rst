Plugin demo
============

This directory contains a demo of our commercial plugins.

Setup
-----

This project should be installed under root dir /elk-poc
ex : git clone https://github.com/zumo64/ELK_POC.git elk-poc


Assuming you have working ``docker 1.9.1`` with ``docker-compose``, run::

    docker-compose build
    docker-compose --x-networking up

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


Use jmeter-compose-ext.yml when load testing an external (bare metal) ES instance 
Use jmeter-compose.yml to load ES as another docker container

JMeter can be run from docker as well. In ``jmeter-compose.yml`` you can find
an example that will run:

1. 1 node ES cluster for marvel (1 node)

2. 1 elasticsearch cluster (client and 2 data nodes), shipping marvel data to marvel cluster

3. two jmeter servers

4. jmeter client

The jmeter client will start, wait for 20 seconds to give the clusters some
time to come up and then run the test in ``load-test/test1.jmx`` on the two
servers. The test is configured to work with shield and will log the results
into the marvel cluster.


load-test
----------

This is a set of tools you can use to do load testing with Jmeter and docker  :

1. Install coffee script on your machine as documented in http://coffeescript.org/#top 

2. Go to the folder tools and run npm install.

There are 2 scripts that can make your tests easy to setup :

* genbulks.coffee can be used to generate chunks of bulks index files which can be used by JMeter to ingest documents at a given rate.
genbulks takes as an input 1 or many JSON files containing the prod representative documents you wish to ingest for load testing. You can generate those JSON files using logstash (using the JSON output)
The required fomat for each JSON file is an array of documents like this:
[{doc1},{doc2},{doc3}........]

example of an extract (2 documents) taken from the HoW training files :

[{"message":"83.149.9.216 - - [22/Aug/2015:23:13:42 +0000] \"GET /presentations/logstash-monitorama-2013/images/kibana-dashboard3.png HTTP/1.1\" 200 171717 \"http://semicomplete.com/presentations/logstash-monitorama-2013/\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.77 Safari/537.36\"","@version":"1","@timestamp":"2015-08-22T23:13:42.000Z","host":"Zumos-elastic-MacBook-Pro.local","clientip":"83.149.9.216","ident":"-","auth":"-","timestamp":"22/Aug/2015:23:13:42 +0000","verb":"GET","request":"/presentations/logstash-monitorama-2013/images/kibana-dashboard3.png","httpversion":"1.1","response":200,"bytes":171717,"referrer":"\"http://semicomplete.com/presentations/logstash-monitorama-2013/\"","agent":"\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.77 Safari/537.36\"","geoip":{"ip":"83.149.9.216","country_code2":"RU","country_code3":"RUS","country_name":"Russian Federation","continent_code":"EU","region_name":"48","city_name":"Moscow","latitude":55.75219999999999,"longitude":37.6156,"timezone":"Europe/Moscow","real_region_name":"Moscow City","location":[37.6156,55.75219999999999]},"useragent":{"name":"Chrome","os":"Mac OS X 10.9.1","os_name":"Mac OS X","os_major":"10","os_minor":"9","device":"Other","major":"32","minor":"0","patch":"1700"}},
{"message":"83.149.9.216 - - [22/Aug/2015:23:13:44 +0000] \"GET /presentations/logstash-monitorama-2013/plugin/highlight/highlight.js HTTP/1.1\" 200 26185 \"http://semicomplete.com/presentations/logstash-monitorama-2013/\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.77 Safari/537.36\"","@version":"1","@timestamp":"2015-08-22T23:13:44.000Z","host":"Zumos-elastic-MacBook-Pro.local","clientip":"83.149.9.216","ident":"-","auth":"-","timestamp":"22/Aug/2015:23:13:44 +0000","verb":"GET","request":"/presentations/logstash-monitorama-2013/plugin/highlight/highlight.js","httpversion":"1.1","response":200,"bytes":26185,"referrer":"\"http://semicomplete.com/presentations/logstash-monitorama-2013/\"","agent":"\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.77 Safari/537.36\"","geoip":{"ip":"83.149.9.216","country_code2":"RU","country_code3":"RUS","country_name":"Russian Federation","continent_code":"EU","region_name":"48","city_name":"Moscow","latitude":55.75219999999999,"longitude":37.6156,"timezone":"Europe/Moscow","real_region_name":"Moscow City","location":[37.6156,55.75219999999999]},"useragent":{"name":"Chrome","os":"Mac OS X 10.9.1","os_name":"Mac OS X","os_major":"10","os_minor":"9","device":"Other","major":"32","minor":"0","patch":"1700"}}]



After running genbulks on these files, you will get chunks of compatible ES bulk API files :

{ "index" : { "_index" : "<index>", "_type" : "<type>" } }
{doc1}
{ "index" : { "_index" : "<index>", "_type" : "<type>" } }
{doc2}
....

Available parameters :
inputFolder : The path where the source JSON files are 
outputFolder : The path where the traget bulks are generated
indexName : the name of the index
typeName = the Type name
bulkSize = Number of dpcs on each bulk (example: 500)
nbFiles = Number of chunks to generate (set to -1 for let it generate as many as needed)
indexType =  "daily" or "single"  (default single) Use this option if you want to index to daily indices (ex daily logs) or one only index) When using daily , it will use the timestamp field in the source docs.


The jmeter test plans will know how to iterate over the generated files and create index load.


You can call genbulks like this :
coffee genbulks.coffee /Users/zumo/Desktop/inputlogs1 ./output apachelogs logs 500 5 daily


* genDateIntervals.coffee generates a CSV file that can be used by jmeter when generating load at the query side. JMeter will iterate this CSV file and use each column to set variable timestamp parameters for time range queries.

These are the parameters :

date1 = date min 
date2 = date max 
interval = width of the interval
unit = time unit  (s,m,h,d)
nbSamples = number of intervals to generate


example :

coffee genDateIntervals.coffee "22/08/2015 23:13:42" "21/09/2015 14:00:00" 10 m 5000 > input5K10m.csv
This will generate 5000 intervals of 10 minutes each between the "22/08/2015 23:13:42" and the "21/09/2015 14:00:00"

1442414393929,1442420393929
1441669056891,1441675056891
1442289841447,1442295841447
1440405924480,1440411924480
1440843795433,1440849795433
1441414408742,1441420408742
1441073914319,1441079914319
1440498223420,1440504223420
.....



Using the jmeter load tester:

the jmeter load tester comes in 2 flavours :

* jmeter-compose.yml : 

This Docker compose file contains :

client,data1,data2 : ES Client and Data nodes = the ES instance we are load testing. (the target)

marvel: The marvel monitoring cluster
kibana_client: A kibana 4.4  client for the ES target with Sense, Marvel, Shield, Timelion plugins
kibana_marvel: A Kibana client for the marvle instance same plugins as above
jmeter1: A jmeter server (slave) 2.13
jmeter2: A jmeter server (slave)
jmeter_client_1: the jmeter master client




* jmeter-compose-ext.yml :
Run this compose if you are load testing an external ES instance (the target is outside Docker)

marvel: The marvel monitoring cluster
kibana_marvel: A Kibana client for the marvel instance with Sense, Marvel, Shield, Timelion plugins
jmeter1: A jmeter server (slave)
jmeter_client_1: the jmeter master



JMeter test plans 
==================
Use es_admin/password credentials when logging to Kibana

run_queries_and_bulk_index_ext
run_queries_and_bulk_index

These are basically the same test plans with _ext being used by   jmeter-compose-ext.yml

run_queries_and_bulk_index will send aggregation queries AND send documents for index. You can configure the load in the jmeter command line inside the compose files :

    command: sh -c "sleep 90;./jmeter -n -t /load-test/run_queries_and_bulk_index_ext.jmx -GesUser=admin -GesPassword=password -GesProtocol=http -GesHost=192.168.1.13 -GesPort=9200 -GinputFiles=599 -GindexName=apachelogs -GtypeName=logs -GqueryThroughPut=1 -GindexThroughPut=1 -JtestRunId=T020116.1 -R jmeter1.elkpoc"
    
where :
esHost is the IP of the target ES instance
inputFile is the number of bulk files generated 
queryThroughPut and  indexThroughPut are throughputs in queries per minute. 
(above we set  1 bulk API call per minute -> 500 docs per minute)
testRunId will tag the JMETER metrics (see below)

    

step 1: Place your input files (files generated by genbulks) in load-test/input.

step 2: Place your queries in the "Queries Thread group" on the JMeter test plan. You should use a Jmeter GUI client for this , same version as the JMeter used in the docker container. In the packaged test plane you'll find  vis_1_apachelogs you can take as an example. In order to be perform more realistic tests and avoid caching use the  CSV files generated by  genDateIntervals so jmeter can send random queries.

step 3: Create mappings : We need to create the indices with correct mappings prior to starting load testing.
In order to create the correct mappings, start docker-compose ( docker-compose --x-networking up) and open the Kibana client (port 5602) for the marvel monitoring cluster. 
Then open a Sense console pointing to the marvel sense cluster (ex : http://192.168.99.100:9203)
Paste load-test/template/jmeter_tempate.json in order to create the template for the indices that will collect the Jmeter metrics (read below) . Use es_admin/password credentials

If you are planning  to load test the ES target instance embedded in Docker (the one in jmeter-compose.yml) from the sense console (poining to http://192.168.99.100:9200), create the index/ indices you are basing your test on. In this example we are creating the index apachelogs, the index creation is apachelogs_mappings.json. 

If you are planning to test an external ES cluster then do the same as above but using a sense console pointing at it.


step 4: start the performance test :

docker-compose -f jmeter-compose-ext.yml build
or 
docker-compose -f jmeter-compose.yml build

then

docker-compose -f jmeter-compose-ext.yml --x-networking up
or 
docker-compose -f jmeter-compose.yml --x-networking up

run one or the other depending wether your ES target is the one embedded in the docker compose,  or an extrenal instance


step5 : On the Kibana Marvel instance use Marvel and Timelion to monitor the ES cluster 





JMeter metrics (WIP)
===================
We are using the Jmeter backend listener API to send index latency and query latency to a separate index called .jmeter-sampler. The code for this sampler can be found in jmeter/ElasticSearchBackendListenerClient.java the corresponding jar is jmeter-elasticsearch.jar
This is still WIP but you can basically visulize the latencies and assertion errors on a Timelion dashboard besides the marvel metrics. 

Timelion sheets below for your reference :

"timelion_sheet": [
            ".es(index='.jmeter-sampler-*',metric=avg:latency,q='sampleLabel:Bulk_index')",
            ".es(index='.jmeter-sampler-*',q='sampleLabel:Bulk_index AND assertions.failure:true')",
            ".es(index='.marvel-es-*',metric='avg:node_stats.jvm.mem.heap_used_percent',q='_type:node_stats')",
            ".es(index='.marvel-es-*',metric='max:node_stats.jvm.gc.collectors.old.collection_time_in_millis',q='_type:node_stats'),.es(index='.marvel-es-*',metric='max:node_stats.jvm.gc.collectors.old.collection_count',q='_type:node_stats')",
            ".es(index='.marvel-es-*',metric='avg:node_stats.process.cpu.percent',q='_type:node_stats')",
            ".es(index='.marvel-es-*',metric='avg:node_stats.thread_pool.bulk.rejected',q='_type:node_stats')",
            ".es(*)"
          ]














