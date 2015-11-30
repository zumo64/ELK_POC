FROM java:8-jre

RUN apt-get update
RUN apt-get install -y wget

ENV ES_VERSION 2.1.0

WORKDIR /opt

RUN useradd -ms /bin/bash elastic

RUN wget -O- https://download.elasticsearch.org/elasticsearch/release/org/elasticsearch/distribution/tar/elasticsearch/$ES_VERSION/elasticsearch-$ES_VERSION.tar.gz  | tar xfz -
RUN mv /opt/elasticsearch-$ES_VERSION /opt/elasticsearch

RUN /opt/elasticsearch/bin/plugin install license
RUN /opt/elasticsearch/bin/plugin install shield
RUN /opt/elasticsearch/bin/plugin install marvel-agent
RUN /opt/elasticsearch/bin/plugin install watcher
RUN /opt/elasticsearch/bin/plugin install discovery-multicast

VOLUME /data
VOLUME /config
VOLUME /logs

ENV ES_JAVA_OPTS -Des.path.conf=/config
USER elastic
WORKDIR /opt/elasticsearch/bin

CMD ["/opt/elasticsearch/bin/elasticsearch"]
