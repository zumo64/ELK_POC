FROM kibana:latest

WORKDIR /opt

VOLUME /data
VOLUME /config
VOLUME /logs

RUN /opt/kibana/bin/kibana plugin --install kibana/shield/latest
RUN /opt/kibana/bin/kibana plugin --install elasticsearch/marvel/latest
RUN /opt/kibana/bin/kibana plugin --install elastic/sense
RUN /opt/kibana/bin/kibana plugin --install kibana/timelion

COPY timelion.json ./kibana/installedPlugins/timelion/timelion.json


CMD ["/opt/kibana/bin/kibana"]
