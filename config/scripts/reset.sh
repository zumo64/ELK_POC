#!/bin/bash
docker exec $1 curl -u es_admin:password -XDELETE http://172.17.0.3:9200/logstash
docker exec $1 curl -u es_admin:password -XPUT --data @/config/scripts/logstash_mappings.json http://172.17.0.3:9200/logstash
docker exec $1 curl -u es_admin:password -XPUT --data @/config/scripts/jmeter_template.json http://elkpoc_marvel_1:9200/_template/jmeter