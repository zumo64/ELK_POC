# Jmeter
#
# VERSION	0.0.1

FROM	ubuntu:12.04
MAINTAINER Jinuk Baek <kuniz37@gmail.com>

RUN apt-get update

RUN apt-get install -y wget openjdk-6-jre openjdk-6-jre-headless

RUN wget http://archive.apache.org/dist/jmeter/binaries/apache-jmeter-2.13.tgz
RUN tar xf apache-jmeter-2.13.tgz

CMD echo 0 > /proc/sys/net/ipv6/bindv6only

ENV RMI_LOCALHOSTNAME 192.168.99.100


EXPOSE 1099
#CMD apache-jmeter-2.13/bin/jmeter -s -j jmeter-server.log #-Dserver.rmi.localhostname=${RMI_LOCALHOSTNAME} -Dserver.rmi.localport=${RMI_LOCALPORT} #-Dserver_port=${SERVER_PORT} -Djava.rmi.server.hostname=${RMI_LOCALHOSTNAME}
