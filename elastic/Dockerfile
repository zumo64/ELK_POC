FROM java:8-jre

RUN apt-get update
RUN apt-get install -y wget
RUN apt-get install -y sysstat
#RUN apt-get install -y openssh-server
#RUN apt-get install curl sudo -y


ENV ES_VERSION 2.2.0

#ENV GOSU_URL https://github.com/tianon/gosu/releases/download/1.6/gosu-amd64
#RUN useradd -m -s /bin/bash -g users -G sudo -p elastic elastic && \
#    echo 'docker ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers

WORKDIR /opt

# was here
RUN useradd -g staff -ms /bin/bash elastic

RUN wget -O- https://download.elasticsearch.org/elasticsearch/release/org/elasticsearch/distribution/tar/elasticsearch/$ES_VERSION/elasticsearch-$ES_VERSION.tar.gz  | tar xfz -
RUN mv /opt/elasticsearch-$ES_VERSION /opt/elasticsearch

RUN /opt/elasticsearch/bin/plugin install license
RUN /opt/elasticsearch/bin/plugin install shield
RUN /opt/elasticsearch/bin/plugin install marvel-agent
RUN /opt/elasticsearch/bin/plugin install watcher
RUN /opt/elasticsearch/bin/plugin install discovery-multicast

# Download and install gosu
#RUN curl -s -L -o /usr/local/bin/gosu $GOSU_URL && \
#    chmod +x /usr/local/bin/gosu


#RUN mkdir /var/run/sshd
#RUN echo 'root:password' | chpasswd
#RUN sed -i 's/PermitRootLogin without-password/PermitRootLogin yes/' /etc/ssh/sshd_config

# SSH login fix. Otherwise user is kicked off after login
#RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd

#ENV NOTVISIBLE "in users profile"
#RUN echo "export VISIBLE=now" >> /etc/profile



VOLUME /data
VOLUME /config
VOLUME /logs

ENV ES_JAVA_OPTS -Des.path.conf=/config

USER elastic
WORKDIR /opt/elasticsearch/bin
CMD ["/opt/elasticsearch/bin/elasticsearch"]
