FROM java:8-jre

RUN apt-get update
RUN apt-get install -y wget

ENV JM_VERSION 2.13

WORKDIR /opt

RUN wget -O - http://archive.apache.org/dist/jmeter/binaries/apache-jmeter-${JM_VERSION}.tgz | tar xzf -
RUN mv /opt/apache-jmeter-${JM_VERSION} /opt/apache-jmeter

COPY jmeter-elasticsearch.jar /opt/apache-jmeter/lib/ext/

VOLUME /load-test

EXPOSE 1099

WORKDIR /opt/apache-jmeter/bin

ENTRYPOINT ["./jmeter", "-s"]
#CMD ./jmeter -s -j jmeter-server.log #-Dserver.rmi.localhostname=${RMI_LOCALHOSTNAME} -Dserver.rmi.localport=${RMI_LOCALPORT} #-Dserver_port=${SERVER_PORT} -Djava.rmi.server.hostname=${RMI_LOCALHOSTNAME}
