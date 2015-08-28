FROM java:8-jre

RUN apt-get update
RUN apt-get install -y wget

ENV LS_VERSION 1.5.4

WORKDIR /opt

RUN wget -O- https://download.elastic.co/logstash/logstash/logstash-$LS_VERSION.tar.gz | tar xfz -
RUN mv /opt/logstash-$LS_VERSION /opt/logstash

VOLUME /config

WORKDIR /opt/logstash/bin

CMD ["/opt/logstash/bin/logstash", "--config", "/config/logstash.conf"]
