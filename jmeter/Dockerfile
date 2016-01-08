FROM java:8-jre

RUN apt-get update
RUN apt-get install -y wget

ENV JM_VERSION 2.13
ENV ES_VERSION 2.1.1

WORKDIR /opt

RUN wget -O - http://archive.apache.org/dist/jmeter/binaries/apache-jmeter-${JM_VERSION}.tgz | tar xzf -
RUN mv /opt/apache-jmeter-${JM_VERSION} /opt/apache-jmeter

WORKDIR /opt/apache-jmeter/lib


# and download all its dependencies
RUN wget http://central.maven.org/maven2/org/elasticsearch/elasticsearch/${ES_VERSION}/elasticsearch-${ES_VERSION}.jar
RUN wget http://central.maven.org/maven2/org/apache/lucene/lucene-analyzers-common/5.3.1/lucene-analyzers-common-5.3.1.jar
RUN wget http://central.maven.org/maven2/org/apache/lucene/lucene-core/5.3.1/lucene-core-5.3.1.jar
RUN wget http://maven.elasticsearch.org/releases/org/elasticsearch/plugin/shield/${ES_VERSION}/shield-${ES_VERSION}.jar

RUN wget http://central.maven.org/maven2/com/google/guava/guava/18.0/guava-18.0.jar
RUN wget http://central.maven.org/maven2/com/carrotsearch/hppc/0.7.1/hppc-0.7.1.jar
RUN wget http://central.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.5.3/jackson-core-2.5.3.jar
RUN wget http://central.maven.org/maven2/com/twitter/jsr166e/1.1.0/jsr166e-1.1.0.jar
RUN wget http://central.maven.org/maven2/com/tdunning/t-digest/3.0/t-digest-3.0.jar
RUN wget http://central.maven.org/maven2/org/joda/joda-convert/1.2/joda-convert-1.2.jar
RUN wget http://central.maven.org/maven2/joda-time/joda-time/2.8.2/joda-time-2.8.2.jar
RUN wget http://central.maven.org/maven2/io/netty/netty/3.10.5.Final/netty-3.10.5.Final.jar


RUN wget http://central.maven.org/maven2/commons-beanutils/commons-beanutils/1.8.3/commons-beanutils-1.8.3.jar
RUN wget http://central.maven.org/maven2/commons-lang/commons-lang/2.6/commons-lang-2.6.jar
RUN wget http://central.maven.org/maven2/commons-pool/commons-pool/1.6/commons-pool-1.6.jar
RUN wget http://central.maven.org/maven2/net/sf/ezmorph/ezmorph/1.0.6/ezmorph-1.0.6.jar
RUN wget http://central.maven.org/maven2/redis/clients/jedis/2.2.1/jedis-2.2.1.jar
RUN wget http://central.maven.org/maven2/com/jayway/jsonpath/json-path/0.9.0/json-path-0.9.0.jar
RUN wget http://central.maven.org/maven2/net/minidev/json-smart/1.2/json-smart-1.2.jar
RUN wget http://central.maven.org/maven2/org/apache/qpid/qpid-client/0.20/qpid-client-0.20.jar
RUN wget http://central.maven.org/maven2/org/apache/qpid/qpid-amqp-1-0-common/0.20/qpid-amqp-1-0-common-0.20.jar
RUN wget http://central.maven.org/maven2/org/springframework/spring-core/2.5.6/spring-core-2.5.6.jar
RUN wget http://mvnrepository.com/artifact/org.springframework/spring-jms/2.5.6
RUN wget http://central.maven.org/maven2/com/io7m/xom/xom/1.2.10/xom-1.2.10.jar


# upload our ElasticSearchBackendListenerClient
COPY jmeter-elasticsearch.jar ./ext/
COPY JMeterPlugins-ExtrasLibs.jar ./ext/
COPY json-lib-2.4-jdk15.jar ./

WORKDIR /opt/apache-jmeter/bin

CMD ["./jmeter", "-s"]
