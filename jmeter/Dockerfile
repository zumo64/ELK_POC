FROM java:8-jre

RUN apt-get update
RUN apt-get install -y wget

ENV JM_VERSION 2.13

WORKDIR /opt

RUN wget -O - http://archive.apache.org/dist/jmeter/binaries/apache-jmeter-${JM_VERSION}.tgz | tar xzf -
RUN mv /opt/apache-jmeter-${JM_VERSION} /opt/apache-jmeter

WORKDIR /opt/apache-jmeter/lib

# upload our ElasticSearchBackendListenerClient
COPY jmeter-elasticsearch.jar ./ext/
# and download all its dependencies
RUN wget http://maven.elasticsearch.org/releases/org/elasticsearch/elasticsearch-shield/1.3.2/elasticsearch-shield-1.3.2.jar
RUN wget http://central.maven.org/maven2/org/elasticsearch/elasticsearch/1.7.1/elasticsearch-1.7.1.jar
RUN wget http://central.maven.org/maven2/org/apache/lucene/lucene-analyzers-common/4.10.4/lucene-analyzers-common-4.10.4.jar
RUN wget http://central.maven.org/maven2/org/apache/lucene/lucene-core/4.10.4/lucene-core-4.10.4.jar

WORKDIR /opt/apache-jmeter/bin

CMD ["./jmeter", "-s"]
