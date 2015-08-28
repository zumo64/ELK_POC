for id in `seq ${1:-10}`; do
  curl -sX PUT localhost:9200/test/d/$id -d '{
    "timestamp": "'$(date -Iseconds)'",
    "ip": "10.0.0.'$(echo $RANDOM | cut -c -1)'",
    "status": 500,
  }' -u es_admin:password > /dev/null
  printf '.'
done
echo "DONE"
