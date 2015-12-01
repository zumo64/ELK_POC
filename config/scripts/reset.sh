#!/bin/bash
# ./reset.sh <client container> <client container IP> <marvel monitorin container IP>
# ex: ./reset.sh b966b1b176f0 elkpoc_client_1 elkpoc_marvel_1

docker exec $1 curl -u es_admin:password -XDELETE http://$2:9200/logstash
docker exec $1 curl -u es_admin:password -XPUT --data @/config/scripts/logstash_mappings.json http://$2:9200/logstash
docker exec $1 curl -u es_admin:password -XPUT --data @/config/scripts/jmeter_template.json http://$3:9200/_template/jmeter