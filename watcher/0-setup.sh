curl -X DELETE localhost:9200/alerts
curl -X DELETE localhost:9200/test

curl -X PUT localhost:9200/_watcher/watch/error_500 --data-binary @watch.yaml
