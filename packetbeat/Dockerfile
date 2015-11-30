FROM ubuntu:14.04

ENV DEBIAN_FRONTEND noninteractive
ENV VERSION 1.0.0
RUN apt-get update

VOLUME /data
VOLUME /config
VOLUME /logs

RUN \
  apt-get install -qy curl libpcap0.8 && \
  curl -L -o /tmp/packetbeat_${VERSION}_amd64.deb https://download.elastic.co/beats/packetbeat/packetbeat_${VERSION}_amd64.deb && \
  dpkg -i /tmp/packetbeat_${VERSION}_amd64.deb

ADD packetbeat.yml /etc/packetbeat/packetbeat.yml

#CMD ["/bin/sleep", "15", "packetbeat", "-e"]